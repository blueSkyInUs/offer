package com.money.service;

import com.alibaba.fastjson.JSONArray;
import com.money.constant.RedisKey;
import com.money.domain.*;
import com.money.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class CacheService {
    @Autowired
    private AffiliateNetworkMapper affiliateNetworkMapper;
    @Autowired
    private CampaignMapper campaignMapper;
    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private TrafficSourceMapper trafficSourceMapper;
    @Autowired
    private NormalConfigMapper normalConfigMapper;
    @Autowired
    private OfferInCampaignMapper offerInCampaignMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    Map<Integer,AffiliateNetwork> affiliateNetworkMap=new HashMap<>();
     Map<String,Campaign> campaignMap=new HashMap<>();
    Map<Integer,Offer> offerMap=new HashMap<>();
    Map<Integer,TrafficSource> trafficSourceMap=new HashMap<>();
    Map<String,String> normalConfigMap=new HashMap<>();
    Map<String,List<OfferRate>>offerRatemap=new HashMap<>();

    public String getConfValueOrDefault(String key,String defaultValue){
        String value= normalConfigMap.get(key);
        return value!=null?value:defaultValue;
    }

    public List<OfferRate>  getOfferInCampaign(String  compaignCode){
        return offerRatemap.getOrDefault(compaignCode,new ArrayList<>());
    }


    public Map<String,List<OfferRate>> getAllOfferRate(){
        return  offerRatemap;
    }

    public Set<String> getCampaignCode(){
        return campaignMap.keySet();
    }

    public TrafficSource getTrafficSourceById(int trafficId){
        return trafficSourceMap.get(trafficId);
    }

    public Offer getOfferById(int offerId){
        return offerMap.get(offerId);
    }

    public Campaign getCampaignByCode(String campaignCode){
        return campaignMap.get(campaignCode);
    }

    public AffiliateNetwork getAffiateNetworkById(int affId){
        return affiliateNetworkMap.get(affId);
    }

    @PostConstruct
    public void init(){
        MDC.put("traceId", UUID.randomUUID().toString());
        try{
            Map<Integer,AffiliateNetwork> affiliateNetworkMapTmp=new HashMap<>();
            Map<String,Campaign> campaignMapTmp=new HashMap<>();
            Map<Integer,Offer> offerMapTmp=new HashMap<>();
            Map<Integer,TrafficSource> trafficSourceMapTmp=new HashMap<>();
            Map<String,String> normalConfigMapTmp=new HashMap<>();
            Map<String,List<OfferRate>> offerRateMapTmp=new HashMap<>();
            Map<String,List<OfferInCampaign>> tmp=new HashMap<>();

            List<OfferInCampaign> offerInCampaigns=offerInCampaignMapper.selectAllOfferInCampign();
            offerInCampaigns.stream().forEach(offerInCampaign -> {
                 List<OfferInCampaign> entry=tmp.get(offerInCampaign.getCampaignCode());
                 if (Objects.isNull(entry)){
                     entry=new ArrayList<>();
                     tmp.put(offerInCampaign.getCampaignCode(),entry);
                 }
                 entry.add(offerInCampaign);
            });
            List<AffiliateNetwork> affiliateNetworks=affiliateNetworkMapper.selectAllAffiliateNetwork();
            affiliateNetworks.stream().forEach(affiliateNetwork -> {
                affiliateNetworkMapTmp.put(affiliateNetwork.getId(),affiliateNetwork);
            });
            List<Campaign> campaigns=campaignMapper.selectAllCampaign();
            Map<Integer,Campaign> idCampignMaps=new HashMap<>();
            campaigns.stream().forEach(campaign -> idCampignMaps.put(campaign.getId(),campaign));

            campaigns.stream().forEach(campaign -> {
                campaignMapTmp.put(campaign.getCampaignCode(),campaign);
                List<OfferInCampaign> offerInCampaigns1;

                //this mean ,it's depends on other camp
                if ("2".equals(campaign.getIsTemplate())){
                     Integer id=campaign.getTempId();
                     if (Objects.isNull(idCampignMaps.get(id))){
                         log.info("campid:{} not exist",id);
                         return;
                     }
                     String campaignCode=idCampignMaps.get(id).getCampaignCode();
                     offerInCampaigns1=tmp.getOrDefault(campaignCode,new ArrayList<>());
                }else{
                    offerInCampaigns1=tmp.getOrDefault(campaign.getCampaignCode(),new ArrayList<>());
                }
                List<OfferRate> offerRates=new ArrayList<>();
                for (OfferInCampaign offerInCampaign1:offerInCampaigns1){
                    OfferRate offerRate=new OfferRate();
                    offerRate.setOfferId(offerInCampaign1.getOfferId());
                    offerRate.setRate(offerInCampaign1.getWeight());
                    offerRate.setCountry(offerInCampaign1.getCountry());
                    offerRate.setMobileCarrier(offerInCampaign1.getOperators());
                    offerRates.add(offerRate);
                }
                offerRateMapTmp.put(campaign.getCampaignCode(),offerRates);
            });
            log.info("offerRateMapTmp:{}",offerRateMapTmp);
            List<Offer> offers=offerMapper.selectAllOffers();
            offers.stream().forEach(offer -> {
                offerMapTmp.put(offer.getId(),offer);
            });
            List<TrafficSource> trafficSources=trafficSourceMapper.selectAllTrafficSource();
            trafficSources.stream().forEach(trafficSource -> {
                trafficSourceMapTmp.put(trafficSource.getId(),trafficSource);
            });
            List<NormalConfig> normalConfigs=normalConfigMapper.selectAllNormalConfig();
            normalConfigs.stream().forEach(normalConfig -> {
                normalConfigMapTmp.put(normalConfig.getConfKey(),normalConfig.getConfValue());
            });

            // if count(offer) in traffic , now clear Statistics
            offerRateMapTmp.entrySet().forEach(entry->{
                 if (offerRatemap.size()!=0 && entry.getValue().size()!=offerRatemap.getOrDefault(entry.getKey(),new ArrayList<>()).size()){
                     //clear this traffic count data
                     log.info("offer in campaign:{} count change,clear statistics data",entry.getKey());
                     clearStatisticsData(entry.getKey());
                 }
            });
            this.affiliateNetworkMap=affiliateNetworkMapTmp;
            this.campaignMap=campaignMapTmp;
            this.offerMap=offerMapTmp;
            this.trafficSourceMap=trafficSourceMapTmp;
            this.normalConfigMap=normalConfigMapTmp;
            this.offerRatemap=offerRateMapTmp;
            offerRatemap.entrySet().parallelStream().filter(entry -> campaignMap.get(entry.getKey()).canAuto()).forEach(this::adjustBestOffer);
        }catch (Exception exp){
            log.error("刷新缓存出错"+exp.getMessage(),exp);
        }
        MDC.remove("traceId");
    }

    public void clearStatisticsData(String compaignCode){
        redisTemplate.delete(RedisKey.OFFER_CALLBACK_AMOUNT_SORTSET+"1:"+compaignCode);
        redisTemplate.delete(RedisKey.OFFER_OBTAIN_COUNT_SORTSET+"1:"+compaignCode);
    }
    /**
     * select the best offer in this traffic
     * @return
     */
    public void adjustBestOffer(Map.Entry<String ,List<OfferRate>> data){
        String campaignCode=data.getKey();
        List<OfferRate> offerRates=data.getValue();
        double totalScore=0d;
        int autoOptThreshold=Integer.parseInt(getConfValueOrDefault("opt_threshold_sample_total_int","10000"));
        Map<Object,Object> requestMap=redisTemplate.opsForHash().entries(RedisKey.OFFER_OBTAIN_COUNT_SORTSET+"1:"+campaignCode);
        Map<Object,Object> callbackMap=redisTemplate.opsForHash().entries(RedisKey.OFFER_CALLBACK_AMOUNT_SORTSET+"1:"+campaignCode);
        log.info("campcode:{},stastics origin data:request:{},amount:{}",campaignCode,requestMap,callbackMap);
        long total=0;
        for (Object count:requestMap.values()){
            total+=Long.parseLong((String)count);
        }
        if (total<=autoOptThreshold){
            //sample is less, just follow the db setting,don't opt
            return ;
        }
        Map<String,Double> offerScore=new HashMap<>();
        int count30Upper=0;
        for (Map.Entry<Object,Object> entry:requestMap.entrySet()){
            long money=Long.parseLong( (String)callbackMap.getOrDefault(entry.getKey(),"0"));
            double myScore=(money*1.0/Long.parseLong((String)entry.getValue()));
            //低于 0.5 或者高于30 需特殊处理  先标记为-1
            if (myScore>=30 || myScore<=0.5){
                offerScore.put((String)entry.getKey(),-1d);
                count30Upper++;
            }else{
                offerScore.put((String)entry.getKey(),myScore);
                totalScore+=myScore;
            }

        }
        log.info("{} socres:{}",campaignCode,offerScore);
        final double tmp=totalScore;
        //低于 0.5 或者高于30 的流量总共只能占用10%
        if (tmp!=0){
            int rate=100;
            int avgRate=0;
            if (count30Upper!=0){
                avgRate=10/count30Upper==0?1:10/count30Upper;
                rate=90;
            }
            int tempRate=rate;
            int tempAvgRate=avgRate;
            offerRates.stream().forEach(offerRate -> {
                double score=offerScore.getOrDefault(getOfferWithCountryAndCarrier(offerRate.getOfferId(),offerRate.getCountry(),offerRate.getMobileCarrier()),0d);
                if (score!=-1){
                    offerRate.setRate((int)(score/tmp*tempRate));
                }else{
                    offerRate.setRate(tempAvgRate);
                }
            });
        }
        log.info("campaignCode:{}  offerRate:{}",campaignCode, JSONArray.toJSON(offerRates));
    }


    public void clearAllStatisticsData() {
        for (String campaignCode:campaignMap.keySet()){
            clearStatisticsData(campaignCode);
        }
    }


    private String getOfferWithCountryAndCarrier(int offerId,String country,String carrier){
        return offerId+"-"+country+"-"+carrier;
    }
}
