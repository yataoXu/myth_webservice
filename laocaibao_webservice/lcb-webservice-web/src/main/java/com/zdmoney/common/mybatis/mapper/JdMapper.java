package com.zdmoney.common.mybatis.mapper;

import com.zdmoney.common.entity.AbstractEntity;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;


/**
 * JdMapper
 * <p/>
 * Author: Hao Chen
 * Date: 2016年1月4日 11:31:17
 * Mail: haoc@zendaimoney.com
 */
public interface JdMapper<T extends AbstractEntity, ID extends Serializable> extends Mapper<T> {

}
