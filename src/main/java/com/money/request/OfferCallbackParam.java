package com.money.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferCallbackParam {
    private String clickId;
    private String status;
    private String payout;
    private Integer affid;
    private Integer offerId;
    private Integer trafficId;
}
