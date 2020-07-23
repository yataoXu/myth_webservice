package com.zdmoney.service.wdzj;

import com.zdmoney.webservice.api.dto.wdzj.LoanInfoDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoVo;

import java.util.List;

public interface LoanInfoService {

    String getLoanInfo(LoanInfoDto loanInfoDto);

    List<LoanInfoVo> getSplitData();

    List<LoanInfoVo> getNoSplitData();

    String gainBorrowInfo(String projectId,Long planId,String type) throws Exception;
}
