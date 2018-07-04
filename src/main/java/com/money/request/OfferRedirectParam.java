package com.money.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferRedirectParam {
    private String clickId;
    private String campaignId;
    private String campaignName;
    private Integer trafficSourceId;
    private String trafficSourceName;
    private String landingId;
    private String landingName;
    private Integer offerId;
    private String offerName;
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
    private String userAgent;
    private String ip;
    private String trackingDomain;
    private String referrerDomain;
    private String lang;
    private String connectionType;
    private String mobileCarrier;
}
