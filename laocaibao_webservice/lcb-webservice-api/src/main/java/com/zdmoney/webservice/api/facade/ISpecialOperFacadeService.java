package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.UnpaidDebtToTransferDto;

import java.util.List;

/**
 * Created by user on 2018/8/16.
 */
public interface ISpecialOperFacadeService {

    ResultDto<List<Integer>> transferUnpaidDebt(UnpaidDebtToTransferDto dto);
    ResultDto correctFailedOpers(UnpaidDebtToTransferDto dto);

    ResultDto checkOperResult(String initOrderNo);
}
