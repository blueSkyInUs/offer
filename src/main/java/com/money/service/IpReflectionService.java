package com.money.service;

import com.money.domain.IpRelection;
import com.money.mapper.IpRelectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Semaphore;

@Service
@Slf4j
public class IpReflectionService {
    @Autowired
    private IpRelectionMapper ipReflectionMapper;

    private static final TreeMap<Long,IpRelection> TREE_MAP=new TreeMap<>();

    //only allow 50 thread to query db
    private Semaphore semaphore=new Semaphore(50);

    public IpRelection getRelectionByIpLong(String ip){
        Long ipLong=dot2LongIP(ip);
        log.info("mark ip:{}",ipLong);
        Map.Entry<Long,IpRelection> ipRelectionEntry=TREE_MAP.ceilingEntry(ipLong);
        if (Objects.nonNull(ipRelectionEntry)){
            return ipRelectionEntry.getValue();
        }
        try{
            semaphore.acquire();
            List<IpRelection> ipRelections=ipReflectionMapper.selectByIpTraslate(ipLong);
            if (ipRelections.size()>0){
                IpRelection ipRelection=ipRelections.get(0);
                TREE_MAP.put(ipRelection.getIpTo(),ipRelection);
                return ipRelection;
            }
        }catch (Exception exp){
            log.error("obtain semaphore error "+exp.getMessage(),exp);
        }finally {
            semaphore.release();
        }
         return null;
    }


    private static long dot2LongIP(String ipstring) {
        String[] ipAddressInArray = ipstring.split("\\.");
        long result = 0;
        long ip = 0;
        for (int x = 3; x >= 0; x--) {
            ip = Long.parseLong(ipAddressInArray[3 - x]);
            result |= ip << (x << 3);    }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(dot2LongIP("61.144.175.17"));
    }
}
