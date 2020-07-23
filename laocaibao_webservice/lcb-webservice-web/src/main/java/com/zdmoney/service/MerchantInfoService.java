package com.zdmoney.service;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.MerchantInfoMapper;
import com.zdmoney.models.MerchantInfo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * MerchantInfoService
 * <p/>
 * Author: jb sun
 * Date: 2016-04-29 15:40:10
 * Mail: sjb0223@hotmail.com
 */
@Service
public class MerchantInfoService extends BaseService<MerchantInfo, Long> {

    private MerchantInfoMapper getMerchantInfoMapper() {
        return (MerchantInfoMapper) baseMapper;
    }

    public MerchantInfo findMerchantInfo(String merchantCode) {
        Example example = new Example(MerchantInfo.class);
        example.createCriteria().andEqualTo("merchantCode", merchantCode).andEqualTo("status", "1");
        List<MerchantInfo> merchantInfoList = this.findByExample(example);
        if (merchantInfoList.size() > 0) {
            return merchantInfoList.get(0);
        } else {
            return null;
        }
    }

}