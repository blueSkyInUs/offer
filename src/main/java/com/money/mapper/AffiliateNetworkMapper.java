package com.money.mapper;

import com.money.domain.AffiliateNetwork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AffiliateNetworkMapper {

    @Select("select * from tb_affiliate_network")
    List<AffiliateNetwork> selectAllAffiliateNetwork();
}
