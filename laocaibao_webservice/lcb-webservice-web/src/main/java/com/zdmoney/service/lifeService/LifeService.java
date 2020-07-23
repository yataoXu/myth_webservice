package com.zdmoney.service.lifeService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.exception.HessianRpcException;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.IntegralAccountDto;
import com.zdmoney.integral.api.dto.order.IntegralExchangePhoneDto;
import com.zdmoney.integral.api.dto.order.IntegralExchangeResDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderSearchDto;
import com.zdmoney.integral.api.dto.product.IntegralPhoneProductDto;
import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.integral.api.facade.IIntegralOrderFacadeService;
import com.zdmoney.integral.api.facade.IIntegralProductFacadeService;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.vo.LifeServiceOrder;
import com.zdmoney.vo.LifeServicePhoneInfoVo;
import com.zdmoney.vo.LifeServiceProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_500019;
import websvc.models.Model_500020;
import websvc.models.Model_500021;
import websvc.req.ReqMain;

import java.util.List;
import java.util.Map;

/**
 * Created by jb sun on 2016/3/17.
 */
@Service
@Slf4j
public class LifeService {

    @Autowired
    private IIntegralProductFacadeService integralProductFacadeService;

    @Autowired
    private IIntegralOrderFacadeService integralOrderFacadeService;

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    public Map<String, Object> getProductByPhone(ReqMain reqMain) {
        Map<String, Object> resultMap = Maps.newTreeMap();
        //获取model对象
        Model_500019 cdtModel = (Model_500019) reqMain.getReqParam();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Long.parseLong(cdtModel.getCustomerId()));
        if (customerMainInfo == null) {
            throw new BusinessException("用户不存在");
        }
        cdtModel.setMobilePhone(StringUtils.isEmpty(cdtModel.getMobilePhone()) ? customerMainInfo.getCmCellphone() : cdtModel.getMobilePhone());
        //查询积分余额
        ResultDto<IntegralAccountDto> queryIntegerResultDto = integralAccountFacadeService.getIntegralAccount(customerMainInfo.getCmNumber());
        if (queryIntegerResultDto.isSuccess()) {
            IntegralAccountDto accountDto = queryIntegerResultDto.getData();
            resultMap.put("integerBalance", accountDto.getAvailableIntegral());
        } else {
            resultMap.put("integerBalance", 0);
        }


        ResultDto<IntegralPhoneProductDto> resultDto = null;
        if (cdtModel.getType().equals("1"))
            resultDto = integralProductFacadeService.getChargeProductsByPhone(cdtModel.getMobilePhone());
        else
            resultDto = integralProductFacadeService.getFlowProductsByPhone(cdtModel.getMobilePhone());


        if (resultDto.isSuccess()) {
            IntegralPhoneProductDto dto = resultDto.getData();
            if (dto != null) {
                LifeServicePhoneInfoVo servicePhoneInfoVo = new LifeServicePhoneInfoVo();
                servicePhoneInfoVo.setCity(dto.getCity());
                servicePhoneInfoVo.setMobilePhone(cdtModel.getMobilePhone());
                servicePhoneInfoVo.setOperator(dto.getOperator());
                resultMap.put("phoneInfo", servicePhoneInfoVo);

                List<LifeServiceProductVo> productVoList = Lists.newArrayList();
                for (IntegralProductDto productDto : dto.getList()) {
                    LifeServiceProductVo productVo = new LifeServiceProductVo(productDto);
                    productVoList.add(productVo);
                }
                resultMap.put("productList", productVoList);
            }
        }
        return resultMap;
    }

    public Map<String, Object> rechargeToPhone(ReqMain reqMain) {
        //获取model对象
        Model_500020 cdtModel = (Model_500020) reqMain.getReqParam();

        CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Long.parseLong(cdtModel.getCustomerId()));
        if (customerMainInfo == null) {
            throw new BusinessException("用户不存在");
        }

        IntegralExchangePhoneDto exchangeDto = new IntegralExchangePhoneDto();
        exchangeDto.setAccountNo(customerMainInfo.getCmNumber());
        exchangeDto.setProductNo(cdtModel.getProductNo());
        exchangeDto.setPhone(cdtModel.getMobilePhone());

        Map<String, Object> resultMap = Maps.newTreeMap();
        ResultDto<IntegralExchangeResDto> resultDto = integralOrderFacadeService.lifeExchange(exchangeDto);

        if (resultDto.isSuccess()) {
            IntegralExchangeResDto dto = resultDto.getData();
            if (dto != null) {
                resultMap.put("orderNo", dto.getOrderNo());
            }
            return resultMap;
        } else {
            throw new HessianRpcException("life.recharge", resultDto.getMsg());
        }
    }

    public Map<String, Object> getRechargeDetail(ReqMain reqMain) {
        Model_500021 cdtModel = (Model_500021) reqMain.getReqParam();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOne(Long.parseLong(cdtModel.getCustomerId()));

        cdtModel.setPageNo(cdtModel.getPageNo() == null ? 1 : cdtModel.getPageNo());
        cdtModel.setPageSize(cdtModel.getPageSize() == null ? 10 : cdtModel.getPageSize());

        IntegralOrderSearchDto integralOrderSearchDto = new IntegralOrderSearchDto();
        integralOrderSearchDto.setAccountNo(customerMainInfo.getCmNumber());
        integralOrderSearchDto.setPageNo(cdtModel.getPageNo());
        integralOrderSearchDto.setPageSize(cdtModel.getPageSize());
        PageResultDto<IntegralOrderDto> resultDto =
                integralOrderFacadeService.searchIntegralOrdersByAccount(integralOrderSearchDto);

        Map<String, Object> resultMap = Maps.newTreeMap();
        if (resultDto.isSuccess()) {
            List<IntegralOrderDto> orderDtos = resultDto.getDataList();
            List<LifeServiceOrder> orderList = Lists.newArrayList();
            for (IntegralOrderDto orderDto : orderDtos) {
                LifeServiceOrder order = new LifeServiceOrder();
                order.setId(orderDto.getId());
                order.setMobilePhone(orderDto.getPhone());
                order.setName(orderDto.getProductName());
                order.setOrderTime(DateUtil.getDateFormatString(orderDto.getIntegralTime(), DateUtil.fullFormat1));
                order.setStatus(orderDto.getStatus());
                order.setStatusDesc(orderDto.getViewStatus());
                order.setConsumeAmount(orderDto.getIntegral() + "积分");
                order.setProductSerialNo(orderDto.getProductSerialNo());
                order.setExpiredTime(orderDto.getExpiredTime());
                order.setLifeType(orderDto.getLifeType());
                orderList.add(order);
            }

            resultMap.put("results", orderList);
            resultMap.put("pageNo", resultDto.getPageNo());
            resultMap.put("pageSize", cdtModel.getPageSize());
            resultMap.put("totalPage", resultDto.getTotalPage());
            resultMap.put("totalRecord", resultDto.getTotalSize());
        }
        return resultMap;

    }


}
