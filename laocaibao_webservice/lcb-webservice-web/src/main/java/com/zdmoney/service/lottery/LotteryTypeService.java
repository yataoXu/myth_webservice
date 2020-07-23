package com.zdmoney.service.lottery;

import com.github.pagehelper.PageHelper;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.lottey.LotteryTypeMapper;
import com.zdmoney.models.lottey.LotteryType;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.web.dto.LotteryTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 00225181 on 2016/1/5.
 */
@Service
public class LotteryTypeService extends BaseService<LotteryType, Long> {

    private LotteryTypeMapper getLotteryTypeMapper() {
        return (LotteryTypeMapper) baseMapper;
    }

    @Autowired
    private ConfigParamBean configParamBean;

    public Page<LotteryTypeDTO> getAllLotteryTypePage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<LotteryTypeDTO> list = getLotteryTypeMapper().selectLotteryType();
        com.github.pagehelper.Page<LotteryTypeDTO> page = (com.github.pagehelper.Page<LotteryTypeDTO>) list;
        for (LotteryTypeDTO lotteryTypeDTO : page) {
            lotteryTypeDTO.setImgUrl(configParamBean.getImgPath() + "/" + lotteryTypeDTO.getImgUrl());
        }
        return LaocaiUtil.convertPage(page);
    }
}
