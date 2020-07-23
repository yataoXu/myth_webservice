package websvc.servant.impl;/**
 * Created by pc05 on 2017/9/19.
 */

import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.data.agent.api.base.OpenProcessCode;
import com.zdmoney.data.agent.api.base.OpenResponse;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.dto.coupon.CouponDto;
import com.zdmoney.integral.api.dto.coupon.CouponSearchDto;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.utils.JSONUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_900000;
import websvc.servant.AuditFunctionService;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-09-19 9:49
 * @email : huangcy01@zendaimoney.com
 **/
@Service
public class AuditFunctionServiceImpl implements AuditFunctionService{

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @FunctionId("900000")
    public OpenResponse auditCallback(Model_900000 brDto) throws Exception {
        if(StringUtils.isBlank(brDto.getNotifyNo()) || StringUtils.isBlank(brDto.getFeatureNo()) || StringUtils.isBlank(brDto.getNotifyContent())
                || StringUtils.isBlank(brDto.getSystemNo())){
            return OpenResponse.handleFail(OpenProcessCode.PARAMETER_ILLEGAL);
        }
        if(StringUtils.equals(brDto.getFeatureNo(),"001")){//注册送红包
            CouponSearchDto couponSearchDto = new CouponSearchDto();
            couponSearchDto.setType("REG");//发放原因注册
            PageResultDto<CouponDto> couponDtoPageResultDto = couponFacadeService.searchCoupons(couponSearchDto);
            if(!couponDtoPageResultDto.isSuccess()||couponDtoPageResultDto.getDataList().size()==0){
                return OpenResponse.fail("审计:注册发送红包失败!params:["+ JSONUtils.toJSON(couponSearchDto)+"]");
            }

        }
        if(StringUtils.equals(brDto.getFeatureNo(),"002")){//实名认证送捞财币

        }
        System.out.println("**********回调成功**********");
        return OpenResponse.SUCCESS();
    }
}
