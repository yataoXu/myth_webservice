package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by user on 2017/11/8.
 */
@Getter
@Setter
public class UnbindingReqDto implements Serializable{

  @NotNull(message = "用户id不能为空")
  private Long customerId;

  private String bankCode;

  @NotNull(message = "银行卡号不能为空")
  private String bankAccount;

}
