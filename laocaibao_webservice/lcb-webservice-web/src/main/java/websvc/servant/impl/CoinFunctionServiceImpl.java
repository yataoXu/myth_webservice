/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.facade.CustomerInfoService;
import com.zdmoney.models.BusiMall;
import com.zdmoney.service.BusiMallService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.mall.BusiTaskService;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.*;
import com.zdmoney.web.dto.mall.MallTaskDTO;
import com.zdmoney.web.dto.mall.MonthTaskListDTO;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.CoinFunctionService;

import java.util.List;
import java.util.Map;

/**
 * CoinFunctionService
 * <p/>
 * Author: Zhou Yj
 * Date: 2016-03-29 16:00
 * Mail: 00231247
 */
@Service
public class CoinFunctionServiceImpl implements CoinFunctionService {

    @Autowired
    private BusiMallService busiMallService;

    @Autowired
    private BusiTaskService busiTaskService;
    @Autowired
    private CustomerInfoService customerInfoService;

    @FunctionId("800001")
    public Result coinProductList(ReqMain reqMain) throws Exception {
        Model_800001 cdtModel = (Model_800001) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        ProductDTO dto = busiMallService.mallHome(customerId);
        return Result.success(dto);
    }

    @FunctionId("800002")
    public Result coinProductDetail(ReqMain reqMain) throws Exception {
        Model_800002 cdtModel = (Model_800002) reqMain.getReqParam();
        BusiMall busiProduct = busiMallService.findOne(cdtModel.getProductId());
        if (busiProduct == null) {
            return Result.fail("产品不存在");
        }
        BusiMallDTO dto = new BusiMallDTO();
        BeanUtils.copyProperties(busiProduct, dto);
        dto.setSurplusNum(busiProduct.getMerchandiseNum() - busiProduct.getBuyNum());
        return Result.success(dto);
    }

    @FunctionId("800003")
    public Result coinGet(ReqMain reqMain) throws Exception {
        Model_800003 cdtModel = (Model_800003) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long coin = cdtModel.getCoin();
        String tips = cdtModel.getTips();
        boolean isSuccess = busiMallService.gainCoin(customerId, coin, tips);
        return new Result(isSuccess);
    }

    @FunctionId("800004")
    public Result coinTradeLogList(ReqMain reqMain) throws Exception {
        Model_800004 cdtModel = (Model_800004) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        Page<ExchangeRecordDTO> page = busiMallService.exchangeRecord(customerId, pageNo, pageSize);
        return Result.success(page);
    }

    @FunctionId("800006")
    public Result exchange(ReqMain reqMain) throws Exception {
        Model_800006 cdtModel = (Model_800006) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        ExchangeResultDTO result = busiMallService.exchangeCoin(customerId, productId, "this");
        return Result.success(result);
    }

    @FunctionId("800017")
    public Result exchangeLast(ReqMain reqMain) throws Exception {
        Model_800017 cdtModel = (Model_800017) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        ExchangeResultDTO result = busiMallService.exchangeCoin(customerId, productId,"last");
        return Result.success(result);
    }

    @FunctionId("800009")
    public Result coinDetail(ReqMain reqMain) throws Exception {
        Model_800009 cdtModel = (Model_800009) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Integer pageNo = cdtModel.getPageNo() != null ? cdtModel.getPageNo() : 1;
        Integer pageSize = cdtModel.getPageSize() != null ? cdtModel.getPageSize() : ParamConstant.PAGESIZE;
        Page<BusiCoinRecordDTO> page = busiMallService.coinRecord(customerId, pageNo, pageSize);
        return Result.success(page);
    }

    @FunctionId("810001")
    @Override
    public Result taskList(ReqMain reqMain) throws Exception {
        Model_810001 cdtModel = (Model_810001) reqMain.getReqParam();
        String cmToken = cdtModel.getCmToken();
        MallTaskDTO taskDTO = busiTaskService.getTask(cmToken);
        return Result.success(taskDTO);
    }

    @FunctionId("810002")
    @Override
    public Result receiveTaskReward(ReqMain reqMain) throws Exception {
        Model_810002 cdtModel = (Model_810002) reqMain.getReqParam();
        String cmToken = cdtModel.getCmToken();
        String flowIds = cdtModel.getFlowId();
        Map<String, Object> resultMap = busiTaskService.receiveTaskReward(cmToken, flowIds);
        return Result.success(resultMap);
    }

    @FunctionId("810003")
    @Override
    public Result monthTaskList(ReqMain reqMain) throws Exception {
        List<MonthTaskListDTO> monthTask = busiTaskService.getMonthTask();
        return Result.success(monthTask);
    }
}
