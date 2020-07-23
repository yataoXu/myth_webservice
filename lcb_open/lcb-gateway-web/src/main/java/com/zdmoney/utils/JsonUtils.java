package com.zdmoney.utils;

import com.google.gson.Gson;


/**
 * Created by rui on 15/8/14.
 */
public final class JsonUtils<D> {

    private JsonUtils() {
    }

    public static <D> D fromJson(String json, Class<D> d) {
        Gson gson = new Gson();
        return gson.fromJson(json, d);
    }

    public static String toJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

//    public static Map<String, String> toMap(String json) {
//        Gson gson = new Gson();
//        Map map = gson.fromJson(json, new TypeToken<Map<String, String>>() {}.getType());
//        return map;
//    }
}
