package com.zdmoney.webservice.api.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShowMsgDTO implements Serializable {
    /**
     * 是否显示消息小红点
     */
    private boolean showMsgDot;
}
