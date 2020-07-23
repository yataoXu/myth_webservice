package com.zdmoney.service.agreement;

import com.zdmoney.common.Result;
import com.zdmoney.web.dto.agreement.SubjectLoanAgreementDTO;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * Created by 00225181 on 2016/4/5.
 */
public interface SubjectLoanAgreementService extends FunctionService{
    SubjectLoanAgreementDTO getAgreement(Long orderId);

    /**
     * 动态查询借款协议
     *
     * @param reqMain
     * @return
     */
    Result getDynamicBorrowContracts(ReqMain reqMain);
}
