package com.zdmoney.base;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;

/**
 * Created by 00225181 on 2016/1/4.
 */
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {

    static{
        System.setProperty("env","dev");
        System.setProperty("dev_meta","http://172.17.34.211:8080");
        System.setProperty("app.id","webservice");
        System.setProperty("file.encoding","UTF-8");
    }

    public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
}