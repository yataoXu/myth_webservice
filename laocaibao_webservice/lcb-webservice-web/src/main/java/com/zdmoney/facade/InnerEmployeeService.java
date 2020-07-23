package com.zdmoney.facade;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.staffWhilte.BusiStaffWhilteMapper;
import com.zdmoney.models.BusiDimissionStaffWhilte;
import com.zdmoney.models.BusiStaffWhilte;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.staffWhilte.IBusiDimissionStaffWhilteService;
import com.zdmoney.utils.IPWhiteList;
import com.zdmoney.utils.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import websvc.models.Model_510003;
import websvc.req.ReqMain;


import java.util.Arrays;
import java.util.Map;

@Component
@Slf4j
public class InnerEmployeeService {

    @Autowired
    ConfigParamBean configParamBean;

    @Autowired
    ProductService productService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private IBusiDimissionStaffWhilteService staffExclusiveService;

    @Autowired
    private BusiStaffWhilteMapper busiStaffWhilteMapper;


    /**
     * 判断IP是否在IP白名单中
     *
     * @param ipStr
     * @return
     */
    public Boolean isIpWhilte(String ipStr) {
        String ipWhilte = configParamBean.getIpWhiteList();
        return IPWhiteList.checkLoginIP(ipStr, ipWhilte);

    }

    /**
     * 判断用户是否是理财师
     *
     * @param customerMainInfo
     * @return
     */
    public Boolean isFinancialAdvisor(CustomerMainInfo customerMainInfo) {
        String staffLevelString = configParamBean.getStaffLevel();
        String[] staffLevel = staffLevelString.split(",");
        boolean isContain = Arrays.asList(staffLevel).contains(customerMainInfo.getUserLevel());
        if (isContain) {
            return true;
        }
        //校验该用户是否在白名单中
        BusiDimissionStaffWhilte staffExclusive = staffExclusiveService.getStaffByCmNumber(customerMainInfo.getCmNumber());
        if (staffExclusive != null && DateUtil.date().compareTo(staffExclusive.getQuitTime()) >= 0 &&
                DateUtil.date().compareTo(staffExclusive.getExpiryDate()) <= 0 && customerMainInfo.getUserLevel().equals("3")) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否是爱特员工
     *
     * @param customerMainInfo
     * @return
     */
    public Boolean isStaffForLCB(CustomerMainInfo customerMainInfo) {
        BusiStaffWhilte busiStaffWhilte = busiStaffWhilteMapper.getStaffByCustomerId(customerMainInfo.getId());
        if (busiStaffWhilte != null) {
            return true;
        }
        return false;
    }

    /**
     * 专区产品校验
     * @param limitType
     * @return
     */
    public Boolean isProductLimitType(String limitType){
        String productLimitTypeStr = configParamBean.getProductLimitType();
        String[] limitTypes = productLimitTypeStr.split(",");
        boolean isContain = Arrays.asList(limitTypes).contains(limitType);
        return isContain;
    }

    /**
     * 是否符合专区产品校验
     * @param customerId
     * @param ipStr
     * @return
     */
    public Result staffAuth(Long customerId, String ipStr) {

        log.info("请求参数 customerId:[{}],IP地址：[{}]", customerId, ipStr);
        if (!isIpWhilte(ipStr)) {
            return new Result(Boolean.FALSE, "访问专区产品的IP不在规定范围内", 0);
        }
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(customerId);
        if (customerMainInfo == null) {
            return new Result(Boolean.FALSE, "该用户不存在", 0);
        }
        //判断用户是否是爱特员工
        if (isStaffForLCB(customerMainInfo)) {
            return new Result(Boolean.TRUE, "该用户为爱特员工", 8);
        }
        //判断用户是否是理财师
        if (isFinancialAdvisor(customerMainInfo)) {
            return new Result(Boolean.TRUE, "该用户是理财师", 7);
        }
        return new Result(Boolean.FALSE, "fail", 0);
    }

    public Result staffProductList(ReqMain reqMain) throws Exception {
        Model_510003 model = (Model_510003) reqMain.getReqParam();
        Long customerId = Convert.toLong(model.getCustomerId());
        String IPStr = reqMain.getReqHeadParam().getIp();

        //是否符合规定用户level
        Result result = staffAuth(customerId, IPStr);
        if (!(result.getSuccess()) || StrUtil.equals(result.getData().toString(), "0")) {
            return Result.fail(result.getMessage());
        }
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", model.getCustomerId());
        map.put("pageNo", model.getPageNo());
        map.put("productType", model.getProductType());
        map.put("limitType", result.getData().toString());
        Page<BusiProductInfo> page = productService.queryStaffProductList(map);
        return Result.success(page);
    }


}
