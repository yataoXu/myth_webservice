/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.common.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator
 * <p/>
 * Author: Hao Chen
 * Date: 2015/11/5 10:40
 * Mail: haoc@zendaimoney.com
 * $Id$
 */
public class Generator {
    public static void main(String[] args) throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(Generator.class.getResourceAsStream("/generator/generatorConfig.xml"));
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

//        try {
//            List<String> warnings = new ArrayList<String>();
//            boolean overwrite = true;
//            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//            InputStream is = classloader.getResourceAsStream("generator/generatorConfig.xml");
//            ConfigurationParser cp = new ConfigurationParser(warnings);
//            Configuration config = cp.parseConfiguration(is);
//            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//            myBatisGenerator.generate(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}