package com.money.mapper;

import com.money.domain.Campaign;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CampaignMapper {
    @Select("select * from tb_campaign")
    List<Campaign> selectAllCampaign();
}
