package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsAmountOutInfo {
    private String statisticKey;
    private Double totalMoney;

    public Double getTotalMoney(){
        if (Objects.nonNull(totalMoney)){
            return totalMoney;
        }
        return 0d;
    }
}
