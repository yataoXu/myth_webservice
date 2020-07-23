package com.zdmoney.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TaskThreadPoolConfig
 * @Description 异步任务线程池配置
 * @Author huangcy
 * @Date 2018/9/4 10:16
 * @Version 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.task.pool")
public class TaskThreadPoolConfig {

	//核心线程数
	private int corePoolSize = 5;

	//最大线程数
	private int maxPoolSize = 50;

	//线程池维护线程所允许的空闲时间
	private int keepAliveSeconds = 60;

	//队列长度
	private int queueCapacity = 10000;

	//线程名称前缀
	private String threadNamePrefix = "Lcb_Open_AsyncTask-";
}
