package com.money.mapper;

import com.money.domain.Offer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OfferMapper {
    @Select("select * from tb_offers")
    List<Offer> selectAllOffers();
}
