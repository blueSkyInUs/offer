package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferRequestLog {
    private Integer id;
    private String compaignCode;
    private String clickid;
    private String affId;
    private String offerid;
    private String trafficId;
    private String subId;
    private String campId;
    private String extParam1;
    private String extParam2;
    private String busId;
    private String carrier;
    private String ip;
    private Date reqtime;
    private String country;
    private String type;
    private String uniqueKey;
}
