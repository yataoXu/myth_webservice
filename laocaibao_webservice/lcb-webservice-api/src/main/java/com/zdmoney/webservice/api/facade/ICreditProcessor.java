package com.zdmoney.webservice.api.facade;

/**
 * Created by user on 2017/10/20.
 */
public interface ICreditProcessor<T> {

    void preProcess(T arg);

    void afterProcess(T arg);
}
