package com.money.mapper;

import com.money.domain.NormalConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NormalConfigMapper {
    @Select("select * from tb_normal_config")
    List<NormalConfig> selectAllNormalConfig();
}
