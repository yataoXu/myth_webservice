package com.zdmoney.web.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by gaol on 2017/5/15
 **/
@Data
public class AuthAndBindCardInfoDTO {

    private String realName;

    private String idNum;

    private Long oldBankAccountId;

    private String cellPhone;

}
