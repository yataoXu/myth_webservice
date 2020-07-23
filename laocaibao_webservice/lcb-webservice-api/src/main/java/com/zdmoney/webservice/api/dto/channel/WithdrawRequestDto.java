package com.zdmoney.webservice.api.dto.channel;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by qinz on 2019/2/13.
 * 提现请求参数
 */
@Data
public class WithdrawRequestDto  implements Serializable {

    /**渠道码 1001-证大前前 1002-挖财 1003-信贷*/
    @NotBlank(message = "渠道码不能为空")
    private String channelCode;

    /**用户*/
    @NotNull(message = "用户id不能为空")
    private Long customerId;

    /**身份证*/
    @NotBlank(message = "身份证号不能为空")
    private String idNum;

    /**前端回调地址*/
    @NotBlank(message = "前端回调地址不能为空")
    private String pageUrl;

    /**PC或APP*/
    @NotBlank(message = "客户端类型不能为空")
    private String clientType;

    /**提现金额*/
    @NotBlank(message = "提现金额不能为空")
    private String amount;

    /**1-普通提现 2-快速提现*/
    @NotBlank(message = "提现类型不能为空")
    private String withdrawType;

    @Override
    public String toString() {
        return "WithdrawRequestDto{" +
                "channelCode='" + channelCode + '\'' +
                ", customerId=" + customerId +
                ", idNum='" + idNum + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", clientType='" + clientType + '\'' +
                ", amount='" + amount + '\'' +
                ", withdrawType='" + withdrawType + '\'' +
                '}';
    }
}
