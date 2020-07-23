package com.zdmoney.facade;

import com.alibaba.fastjson.JSON;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.product.BusiSubjectInfoMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiSubjectInfo;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.TradeService;
import com.zdmoney.utils.CopyUtil;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.utils.ValidatorUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.finance.OrderOfSubjectDto;
import com.zdmoney.webservice.api.dto.product.BusiSubjectDto;
import com.zdmoney.webservice.api.facade.ISubjectinfoFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 描述 :推标外部服务接口
 *
 * @author : weiNian
 * @create : 2019-03-04 10:57
 * @Mail: wein@zendaimoney.com
 **/
@Component
@Slf4j
public class SubjectinfoFacadeService implements ISubjectinfoFacadeService {

    @Autowired
    BusiSubjectInfoMapper busiSubjectInfoMapper;

    @Autowired
    private TradeService tradeService;

    @Autowired
    BusiProductContractMapper busiProductContractMapper;

    @Autowired
    BusiOrderSubMapper busiOrderSubMapper;


    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;


    /**
     * 挖财推标
     * @return
     */
    @Override
    @Transactional
    public ResultDto pushBidForWacai(BusiSubjectDto busiSubjectDto){
        log.info("挖财推标参数：{}", JSON.toJSONString(busiSubjectDto));
        try{

            ValidatorUtils.validate(busiSubjectDto);

            CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByIdNum(busiSubjectDto.getIdNo());
            if (customerMainInfo==null || StringUtil.isEmpty(customerMainInfo.getCmIdnum())){
                return ResultDto.FAIL("根据身份证号未找到对应借款人信息，请检查！标的号："+busiSubjectDto.getSubjectNo());
            }
            if (!busiSubjectDto.getBorrowerName().equals(customerMainInfo.getCmRealName())){
                return ResultDto.FAIL("借款人身份证号与姓名不匹配，请检查！标的号："+busiSubjectDto.getSubjectNo());
            }
            busiSubjectDto.setCmNumber(customerMainInfo.getCmNumber());
            busiSubjectDto.setStatus("0");
            //默认已通知
            busiSubjectDto.setIsNotify("1");
            //是否理财计划 0-否 1-是
            busiSubjectDto.setIsPlan("1");
            //创建标的信息
            BusiSubjectInfo busiSubjectInfo = new BusiSubjectInfo();
            CopyUtil.copyProperties(busiSubjectInfo, busiSubjectDto);
            //推标到账户账户
            BusiProductContract busiProductContract =new BusiProductContract();
            CopyUtil.copyProperties(busiProductContract, busiSubjectInfo);
            ResultDto resultDto = tradeService.bidBuild(busiProductContract,null);
            if (resultDto.isSuccess()){
//            if (true){
                //落地到标的表
                busiProductContract.setStatus("0");
                busiProductContract.setCreditSource("WACAI");
                busiProductContractMapper.insertSelective(busiProductContract);

                //落地到标的推送表
                CopyUtil.copyProperties(busiSubjectInfo, busiProductContract);
                busiSubjectInfo.setCreditSource("WACAI");
                busiSubjectInfo.setIsPacked(0);
                busiSubjectInfo.setPushTime(new Date());
                busiSubjectInfoMapper.insertSelective(busiSubjectInfo);
                log.info("挖财推标标的号：{} 推标成功！", busiSubjectDto.getSubjectNo());
                return ResultDto.SUCCESS();
            }
            else {
                return ResultDto.FAIL("调用账户建标失败！标的号："+busiSubjectDto.getSubjectNo()+"   失败原因："+resultDto.getMsg());
            }
        }catch (Exception e){
            log.error("标的挖财推标异常！标的号:{} ",busiSubjectDto.getSubjectNo(),e);
            return ResultDto.FAIL("建标失败！标的号："+busiSubjectDto.getSubjectNo()+"  失败原因："+e.getMessage());
        }

    }


