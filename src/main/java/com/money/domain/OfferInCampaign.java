package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferInCampaign {
    private String campaignCode;
    private Integer offerId;
    private Integer weight;
    private String country;
    private String operators;
}
