package com.money.mapper;

import com.money.domain.AffiliateNotifyLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AffiliateNotifyLogMapper {

    @Insert("insert into hwlk_affiliate_notify(compaign_code,clickid,payout,status,aff_id,offerid,camp_id,traffic_id,sub_id,ext_param1,ext_param2,bus_id,country,carrier,ip,notify_time,type,unique_key)" +
                                 "values(#{compaignCode},#{clickid},#{payout},#{status},#{affId},#{offerid},#{campId},#{trafficId},#{subId},#{extParam1},#{extParam2},#{busId},#{country},#{carrier},#{ip},#{notifyTime},#{type},#{uniqueKey})")
    void recordAffiliateNotifyCallback(AffiliateNotifyLog affiliateNotifyLog);
}
