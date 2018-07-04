package com.money.task;


import com.alibaba.fastjson.JSONArray;
import com.money.constant.StatisticsType;
import com.money.domain.CampaignStatistics;
import com.money.domain.WholeStatistic;
import com.money.mapper.CampaignStatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class StatisticsEveryDayTask {

    @Autowired
    private CampaignStatisticsMapper campaignStatisticsMapper;

    @Scheduled(cron = "0 0 3 * * *")
    public void statistics(){
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info("statistics begin.....");
        long beginTime=System.currentTimeMillis();
        for (StatisticsType statisticsType:StatisticsType.values()){
            if (statisticsType==StatisticsType.CAMPAIGNCODE){
                continue;
            }
            try{
                statisticsAll(statisticsType);
            }catch (Exception exp){
                log.error("count:{"+statisticsType.getCode()+"} error "+exp.getMessage(),exp);
            }
        }
        long endTime=System.currentTimeMillis();
        log.info("cost:{}ms",endTime-beginTime);
        MDC.remove("traceId");
    }
    @Scheduled(cron = "0 10 0/1 * * *")
    public void statisticsCampaginInHour(){
        MDC.put("traceId", UUID.randomUUID().toString());
        log.info("statistics begin.....");
        long beginTime=System.currentTimeMillis();
        StatisticsType statisticsType=StatisticsType.CAMPAIGNCODE;
            try{
                statisticsAll(statisticsType);
            }catch (Exception exp){
                log.error("count:{"+statisticsType.getCode()+"} error "+exp.getMessage(),exp);
            }
        long endTime=System.currentTimeMillis();
        log.info("cost:{}ms",endTime-beginTime);
        MDC.remove("traceId");
    }

    private void statisticsAll(StatisticsType statisticsType) {
        LocalDateTime localDateTime=LocalDateTime.now();
        LocalDateTime oneHourBefore=localDateTime.minusHours(1);
        String oneHourBeforeDay=oneHourBefore.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nowDay=localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String day="";
        if (!oneHourBeforeDay.equals(nowDay)){
            day="_"+oneHourBeforeDay;
        }
        List<WholeStatistic> wholeStatistics=statisticsType==StatisticsType.CAMPAIGNCODE
                ?campaignStatisticsMapper.statisticsInCamp(day,oneHourBefore.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00")),localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00")))
                :campaignStatisticsMapper.statisticsAll(statisticsType.getCode(),LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        log.info("result:{}", JSONArray.toJSONString(wholeStatistics));
        for (WholeStatistic wholeStatistic:wholeStatistics){
            CampaignStatistics campaignStatistics=new CampaignStatistics();
            campaignStatistics.setStasticType(statisticsType.getIndex());
            campaignStatistics.setStaticKey(wholeStatistic.getStatisticKey());
            campaignStatistics.setCountry(wholeStatistic.getCountry());
            campaignStatistics.setCarrier(wholeStatistic.getCarrier());
            campaignStatistics.setOfferId(wholeStatistic.getOfferId());
            campaignStatistics.setTotalClick(wholeStatistic.getTotalClick());
            campaignStatistics.setTotalMoney(wholeStatistic.getAmount());
            campaignStatistics.setProfit(wholeStatistic.getAmount()-wholeStatistic.getCost());
            campaignStatistics.setCost(wholeStatistic.getCost());
            campaignStatistics.setConversions(wholeStatistic.getAvailableClick());
            campaignStatistics.setStatisticsDate(statisticsType==StatisticsType.CAMPAIGNCODE?oneHourBefore.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00")):LocalDate.now().minusDays(1).toString());
            double ecpm=wholeStatistic.getAmount()/wholeStatistic.getTotalClick()*1000;
            DecimalFormat dFormat=new DecimalFormat("#.00");
            String result=dFormat.format(ecpm);
            campaignStatistics.setEcpm(Double.valueOf(result));
            campaignStatistics.setTranslatePercent(wholeStatistic.getAvailableClick()/wholeStatistic.getTotalClick()*100);
            campaignStatisticsMapper.recordCampaignStatistics(campaignStatistics);
        }

    }

}
