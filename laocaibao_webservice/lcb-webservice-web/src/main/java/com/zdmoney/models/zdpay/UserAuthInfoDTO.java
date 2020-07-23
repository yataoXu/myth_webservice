package com.zdmoney.models.zdpay;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserAuthInfoDTO implements Serializable {

    /**
     * 0: 成功
     * 1: 失败
     */
    private int authStatus;

    private List<Result> resultList;

    @Data
    public class Result {

        private String question;

        private String answer;
    }

}
