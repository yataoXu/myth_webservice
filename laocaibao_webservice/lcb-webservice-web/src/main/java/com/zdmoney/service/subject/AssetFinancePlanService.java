package com.zdmoney.service.subject;

import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.match.MatchPvReqDto;
import com.zdmoney.assets.api.facade.subject.ILcbFinancialPlanFacadeService;
import com.zdmoney.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import websvc.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wu.hg on 2016/6/13.
 */
@Service
@Slf4j
@EnableAsync
public class AssetFinancePlanService {

    @Autowired
    private ILcbFinancialPlanFacadeService lcbFinancialPlanFacadeService;


    public BigDecimal queryPV(String subjectNo,Long financeId){
        BigDecimal orderAmount ;
        MatchPvReqDto matchPvReqDto = new MatchPvReqDto();
        matchPvReqDto.setSubjectNo(subjectNo);
        matchPvReqDto.setPartnerNo(AppConstants.PARTNER_NO);
        matchPvReqDto.setSearchDate(new Date());
        matchPvReqDto.setFinanceId(financeId);
        try {
            AssetsResultDto<BigDecimal> resultDto = lcbFinancialPlanFacadeService.queryPv(matchPvReqDto);
            if (resultDto.isSuccess()) {
                orderAmount = resultDto.getData();
            }
            else {
                throw new BusinessException("查询债权价值失败。资金编号："+financeId);
            }
            return orderAmount;
        }
        catch (Exception e){
            throw new BusinessException("查询债权价值异常。资金编号："+financeId);
        }
    }
}
