package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsClickInfo {
    private String statisticKey;
    private Long total;
    public Long getTotal(){
        if (Objects.nonNull(total)){
            return total;
        }
        return 0L;
    }

}
