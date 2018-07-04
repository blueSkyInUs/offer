package com.money.constant;

public enum CampaignType {
    TEST("0"),NORMAL("1");
    private String code;
    CampaignType(String code){
        this.code=code;
    }
    public String getCode(){
        return code;
    }
}
