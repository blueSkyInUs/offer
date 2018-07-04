package com.money.domain;

import com.money.constant.OfferPayoutType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Offer {
    private Integer id;
    private Integer affiliateNetworkId;
    private String name;
    private String url;
    private String postbackUrl;
    private String paramsReplace;
    private Integer busId;
    private Integer status;
    private String remark;
    private String payoutType;
    private String payoutAmount;
    private Integer bakOfferid;
    private String supIsp;
    public boolean isStaticPrice(){
        return OfferPayoutType.STATIC.getCode().equals(payoutType);
    }
}
