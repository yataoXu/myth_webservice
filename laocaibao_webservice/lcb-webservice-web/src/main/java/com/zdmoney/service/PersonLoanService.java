package com.zdmoney.service;

import com.google.common.collect.Maps;
import com.zdmoney.assets.api.dto.agreement.AgreementUrlResDto;
import com.zdmoney.assets.api.dto.signature.enums.ContractType;
import com.zdmoney.assets.api.dto.subject.borrow.BorrowPlanDetailDto;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.service.subject.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;

import java.util.Map;

@Service
@Slf4j
public class PersonLoanService {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiOrderTempService busiOrderTempService;

    public Result queryRepayDetail(ReqMain reqMain) throws Exception {
        Model_540003 cdtModel = (Model_540003) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        String subjectNo = cdtModel.getSubjectNo();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(customerId);
        BorrowPlanDetailDto borrowDto = subjectService.borrowRepayPlanDetail(mainInfo.getCmCellphone(), mainInfo.getCmIdnum(), subjectNo);
        return Result.success(borrowDto);
    }

    public Result signBorrow(ReqMain reqMain) throws Exception {
        Model_540004 cdtModel = (Model_540004) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        String subjectNo = cdtModel.getSubjectNo();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(customerId);
        if (AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS != mainInfo.getCmStatus()) {
            throw new BusinessException("customer.not.auth");
        }
        String url = subjectService.signBorrow(mainInfo.getCmCellphone(), mainInfo.getCmIdnum(), subjectNo);
        Map map = Maps.newTreeMap();
        map.put("url", url);
        return Result.success(map);
    }

    public Result queryBorrowAgreement(ReqMain reqMain) throws Exception {
        Model_540005 cdtModel = (Model_540005) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        String subjectNo = cdtModel.getSubjectNo();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(customerId);
        AgreementUrlResDto result = subjectService.queryBorrowAgreement(mainInfo.getCmCellphone(), mainInfo.getCmIdnum(), subjectNo);
        return Result.success(result);
    }

    public Result queryAuthAgreement(ReqMain reqMain) throws Exception {
        Model_540006 cdtModel = (Model_540006) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
        if (busiOrderTemp == null) {
            throw new BusinessException("订单信息不存在。");
        }
        AgreementUrlResDto result = subjectService.queryOrderAgreementByType(busiOrderTemp.getOrderId(), ContractType.AUTHORIZE);
        return Result.success(result);
    }


    public Result queryInvestBorrowAgreement(ReqMain reqMain) throws Exception {
        Model_540008 cdtModel = (Model_540008) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
        if (busiOrderTemp == null) {
            throw new BusinessException("订单信息不存在。");
        }
        AgreementUrlResDto result = subjectService.queryOrderAgreementByType(busiOrderTemp.getOrderId(), ContractType.SUBJECT);
        return Result.success(result);
    }

    public Result queryFinPlanAgreement(ReqMain reqMain) throws Exception {
        Model_541000 cdtModel = (Model_541000) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
        if (busiOrderTemp == null) {
            throw new BusinessException("订单信息不存在。");
        }

        AgreementUrlResDto result = subjectService.queryOrderAgreementByType(busiOrderTemp.getOrderId(), ContractType.PLAN_INVEST);
        return Result.success(result);
    }

    public Result queryFinPlanReInvest(ReqMain reqMain) throws Exception {
        Model_541001 cdtModel = (Model_541001) reqMain.getReqParam();
        Long orderId = cdtModel.getOrderId();
        BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
        if (busiOrderTemp == null) {
            throw new BusinessException("订单信息不存在。");
        }

        AgreementUrlResDto result = subjectService.queryOrderAgreementByType(busiOrderTemp.getOrderId(), ContractType.CONTINUE_INVEST);
        return Result.success(result);
    }

}
