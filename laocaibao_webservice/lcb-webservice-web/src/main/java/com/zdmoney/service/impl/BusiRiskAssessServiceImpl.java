package com.zdmoney.service.impl;

import com.zdmoney.mapper.BusiRiskAssessMapper;
import com.zdmoney.models.BusiRiskAssess;
import com.zdmoney.models.BusiRiskTest;
import com.zdmoney.models.bank.BusiUnbindRecord;
import com.zdmoney.service.BusiRiskAssessService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.RiskEvaluateNotifyDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gaol on 2017/5/16
 **/
@Service
@Slf4j
public class BusiRiskAssessServiceImpl implements BusiRiskAssessService {

    @Autowired
    private BusiRiskAssessMapper busiRiskAssessMapper;

    @Override
    public List<BusiRiskAssess> listBusiRiskAssess() {
        return busiRiskAssessMapper.listBusiRiskAssess();
    }

    @Override
    public void saveRiskAnswer(BusiRiskTest riskTest) {
        busiRiskAssessMapper.saveRiskAnswer(riskTest);
    }

    @Override
    public long queryRiskTest(Long customerId) {
        return busiRiskAssessMapper.queryRiskTest(customerId);
    }

    @Override
    public void updateRiskAnswer(BusiRiskTest riskTest) {
        busiRiskAssessMapper.updateRiskAnswer(riskTest);
    }

    @Override
    public BusiRiskAssess queryCustomerRiskTestType(Long customerId) {
        return busiRiskAssessMapper.queryCustomerRiskTestType(customerId);
    }

    @Override
    public PageResultDto<RiskEvaluateNotifyDto> queryRiskEvaluateNotify(Map<String, Object> map) {
        PageResultDto<RiskEvaluateNotifyDto> resultDto= new PageResultDto<>();
        if(map.get("pageNo") == null || map.get("pageSize") == null){
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<BusiUnbindRecord> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)){
            map.put("startDate", DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(),7),DateUtil.fullFormat));
        }
        map.put("page",page);
        try {
            List<RiskEvaluateNotifyDto> recordList = busiRiskAssessMapper.queryRiskEvaluateNotify(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

}
