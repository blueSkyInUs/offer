package com.money.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UniqueKeyUtil {

    private UniqueKeyUtil(){};

    public static String generateKey(){
       return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ UUID.randomUUID().toString();
    }
}
