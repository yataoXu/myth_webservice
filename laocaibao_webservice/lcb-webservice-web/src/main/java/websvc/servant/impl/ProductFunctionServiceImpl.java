package websvc.servant.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectBorrowerDetailReqDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.facade.InnerEmployeeService;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.product.BusiProductRuleMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.models.product.BusiProductListDTO;
import com.zdmoney.models.product.BusiProductRule;
import com.zdmoney.service.*;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.service.payChannel.BusiPayChannelService;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.JSONUtils;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.*;
import com.zdmoney.web.dto.BannerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.ProductFunctionService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ProductFunctionServiceImpl implements ProductFunctionService {

    @Autowired
    BusiProductRuleMapper busiProductRuleMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;
    @Autowired
    private AccountOverview520003Service accountOverview520003Service;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PayInfo520002Service pay520002Service;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private UserAssetService userAsset520004Service;
    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private BusiOrderTempService busiOrderTempService;
    @Autowired
    private BusiProductService busiProductService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;
    @Autowired
    private BusiOrderService busiOrderService;
    @Autowired
    private WelfareService welfareService;
    @Autowired
    private RedisSessionManager redisSessionManager;
    @Autowired
    private CustomerMainInfoService customerMainInfoService;
    @Autowired
    private BusiPayChannelService busiPayChannelService;
    @Autowired
    private ConfigParamBean configParamBean;
    @Autowired
    private InnerEmployeeService innerEmployeeService;

    /**
     * 500016 查询产品详情
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500016")
    @Override
    public Result productDetail(ReqMain reqMain) throws Exception {
        Model_500016 cdtModel = (Model_500016) reqMain.getReqParam();
        Map<String, Object> map = new HashMap<>();
        map.put("productId", cdtModel.getProductId());
        map.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
        BusiProduct busiProduct = busiProductService.findProductById(map);

        if (busiProduct != null) {
            if (busiProduct.getShowStartDate() != null) {
                boolean flag = busiProduct.getShowStartDate().getTime() > new Date().getTime();
                if (flag || "0".equals(busiProduct.getAuditFlag())) {
                    return Result.fail("产品不存在");
                }
            }
            if (busiProduct.getInterestEndDate() != null && busiProduct.getInterestStartDate() != null) {
                int investPeriod = DateUtil.getIntervalDays2(busiProduct.getInterestEndDate(), busiProduct.getInterestStartDate());
                busiProduct.setInitInvestPeriod(investPeriod + 1);
            }
            if (busiProduct.getSaleEndDate() != null && busiProduct.getSaleStartDate() != null) {
                int collectPeriod = DateUtil.getIntervalDays2(busiProduct.getSaleEndDate(), busiProduct.getSaleStartDate());
                busiProduct.setCollectPeriod(collectPeriod + 1);//募集期限
            }

            Date a = busiProduct.getReservatTime(); // 预约购买截至时间
            Date b = busiProduct.getSaleStartDate();// 起售时间
            Date c = new Date();// 当前时间
            Date d = busiProduct.getSaleEndDate(); // 结售时间

            BigDecimal principal = busiProduct.getProductPrincipal();// 项目本金
            BigDecimal investAmt = busiProduct.getTotalInvestAmt();// 投资总金额

            Long s = 0L;
            if (a != null && b != null) {
                // 预约产品，起售时间>=当前时间>=预约购买时间 >=结售时间，投资总金额=项目本金， 不显示倒计时
                if (b.getTime() >= c.getTime() && c.getTime() >= a.getTime() && a.getTime() >= d.getTime() && principal.compareTo(investAmt) == 0) {
                    busiProduct.setCountdown(s);
                } else {
                    Long surplus = (a.getTime() - c.getTime()) / 1000;
                    busiProduct.setCountdown(surplus <= s ? s : surplus);
                }
            }
            if ("1".equals(busiProduct.getIsTransfer())) {//转让产品
                busiProduct.setWelfare(AppConstants.PRODUCT_NO_WELFARE);
            } else {
                busiProduct.setWelfare(getUseWelfareInfo(busiProduct));
            }

            busiProduct.setProductDesc(getIteamInfo(busiProduct.getSubjectNo()));

            return Result.success(busiProduct);
        } else {
            return Result.fail("产品不存在");
        }
    }

    /**
     * 501016 查询产品详情,pc端新增接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("501016")
    @Override
    public Result productDetailPC(ReqMain reqMain) throws Exception {
        Model_501016 cdtModel = (Model_501016) reqMain.getReqParam();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("productId", cdtModel.getProductId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("sDate", sdf.format(new Date()));
        BusiProductVO busiProduct = busiProductService.getProductDetailPC(map);
        if (busiProduct != null) {
            if (busiProduct.getShowStartDate() != null) {
                boolean flag = busiProduct.getShowStartDate().getTime() > new Date().getTime();
                if (flag || "0".equals(busiProduct.getAuditFlag())) {
                    return Result.fail("产品不存在");
                }
            }
            if ("1".equals(busiProduct.getIsTransfer())) {//转让产品
                busiProduct.setWelfare(AppConstants.PRODUCT_NO_WELFARE);
            } else {
                busiProduct.setWelfare(getUseWelfareInfo(busiProduct));
            }

            busiProduct.setProductDesc(getIteamInfo(busiProduct.getSubjectNo()));

            //专区产品
            Boolean productLimitType = innerEmployeeService.isProductLimitType(String.valueOf(busiProduct.getLimitType()));
            if (productLimitType) {
            CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(cdtModel.getCustomerId());
            if (mainInfo == null) {
                throw new BusinessException("该用户信息不存在。");
            }
            //积分余额
            Integer integral = accountOverview520003Service.getIntegralBalance(mainInfo);
            //内部员工积分兑换比例
            String staffRate = configParamBean.getStaffPaymentRate();
            double rate = Double.valueOf(staffRate);
            int staffMaxAmt = (int)(Math.floor(integral / (1000*rate)) * 10);
            busiProduct.setStaffMaxAmt(staffMaxAmt);
        }
            return Result.success(busiProduct);
        } else {
            return Result.fail("产品不存在");
        }
    }


    private String getUseWelfareInfo(BusiProduct busiProduct) {
        String welfareInfo = AppConstants.PRODUCT_NO_WELFARE;
        StringBuffer welfareStr = new StringBuffer();
        BusiProductRule busiProductRule = busiProductRuleMapper.selectByPrimaryKey(busiProduct.getRuleId());
        if (busiProductRule != null && StringUtils.isNotBlank(busiProductRule.getWelfare())) {
            String[] statusList = busiProductRule.getWelfare().split(",");
            if (statusList != null && statusList.length > 0) {
                for (String status : statusList) {
                    if ("1".equals(status)) {
                        welfareStr.append("积分,");
                    }
                    if ("2".equals(status)) {
                        welfareStr.append("红包,");
                    }
                    if ("3".equals(status)) {
                        welfareStr.append("加息券,");
                    }
                }
                welfareInfo = welfareStr.toString().substring(0, welfareStr.length() - 1);
            }

        }
        return welfareInfo;
    }

    /**
     * 项目简介
     *
     * @param subjctNo
     * @return
     */
    private String getIteamInfo(String subjctNo) {
        String itemInfo = "";
        SubjectBorrowerDetailReqDto reqDto = new SubjectBorrowerDetailReqDto();
        reqDto.setPartnerNo("LCB");
        reqDto.setSubjectNo(subjctNo);
        try {
            AssetsResultDto<String> resultDto = lcbSubjectFacadeService.getDiscloseItemInfo(reqDto);
            log.info("调用标的系统查询产品项目简介信息：" + JSONUtils.toJSON(resultDto));
            if (resultDto.isSuccess()) itemInfo = resultDto.getData();
        } catch (Exception e) {
            log.error("调用标的系统查询产品项目简介信息失败,标的编号:" + subjctNo);
        }
        return itemInfo;
    }

    /**
     * 510002 产品列表 - 更多
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("510002")
    @Override
    public Result queryProductListByType(ReqMain reqMain) throws Exception {
        Model_510002 model = (Model_510002) reqMain.getReqParam();
        Map<String, Object> map = new HashMap<>();
        map.put("productType", model.getProductType());
        map.put("customerId", model.getCustomerId());
        map.put("pageNo", model.getPageNo());
        map.put("rateSort", model.getRateSort());
        map.put("termSort", model.getTermSort());
        Page<BusiProductInfo> page = productService.queryProductListByType(map);
        return Result.success(page);
    }

    /**
     * 710003 查询渠道产品
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("710003")
    @Override
    public Result getChannelProduct(ReqMain reqMain) throws Exception {
        Model_710003 model_710003 = (Model_710003) reqMain.getReqParam();
        String channelCode = model_710003.getChannelCode();
        BusiProductVO productDetail = productService.getChannelProductDetail(channelCode);
        return Result.success(productDetail);
    }

    /**
     * 510001 查询产品列表-[6周年改版]
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("510001")
    @Override
    @Deprecated
    public Result queryProductList(ReqMain reqMain) throws Exception {
        Model_510001 model = (Model_510001) reqMain.getReqParam();
        Long customerId = model.getCustomerId();
        Map<String, Object> map = new HashMap<>();
        map.put("productType", "0");
        map.put("customerId", customerId);
        BusiProductListVO productList = productService.queryProductList(map);
        return Result.success(productList);
    }

    /**
     * 500036 查询转让产品列表接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500036")
    @Override
    public Result transferList(ReqMain reqMain) throws Exception {
        Model_500036 cdtModel = (Model_500036) reqMain.getReqParam();
        Long id = cdtModel.getId();
        Long customerId = cdtModel.getCustomerId();
        String isLoan = cdtModel.getIsLoan();
        String topFlag = cdtModel.getTopFlag();
        String productId = cdtModel.getProductId();
        String upLowFlag=cdtModel.getUpLowFlag();
        int pageNo = ObjectUtils.defaultIfNull(cdtModel.getPageNo(), 1);
        int pageSize = ObjectUtils.defaultIfNull(cdtModel.getPageSize(), ParamConstant.PAGESIZE);
        Page<BusiTransferProductVO> productDetail = productService.getTransferProductDetail(id, customerId, topFlag, isLoan, pageNo, pageSize, productId,upLowFlag);
        return Result.success(productDetail);
    }

}
