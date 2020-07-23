/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.utils;

import com.zdmoney.trace.utils.LcbTraceRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AsyncTaskUtil
 * <p/>
 * Author: Hao Chen
 * Date: 2016-08-11 17:39
 * Mail: haoc@zendaimoney.com
 */
public class AsyncTaskUtil {

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void asyncTask(LcbTraceRunnable runnable) throws InterruptedException {
        executor.submit(runnable);
    }

}