package com.zdmoney.service.impl.agreement;

import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.signature.DynamicBorrowContractsDto;
import com.zdmoney.assets.api.facade.signature.ISignatureFacadeService;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.BusiAgreementMapper;
import com.zdmoney.service.agreement.SubjectLoanAgreementService;
import com.zdmoney.trace.utils.TraceGenerator;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.vo.SubjectLoanAgreementVo;
import com.zdmoney.web.dto.agreement.SubjectLoanAgreementDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_500039;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by 00225181 on 2016/4/5.
 * 标的相关协议
 */
@Slf4j
@Service
public class SubjectLoanAgreementServiceImpl implements SubjectLoanAgreementService {

    @Autowired
    private BusiAgreementMapper busiAgreementMapper;

    @Autowired
    private ISignatureFacadeService signatureFacadeService;

    @FunctionId("500039")
    @Override
    public Result getDynamicBorrowContracts(ReqMain reqMain) {
        Model_500039 model_500039 = (Model_500039) reqMain.getReqParam();
        DynamicBorrowContractsDto dynamicBorrowContractsDto;
        try {
            AssetsResultDto<DynamicBorrowContractsDto> result = signatureFacadeService.getDynamicBorrowContracts(model_500039.getSubjectNo());
            if (result.isSuccess()) {
                dynamicBorrowContractsDto = result.getData();
            } else {
                return Result.fail(result.getMsg());
            }
        } catch (Exception e) {
            log.error("查询协议异常,TraceId[{}]", TraceGenerator.generatorId());
            e.printStackTrace();
            throw new BusinessException("查询协议异常");
        }
        return Result.success(dynamicBorrowContractsDto);
    }


    @Override
    public SubjectLoanAgreementDTO getAgreement(Long orderId) {
        SubjectLoanAgreementVo subjectLoanAgreementVo = busiAgreementMapper.selectSubjectLoanAgreeemnt(orderId);
        if (subjectLoanAgreementVo == null) {
            throw new BusinessException("查询借款协议失败！");
        }
        SubjectLoanAgreementDTO dto = new SubjectLoanAgreementDTO();
        dto.setOrderNum(subjectLoanAgreementVo.getOrderNum());
        dto.setBankCardNo(subjectLoanAgreementVo.getBankCardNo());
        dto.setBankName(subjectLoanAgreementVo.getBankName());
        dto.setBorrowerName(subjectLoanAgreementVo.getBorrowerName());
        dto.setBorrowPurpose(subjectLoanAgreementVo.getBorrowPurpose());
        dto.setCmNumber(subjectLoanAgreementVo.getCmNumber());
        dto.setCustomerName(subjectLoanAgreementVo.getCustomerName());

        String idNum = subjectLoanAgreementVo.getIdNo();
        if (idNum.length() == 18) {
            idNum = idNum.substring(0, 4) + "**********" + idNum.substring(14, 18);
            dto.setIdNo(idNum);
        }
        if (idNum.length() == 15) {
            idNum = idNum.substring(0, 4) + "**********" + idNum.substring(11, 15);
            dto.setIdNo(idNum);
        }
        dto.setPayEndTime(subjectLoanAgreementVo.getPayEndTime());
        dto.setLastExpire(getTime(subjectLoanAgreementVo.getLastExpire(), "yyyy-MM-dd"));
        DateTime begin = new DateTime(subjectLoanAgreementVo.getInterestStartDate());
        DateTime end = new DateTime(subjectLoanAgreementVo.getInterestEndDate());
        int days = Days.daysBetween(begin, end).getDays() + 1;
        dto.setLoanDays(String.valueOf(days));
        dto.setOrderAmt(CoreUtil.BigDecimalAccurate(subjectLoanAgreementVo.getOrderAmt()));
        dto.setPrincipalInterest(CoreUtil.BigDecimalAccurate(subjectLoanAgreementVo.getPrincipalInterest()));
        BigDecimal totalInvestAmt = subjectLoanAgreementVo.getTotalInvestAmt();
        BigDecimal yearRate = subjectLoanAgreementVo.getYearRate();
        MathContext mc = new MathContext(8, RoundingMode.DOWN);
        BigDecimal totalPrincipalInterest = totalInvestAmt.add(totalInvestAmt.multiply(yearRate.multiply(new BigDecimal(days)).divide(new BigDecimal(365), mc)));
        dto.setTotalInvestAmt(CoreUtil.getAmtInWords(totalInvestAmt.doubleValue()));
        dto.setProductPrincipalInterest(CoreUtil.getAmtInWords(totalPrincipalInterest.doubleValue()));
        return dto;
    }

    private String getWord(double value) {
        String[][] words = CoreUtil.getAmtPerWord(value);
        String word = "";
        for (int i = 0; i < words.length; i++) {
            word += words[i][1];
            word += words[i][0];
            if (i < words.length - 1) {
                word += ",";
            }
        }
        return word;
    }

    private String getTime(Date date, String format) {
        if (date != null) {
            DateTime dateTime = new DateTime(date);
            return dateTime.toString(format);
        }
        return "";
    }
}