    @Override
    public ResultDto<BusiSubjectDto> searchWacaiBidBySubjectNo(String subjectNo){
        try {
            log.info("标的号:{} 查询推标结果",subjectNo);
            BusiSubjectDto busiSubjectDto =new BusiSubjectDto();
            BusiSubjectInfo busiSubjectInfo =new BusiSubjectInfo();
            busiSubjectInfo.setSubjectNo(subjectNo);
            busiSubjectInfo = busiSubjectInfoMapper.selectOne (busiSubjectInfo);
            if (busiSubjectInfo!=null){
                CopyUtil.copyProperties(busiSubjectDto, busiSubjectInfo);
            }
            return new ResultDto<>(busiSubjectDto);
        }catch (Exception e){
            log.error("标的号：{} 反查失败！",subjectNo,e);
            return ResultDto.FAIL(e.getMessage());
        }

    }

    @Override
    public ResultDto<OrderOfSubjectDto> searchOrderSubsBySubjectNo(String subjectNo){
        try {
            log.info("标的号:{} 查询有效子订单列表",subjectNo);
            if (StringUtil.isEmpty(subjectNo)){
                throw new BusinessException("标的编号不能为空！");
            }

            Map param = new HashMap();
            param.put("subjectNo",subjectNo);
            List status = new ArrayList();
            status.add(0);
            status.add(9);
            status.add(10);
            status.add(14);
            status.add(16);
            status.add(17);
            status.add(18);
            status.add(19);
            param.put("status",status);
            List<BusiOrderSub> busiOrderSubs = busiOrderSubMapper.queryBusiOrderSubInfoBySubjectNoStatus(param);

            OrderOfSubjectDto orderOfSubjectDto =new OrderOfSubjectDto();
            BigDecimal orderAmount = BigDecimal.ZERO;
            for (BusiOrderSub busiOrderSub : busiOrderSubs){
                OrderOfSubjectDto.OrderSubDto dto = orderOfSubjectDto.new OrderSubDto();
                CustomerMainInfo mainInfo = customerMainInfoService.findAuthCustomerById(busiOrderSub.getCustomerId());
                dto.setSubjectNo(busiOrderSub.getSubjectNo());
                dto.setCustomerNo(mainInfo.getCmNumber());
                dto.setCustomerIdNo(mainInfo.getCmIdnum());
                dto.setCustomerName(mainInfo.getCmRealName());
                dto.setCustomerPhone(mainInfo.getCmCellphone());
                dto.setOrderAmount(busiOrderSub.getOrderAmt());
                dto.setOrderTime(busiOrderSub.getOrderTime());
                dto.setOrderNo(busiOrderSub.getOrderId());
                dto.setPrincipal(busiOrderSub.getOrderAmt());
                dto.setYearRate(busiOrderSub.getYearRate());
                dto.setInterestStartDate(busiOrderSub.getInterestStartDate());
                dto.setInterestEndDate(busiOrderSub.getInterestEndDate());
                dto.setProductNo(busiOrderSub.getProductId().toString());
                dto.setProductName(busiOrderSub.getProductName());
                dto.setPartnerNo(AppConstants.PARTNER_NO);
                dto.setProductInterest(busiOrderSub.getPrincipalinterest().subtract(busiOrderSub.getOrderAmt()));
                orderOfSubjectDto.getOrderSubs().add(dto);
                orderAmount = orderAmount.add(busiOrderSub.getOrderAmt());
            }
            orderOfSubjectDto.setOrderCount(busiOrderSubs.size());
            orderOfSubjectDto.setOrderAmount(orderAmount);
            log.info("标的号:{} 查询有效子订单列表,结果：{}",subjectNo,JSON.toJSONString(orderOfSubjectDto));
            return new ResultDto(orderOfSubjectDto);

        } catch (Exception e) {
            log.error("根据标的号查询有效子订单失败！",subjectNo,e);
            return ResultDto.FAIL(e.getMessage());
        }
    }

}
