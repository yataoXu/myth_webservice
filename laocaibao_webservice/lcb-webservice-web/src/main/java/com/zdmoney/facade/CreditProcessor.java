package com.zdmoney.facade;

import com.zdmoney.exception.BusinessException;
import com.zdmoney.webservice.api.dto.credit.BaseRequestDto;
import com.zdmoney.webservice.api.dto.credit.BaseResponseDto;
import com.zdmoney.webservice.api.facade.ICreditProcessor;

/**
 * Created by user on 2017/10/20.
 */
public abstract class CreditProcessor implements ICreditProcessor<BaseRequestDto> {

    @Override
    public void preProcess(BaseRequestDto requestDto) throws BusinessException{
        if(!requestDto.isValid())
            throw new BusinessException(BaseResponseDto.CreditResponse.INVALID_PARAMS.getCode(),requestDto.getValidationMsg());
        if(!requestDto.checkSignature())
            throw new BusinessException(BaseResponseDto.CreditResponse.INVALID_SIGNATURE.getCode(),
                    BaseResponseDto.CreditResponse.INVALID_SIGNATURE.getDescr());
    }

    @Override
    public void afterProcess(BaseRequestDto requestDto) throws BusinessException {

    }
}
