package com.money.controller;


import com.money.config.CommonProp;
import com.money.dto.BaseResult;
import com.money.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/mock/user")
@Slf4j
public class MockUserClickAndCallbackController {

    private static final String PASSWORD="23nsfi2";

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CommonProp commonProp;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping(value = "/click",method = RequestMethod.POST)
    public BaseResult mock(String password){
        BaseResult baseResult=new BaseResult();
        if (!password.equals(PASSWORD)){
            baseResult.setCode(500);
            baseResult.setMsg("unlegal");
            return baseResult;
        }

        Set<String> campaignCodes=cacheService.getCampaignCode();

        campaignCodes.parallelStream().forEach(this::mockHttp);
        return baseResult;
    }

    private void mockHttp(String campaignCode) {
//        String serverUrl="http://47.75.64.116:8080/campaign";

        String serverUrl=commonProp.getServerUrlPrefix();
        for (int i=0;i<2200;i++){

            log.info("current: {}",i);
            String clickId= UUID.randomUUID().toString();

            try{
                restTemplate.getForObject(serverUrl+"/offer/url/"+campaignCode+"?txid="+clickId,String.class);
            }catch (Exception exp){
                log.error("request server error:"+exp.getMessage(),exp);
            }

            int score=new Random().nextInt(100);
//            if (score>30){
                try{
                    restTemplate.getForObject(serverUrl+"/offer/callback?txid="+clickId+"&cid="+campaignCode+"&payout=0.25",String.class);
                }catch (Exception exp){
                    log.error("request server error:"+exp.getMessage(),exp);
                }
//            }



        }

    }
}
