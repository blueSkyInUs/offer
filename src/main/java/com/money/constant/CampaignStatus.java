package com.money.constant;

public enum CampaignStatus {

    UN_AVAILABLE("0"),AVAILABLE("1");
    private String code;
    CampaignStatus(String code){
        this.code=code;
    }
    public String getCode(){
        return code;
    }

}
