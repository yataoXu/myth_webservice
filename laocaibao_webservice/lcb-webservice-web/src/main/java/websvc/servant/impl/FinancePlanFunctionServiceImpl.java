package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.service.BusiFinancePlanService;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiDebtDetailVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_500802;
import websvc.models.Model_500803;
import websvc.req.ReqMain;
import websvc.servant.FinancePlanFunctionService;


/**
 * 理财计划接口
 * Author: silence.cheng
 * Date: 2017-06-03 14:10
 */

@Service
public class FinancePlanFunctionServiceImpl implements FinancePlanFunctionService{

    @Autowired
    private ProductService productService;

    @Autowired
    private BusiFinancePlanService busiFinancePlanService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;




    /**
     * 产品详情--借款人明细列表（app）
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500802")
    public Result creditorRightsInfo(ReqMain reqMain) throws Exception {
        Model_500802 cdtModel = (Model_500802) reqMain.getReqParam();
        String productId = cdtModel.getProductId();
        int pageNo = ObjectUtils.defaultIfNull(cdtModel.getPageNo(), 1);
        int pageSize = ObjectUtils.defaultIfNull(cdtModel.getPageSize(), ParamConstant.PAGESIZE);
        Page<BusiDebtDetailVo> debtDetail = busiFinancePlanService.findBusiDebtDetails(pageNo, pageSize, productId);
        return Result.success(debtDetail);
    }

    /**
     * 理财计划订单产品详情--借款人明细列表（app）
     */
    @FunctionId("500803")
    public Result orderCreditorRightsInfo(ReqMain reqMain) throws Exception {
        Model_500803 cdtModel = (Model_500803) reqMain.getReqParam();
        String orderId = cdtModel.getOrderId();
        int pageNo = ObjectUtils.defaultIfNull(cdtModel.getPageNo(), 1);
        int pageSize = ObjectUtils.defaultIfNull(cdtModel.getPageSize(), ParamConstant.PAGESIZE);
        Page<BusiDebtDetailVo> debtDetail = busiFinancePlanService.findOrderBusiDebtDetails(pageNo, pageSize, orderId);
        return Result.success(debtDetail);
    }

    /**
     * 理财计划订单提前退出初始化
     */
    @FunctionId("500804")
    public Result initExitFinPlanOrder(ReqMain reqMain) throws Exception{
        return busiDebtTransferService.initExitFinPlanOrder(reqMain);
    }

    /**
     * 理财计划订单提前退出
     */
    @FunctionId("500805")
    public Result exitFinPlanOrder(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.exitFinPlanOrder(reqMain);
    }
}
