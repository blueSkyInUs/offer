package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferInTrafficStatistics {
    private int offerId;
    private int requestCount;
    private int successCallbackCount;
}
