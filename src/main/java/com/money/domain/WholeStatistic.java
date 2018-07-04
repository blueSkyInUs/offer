package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WholeStatistic {
    private String country;
    private Double totalClick;
    private Double availableClick;
    private Double amount;
    private Double cost;
    private String statisticKey;
    private String carrier;
    private String offerId;

}
