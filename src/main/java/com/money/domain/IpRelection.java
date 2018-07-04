package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IpRelection {
    private Long id;
    private Long ipFrom;
    private Long ipTo;
    private String countryCode;
    private String countryName;
    private String mobileCarrier;
}
