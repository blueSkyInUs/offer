package com.money.service;


import com.money.domain.IpRelection;
import com.money.mapper.IpRelectionMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class IpCacheService {

    @Autowired
    IpRelectionMapper ipRelectionMapper;

    @Autowired
    IpReflectionService ipReflectionService;

    private static final CountDownLatch countDownLatch=new CountDownLatch(80);

    @PostConstruct
    @SneakyThrows
    public void init(){
        log.info("begin init ip tables....");
        long beginTimes=System.currentTimeMillis();
        TreeMap<Long,IpRelection> tempMap=ipReflectionService.TREE_MAP;
        if (tempMap.size()>0){
            log.info("already read from file ,just ignore");
            return;
        }

        for (int i=0;i<80;i++){
            int temp=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        recordData(temp);
                    }catch (Exception exp){
                        log.error("录入数据出错:"+exp.getMessage(),exp);
                    }
                    countDownLatch.countDown();

                }
            }).start();
        }
        countDownLatch.await();
        long endTimes=System.currentTimeMillis();
        log.info("init ip tables cost:{}s",(endTimes-beginTimes)/1000);
        log.info("size:{}",ipReflectionService.TREE_MAP.size());
        ipReflectionService.writeMap();



    }

    private void recordData( int i) {
        long beginAdd=12772653;

        log.info("current index:{}",i);
        List<IpRelection> ipRelections= ipRelectionMapper.selectByIdRange(beginAdd+i*25000,beginAdd+(i+1)*25000);
        log.info("size:{}",ipRelections.size());
        ipReflectionService.record(ipRelections);
    }
}
