package com.zdmoney.exception;/**
 * Created by pc05 on 2017/11/22.
 */

import com.zdmoney.webservice.api.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 15:17
 * @email : huangcy01@zendaimoney.com
 **/
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResultDto exceptionHandler(Exception e){
        log.error("异常统一捕获",e);
        return ResultDto.FAIL("系统异常");
    }
}
