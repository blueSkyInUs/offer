package com.money.mapper;

import com.money.domain.IpRelection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IpRelectionMapper {

    @Select("select * from tb_ip_list_tmp where ip_from<=#{ipLong} and ip_to>=#{ipLong}")
    List<IpRelection> selectByIpTraslate(@Param("ipLong") long ipLong);


    @Select("select * from tb_ip_list_tmp where id between #{beginId} and #{endId} ")
    List<IpRelection> selectByIdRange(@Param("beginId") long beginId,@Param("endId") long endId);

}
