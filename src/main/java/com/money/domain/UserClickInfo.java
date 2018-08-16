package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserClickInfo {
    private String clickId;
    private String campaignCode;
    private Integer offerId;
    private String country;
    private String carrier;
    //这两个字段仅用作后续统计用 规避前端比如 country有global 空这种杂七杂八的鸡儿东西
    private String country_temp;
    private String carrier_temp;
}
