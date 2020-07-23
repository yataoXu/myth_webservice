package com.zdmoney.webservice.api.facade;

/**
 * open facade接口
 */
public interface IFacadeOpenService {

    /**
     * 每日中午12点对比网贷之家和捞财宝昨日成交额
     */
    void checkWdzjData();

    /**
     * 每日更新标的数据
     */
    void updateWdzjData();

	/**
	 * 网贷天眼每日更新数据
	 */
	void updateWdtyData();
}
