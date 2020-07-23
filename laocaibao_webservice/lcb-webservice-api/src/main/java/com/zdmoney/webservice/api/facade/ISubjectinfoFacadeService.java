package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.finance.OrderOfSubjectDto;
import com.zdmoney.webservice.api.dto.product.BusiSubjectDto;

import java.util.List;

/**
 * 描述 :推标外部服务接口
 *
 * @author : weiNian
 * @create : 2019-03-04 10:57
 * @Mail: wein@zendaimoney.com
 **/
public interface ISubjectinfoFacadeService {

    /**
     * 挖财推标接口
     * @return
     */
    ResultDto pushBidForWacai(BusiSubjectDto busiSubjectDto);

    /**
     * 根据标的号查询推标结果
     * @return
     */
    ResultDto<BusiSubjectDto> searchWacaiBidBySubjectNo(String subjectNo);


    /**
     * @title
     * @description 根据标的号查询有效子订单
     * @author weiNian 
     * @updateTime 2019/3/8 13:56
     * @throws 
     */
    ResultDto<OrderOfSubjectDto> searchOrderSubsBySubjectNo(String subjectNo);
}
