package com.money.service;

import com.alibaba.fastjson.JSONObject;
import com.money.constant.CampaignStatus;
import com.money.constant.CommonRequestParamConf;
import com.money.constant.OfferStatus;
import com.money.domain.*;
import com.money.mapper.AffiliateNotifyLogMapper;
import com.money.mapper.OfferRequestLogMapper;
import com.money.mapper.TrafficNotifyLogMapper;
import com.money.util.RedisUtil;
import com.money.util.UniqueKeyUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OfferCallbackService {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SecondCacheService secondCacheService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private AffiliateNotifyLogMapper affiliateNotifyLogMapper;

    @Autowired
    private TrafficNotifyLogMapper trafficNotifyLogMapper;

    @Autowired
    private OfferRequestLogMapper offerRequestLogMapper;
    @Autowired
    private IpReflectionService ipReflectionService;

    @Autowired
    private RestTemplate restTemplate;

    @SneakyThrows
    @Async
    public void callback(String queryString,String ip){
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info("callback ip:{}",ip);

        JSONObject jsonObject=queryStringToJson(queryString);
        String payout=jsonObject.getString(CommonRequestParamConf.PAYOUT.getRequestParam());
        String uniqueKey=jsonObject.getString(CommonRequestParamConf.CLICKID.getRequestParam());
        String status=(String)jsonObject.getOrDefault(CommonRequestParamConf.STATUS.getRequestParam(),"1");
        //to query in application second cache
        UserClickInfo userClickInfo=secondCacheService.queryClickInfo(uniqueKey);


        String country=userClickInfo.getCountry();
        String carrier=userClickInfo.getCarrier();
        String countryTemp=(null==userClickInfo.getCountry_temp()?country:userClickInfo.getCountry_temp());
        String carrierTemp=(null==userClickInfo.getCarrier_temp()?carrier:userClickInfo.getCarrier_temp());
        String campaignCode=userClickInfo.getCampaignCode();
        Integer offerId=userClickInfo.getOfferId();
        String clickid=userClickInfo.getClickId();
        Campaign campaigns= cacheService.getCampaignByCode(campaignCode);
        Offer offer=cacheService.getOfferById(offerId);

        AffiliateNetwork affiliateNetwork=cacheService.getAffiateNetworkById(offer.getAffiliateNetworkId());
        String ipWhiteList=affiliateNetwork.getPostbackIpWhiteList();
        if (!StringUtils.isEmpty(ipWhiteList)&&!ipWhiteList.contains("0.0.0.0")&&!ipWhiteList.contains(ip)){
            log.info("ip:{} unlegal access",ip);
            return;
        }
        AffiliateNotifyLog affiliateNotifyLog=new AffiliateNotifyLog();
        affiliateNotifyLog.setAffId(offer.getAffiliateNetworkId()+"");
        affiliateNotifyLog.setOfferid(offerId+"");
        affiliateNotifyLog.setTrafficId(campaigns.getTrafficId()+"");
        affiliateNotifyLog.setBusId("0");
        affiliateNotifyLog.setCompaignCode(campaignCode);
        affiliateNotifyLog.setCampId(campaigns.getId()+"");
        affiliateNotifyLog.setCarrier(carrier);
        affiliateNotifyLog.setClickid(clickid);
        affiliateNotifyLog.setCountry(country);
        affiliateNotifyLog.setExtParam1(jsonObject.getString("ext_param1"));
        affiliateNotifyLog.setExtParam2(jsonObject.getString("ext_param2"));
        affiliateNotifyLog.setIp(ip);
        affiliateNotifyLog.setNotifyTime(new Date());
        affiliateNotifyLog.setPayout(payout);
        affiliateNotifyLog.setType(campaigns.getType());
        affiliateNotifyLog.setUniqueKey(uniqueKey);
        affiliateNotifyLogMapper.recordAffiliateNotifyCallback(affiliateNotifyLog);

        redisUtil.recordOfferCallback(campaignCode,offerId,Double.parseDouble(payout),countryTemp,carrierTemp);
        String response="";
        int resultCode=200;

        TrafficSource trafficSource=cacheService.getTrafficSourceById(campaigns.getTrafficId());
        Integer retryTimes=trafficSource.getRetryTimes();
        if (Objects.isNull(retryTimes)){
            retryTimes=0;
        }
        int currentRetry=0;
        Double contrProportion=campaigns.getContrProportion();
        if (Objects.isNull(contrProportion)){
            contrProportion=-1d;
        }


        String callbackUrl=cacheService.getTrafficSourceById(campaigns.getTrafficId()).getPostbackUrl();

        callbackUrl=callbackUrl.replace(CommonRequestParamConf.CLICKID.getReplaceString(),clickid);

        if(offer.isStaticPrice()){
            payout=offer.getPayoutAmount();
        }else{
            payout=Double.parseDouble(payout)*Double.parseDouble(offer.getPayoutAmount())/100+"";
        }

        if (campaigns.isPriceFixed()){
            payout=campaigns.getPayoutValue()+"";
        }else{
            payout=Double.parseDouble(payout)*campaigns.getPayoutValue()/100+"";
        }

        if(campaigns.isKouByThresholdAmount()){
            if (redisUtil.needNotifyAndClear(campaigns)){
                payout=campaigns.getContrProportion()+"";
            }else{
                log.info("threshold can't occur ,just ignore");
                return ;
            }
        }

        callbackUrl=callbackUrl.replace(CommonRequestParamConf.PAYOUT.getReplaceString(),payout);
        for (CommonRequestParamConf commonRequestParamConf:CommonRequestParamConf.values()){
            callbackUrl=callbackUrl.replace(commonRequestParamConf.getReplaceString(),(String)jsonObject.getOrDefault(commonRequestParamConf.getRequestParam(),""));
        }


        log.info("callback url:{}",callbackUrl);

        int score=new Random().nextInt(100);
        boolean isKou=campaigns.isKouByThresholdAmount()?false:score<=contrProportion;
        if (isKou){
            log.info("this request clickid:{} no need notify traffic.",clickid);
            resultCode=200;
        }else{
            do{
                try{
                    response=restTemplate.getForObject(callbackUrl,String.class);
                    log.info("response:{}",response.substring(0,Math.min(response.length(),500)));
                    break;
                }catch ( Exception exp){
                    resultCode=500;
                    log.error("call traffic url error:"+exp.getMessage(),exp);
                }
            }while ( currentRetry++<retryTimes );
        }
        TrafficNotifyLog trafficNotifyLog=new TrafficNotifyLog();
        trafficNotifyLog.setAffId(offer.getAffiliateNetworkId()+"");
        trafficNotifyLog.setBusId("0");
        trafficNotifyLog.setTrafficId(campaigns.getTrafficId()+"");
        trafficNotifyLog.setCompaignCode(campaignCode);
        trafficNotifyLog.setCampId(campaigns.getId()+"");
        trafficNotifyLog.setCarrier(carrier);
        trafficNotifyLog.setClickid(clickid);
        trafficNotifyLog.setCountry(country);
        trafficNotifyLog.setExtParam1(jsonObject.getString("ext_param1"));
        trafficNotifyLog.setExtParam2(jsonObject.getString("ext_param2"));
        trafficNotifyLog.setKouFlag(isKou?"1":"0");
        trafficNotifyLog.setNotifyLasttime(new Date());
        trafficNotifyLog.setNotifyUrl(callbackUrl);
        trafficNotifyLog.setPayout(payout);
        trafficNotifyLog.setNotifyRescode(resultCode+"");
        trafficNotifyLog.setOfferid(offerId+"");
        trafficNotifyLog.setNum(currentRetry+"");
        trafficNotifyLog.setStatus(resultCode==200?"1":"0");
        trafficNotifyLog.setRegtime(new Date());
        trafficNotifyLog.setOrderStatus(status);
        trafficNotifyLog.setType(campaigns.getType());
        trafficNotifyLog.setUniqueKey(uniqueKey);
        trafficNotifyLogMapper.recoreTrafficNotifyCallback(trafficNotifyLog);
    }

    public String obtain(HttpServletRequest httpServletRequest, String campaignCode){
        String ip= StringUtils.isEmpty(httpServletRequest.getHeader("X-Real-IP"))?httpServletRequest.getRemoteAddr():httpServletRequest.getHeader("X-Real-IP");

        log.info("campaignCode:{} obtain ip:{}",campaignCode,ip);
        String country=null;
        String carrier=null;
        IpRelection ipRelection=ipReflectionService.getRelectionByIpLong(ip);
        log.info("ipRelection:{}",ipRelection);
        if (Objects.nonNull(ipRelection)){
            country=ipRelection.getCountryCode();
            carrier=ipRelection.getMobileCarrier();
        }
        String queryString=httpServletRequest.getQueryString();
        JSONObject jsonObject=queryStringToJson(queryString);
        String clickId=(String)jsonObject.getOrDefault(CommonRequestParamConf.CLICKID.getRequestParam(), UUID.randomUUID().toString());
        String subPubId=(String)jsonObject.getOrDefault(CommonRequestParamConf.SUBPUBID.getRequestParam(),"1");

        Campaign campaign=cacheService.getCampaignByCode(campaignCode);
        if (CampaignStatus.UN_AVAILABLE.getCode().equals(campaign.getStatus())){
            log.info("campaignid:{} already down",campaign.getId());
            OfferRequestLog offerRequestLog =new OfferRequestLog();
            offerRequestLog.setAffId("0");
            offerRequestLog.setBusId("0");
            offerRequestLog.setCampId(campaign.getId()+"");
            offerRequestLog.setCarrier(carrier);
            offerRequestLog.setClickid(clickId);
            offerRequestLog.setCompaignCode(campaignCode);
            offerRequestLog.setExtParam1(jsonObject.getString("ext_param1"));
            offerRequestLog.setExtParam2(jsonObject.getString("ext_param2"));
            offerRequestLog.setIp(ip);
            offerRequestLog.setOfferid("-1");
            offerRequestLog.setCountry("unknown");
            offerRequestLog.setReqtime(new Date());
            offerRequestLog.setSubId(subPubId);
            offerRequestLog.setTrafficId(campaign.getTrafficId()+"");
            offerRequestLog.setType(campaign.getType());
            offerRequestLog.setUniqueKey(UniqueKeyUtil.generateKey());
            offerRequestLogMapper.recordOfferRequestLog(offerRequestLog);
            throw new RuntimeException("campaign id["+campaign.getId()+"] already down");
        }
        if ("1".equals(campaign.getIsTemplate())){
            log.info("campaginid:{} is a temp, can't use",campaign.getId());
            throw new RuntimeException("campaign id["+campaign.getId()+"] can't use");
        }

        List<OfferRate> offerRates= cacheService.getOfferInCampaign(campaignCode);

        List<OfferRate> global=offerRates.stream().filter(OfferRate::canGlobal).collect(Collectors.toList());

        int totalScore=0;
        for (OfferRate offerRate:offerRates){
            if( Objects.isNull(ipRelection) || offerRate.canFitThisIpInfo(ipRelection) ){
                log.info("offer:{} can choose",offerRate);
                totalScore+=offerRate.getRate();
            }
        }
        log.info("totalScore:{}",totalScore);
        int bestOfferId=-1;
        OfferRate targetOfferRate=null;
        if (totalScore==0){
            if (global.isEmpty()){
                OfferRequestLog offerRequestLog =new OfferRequestLog();
                offerRequestLog.setAffId("0");
                offerRequestLog.setBusId("0");
                offerRequestLog.setCampId(campaign.getId()+"");
                offerRequestLog.setCarrier(carrier);
                offerRequestLog.setClickid(clickId);
                offerRequestLog.setCompaignCode(campaignCode);
                offerRequestLog.setExtParam1(jsonObject.getString("ext_param1"));
                offerRequestLog.setExtParam2(jsonObject.getString("ext_param2"));
                offerRequestLog.setIp(ip);
                offerRequestLog.setOfferid("-1");
                offerRequestLog.setCountry("unknown");
                offerRequestLog.setReqtime(new Date());
                offerRequestLog.setSubId(subPubId);
                offerRequestLog.setTrafficId(campaign.getTrafficId()+"");
                offerRequestLog.setType(campaign.getType());
                offerRequestLog.setUniqueKey(UniqueKeyUtil.generateKey());
                offerRequestLogMapper.recordOfferRequestLog(offerRequestLog);
                throw new RuntimeException("can't find offer");
            }
            int globaltotalScore=0;
            for (OfferRate offerRate:global){
                globaltotalScore+=offerRate.getRate();
            }
            int globalTargetScore=new Random().nextInt(globaltotalScore);
            // random a offerid  default
            for (OfferRate offerRate:global){
                globalTargetScore = globalTargetScore - offerRate.getRate();
                if (globalTargetScore<=0){
                    bestOfferId=offerRate.getOfferId();
                    targetOfferRate=offerRate;
                    break;
                }
            }
            log.info("random chooose global offerid:{}",bestOfferId);
        }else{
            int targetScore=new Random().nextInt(totalScore);
            // random a offerid  default
            for (OfferRate offerRate:offerRates){
                if( Objects.isNull(ipRelection) || offerRate.canFitThisIpInfo(ipRelection) ){
                    targetScore = targetScore - offerRate.getRate();
                    if (targetScore<=0){
                        bestOfferId=offerRate.getOfferId();
                        targetOfferRate=offerRate;
                        break;
                    }
                }
            }
        }
        Offer offer=cacheService.getOfferById(bestOfferId);
        if (OfferStatus.UN_AVAILABLE.getCode().equals(offer.getStatus())&& Objects.nonNull(offer.getBakOfferid())){
            log.info("offerid:{} not available , select bak offerid:{}",offer.getId(),offer.getBakOfferid());
            offer=cacheService.getOfferById(offer.getBakOfferid());
        }
        String uniqueKey= UniqueKeyUtil.generateKey();
        jsonObject.put(CommonRequestParamConf.CLICKID.getRequestParam(),uniqueKey);

        UserClickInfo userClickInfo=new UserClickInfo();
        userClickInfo.setCampaignCode(campaignCode);
        userClickInfo.setClickId(clickId);
        userClickInfo.setOfferId(offer.getId());
        userClickInfo.setCountry(country);
        userClickInfo.setCarrier(carrier);
        userClickInfo.setCarrier_temp(targetOfferRate.getMobileCarrier());
        userClickInfo.setCountry_temp(targetOfferRate.getCountry());


        String fitCampaignUrl=offer.getUrl();

        for (CommonRequestParamConf commonRequestParamConf:CommonRequestParamConf.values()){
            fitCampaignUrl=fitCampaignUrl.replace(commonRequestParamConf.getReplaceString(),(String)jsonObject.getOrDefault(commonRequestParamConf.getRequestParam(),""));
        }
        fitCampaignUrl=fitCampaignUrl.replace("{trafficSourceId}",campaign.getTrafficId()+"");
        OfferRequestLog offerRequestLog =new OfferRequestLog();
        offerRequestLog.setAffId(offer.getAffiliateNetworkId()+"");
        offerRequestLog.setBusId("0");
        offerRequestLog.setCampId(campaign.getId()+"");
        offerRequestLog.setCarrier(carrier);
        offerRequestLog.setClickid(clickId);
        offerRequestLog.setCompaignCode(campaignCode);
        offerRequestLog.setExtParam1(jsonObject.getString("ext_param1"));
        offerRequestLog.setExtParam2(jsonObject.getString("ext_param2"));
        offerRequestLog.setIp(ip);
        offerRequestLog.setOfferid(offer.getId()+"");
        offerRequestLog.setCountry(country);
        offerRequestLog.setReqtime(new Date());
        offerRequestLog.setSubId(subPubId);
        offerRequestLog.setTrafficId(campaign.getTrafficId()+"");
        offerRequestLog.setType(campaign.getType());
        offerRequestLog.setUniqueKey(uniqueKey);

        offerRequestLogMapper.recordOfferRequestLog(offerRequestLog);
        redisUtil.recordOfferObtain(campaignCode,bestOfferId,targetOfferRate.getCountry(),targetOfferRate.getMobileCarrier());
        secondCacheService.recordClickInfo(uniqueKey,userClickInfo);
        return fitCampaignUrl;
    }

    private JSONObject queryStringToJson(String queryString){
        String []params=queryString.split("&");
        JSONObject json=new JSONObject();
     for (String param:params){
            String[] elements=param.split("=");
            if(elements.length!=2){
                continue;
            }
            json.put(elements[0],elements[1]);
        }
        return json;
    }


}
