package com.money.service;

import com.money.domain.IpRelection;
import com.money.mapper.IpRelectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class IpReflectionService {
    @Autowired
    private IpRelectionMapper ipReflectionMapper;

    public static  TreeMap<Long,IpRelection> TREE_MAP;

    private ReentrantLock reentrantLock=new ReentrantLock();

    //only allow 50 thread to query db
    private Semaphore semaphore=new Semaphore(50);

    static{
        try{
            String usrHome = System.getProperty("user.home");
            if(!usrHome.endsWith("/")){
                usrHome=usrHome+"/";
            }
            String ipMapFilePath=usrHome+"ipMapFilePath.important";
            File file=new File(ipMapFilePath);
            if (file.exists()){
                FileInputStream inputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                long beginTime=System.currentTimeMillis();
                TREE_MAP=(TreeMap<Long,IpRelection>)objectInputStream.readObject();
                log.info("read from file cost:{}s",(System.currentTimeMillis()-beginTime)/1000);
            }else{
                TREE_MAP=new TreeMap<>();
            }

        }catch (Exception exp){
            log.error("read from file error"+exp.getMessage(),exp);
            TREE_MAP=new TreeMap<>();
        }

    }

    public IpRelection getRelectionByIpLong(String ip){
        Long ipLong=dot2LongIP(ip);
        log.info("mark ip:{}",ipLong);
        Map.Entry<Long,IpRelection> ipRelectionEntry=TREE_MAP.ceilingEntry(ipLong);
        if (Objects.nonNull(ipRelectionEntry)){
            IpRelection ipRelection=ipRelectionEntry.getValue();
            Long ipfrom=ipRelection.getIpFrom();
            Long ipTo=ipRelection.getIpTo();
            if (ipLong>=ipfrom && ipLong<=ipTo){
                return ipRelectionEntry.getValue();
            }
        }
//        try{
//            semaphore.acquire();
//            List<IpRelection> ipRelections=ipReflectionMapper.selectByIpTraslate(ipLong);
//            if (ipRelections.size()>0){
//                IpRelection ipRelection=ipRelections.get(0);
//                TREE_MAP.put(ipRelection.getIpTo(),ipRelection);
//                return ipRelection;
//            }
//        }catch (Exception exp){
//            log.error("obtain semaphore error "+exp.getMessage(),exp);
//        }finally {
//            semaphore.release();
//        }
         return null;
    }

    public void record(List<IpRelection> ipRelections){
        reentrantLock.lock();
        try{
            ipRelections.stream().forEach(ipRelection -> {
                TREE_MAP.put(ipRelection.getIpTo(),ipRelection);
            });
        }catch (Exception exp){
            log.error(exp.getMessage(),exp);

        }finally {
            reentrantLock.unlock();
        }
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

    public void writeMap() {
        try{
            String usrHome = System.getProperty("user.home");
            if(!usrHome.endsWith("/")){
                usrHome=usrHome+"/";
            }
            String ipMapFilePath=usrHome+"ipMapFilePath.important";
            File file=new File(ipMapFilePath);
            if (!file.exists()){
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                ObjectOutputStream objectInputStream = new ObjectOutputStream(outputStream);
                long beginTime=System.currentTimeMillis();
                objectInputStream.writeObject(TREE_MAP);
                log.info("write to file cost:{}s",(System.currentTimeMillis()-beginTime)/1000);
            }
        }catch (Exception exp){
            log.error("write to file error"+exp.getMessage(),exp);
        }
    }
}
