package com.zdmoney.service.order;/**
 * Created by pc05 on 2017/11/22.
 */

import com.github.pagehelper.PageHelper;
import com.zdmoney.mapper.order.ViewOrderMapper;
import com.zdmoney.webservice.api.dto.ym.ViewOrderDto;
import com.zdmoney.webservice.api.dto.ym.vo.ViewOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 11:21
 * @email : huangcy01@zendaimoney.com
 **/
@Service
public class ViewOrderServiceImpl implements ViewOrderService {

    @Autowired
    private ViewOrderMapper viewOrderMapper;

    @Override
    public List<ViewOrderVo> getOrderInfo(ViewOrderDto viewOrderDto) {
        PageHelper.startPage(viewOrderDto.getPageNo(),viewOrderDto.getPageSize());
        return viewOrderMapper.getOrderInfo(viewOrderDto);
    }
}
