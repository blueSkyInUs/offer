package com.money.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficSourceCallbackParam {
    private String externalId;
    private String payout;
    private String campaignCpa;
    private String campaignId;
    private String trafficSourceId;
    private String landingId;
    private String offerId;
    private String deviceType;
    private String deviceVendor;
    private String deviceModel;
    private String browser;
    private String browserVersion;
    private String os;
    private String osVersion;
    private String country;
    private String countryCode;
    private String city;
    private String region;
    private String isp;
    private String connectionType;
    private String mobileCarrier;
    private String ip;
    private String referrerDomain;
    private String lang;
    private String transactionId;
    private String clickId;
    private String status;
    private String currency;
}
