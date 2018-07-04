package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficNotifyLog {
    private Integer id;
    private String compaignCode;
    private String clickid;
    private String payout;
    private String status;
    private String affId;
    private String offerid;
    private String campId;
    private String trafficId;
    private String subId;
    private String extParam1;
    private String extParam2;
    private String busId;
    private String country;
    private String carrier;
    private String kouFlag;
    private String num;
    private String notifyUrl;
    private String notifyRescode;
    private Date notifyLasttime;
    private Date regtime;
    private String orderStatus;
    private String type;
    private String uniqueKey;
}
