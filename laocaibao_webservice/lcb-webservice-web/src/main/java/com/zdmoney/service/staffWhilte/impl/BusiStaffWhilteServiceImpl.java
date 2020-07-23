package com.zdmoney.service.staffWhilte.impl;

import com.zdmoney.mapper.staffWhilte.BusiStaffWhilteMapper;
import com.zdmoney.models.BusiStaffWhilte;
import com.zdmoney.service.staffWhilte.IBusiStaffWhilteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @date 2019-03-13 11:08:53
 */
@Slf4j
@Service
public class BusiStaffWhilteServiceImpl implements IBusiStaffWhilteService {

    @Autowired
    private BusiStaffWhilteMapper busiStaffWhilteMapper;

    @Override
    public List<BusiStaffWhilte> queryBusiStaffWhilte(Map<String, Object> paramsMap){
        return busiStaffWhilteMapper.queryBusiStaffWhilte(paramsMap);
    }

    @Override
    public int updateBusiStaffWhilte(BusiStaffWhilte busiStaffWhilte){
        return busiStaffWhilteMapper.updateBusiStaffWhilte(busiStaffWhilte);
    }

    @Override
    public int saveBusiStaffWhilte(BusiStaffWhilte busiStaffWhilte){
        return busiStaffWhilteMapper.saveBusiStaffWhilte(busiStaffWhilte);
    }

    @Override
    public int removeBusiStaffWhilteById(long id){
        return busiStaffWhilteMapper.removeBusiStaffWhilteById(id);
    }


}
