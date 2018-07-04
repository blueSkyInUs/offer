package com.money.mapper;

import com.money.domain.TrafficSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrafficSourceMapper {
    @Select("select * from tb_traffic_source")
    List<TrafficSource> selectAllTrafficSource();
}
