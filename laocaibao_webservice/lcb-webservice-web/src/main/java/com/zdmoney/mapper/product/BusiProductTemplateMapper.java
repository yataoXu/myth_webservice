package com.zdmoney.mapper.product;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.product.BusiProductTemplate;


public interface BusiProductTemplateMapper  extends JdMapper<BusiProductTemplate, Long> {


    Long queryWacaiProductType();

}