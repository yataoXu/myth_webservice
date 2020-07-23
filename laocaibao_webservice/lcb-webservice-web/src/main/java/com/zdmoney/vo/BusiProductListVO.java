package com.zdmoney.vo;

import com.zdmoney.models.product.BusiProductInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 00250968 on 2017/9/4
 **/
@Data
public class BusiProductListVO implements Serializable {

    /**
     * 热销推荐产品
     */
    private BusiProductInfo hotProduct;

    /**
     * 定期产品
     */
    private List<BusiProductInfo> timeDeposit;

    /**
     * 定期产品总数
     */
    private int timeDepositCount;

    /**
     * 个贷产品
     */
    private List<BusiProductInfo> personalLoan;

    /**
     * 定期产品总数
     */
    private int personalLoanCount;

    /**
     * 理财计划
     */
    private List<BusiProductInfo> financialPlan;

    /**
     * 定期产品总数
     */
    private int financialPlanCount;

    /**
     * 转让产品
     */
    private List<BusiProductInfo> transfer;

    /**
     * 转让产品数量
     */
    private int transferCount;

    /**
     * 新手标
     */
    private List<BusiProductInfo> newHand;

    /**
     * 新手标产品总数
     */
    private int newHandCount;

    /**
     * 专区产品
     */
    private List<BusiProductInfo> staffProduct;

    /**
     * 专区产品总数
     */
    private int staffProductCount;

    /**
     * 公告
     */
    private String notice;

}
