package com.money.mapper;

import com.money.domain.TrafficNotifyLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrafficNotifyLogMapper {

    @Insert("insert into hwlk_traffic_src_notify(compaign_code,clickid,payout,status,aff_id,offerid,camp_id,traffic_id,sub_id,ext_param1,ext_param2,bus_id,country,carrier,kou_flag,num,notify_url,notify_rescode,notify_lasttime,regtime,order_status,type,unique_key)" +
            "                    values(#{compaignCode},#{clickid},#{payout},#{status},#{affId},#{offerid},#{campId},#{trafficId},#{subId},#{extParam1},#{extParam2},#{busId},#{country},#{carrier},#{kouFlag},#{num},#{notifyUrl},#{notifyRescode},#{notifyLasttime},#{regtime},#{orderStatus},#{type},#{uniqueKey})")
    void recoreTrafficNotifyCallback(TrafficNotifyLog affiliateNotifyLog);

}
