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
}
