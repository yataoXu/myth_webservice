package com.zdmoney.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by rui on 15/8/14.
 */
public class DateUtils {

    public static String getTimestamp() {
        SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
        return formate.format(new Timestamp(System.currentTimeMillis()));
    }

}
