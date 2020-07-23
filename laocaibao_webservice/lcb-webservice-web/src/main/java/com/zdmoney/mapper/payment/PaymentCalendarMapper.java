package com.zdmoney.mapper.payment;

import com.zdmoney.vo.AssetCalendar;
import com.zdmoney.vo.AssetDetailVO;
import com.zdmoney.vo.AssetInfo;
import com.zdmoney.vo.PaymentCalendar;

import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2016/4/6.
 */
public interface PaymentCalendarMapper {
    List<PaymentCalendar> selectPaymentCalendar(Long customerId);

    /**
     * 查询待回款
     * 定期, 理财计划, 转让或标的, 个贷
     * 回款数据
     * @param map
     * @return
     */
    List<AssetCalendar> queryAssetCalendarByWaitReceive(Map map);

    /**
     * 查询已回款
     * 定期, 理财计划, 转让或标的, 个贷
     * 回款数据
     * @param map
     * @return
     */
    List<AssetCalendar> queryAssetCalendarByReceivable(Map map);

    /**
     * 查询资产详情
     * @param customerId
     * @return
     */
    List<AssetInfo> queryAssetDetail(Long customerId);

}
