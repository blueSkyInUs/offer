package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffiliateNetwork {
    private Integer id;
    private String name;
    private String postbackUrl;
    private String securePostbackUrl;
    private Integer retryTimes;
    private Integer postbackStatus;
    private String postbackIpWhiteList;
}
