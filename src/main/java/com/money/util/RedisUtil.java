package com.money.util;

import com.money.constant.RedisKey;
import com.money.domain.Campaign;
import com.money.domain.CountAndAmount;
import com.money.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class RedisUtil {

    Map<String,CountAndAmount> offerStatusCounter=new HashMap<>();

    Map<String,Double> campaidTotalAmountMap=new HashMap<>();

    ReentrantLock mapLock=new ReentrantLock();

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    private CacheService cacheService;

    /**
     *
     * @param campaignCode
     * @param offerId
     * @param payout
     */
    public void recordOfferCallback(String campaignCode,int offerId,double payout){
        try{
            mapLock.lock();
            String key=campaginOfferKey(campaignCode,offerId);
            CountAndAmount offerMoney=offerStatusCounter.get(key);
            if (Objects.isNull(offerMoney)){
                offerMoney=new CountAndAmount(0,0);
                offerStatusCounter.put(key,offerMoney);
            }
            int money=(int)payout*100;
            offerMoney.setAmount(offerMoney.getAmount()+money);
            double totalMoney=payout+campaidTotalAmountMap.getOrDefault(campaignCode,0d);
            campaidTotalAmountMap.put(campaignCode,totalMoney);
        }catch ( Exception exp){
            log.error(exp.getMessage(),exp);
        }finally {
            mapLock.unlock();
        }
    }

    public boolean needNotifyAndClear(Campaign campaign){
        try{
            mapLock.lock();
            String campaignCode=campaign.getCampaignCode();
            double totalMoney=campaidTotalAmountMap.getOrDefault(campaignCode,0d);
            if (totalMoney>=campaign.getContrProportion()){
                campaidTotalAmountMap.put(campaignCode,0d);
                return true;
            }
        }catch ( Exception exp){
            log.error(exp.getMessage(),exp);
        }finally {
            mapLock.unlock();
        }
        return false;
    }

    /**
     * record the total request
     * @param offerId
     */
    public void recordOfferObtain(String campaigCode,int offerId){
        try{
            mapLock.lock();
            String key=campaginOfferKey(campaigCode,offerId);
            CountAndAmount offerMoney=offerStatusCounter.get(key);
            if (Objects.isNull(offerMoney)){
                offerMoney=new CountAndAmount(0,0);
                offerStatusCounter.put(key,offerMoney);
            }
            offerMoney.setCount(offerMoney.getCount()+1);
        }catch ( Exception exp){
            log.error(exp.getMessage(),exp);
        }finally {
            mapLock.unlock();
        }
    }

    public void refreshToRedis(){
        try{
          mapLock.lock();
          log.info("refresh localdata to  redis....");
          offerStatusCounter.entrySet().parallelStream().forEach(entry->{
                String[] elements=entry.getKey().split(":");
                String campaignCode=elements[0];
                String offerId=elements[1];
                log.info("value in here:{}-->{}",entry.getKey(),entry.getValue());
                redisTemplate.opsForHash().increment(RedisKey.OFFER_OBTAIN_COUNT_SORTSET+":"+campaignCode,offerId,entry.getValue().getCount());
                redisTemplate.opsForHash().increment(RedisKey.OFFER_CALLBACK_AMOUNT_SORTSET+":"+campaignCode,offerId,entry.getValue().getAmount());
          });
          log.info("end refresh localdata to redis....");
            offerStatusCounter=new HashMap<>();
        }catch (Exception exp){
          log.error(exp.getMessage(),exp);
        }finally {
          mapLock.unlock();
        }
    }

    private String campaginOfferKey(String campaignCode,int offerId){
       return campaignCode+":"+offerId;
    }





}
