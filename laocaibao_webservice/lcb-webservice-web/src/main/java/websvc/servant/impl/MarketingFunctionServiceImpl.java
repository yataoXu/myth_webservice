package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.integral.api.dto.IntegralAccountDetailsDto;
import com.zdmoney.service.BusiMallService;
import com.zdmoney.service.customer.CustomerCenterService;
import com.zdmoney.service.lifeService.LifeService;
import com.zdmoney.service.lottery.LotteryTypeService;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.MarketingFunctionService;

import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2016/3/26.
 */
@Service
public class MarketingFunctionServiceImpl implements MarketingFunctionService {

    @Autowired
    private LotteryTypeService lotteryTypeService;

    @Autowired
    private CustomerCenterService customerCenterService;

    @Autowired
    private BusiMallService busiMallService;

    @Autowired
    private LifeService lifeService;


    @FunctionId("400010")
    public Result integralInfo(ReqMain reqMain) throws Exception {
        Model_400010 cdtModel = (Model_400010) reqMain.getReqParam();
        Long accountNo = cdtModel.getAccountNo();
        Integer pageNo = cdtModel.getPageNo();
        Integer pageSize = cdtModel.getPageSize();
        List<IntegralAccountDetailsDto> dtoList = customerCenterService.integralInfo(accountNo, pageNo, pageSize);
        return Result.success(dtoList);
    }

    @FunctionId("400013")
    public Result couponInfo(ReqMain reqMain) throws Exception {
        Model_400013 cdtModel = (Model_400013) reqMain.getReqParam();
        Long customerId = cdtModel.getCmCustomerId();
        Integer pageSize = cdtModel.getPageSize();
        Integer pageNo = cdtModel.getPageNo();
        Map<String, Object> map = customerCenterService.couponInfo(customerId, pageSize, pageNo);
        return Result.success(map);
    }

    @FunctionId("400016")
    public Result invitationCouponInfo(ReqMain reqMain) throws Exception {
        Model_400016 cdtModel = (Model_400016) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        List<CouponShareDto> couponShareDtos = customerCenterService.invitationCouponInfo(customerId);
        return Result.success(couponShareDtos);
    }

    @FunctionId("500015")
    public Result lotteryTypePage(ReqMain reqMain) throws Exception {
        Model_500015 cdtModel = (Model_500015) reqMain.getReqParam();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        Page<LotteryTypeDTO> lotteryTypePage = lotteryTypeService.getAllLotteryTypePage(pageNo, pageSize);
        return Result.success(lotteryTypePage);
    }


    @Override
    @FunctionId("500019")
    public Result lifeProductList(ReqMain reqMain) throws Exception {
        Map<String, Object> map = lifeService.getProductByPhone(reqMain);
        return Result.success(map);
    }

    @Override
    @FunctionId("500020")
    public Result lifeRecharge(ReqMain reqMain) throws Exception {
        Map<String, Object> map = lifeService.rechargeToPhone(reqMain);
        return Result.success(map);
    }

    @Override
    @FunctionId("500021")
    public Result lifeRechargeRecord(ReqMain reqMain) throws Exception {
        Map<String, Object> map = lifeService.getRechargeDetail(reqMain);
        return Result.success(map);
    }

    @FunctionId("700001")
    public Result myIntegral(ReqMain reqMain) throws Exception {
        Model_700001 cdtModel = (Model_700001) reqMain.getReqParam();
        Long accountNo = cdtModel.getAccountNo();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        IntegralDTO integralDTO = customerCenterService.myIntegral(accountNo, pageNo, pageSize);
        return Result.success(integralDTO);
    }

    @FunctionId("700002")
    public Result integralBalance(ReqMain reqMain) throws Exception {
        Model_700002 cdtModel = (Model_700002) reqMain.getReqParam();
        Long accountNo = cdtModel.getAccountNo();
        IntegralAccountBalanceDTO balanceDTO = customerCenterService.integralBalance(accountNo);
        return Result.success(balanceDTO);
    }

    @FunctionId("700005")
    public Result shareFriend(ReqMain reqMain) throws Exception {
        Model_700005 model_700005 = (Model_700005) reqMain.getReqParam();
        String cmToken = model_700005.getCmToken();
        customerCenterService.share(cmToken);
        return Result.success();
    }

    @FunctionId("700006")
    @Override
    public Result myLotteryCard(ReqMain reqMain) throws Exception {
        Model_700006 model_700006 = (Model_700006) reqMain.getReqParam();
        GuaguakaDTO guaguakaDTO = customerCenterService.getGuaguaka(model_700006.getCmToken());
        return Result.success(guaguakaDTO);
    }

    @FunctionId("800008")
    public Result voucherList(ReqMain reqMain) throws Exception {
        Model_800008 cdtModel = (Model_800008) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Integer pageNo = cdtModel.getPageNo();
        Integer pageSize = cdtModel.getPageSize();
        Map<String, Object> map = busiMallService.getVoucherList(customerId, pageNo, pageSize);
        return Result.success(map);
    }

    @FunctionId("800018")
    public Result bespeakList(ReqMain reqMain) throws Exception {
        Model_800018 cdtModel = (Model_800018) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Integer pageNo = cdtModel.getPageNo();
        Integer pageSize = cdtModel.getPageSize();
        Map<String, Object> map = busiMallService.getBespeakList(customerId, pageNo, pageSize);
        return Result.success(map);
    }

    @FunctionId("908001")
    public Result cashInfo(ReqMain reqMain) throws Exception {
        Model_908001 cdtModel = (Model_908001) reqMain.getReqParam();
        Long customerId = cdtModel.getCmCustomerId();
        Integer pageSize = cdtModel.getPageSize();
        Integer pageNo = cdtModel.getPageNo();
        Map<String, Object> map = customerCenterService.cashInfo(customerId, pageSize, pageNo);
        return Result.success(map);
    }
}
