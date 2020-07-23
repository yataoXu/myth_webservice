package com.zdmoney.web.dto;

import com.zdmoney.models.product.BusiProductInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 5.0 app首页
 *
 * @author gaol
 * @create 2019-03-14
 */
@Data
public class IndexDTO implements Serializable {

    private List<BannerDTO> bannerList;

    private List<SysNoticeSubDto> sysNoticeDTO;

    private List<CustomerCenterDTO> customerCenters;

    private List<BusiProductInfo> productInfoList;

    private HistorySaleDTO historySaleDTO;

    /**
     * 首页弹窗
     */
    private BannerDTO popups;

    /**
     * 是否开户
     */
    private boolean openAccount;

    /**
     * 是否消费
     */
    private boolean consume;

    /**
     * 是否实名认证
     */
    private boolean realNameAuth;

    /**
     * 是否显示新手引导弹窗
     */
    private boolean showGuide;

    /**
     * 了解捞财宝相关URL
     */
    private List<String> understandUrl;

    /**
     * 新手运营位URL
     */
    private List<String> newcomerUrl;

}
