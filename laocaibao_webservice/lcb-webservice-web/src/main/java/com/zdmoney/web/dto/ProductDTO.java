package com.zdmoney.web.dto;

import com.zdmoney.models.BusiMall;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by jb sun on 2016/2/24.
 */
@Getter
@Setter
public class ProductDTO {

    private Long coinBalance;   //捞财币余额

    private Long coinBalanceLast;   //捞财币余额上期

    private String overTime;  //过期时间

    private List<BusiMall> coupons;   //红包

    private List<BusiMall> vouchers;  //加息券

    private List<BusiMall> nDaysVouchers;  //N天加息券

    private List<BusiMall> bespeaks;  //预约券

    private String remindTips;//提示信息

    private String remindVail;//有效期
}
