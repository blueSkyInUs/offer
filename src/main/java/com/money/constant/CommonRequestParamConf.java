package com.money.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonRequestParamConf {

    CLICKID("txid","{clickId}"),
    PAYOUT("payout","{payout}"),
    CAMPAIGNCODE("cid","{campaignId}"),
    STATUS("status","{status}"),
    SUBPUBID("subpubid","{subpubId}");
    String requestParam;
    String replaceString;

    public String getRequestParam(){
        return requestParam;
    }
    public String getReplaceString(){
        return replaceString;
    }

}
