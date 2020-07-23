package com.zdmoney.facade;

import com.zdmoney.exception.BusinessException;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author gaol
 * @create 2019-03-20
 */
@Component
public class BaseService {

    @Autowired
    private MessageSource messageSource;

    public ResultDto<?> resultError(Exception e) {
        ResultDto<?> resultDto = new ResultDto<>();
        resultDto.setCode(ResultDto.ERROR_CODE);
        String errMsg = "系统异常";
        if (e instanceof BusinessException) {
            errMsg = messageSource.getMessage(e.getMessage(), new Object []{}, null);
        }
        resultDto.setMsg(errMsg);
        return resultDto;
    }

}
