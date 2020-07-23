package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 00225181 on 2015/12/2.
 */
@Getter
@Setter
public class PayInfo20DTO {

    /*订单金额*/
    private String orderAmount = "0";

    /*账户余额*/
    private String accountBalance = "0";

    /*积分余额*/
    private String integralBalance = "0";

    /*积分兑换比例，如果是100:1，rate为100 */
    private String rate = "100";

    /*积分兑换现金，如果是100:1，cashAmt为1 */
    private String cashAmt = "0";

    /*可交易积分比例，员工90%，非员工10%*/
    private String integralPro = "0";

    private Long limitType;

    /* 转让标志 0-固收产品 1-转让产品*/
    private String isTransfer;

    /*红包列表*/
    private List<PacketDTO> redPackets = Lists.newArrayList();

    /*加息券列表*/
    private  List<PayVoucherDTO> voucherDtos = Lists.newArrayList();

    /*是否使用新规则*/
    private boolean newRule=false;

    /*是否可用积分*/
    private boolean canUseIntegral=false;

    /*是否可用红包*/
    private boolean canUseCoupon=false;

    /*是否可用加息券*/
    private boolean canUseVoucher=false;

    /*协议模板列表*/
    private  List<AgreementNameDto> agreementTempletes = Lists.newArrayList();

    /*产品类型 0-优选 1-智选 2-转让*/
    private String productType;

    private String tips = "";

    private String useWelfare="0";//新手产品是否可用福利  0：可用 1：不可用

    /*内部员工积分兑换比例*/
    private String canUseIntegralStaffRate;
    //需要使用多少积分
    private Integer actualUseIntegral = 0;
    //抵扣多少金额
    private BigDecimal deductionAmount = BigDecimal.ZERO;
    //实际支付多少金额
    private BigDecimal actualPayment = BigDecimal.ZERO;

}
