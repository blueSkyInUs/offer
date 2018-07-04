package com.money.constant;

public enum OfferStatus {
    UN_AVAILABLE("0"),AVAILABLE("1");
    private String code;
    OfferStatus(String code){
        this.code=code;
    }
    public String getCode(){
        return code;
    }
}
