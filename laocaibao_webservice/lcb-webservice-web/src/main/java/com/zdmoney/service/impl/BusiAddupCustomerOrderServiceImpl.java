package com.zdmoney.service.impl;

import com.google.common.collect.Maps;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.BusiAddupCustomerOrderMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.models.BusiAddupCustomerOrder;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.service.BusiAddupCustomerOrderService;
import com.zdmoney.service.BusiOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by jb sun on 2016/2/23.
 */
@Service
@Slf4j
public class BusiAddupCustomerOrderServiceImpl extends BaseService<BusiAddupCustomerOrder,Long> implements BusiAddupCustomerOrderService{

    @Autowired
    BusiAddupCustomerOrderMapper busiAddupCustomerOrderMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Override
    public List<BusiAddupCustomerOrder> selctBusiAddupCustomerOrder(Map<String, String> map) {
        return busiAddupCustomerOrderMapper.selctBusiAddupCustomerOrder(map);
    }

    @Override
    public int insert(BusiAddupCustomerOrder record) {
        return busiAddupCustomerOrderMapper.insert(record);
    }

    @Override
    public BusiAddupCustomerOrder updateAddupCustomerOrderAmt(String customerNo, String yearMonth, BigDecimal orderAmt, BigDecimal orderNum) {

        BusiAddupCustomerOrder busiAddupCustomerOrderVo = new BusiAddupCustomerOrder();
        busiAddupCustomerOrderVo.setCustomerNo(customerNo);
        busiAddupCustomerOrderVo.setYearMonth(yearMonth);
        busiAddupCustomerOrderVo.setOrderAmt(orderAmt);
        busiAddupCustomerOrderVo.setOrderNum(orderNum);
        int updateRows = busiAddupCustomerOrderMapper.updateBusiAddupCustomerOrder(busiAddupCustomerOrderVo);

        if (updateRows == 0)
        {
            updateRows = busiAddupCustomerOrderMapper.insert(busiAddupCustomerOrderVo);
        }

        return  busiAddupCustomerOrderMapper.selctBusiAddupCustomerOrder(busiAddupCustomerOrderVo);


    }

    public     int updateInviteUserCount(Map<String,String> map){
        return busiAddupCustomerOrderMapper.updateInviteUserCount(map);
    }


    @Transactional
    public void setMonthInvestTotalAmt(CustomerMainInfo customerMainInfo, BusiOrderSub order) {
        log.info("统计用户每月资产开始，订单编号：【{}】，客户编号：【{}】",order.getId(),customerMainInfo.getId());
        try {
            String introduceCode = customerMainInfo.getCmIntroduceCode();
            if (!StringUtils.isEmpty(introduceCode)) {
                int num = busiOrderService.selectInviteCustFirstInvestCount(introduceCode, customerMainInfo.getId());
                if (num == 1) {
                    CustomerMainInfo inviteUser = customerMainInfoMapper.selecCustomerByInivteCode(introduceCode);
                    if (inviteUser != null) {
                        String inviteCustomerNum = inviteUser.getCmNumber();
                        Map<String, String> map = Maps.newTreeMap();
                        String date = DateTime.now().toString("yyyyMM");
                        map.put("customerNo", inviteCustomerNum);
                        map.put("yearMonth", date);
                        List<BusiAddupCustomerOrder> addupCustomerOrders = this.selctBusiAddupCustomerOrder(map);
                        if (addupCustomerOrders.size() == 0) {
                            BusiAddupCustomerOrder addupCustomerOrder = new BusiAddupCustomerOrder();
                            addupCustomerOrder.setYearMonth(date);
                            addupCustomerOrder.setCustomerNo(inviteUser.getCmNumber());
                            addupCustomerOrder.setOrderAmt(new BigDecimal(0));
                            addupCustomerOrder.setOrderNum(new BigDecimal(0));
                            addupCustomerOrder.setInviteFirstInvest(1);
                            this.insert(addupCustomerOrder);
                        } else {
                            this.updateInviteUserCount(map);
                        }
                    }
                }
            }
            BusiAddupCustomerOrder busiAddupCustomerOrder = this.updateAddupCustomerOrderAmt(customerMainInfo.getCmNumber(), DateTime.now().toString("YYYYMM"), order.getOrderAmt(), new BigDecimal(1));
            if (busiAddupCustomerOrder != null) {
                log.info("更新用户订单统计：" + busiAddupCustomerOrder.getCustomerNo() + "，金额" + busiAddupCustomerOrder.getOrderAmt() + ",数量：" + busiAddupCustomerOrder.getOrderNum());
            } else {
                log.error("更新用户订单统计失败");
            }
            log.info("统计用户每月资产结束，订单编号：【{}】，客户编号：【{}】", order.getId(), customerMainInfo.getId());
        }
        catch (Exception e){
            log.error("用户每月资产统计失败",e);
        }
    }

    public BusiAddupCustomerOrder findByCustomerNo(String cmNumber, String dateStr) {
        Example example = new Example(getClass());
        example.createCriteria().andEqualTo("customerNo", cmNumber)
                .andEqualTo("yearMonth", dateStr);
        List<BusiAddupCustomerOrder> busiAddupCustomerOrders = baseMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(busiAddupCustomerOrders)){
            return null;
        }
        return busiAddupCustomerOrders.get(0);
    }
}
