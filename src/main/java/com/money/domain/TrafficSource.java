package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficSource {
    private Integer id;
    private String postbackUrl;
    private String availableTokens;
    private Integer externalId;
    private Integer status;
    private Integer retryTimes;
}
