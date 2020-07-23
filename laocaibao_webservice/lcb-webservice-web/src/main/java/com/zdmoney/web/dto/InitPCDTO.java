package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.vo.BusiProductVO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by ZhouYj on 2016/5/12.
 * PC初始化接口dto
 */
@Getter
@Setter
public class InitPCDTO {
    private SysNoticeDTO sysNoticeDTO = new SysNoticeDTO();
    private List<BannerDTO> popups = Lists.newArrayList();
    private List<BannerDTO> banners = Lists.newArrayList();
    private List<HeadDTO> heads = Lists.newArrayList();
    private List<BusiProductVO> hotSellProducts = Lists.newArrayList();
    private List<BusiProductVO> handNewProducts = Lists.newArrayList();
    private List<BusiProductVO> limitProducts = Lists.newArrayList();
    private List<BusiProductVO> orgProducts = Lists.newArrayList();
    private List<BusiProductVO> pcProducts = Lists.newArrayList();
    private List<CustomerCenterDTO> customerCenters = Lists.newArrayList();
    private String taskCenterUrl;
}
