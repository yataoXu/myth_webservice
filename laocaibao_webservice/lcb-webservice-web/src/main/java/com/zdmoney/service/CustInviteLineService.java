/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.CustInviteLineMapper;
import com.zdmoney.models.CustInviteLine;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.vo.TopInviteVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CustInviteLineService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-26 17:14
 * Mail: haoc@zendaimoney.com
 */
@Service
public class CustInviteLineService extends BaseService<CustInviteLine, Long> {

    private CustInviteLineMapper getCustInviteLineMapper() {
        return (CustInviteLineMapper) baseMapper;
    }

    public void saveCustInviteLine(CustomerMainInfo mainInfo, CustomerMainInfo inviteCustomer, String type) {
        CustInviteLine inviteLine = new CustInviteLine();
        inviteLine.setCellphone(inviteCustomer.getCmCellphone());
        inviteLine.setInvitedCellphone(mainInfo.getCmCellphone());
        inviteLine.setStatus(type);
        inviteLine.setActionTime(new Date());
        inviteLine.setInviterId(inviteCustomer.getId());//介绍人用户id
        inviteLine.setInviteeId(mainInfo.getId());////被邀请人用户id
        save(inviteLine);
    }

    public boolean isStaff(Map<String, Object> map){
        return getCustInviteLineMapper().isStaff(map) > 0;
    }

    public TopInviteVo getInviteCountByCellphone(Map<String, Object> map){
        return getCustInviteLineMapper().getInviteCountByCellphone(map);
    }

    public int getRankByCellphone(Map<String, Object> map){
        return getCustInviteLineMapper().getRankByCellphone(map);
    }

    public List<TopInviteVo> getCustInviteLineList(Map<String, Object> map){
        return getCustInviteLineMapper().getTopInvistLineList(map);
    }

}