package com.zdmoney.mapper.wdzj;

import com.zdmoney.webservice.api.dto.wdzj.*;

import java.util.List;
import java.util.Map;

public interface LoanInfoMapper {

    List<LoanInfoVo> getLoanInfo(Map params);

    Double getLoanAmount(LoanInfoDto loanInfoDto);

    int countSplitInfo(Integer dataType);

    int countSplitUser(Integer dataType);

    int countWdzjData();

    List<LoanInfoData> getLoanInfoData(Map params);

    List<LoanInfoData> getLoanTransferData(Map params);

    List<PreapyVo> earlyRepaymentData(Map params);

    List<LoanInfoData> getPreferenceData(Map params);

    List<LoanUserVo> getLoanUserData(Map params);

    int saveLoanInfo(List<LoanInfoData> list);

    int saveLoanUser(List<LoanUserVo> list);

    LoanInfoData getBusiProductInfo(String productId);

    LoanInfoData getBusiProductSubInfo(String productId);

    int getTotalSize(Map params);

    String getLastMoney();

	String getOrderLastMoney();
}
