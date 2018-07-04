package com.money.mapper;

import com.money.domain.CampaignStatistics;
import com.money.domain.WholeStatistic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CampaignStatisticsMapper {

    @Insert("insert into hwlk_campaign_statistics_new(stastic_type,static_key,country,carrier,offer_id,total_click,total_money,conversions,ecpm,statistics_date,profit,cost,translate_percent) " +
            "                             values(#{stasticType},#{staticKey},#{country},#{carrier},#{offerId},#{totalClick},#{totalMoney},#{conversions},#{ecpm},#{statisticsDate},#{profit},#{cost},#{translatePercent})")
    void recordCampaignStatistics(CampaignStatistics campaignStatistics);



    @Select("select r1.${key} as statisticKey,count(*) as totalClick,sum(case when a1.unique_key is not null then 1 else 0 end) as availableClick,\n" +
            "  sum(case when a1.payout is null then 0 else a1.payout end) as amount,sum(case when t1.payout is not null and t1.type='1' and t1.kou_flag='0' and t1.notify_rescode='200' then t1.payout else 0 end) as cost\n" +
            " from hwlk_request_${stasticDate} r1 left join hwlk_affiliate_notify_${stasticDate} a1 on r1.unique_key=a1.unique_key \n" +
            "\t\t\t\tleft join hwlk_traffic_src_notify_${stasticDate} t1 on r1.unique_key=t1.unique_key \n" +
            "      group by r1.${key}")
    List<WholeStatistic> statisticsAll(@Param("key") String key, @Param("stasticDate") String stasticDate);

    @Select("select r1.compaign_code as statisticKey,r1.country as country,r1.carrier as carrier,r1.offerid as offerId,count(*) as totalClick,sum(case when a1.unique_key is not null then 1 else 0 end) as availableClick,\n" +
            "  sum(case when a1.payout is null then 0 else a1.payout end) as amount,sum(case when t1.payout is not null and t1.type='1' and t1.kou_flag='0' and t1.notify_rescode='200' then t1.payout else 0 end) as cost\n" +
            " from hwlk_request${day} r1 left join hwlk_affiliate_notify${day} a1 on r1.unique_key=a1.unique_key \n" +
            "\t\t\t\tleft join hwlk_traffic_src_notify${day} t1 on r1.unique_key=t1.unique_key   where r1.reqtime between #{beginStasticDate} and #{endStasticDate} \n" +
            "      group by r1.compaign_code,r1.country,r1.carrier,r1.offerid ")
    List<WholeStatistic> statisticsInCamp(@Param("day")String day,@Param("beginStasticDate") String beginStasticDate,@Param("endStasticDate") String endStasticDate);


}
