package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignStatistics {
    private int id;
    private int stasticType;
    private String staticKey;
    private String country;
    private String carrier;
    private String offerId;
    private Double totalClick;
    private Double totalMoney;
    private Double cost;
    private Double profit;
    private Double conversions;
    private Double ecpm;
    private Double translatePercent;
    private String statisticsDate;
}
