/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysStaffInfoMapper;
import com.zdmoney.models.sys.SysStaffInfo;
import com.zdmoney.secure.utils.ThreeDesUtil;
import com.zdmoney.utils.ThreeDesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * SysStaffInfoService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-26 18:22
 * Mail: haoc@zendaimoney.com
 */
@Service
public class SysStaffInfoService extends BaseService<SysStaffInfo, Long> {

    private SysStaffInfoMapper getSysStaffInfoMapper() {
        return (SysStaffInfoMapper) baseMapper;
    }

    public boolean isExistIdnum(String staffIdnum) {
        if (StringUtils.isBlank(staffIdnum)) {
            return false;
        }
        staffIdnum = StringUtils.upperCase(staffIdnum);
        Example example = new Example(getEntityClass());
        List<Integer> staffList = new ArrayList<>();
        staffList.add(1);
        staffList.add(4);
        example.createCriteria()
                .andIn("staffStatus", staffList)
                .andEqualTo("staffDismmiss", "0")
                .andCondition("STAFF_IDNUM=", ThreeDesUtil.encryptMode(staffIdnum));
        return getSysStaffInfoMapper().selectCountByExample(example) > 0;
    }

}