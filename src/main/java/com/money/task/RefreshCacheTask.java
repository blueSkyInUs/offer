package com.money.task;

import com.money.service.CacheService;
import com.money.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshCacheTask {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisUtil redisUtil;

    @Scheduled(cron = "0 0/30 * * * *")
    public void refreshCache(){
        cacheService.init();
    }


    @Scheduled(cron = "0 0/7 * * * *")
    public void refreshOfferCounter(){
        redisUtil.refreshToRedis();
    }

}
