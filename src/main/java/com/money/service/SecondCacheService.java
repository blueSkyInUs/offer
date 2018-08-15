package com.money.service;

import com.alibaba.fastjson.JSONObject;
import com.money.constant.RedisKey;
import com.money.domain.OfferRequestLog;
import com.money.domain.UserClickInfo;
import com.money.mapper.OfferRequestLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class SecondCacheService {

    private ReentrantLock clickInfoLock=new ReentrantLock();


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private OfferRequestLogMapper offerRequestLogMapper;


    private LinkedHashMap<String,UserClickInfo> clickInfoLinkedHashMap=new LinkedHashMap<String,UserClickInfo>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, UserClickInfo> eldest) {
            return this.size()>=50000;
        }
    };

    public void recordClickInfo(String uniqueKey,UserClickInfo userClickInfo){
        try{
            clickInfoLock.lock();
            clickInfoLinkedHashMap.put(uniqueKey, userClickInfo);
        }catch (Exception exp){
            log.error(exp.getMessage(),exp);
        }finally {
            clickInfoLock.unlock();
        }
        redisTemplate.opsForValue().set(RedisKey.UNIQUE_KEY_INFO+":"+uniqueKey,JSONObject.toJSONString(userClickInfo),24, TimeUnit.HOURS);
    }

    public UserClickInfo queryClickInfo(String uniqueKey){
        UserClickInfo userClickInfo=null;
        try{
            clickInfoLock.lock();
            userClickInfo=clickInfoLinkedHashMap.remove(uniqueKey);
        }catch (Exception exp){
            log.error(exp.getMessage(),exp);
        }finally {
            clickInfoLock.unlock();
        }

        if (Objects.isNull(userClickInfo)){
            String content=redisTemplate.boundValueOps(RedisKey.UNIQUE_KEY_INFO+":"+uniqueKey).get();
            userClickInfo= JSONObject.parseObject(content,UserClickInfo.class);
        }else{
            log.info("find in second appliation cache....");
        }
        if (Objects.nonNull(userClickInfo)) {
            redisTemplate.delete(RedisKey.UNIQUE_KEY_INFO+":"+uniqueKey);
        }else{
            log.info("find in db");
            OfferRequestLog offerRequestLog=offerRequestLogMapper.obtainRequestLobByUniqueKey(uniqueKey);
            userClickInfo=new UserClickInfo(offerRequestLog.getClickid(),offerRequestLog.getCompaignCode(),Integer.parseInt(offerRequestLog.getOfferid()),offerRequestLog.getCountry(),offerRequestLog.getCarrier());
        }
        return userClickInfo;
    }



}
