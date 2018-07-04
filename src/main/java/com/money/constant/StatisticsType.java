package com.money.constant;

public enum StatisticsType {
    COUNTRY(1,"country"),OFFER(2,"offerid"),TRAFFIC(3,"traffic_id"),AFFIATE(4,"aff_id"),CAMPAIGNCODE(5,"compaign_code");
    StatisticsType(int index,String code){
        this.index=index;
        this.code=code;
    }
    private int index;
    private String code;
    public int getIndex(){
        return index;
    }
    public String getCode(){return code;}
}
