package com.zdmoney.webservice.api.dto.app;

import java.io.Serializable;

/**
 * Created by 00225181 on 2015/11/11.
 */
public class Advert implements Serializable {
    private String imgUrl = "";
    private String detailWebUrl = "";

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDetailWebUrl() {
        return detailWebUrl;
    }

    public void setDetailWebUrl(String detailWebUrl) {
        this.detailWebUrl = detailWebUrl;
    }
}
