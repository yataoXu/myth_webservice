package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.app.*;


/**
 * app相关接口
 *
 * @author gaol
 * @create 2019-03-20
 */
public interface IAppFacadeService {

    /**
     * 获取app首页相关信息
     * 510006
     * @param indexVO
     * @return
     */
    ResultDto<IndexDTO> getIndexInfo(IndexVO indexVO);

    /**
     * 开屏广告&宣传
     * 510007
     * @return
     */
    ResultDto<PublicizeDTO> getPublicizeInfo();

    /**
     * 我的
     * 510008
     * @param accountVO
     * @return
     */
    ResultDto<AccountInfoDTO> getAccountInfo(AccountVO accountVO);

    /**
     * 发现页列表
     * 800026
     * @return
     */
     ResultDto<DiscoveryInfoDTO> discovery();

    /**
     * 是否显示小红点
     * 510009
     */
    ResultDto<ShowMsgDTO> showMsg (IndexVO indexVO);
}
