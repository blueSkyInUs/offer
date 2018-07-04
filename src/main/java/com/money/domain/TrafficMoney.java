package com.money.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficMoney {
    private String statisticKey;
    private Double totalMoney;
    private Long totalCallback;
}
