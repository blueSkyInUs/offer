package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferRate {
    private Integer offerId;
    private Integer rate;
    private String country;
    private String mobileCarrier;
    public boolean canGlobal(){
        return StringUtils.isEmpty(country)||country.contains("global")||country.contains("Global");
    }
    public boolean canFitThisIpInfo(IpRelection ipRelection){
        return ipRelection.getCountryCode().equals(country)&&(StringUtils.isEmpty(mobileCarrier)|| ipRelection.getMobileCarrier().equals(mobileCarrier));
    }

}
