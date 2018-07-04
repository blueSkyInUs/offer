package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Campaign {
    private Integer id;
    private String campaignCode;
    private String name;
    private Integer trafficId;
    private String offerId;
    private String trackingDomain;
    private String percentage;
    private String campaignUrl;
    private String campaignTestUrl;
    private String campaignRedirectJs;
    private String postbackUrl;
    private String securePostbackUrl;
    private String postbackScript;
    private Integer busId;
    private Double contrProportion;
    private String type;
    private String status;
    private Integer contrType;
    private Integer isAuto;
    private String payoutType;
    private Double payoutValue;
    private String isTemplate;
    private Integer tempId;
    public boolean isPriceFixed(){
        return "0".equals(payoutType);
    }
    public boolean canAuto(){
        return isAuto==1;
    }
    public boolean isKouByThresholdAmount(){
        return contrType==1;
    }
}
