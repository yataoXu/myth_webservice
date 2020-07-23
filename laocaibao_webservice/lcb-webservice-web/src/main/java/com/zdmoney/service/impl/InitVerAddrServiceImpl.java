/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.service.impl;  

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.InitVerAddrMapper;
import com.zdmoney.models.InitVerAddr;
import com.zdmoney.service.InitVerAddrService;

@Service
@Transactional
public class InitVerAddrServiceImpl implements InitVerAddrService{
	
	@Autowired 
	InitVerAddrMapper initVerAddrMapper;
	
	Map<String,List<InitVerAddr>> mapResult=new HashMap<String,List<InitVerAddr>>();
	

    public List<InitVerAddr> selectByClientVer(Map<String,Object> map){
    	List<InitVerAddr> initList=mapResult.get("initList");
    	if(initList==null){
    		initList = initVerAddrMapper.selectByClientVer(map);
    		mapResult.put("initList", initList);
    	}
    	return initList;
	}
    
    public Integer insertVerAddr(InitVerAddr initVerAddr)throws BusinessException {
    	try {
    		Integer addObj=initVerAddrMapper.insertVerAddr(initVerAddr);
        	mapResult.clear();
        	List<InitVerAddr> initList = initVerAddrMapper.selectByClientVer(new HashMap<String,Object>());
        	mapResult.put("initList", initList);
        	return addObj;
		} catch (Exception e) {
			throw new BusinessException(e);
		}
    	
    }
    
    public Integer delVerAddr(String clientVer)throws BusinessException {
    	try {
    		Integer delObj=initVerAddrMapper.delVerAddr(clientVer);
        	mapResult.clear();
        	List<InitVerAddr> initList = initVerAddrMapper.selectByClientVer(new HashMap<String,Object>());
        	mapResult.put("initList", initList);
        	return delObj;
		} catch (Exception e) {
			throw new BusinessException(e);
		}
    	
    }
    
    public Integer editVerAddr(InitVerAddr initVerAddr)throws BusinessException{
    	try {
    		Integer editObj=initVerAddrMapper.editVerAddr(initVerAddr);
        	mapResult.clear();
        	List<InitVerAddr> initList = initVerAddrMapper.selectByClientVer(new HashMap<String,Object>());
        	mapResult.put("initList", initList);
        	return editObj;
		} catch (Exception e) {
			throw new BusinessException(e);
		}
    }

}
 