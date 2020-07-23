package com.zdmoney.service.staffWhilte.impl;

import com.zdmoney.mapper.staffWhilte.BusiDimissionStaffWhilteMapper;
import com.zdmoney.models.BusiDimissionStaffWhilte;
import com.zdmoney.service.staffWhilte.IBusiDimissionStaffWhilteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @date 2019-02-28 09:25:53
 */
@Slf4j
@Service
public class BusiDimissionStaffWhilteServiceImpl implements IBusiDimissionStaffWhilteService {

    @Autowired
    private BusiDimissionStaffWhilteMapper busiDimissionStaffWhilteMapper;

    @Override
    public List<BusiDimissionStaffWhilte> queryBusiDimissionStaffWhilte(Map<String, Object> paramsMap){
        return busiDimissionStaffWhilteMapper.queryBusiDimissionStaffWhilte(paramsMap);
    }

    @Override
    public int updateBusiDimissionStaffWhilte(BusiDimissionStaffWhilte busiDimissionStaffWhilte){
        return busiDimissionStaffWhilteMapper.updateBusiDimissionStaffWhilte(busiDimissionStaffWhilte);
    }

    @Override
    public int saveBusiDimissionStaffWhilte(BusiDimissionStaffWhilte busiDimissionStaffWhilte){
        return busiDimissionStaffWhilteMapper.saveBusiDimissionStaffWhilte(busiDimissionStaffWhilte);
    }

    @Override
    public int removeBusiDimissionStaffWhilteById(long id){
        return busiDimissionStaffWhilteMapper.removeBusiDimissionStaffWhilteById(id);
    }

    @Override
    public BusiDimissionStaffWhilte getStaffByCmNumber(String cmNumber) {
        return busiDimissionStaffWhilteMapper.getStaffByCmNumber(cmNumber);
    }
}
