package com.zdmoney.mapper;

import java.util.List;
import java.util.Map;

import com.zdmoney.models.InitVerAddr;

public interface InitVerAddrMapper {

    /**
     * 查询客户端版本对应的url
     * @param map  key=clientVer(String)
     * @return 封装的是用户ID及url地址、客户端版本
     */
	List<InitVerAddr> selectByClientVer(Map<String,Object> map);

	/**
	 * 新增记录
	 * @param initVerAddr
	 * @return
	 */
	Integer insertVerAddr(InitVerAddr initVerAddr);

	/**
	 * 删除版本号对应的url地址
	 * @param clientVer
	 * @return
	 */
	Integer delVerAddr(String clientVer);

	/**
	 * 更新版本号对应的url地址
	 * @param initVerAddr
	 * @return
	 */
	Integer editVerAddr(InitVerAddr initVerAddr);

}