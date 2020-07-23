package com.zdmoney.webservice.api.dto.finance;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: laocaibao_webservice
 * @description: 标的对应的子订单
 * @author: WeiNian
 * <p>
 * wein@zendaimoney.com
 * @create: 2019-03-08 10:40
 **/
@Data
public class OrderOfSubjectDto implements Serializable {

    /**
     * 总金额
     */
    private BigDecimal orderAmount;

    /**
     * 订单数量
     */
    private Integer orderCount;

    /**
     * 有效子订单列表
     */
    private List<OrderSubDto> orderSubs =new ArrayList<>();



    @Data
    public class OrderSubDto implements Serializable {
        private String partnerNo; //商户编号

        private String subjectNo; // 标的编号

        private String customerNo; // 投资人编号

        private String customerIdNo; // 投资人身份证编号

        private String customerName; // 投资人姓名（实名认证姓名）

        private String customerPhone; // 投资人手机号

        private BigDecimal orderAmount; // 订单金额

        private Date orderTime; // 下单时间

        private String orderNo; // 订单编号

        private BigDecimal principal; // 本金

        private BigDecimal yearRate; // 年利率

        private Date interestStartDate; // 起息日

        private Date interestEndDate; // 结息日

        private String productNo; //产品编号

        private String productName; //产品名称

        private BigDecimal productInterest;//产品利息

    }


}
