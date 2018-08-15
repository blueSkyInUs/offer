package com.money.mapper;

import com.money.domain.OfferRequestLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OfferRequestLogMapper {

    @Insert("insert into hwlk_request(compaign_code,clickid,aff_id,offerid,camp_id,traffic_id,sub_id,ext_param1,ext_param2,bus_id,country,carrier,ip,reqtime,type,unique_key)" +
                         "values(#{compaignCode},#{clickid},#{affId},#{offerid},#{campId},#{trafficId},#{subId},#{extParam1},#{extParam2},#{busId},#{country},#{carrier},#{ip},#{reqtime},#{type},#{uniqueKey})")
    void recordOfferRequestLog(OfferRequestLog offerRequestLog);

    @Select("select clickid,compaign_code,offerid,country,carrier from hwlk_request where unique_key=#{uniqueKey} order by id desc limit 1")
    OfferRequestLog obtainRequestLobByUniqueKey(@Param("uniqueKey") String uniqueKey);
}
