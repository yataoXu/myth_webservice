package com.zdmoney.webservice.api.dto.wdzj;

import lombok.Data;

import java.io.Serializable;

@Data
public class PreapyVo implements Serializable {

    private static final long serialVersionUID = 7409958993341396495L;

    private String projectId;

    private Integer deadline;

    private String deadlineUnit;
}
