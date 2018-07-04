package com.money.mapper;

import com.money.domain.OfferInCampaign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OfferInCampaignMapper {
    @Select("select * from tb_campaign_offers_config")
    List<OfferInCampaign> selectAllOfferInCampign();
}
