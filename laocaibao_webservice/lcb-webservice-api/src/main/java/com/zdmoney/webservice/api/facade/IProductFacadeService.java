package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.product.BusiProductListDTO;
import com.zdmoney.webservice.api.dto.product.ProductVO;


/**
 * 产品相关
 *
 * @author gaol
 * @create 2019-03-20
 */
public interface IProductFacadeService {

    /**
     *  查询产品列表
     *  510005
     * @return
     */
    ResultDto<BusiProductListDTO> getProductListByType(ProductVO product);
}
