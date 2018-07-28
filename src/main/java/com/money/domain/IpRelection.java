package com.money.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IpRelection implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long ipFrom;
    private Long ipTo;
    private String countryCode;
    private String countryName;
    private String mobileCarrier;
}
