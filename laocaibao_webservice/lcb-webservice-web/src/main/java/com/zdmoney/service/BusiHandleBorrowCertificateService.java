package com.zdmoney.service;

import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.agreement.InvestEvidenceReqDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.BorrowValidateCodeMapper;
import com.zdmoney.mapper.BusiBorrowCertificateMapper;
import com.zdmoney.models.BorrowValidateCode;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateDto;
import com.zdmoney.webservice.api.facade.IBusiBorrowCertificateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_420007;
import websvc.req.ReqHeadParam;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 46186 on 2018/6/21.
 */
@Service
@Slf4j
public class BusiHandleBorrowCertificateService extends BaseBusinessService<BorrowValidateCode, Long> {

    @Autowired
    private BusiBorrowCertificateMapper busiBorrowCertificateMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private IBusiBorrowCertificateService iBusiBorrowCertificateService;

    @Autowired
    private BorrowValidateCodeMapper borrowValidateCodeMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ILCBSubjectFacadeService ilcbSubjectFacadeService;

    public Result getBorrowCertificateValidateCode(Model_420007 model_420007, ReqHeadParam reqHeadParam) throws Exception {
        String email = StringUtils.trim(model_420007.getEmail());
        String customerId = StringUtils.trim(model_420007.getCustomerId());
        Map<String, Object> resultMap = Maps.newHashMap();

        CustomerMainInfo customerMainInfo = customerMainInfoService.checkCustomerId(Long.parseLong(customerId));
        if (customerMainInfo == null) {
            throw new BusinessException("用户不存在");
        }
        // 校验邮箱
        String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        if (!email.matches(regex)) {
            resultMap.put("infoType","0");//infoType 0:表示前台用toast提示;1:表示用弹框提示
            return Result.fail("请输入正确的邮箱地址",resultMap);
        }
        BusiBorrowCertificateDto dto = busiBorrowCertificateMapper.selectByCmNumber(customerMainInfo.getCmNumber());
        //一个月只能开具一次
        if (dto != null && dto.getExpireDate().getTime() > new Date().getTime()) {
            resultMap.put("infoType","1");
            return Result.fail("出借凭证每月每人只能开具一次，本月您已经开具过，下个月再来吧。",resultMap);
        }
        //当前是否持有投资、实名认证
        ResultDto<String> result = iBusiBorrowCertificateService.getHoldAsset(customerMainInfo.getId().toString());
        String asset = result.getData();
        Integer cmStatus = customerMainInfo.getCmStatus();
        if (asset == null || "".equals(asset) || "0".equals(asset) || cmStatus != 3) {
            resultMap.put("infoType","1");
            return Result.fail("您当前未在捞财宝进行任何投资 ，暂时无法开具出借凭证",resultMap);
        }
        BorrowValidateCode validateCode = new BorrowValidateCode();
        validateCode.setCustomerId(customerMainInfo.getId());
        validateCode.setCvEmail(email);
        String code = RandomStringUtils.randomNumeric(4);
        validateCode.setCvCode(code);
        validateCode.setCvCreateTime(new Date());
        validateCode.setTryTime(0);
        validateCode.setCvType(0);
        validateCode.setCvExpireTime(DateUtil.addMinutes(new Date(), 10));//验证码有效期设为十分钟
        MailUtil.sendMailValidateCode("验证码 用于申请捞财宝", "验证码（有效期十分钟）：" + code, email);
        borrowValidateCodeMapper.insert(validateCode);
        return Result.success();
    }

    public Result checkEmailValidateCode(String email, String sign) {
        //根据email取最新的数据
        Map<String, Object> paramMap = Maps.newHashMap();
        Map<String, Object> resultMap = Maps.newHashMap();
        paramMap.put("email", email);
        //查询出借凭证
        paramMap.put("type", 0);
        BorrowValidateCode borrowValidateCode = borrowValidateCodeMapper.selectLastOneByEmailAndType(paramMap);
        if (borrowValidateCode == null) {
            resultMap.put("infoType","0");//infoType 0:表示前台用toast提示;1:表示用弹框提示
            return Result.fail("validateCode.not.get",resultMap);
        }
        if (StringUtils.isEmpty(borrowValidateCode.getCvCode())) {
            resultMap.put("infoType","0");
            return Result.fail("validateCode.not.get",resultMap);
        }
        if (borrowValidateCode.getTryTime() >= configParamBean.getTppValicodeTryTime()) {
            resultMap.put("infoType","0");
            return Result.fail("验证码尝试次数已超过"+configParamBean.getTppValicodeTryTime()+"次，不能再次尝试",resultMap);
        }
        borrowValidateCode.setTryTime(borrowValidateCode.getTryTime() + 1);
        borrowValidateCode = update(borrowValidateCode);
        if (borrowValidateCode == null) {
            resultMap.put("infoType","0");
            return Result.fail("validateCode.update.failed",resultMap);
        }
        if (new Date().getTime() > borrowValidateCode.getCvExpireTime().getTime()) {
            resultMap.put("infoType","0");
            return Result.fail("validateCode.invalid",resultMap);
        }
        if (!borrowValidateCode.getCvCode().equals(sign)) {
            resultMap.put("infoType","0");
            return Result.fail("邮箱验证码错误",resultMap);
        }
        //验证码通过  开始进行发送凭证操作
        CustomerMainInfo customerMainInfo = customerMainInfoService.checkCustomerId(borrowValidateCode.getCustomerId());
        BusiBorrowCertificateDto model = new BusiBorrowCertificateDto();
        ResultDto<String> result = iBusiBorrowCertificateService.getHoldAsset(customerMainInfo.getId().toString());
        BigDecimal asset = new BigDecimal(result.getData());
        model.setMoney(asset);
        Calendar cal = Calendar.getInstance();
        model.setCreateDate(cal.getTime());
        cal.add(cal.MONTH, 1);
        model.setExpireDate(cal.getTime());
        model.setOrigin("用户");
        model.setCmNumber(customerMainInfo.getCmNumber());
        model.setCmName(customerMainInfo.getCmRealName());
        model.setCellphone(customerMainInfo.getCmCellphone());
        InvestEvidenceReqDto reqDto = new InvestEvidenceReqDto();
        reqDto.setPartnerNo("LCB");
        reqDto.setCustomerEmail(email);
        reqDto.setCustomerIdNo(customerMainInfo.getCmIdnum());
        reqDto.setCustomerName(customerMainInfo.getCmRealName());
        reqDto.setCustomerNo(customerMainInfo.getCmNumber());
        reqDto.setSurplusPrincipal(asset);
        reqDto.setInvestDeadLine(cal.getTime());
        AssetsResultDto assetsResultDto = ilcbSubjectFacadeService.generateInvestEvidenceAgreement(reqDto);
        if("1111".equals(assetsResultDto.getCode())){
            throw new BusinessException("发送凭证失败："+assetsResultDto.getMsg());
        }
        iBusiBorrowCertificateService.create(model);
        return Result.success();
    }


}
