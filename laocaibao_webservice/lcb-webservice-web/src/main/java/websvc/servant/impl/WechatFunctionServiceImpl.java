package websvc.servant.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.facade.UserFacadeService;
import com.zdmoney.models.MerchantInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.MerchantInfoService;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.utils.ReqUtils;
import com.zdmoney.web.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.servant.WechatFunctionService;

import java.util.Map;

/**
 * Created by 00225181 on 2016/3/30.
 */
@Service
@Slf4j
public class WechatFunctionServiceImpl implements WechatFunctionService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;
    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private UserFacadeService userService;

    @FunctionId("400018")
    public Result wechatRegist(ReqMain reqMain) throws Exception {
        Model_400018 cdtModel = (Model_400018) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCellPhone());
        String password = StringUtils.trim(cdtModel.getPassword());
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        String openId = StringUtils.trim(cdtModel.getOpenId());
        String deviceId = cdtModel.getDeviceId();
        String clientId = cdtModel.getClientId();
        String ip = subIp(cdtModel.getIp());
        return customerMainInfoService.wechatRegist(cellphone, password, validateCode, openId, deviceId, clientId, reqMain.getReqHeadParam(), ip);
    }


    @FunctionId("700003")
    @Override
    public Result checkWxBind(ReqMain reqMain) throws Exception {
        Model_700003 model_700003 = (Model_700003) reqMain.getReqParam();
        String openId = model_700003.getOpenId();
        CustomerMainInfo mainInfo = customerMainInfoService.checkBind(openId);
        Map<String, Object> map = Maps.newHashMap();
        if (mainInfo != null) {
            map.put("type", "success");
            map.put("msg", "用户已绑定手机" + mainInfo.getCmCellphone() + "！");
            map.put("customerId", mainInfo.getId());
            CustomerDTO dto = userService.decorateUser(mainInfo);
            map.put("customerInfo",dto);
            return Result.success(map);
        } else {
            map.put("type", "fail");
            map.put("msg", "openId未绑定！");
        }
        return Result.success(map);
    }

    @FunctionId("700004")
    @Override
    public Result wxBind(ReqMain reqMain) throws Exception {
        Model_700004 cdtModel = (Model_700004) reqMain.getReqParam();
        String cellPhone = StringUtils.trim(cdtModel.getCellPhone());
        String openId = StringUtils.trim(cdtModel.getOpenId());
        String password = cdtModel.getPassword();
        Map<String, Object> map = Maps.newHashMap();
        try {
            customerMainInfoService.wxBind(cellPhone, openId, password);
        } catch (BusinessException e) {
            String message = messageSource.getMessage(e.getMessage(), null, null);
            map.put("type", "fail");
            map.put("msg", message);
            return Result.fail(message, map);
        }
        map.put("type", "success");
        map.put("msg", "绑定成功");
        map.put("couponBindStatus", "1");
        return Result.success(map);
    }

    @FunctionId("700008")
    public Result merchantBind(ReqMain reqMain) throws Exception {
        Model_700008 cdtModel = (Model_700008) reqMain.getReqParam();
        String validCode = StringUtils.trim(cdtModel.getValidCode());
        String cellPhone = StringUtils.trim(cdtModel.getCellPhone());
        String ip = subIp(cdtModel.getIp());
        CustomerMainInfo mainInfo =  customerMainInfoService.findOneByPhone(cellPhone);
        if(mainInfo != null){
            throw new BusinessException("您已是捞财宝用户，无法享用此活动");
        }
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.MERCHANT_REGISTOR,cellPhone,validCode);
        String openId = StringUtils.trim(cdtModel.getOpenId());
        mainInfo = customerMainInfoService.checkBind(openId);
        if(mainInfo != null){
            throw new BusinessException("您已是捞财宝用户，无法享用此活动");
        }
        String merchantCode = cdtModel.getMerchantCode();
        MerchantInfo merchantInfo = merchantInfoService.findMerchantInfo(merchantCode);
        if (merchantInfo == null)
            throw new BusinessException("merchant_code.uninvalid");
        String password = cdtModel.getPassword();

        String vCode = customerMainInfoService.marchantRegistor(cellPhone, password, openId, merchantCode, merchantInfo.getMerchantName(), reqMain.getReqHeadParam(), ip);
        Map<String, Object> map = Maps.newHashMap();
