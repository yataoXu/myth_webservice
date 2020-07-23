package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.models.product.BusiProductInit;
import com.zdmoney.vo.BusiProductVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 00225181 on 2016/1/4.
 * 初始化接口dto
 */
@Getter
@Setter
public class InitDTO implements Serializable {

    private static final long serialVersionUID = -7655318976126519668L;

    private List<SysNoticeSubDto> sysNoticeDtoList = Lists.newArrayList();

	private SysNoticeDTO sysNoticeDTO = new SysNoticeDTO();

    private List<BannerDTO> popups = Lists.newArrayList();

    private List<BannerDTO> banners = Lists.newArrayList();

    private List<HeadDTO> heads = Lists.newArrayList();

    private List<BusiProductInit> recommendedProducts = Lists.newArrayList();

    private List<BusiProductVO> hotSellProducts = Lists.newArrayList();

    private List<BusiProductVO> handNewProducts = Lists.newArrayList();

    private List<BusiProductVO> limitProducts = Lists.newArrayList();

    private List<BusiProductVO> orgProducts = Lists.newArrayList();

    private List<CustomerCenterDTO> customerCenters = Lists.newArrayList();

    private List<BusiProductVO> pcProducts = Lists.newArrayList();

    private String taskCenterUrl;

    private Integer unreadNum;

    private HistorySaleDTO historySaleDTO;

    private String infoUrl;//信息披露url

}
