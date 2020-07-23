package com.zdmoney.webservice.api.dto.app;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class DiscoveryInfoDTO  implements Serializable {

    private List<BannerDTO> banners;

    private List<CustomerCenterDTO> customerCenters;

    private List<GoodsDTO> goodsList;

    private List<SysInfoCenterDTO> sysInfoCenters;

}
