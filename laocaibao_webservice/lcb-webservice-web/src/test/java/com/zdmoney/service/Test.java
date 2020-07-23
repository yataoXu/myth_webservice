package com.zdmoney.service;

import com.zdmoney.utils.Base64;
import com.zdmoney.utils.CoreUtil;

import java.text.MessageFormat;
import java.util.Date;


/**
 * Created by 00225181 on 2016/1/13.
 */
public class Test{

    public static void main(String[] args) throws Exception{
        Double d = 5000000.55d;
        String str = CoreUtil.getAmtInWords(d);
        System.out.println(str);
    }
}
