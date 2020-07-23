package com.zdmoney.service.payment;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.payment.BusiSubjectPayPlanMapper;
import com.zdmoney.models.payment.BusiSubjectPayPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class BusiSubjectPayPlanService extends BaseService<BusiSubjectPayPlan, Long> {
    private BusiSubjectPayPlanMapper getBusiSubjectPayPlanMapper() {
        return (BusiSubjectPayPlanMapper) baseMapper;
    }

}
