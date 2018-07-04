package com.money.constant;

public class RedisKey {

    private RedisKey(){};

    public static final String PROJECT_PREFIX="money:";

    public static final String OFFER_CALLBACK_AMOUNT_SORTSET=PROJECT_PREFIX+"offer:amount";

    public static final String OFFER_OBTAIN_COUNT_SORTSET=PROJECT_PREFIX+"offer:obtain";

    public static final String UNIQUE_KEY_INFO=PROJECT_PREFIX+"campaign:uniqueKey";

}
