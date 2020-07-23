package com.zdmoney.service;

import com.zdmoney.models.BusiImg;

public interface BusiImgService {
    int deleteByPrimaryKey(Short id);

    int insert(BusiImg record);

    int insertSelective(BusiImg record);

    BusiImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusiImg record);

    int updateByPrimaryKeyWithBLOBs(BusiImg record);
}