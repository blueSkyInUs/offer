package com.money.constant;

public enum OfferPayoutType {
    STATIC("0"),DYNAMIC("1");
    private String code;
    OfferPayoutType(String code){
        this.code=code;
    }
    public String getCode(){
        return code;
    }
}