//        try {
//            mainInfo = customerMainInfoService.register(cellPhone, password,"","","",reqMain.getReqHeadParam());
//            mainInfo.setOpenId(openId);
//            customerMainInfoService.updateNotNull(mainInfo);
//            //增加商户注册记录
//            MerchantRegisterRecord merchantRegisterRecord = new MerchantRegisterRecord();
//            merchantRegisterRecord.setCustomerNo(mainInfo.getCmNumber());
//            merchantRegisterRecord.setMerchantNo(merchantCode);
//            merchantRegisterRecord.setRegisterDate(new Date());
//            String vCode = "";
//            while (StringUtils.isEmpty(vCode)) {
//                vCode = NumberUtil.randomString(6);
//                vCode = merchantRegisterRecordService.checkUniqueMerchantValidCode(merchantCode, vCode) ? "" : vCode;
//            }
//            merchantRegisterRecord.setValidCode(vCode);
//            merchantRegisterRecordService.save(merchantRegisterRecord);
//            Map<String,String> tplmap = Maps.newTreeMap();
//            tplmap.put("keyword1",merchantCode+"("+merchantInfo.getMerchantName()+")");
//            tplmap.put("keyword2",cellPhone);
//            tplmap.put("keyword3",mainInfo.getId().toString());
//            tplmap.put("keyword4", DateUtil.timeFormat(mainInfo.getCmInputDate(),"yyyy-MM-dd HH:mm:ss"));
//            customerMainInfoService.sendWxTemplateMsg(openId,Constants.MSG_TPL_MERCHANT_REGISTER_SUCCESS,tplmap);
//            map.put("validCode",vCode);
//        } catch (BusinessException e) {
////            String message = messageSource.getMessage(e.getMessage(), null, null);
////            map.put("type", "fail");
////            map.put("msg", message);
////            return Result.fail(message, map);
//            log.error("商户用户注册失败：{}",e.getMessage());
//            throw  new BusinessException("注册失败");
//        }
        map.put("type", "success");
        map.put("msg", "绑定成功");
        map.put("validCode",vCode);
        return Result.success(map);
    }


    @FunctionId("710002")
    @Override
    public Result checkUserChannel(ReqMain reqMain) throws Exception {
        Model_710002 cdtModel = (Model_710002) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String channelCode = StringUtils.trim(cdtModel.getChannelCode());
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByPhone(cellphone);
        Map<String, Object> map = Maps.newHashMap();
        if(customerMainInfo != null){
            map.put("isRegister", true);
            String cmChannelCode = customerMainInfo.getChannelCode();
            if (channelCode.equals(cmChannelCode)) {
                map.put("isChannel", true);
                map.put("isConsumed", customerMainInfo.getIsConsumed() == 1L);
            } else {
                map.put("isChannel", false);
            }
        }else {
            map.put("isRegister", false);
        }
        return Result.success(map);
    }


    @FunctionId("800014")
    @Override
    public Result checkLoginOpenIdBind(ReqMain reqMain) throws Exception {
        Model_800014 cdtModel = (Model_800014) reqMain.getReqParam();
        String deviceId = StringUtils.trim(cdtModel.getDeviceId());
        String clientId = StringUtils.trim(cdtModel.getClientId());
        Map<String, Object> map = Maps.newHashMap();
        CustomerMainInfo mainInfo = customerMainInfoService.checkLoginOpenIdBind(cdtModel.getOpenId());
        if (mainInfo == null) {
            map.put("type", "unbind");
            map.put("userInfo", new CustomerDTO());
        }else {
            String userAgent = ReqUtils.getUserAgent(reqMain.getReqHeadParam());
            String token = reqMain.getReqHeadParam().getToken();
            CustomerDTO dto = customerMainInfoService.decorateUser(BusiConstants.LOGIN_TYPE_APP, mainInfo, token, deviceId, userAgent, mainInfo.getCmCellphone(), clientId);
            map.put("type", "bind");
            map.put("userInfo", dto);
        }
        return Result.success(map);
    }

	/**
	 * 对其他系统传过来的ip进行处理,防止多个ip导致数据库宝库
	 * @param ip
	 * @return
	 */
	private String subIp(String ip){
		if (StrUtil.isNotBlank(ip)){
			return StrUtil.split(StrUtil.trim(ip), ",")[0];
		}
		return "";
	}
}
