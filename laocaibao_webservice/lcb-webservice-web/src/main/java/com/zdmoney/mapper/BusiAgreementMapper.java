package com.zdmoney.mapper;

import com.zdmoney.vo.ContractInvestAgreementVo;
import com.zdmoney.vo.IntercreditorAgreementVo;
import com.zdmoney.vo.InvestAgreementVo;
import com.zdmoney.vo.SubjectLoanAgreementVo;
import com.zdmoney.web.dto.agreement.SubjectInvestAgrementDTO;
import com.zdmoney.web.dto.agreement.SubjectLoanAgreementDTO;

import java.util.List;

/**
 * Created by jb sun on 2015/12/28.
 */
public interface BusiAgreementMapper {
    ContractInvestAgreementVo selectContractInvestAgreemenet(Long orderId);

    InvestAgreementVo selectInvestAgreemenet(Long orderId);

    List<IntercreditorAgreementVo> selectIntercreditorAgreement(Long orderId);

    SubjectInvestAgrementDTO selectInvestLoanAgreement(Long orderId);

    /**标的借款协议**/
    SubjectLoanAgreementVo selectSubjectLoanAgreeemnt(Long orderId);
}
