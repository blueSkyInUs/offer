package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsAmountInfo {
    private String statisticKey;
    private Double total;

    public Double getTotal(){
        if (Objects.nonNull(total)){
            return total;
        }
        return 0d;
    }

}
