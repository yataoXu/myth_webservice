package com.zdmoney.facade;

import cn.hutool.core.date.DateUtil;
import com.zdmoney.service.WdzjService;
import com.zdmoney.thread.AsyncTask;
import com.zdmoney.webservice.api.facade.IFacadeOpenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Slf4j
@DependsOn({"lcbGatewayFacade"})
@Component
public class FacadeOpenService implements IFacadeOpenService {

    @Autowired
    private WdzjService wdzjService;

    @Autowired
	private AsyncTask asyncTask;

    @Override
    public void checkWdzjData() {
        log.info("Elastic job starting >>>>>> job[{}],time [{}]", "网贷之家成交额对比job", DateUtil.now());
        wdzjService.checkWdzjData();
    }

    @Override
    public void updateWdzjData() {
        log.info("Elastic job starting >>>>>> job[{}],time [{}]", "网贷之家数据更新job", DateUtil.now());
        wdzjService.updateWdzjData();
    }

	@Override
	public void updateWdtyData() {
		log.info("Elastic job starting >>>>>> job[{}],time [{}]", "网贷之家数据更新job", DateUtil.now());
		asyncTask.wdtyData();
	}
}
