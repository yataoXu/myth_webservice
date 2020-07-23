package com.zdmoney.service;

import com.zdmoney.mapper.BusiCashFlowMapper;
import com.zdmoney.models.BusiCashFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @date silence.cheng 16:09:12
 */
@Service
public class BusiCashFlowService{

    @Autowired
    private BusiCashFlowMapper busiCashFlowMapper;


    public int updateBusiCashFlow(BusiCashFlow busiCashFlow){
        return busiCashFlowMapper.updateBusiCashFlow(busiCashFlow);
    }

    public int saveBusiCashFlow(BusiCashFlow busiCashFlow){
        return busiCashFlowMapper.saveBusiCashFlow(busiCashFlow);
    }

    /**
     * 现金券领取
     */
    public void gainCash(){
    }

}
