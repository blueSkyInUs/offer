package com.money.task;

import com.money.service.CacheService;
import com.money.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RefreshCacheTask {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisUtil redisUtil;

    @Scheduled(cron = "0 0/30 * * * *")
    public void refreshCache(){
        MDC.put("traceId", UUID.randomUUID().toString());
        cacheService.init();
    }


    @Scheduled(cron = "0 0/7 * * * *")
    public void refreshOfferCounter(){
        MDC.put("traceId", UUID.randomUUID().toString());

        redisUtil.refreshToRedis();
    }


    @Scheduled(cron = "0 10 0 * * *")
    public void clearStasticData(){
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info("clear stasticdata begin.....");
        cacheService.clearAllStatisticsData();
        log.info("clear stasticdata end.....");
    }

}
