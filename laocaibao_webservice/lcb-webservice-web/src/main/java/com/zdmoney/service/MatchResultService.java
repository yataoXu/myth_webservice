package com.zdmoney.service;

import com.zdmoney.common.ResultDto;
import com.zdmoney.match.dto.ResourceMatchResult;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProductContract;

import java.util.List;

/**
 * Created by user on 2019/3/4.
 */
public interface MatchResultService {

    void processMatchResult(ResourceMatchResult matchResult, boolean ensureProcessedOnce);

    void processMatchResult(List<ResourceMatchResult> list, boolean ensureProcessedOnce);

    ResultDto<BusiOrderSub> doTender(BusiOrderSub orderSub, CustomerMainInfo customer, BusiProductContract subjectInfo);
}
