package com.zdmoney.facade;

import com.zdmoney.models.product.BusiProductInfo;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.utils.Page;
import com.zdmoney.utils.ValidatorUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.product.BusiProductListDTO;
import com.zdmoney.webservice.api.dto.product.ProductVO;
import com.zdmoney.webservice.api.facade.IProductFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 产品服务
 * @author gaol
 * @create 2019-03-20
 */
@Slf4j
@Service("productService")
public class ProductFacadeService implements IProductFacadeService {

    @Autowired
    private ProductService productService;

    @Autowired
    private BaseService baseService;

    @Override
    public ResultDto<BusiProductListDTO> getProductListByType(ProductVO product) {
        ResultDto<BusiProductListDTO> resultDto = new ResultDto<>();
        Map<String, Object> map = new HashMap<>();
        map.put("productType", product.getProductType());
        map.put("customerId", product.getCustomerId());
        map.put("pageNo", product.getPageNo());
        map.put("rateSort", product.getRateSort());
        map.put("termSort", product.getTermSort());
        try {
            ValidatorUtils.validate(product);
            Page<BusiProductInfo> page = productService.getProductListByType(map);
            com.zdmoney.webservice.api.common.Page apiPage = new com.zdmoney.webservice.api.common.Page();
            BusiProductListDTO busiProductList = new BusiProductListDTO();
            BeanUtils.copyProperties(page, apiPage);
            busiProductList.setProductList(apiPage);
            com.zdmoney.models.product.BusiProductListDTO.Introduction introduc = productService.getIntroductionInfo(product.getProductType());
            BusiProductListDTO.Introduction introduction = new BusiProductListDTO.Introduction();
            BeanUtils.copyProperties(introduc,introduction);
            busiProductList.setIntroduction(introduction);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(busiProductList);
        } catch (Exception e) {
            e.printStackTrace();
            resultDto = (ResultDto<BusiProductListDTO>) baseService.resultError(e);
        }
        return resultDto;
    }
}

