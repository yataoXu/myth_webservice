/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service.sys;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysNoticeMapper;
import com.zdmoney.message.api.common.dto.MessagePageResultDto;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.message.MsgMessageDto;
import com.zdmoney.message.api.dto.message.MsgMessageReadDto;
import com.zdmoney.message.api.dto.message.MsgMessageSearchDto;
import com.zdmoney.message.api.dto.message.MsgMessageType;
import com.zdmoney.message.api.facade.IMsgMessageFacadeService;
import com.zdmoney.models.sys.SysAnnouncement;
import com.zdmoney.models.sys.SysNotice;
import com.zdmoney.service.SysNoticeService;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.MessageAndNoticeVo;
import com.zdmoney.vo.SysMessageVo;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SysAnnouncementService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-30 17:32
 * Mail: haoc@zendaimoney.com
 */
@Service
@Slf4j
public class SysMessageService extends BaseService<SysAnnouncement, Long> {

    @Autowired
    private IMsgMessageFacadeService iMsgMessageFacadeService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
	private SysNoticeService sysNoticeService;

    @Autowired
	private SysNoticeMapper sysNoticeMapper;


	public JSONObject getMessageOrNoticeList(int type, int pageNo, int pageSize, String userId) {
		Page<MessageAndNoticeVo> pageDto = new Page<>();
		List<MessageAndNoticeVo> msgList = Lists.newArrayList();
		int totalRecord = 0;
		pageDto.setPageNo(pageNo);
		pageDto.setPageSize(pageSize);
    	if(1 == type){//消息
			MsgMessageSearchDto searchDto = new MsgMessageSearchDto();
			searchDto.setPageNo(pageNo);
			searchDto.setPageSize(pageSize);
			searchDto.setUserId(userId);
//			searchDto.setType(type == 1 ? MsgMessageType.ACTIVITY : MsgMessageType.NOTICE);
			//查询3个月之内的消息
			DateTime date = DateUtil.date();
			searchDto.setCreateEndDate(date);
			searchDto.setCreateStartDate(DateUtil.offset(date, DateField.MONTH,-3));
			MessagePageResultDto<MsgMessageDto> resultDto = iMsgMessageFacadeService.search(searchDto);
			List<MsgMessageDto> list = resultDto.getDataList();
			for(MsgMessageDto message : list){
				MessageAndNoticeVo messageVo = new MessageAndNoticeVo();
				messageVo.setId(message.getId());
				messageVo.setType("消息");//消息
				messageVo.setTitle(message.getTitle());
				messageVo.setSummary(message.getSummary());
				messageVo.setStatus(message.getStatus().getValue());
				messageVo.setPubDate(DateUtil.format(message.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				messageVo.setContentType(StrUtil.toString(message.getContentType()));
				messageVo.setUrl(StrUtil.builder(configParamBean.getMessageAndNoticeDetailUrl(),"?msgType=1&userId=",userId,"&msgId=",message.getId().toString()).toString());
				msgList.add(messageVo);
			}
			totalRecord = resultDto.getTotalSize();
		}else if (2 == type){//公告
			Page<SysNotice> noticeVoPage = new Page<>();
			noticeVoPage.setPageNo(pageNo);
			noticeVoPage.setPageSize(pageSize);
			Map<String, Object> map = Maps.newTreeMap();
			map.put("page", noticeVoPage);
			map.put("status", 1);
			map.put("auditStatus", 1);
			map.put("noticeType",1); //公告类型 1:首页公告
			List<SysNotice> sysNoticeList = sysNoticeService.getSysNoticeList(map);
			for (SysNotice sysNotice : sysNoticeList) {
				MessageAndNoticeVo messageVo = new MessageAndNoticeVo();
				messageVo.setId(sysNotice.getId().intValue());
				messageVo.setType("公告");//公告
				messageVo.setTitle(sysNotice.getTitle());
				messageVo.setPubDate(DateUtil.format(sysNotice.getBeginDate(),"yyyy-MM-dd HH:mm:ss"));
				messageVo.setSummary(sysNotice.getSummary());
				messageVo.setStatus(2);//已读
				messageVo.setContentType(sysNotice.getNoticeLabel());
				messageVo.setUrl(StrUtil.builder(configParamBean.getMessageAndNoticeDetailUrl(),"?msgType=2&userId=",userId,"&msgId=",sysNotice.getId().toString()).toString());
				msgList.add(messageVo);
			}
			totalRecord=noticeVoPage.getTotalRecord();
		}
		//分页显示
		pageDto.setResults(msgList);
		pageDto.setTotalRecord(totalRecord);
		JSONObject pageJson = JSONUtil.parseObj(pageDto);
		pageJson.put("unReadNum", unreadMessageNumber(userId));
		return pageJson;
    }

    public SysMessageVo getMessageInfo(Integer msgId, String userId, int isAllRead, int msgType) {
    	//消息标记为已读
        if(1 == isAllRead){
            MsgMessageReadDto readDto = new MsgMessageReadDto();
            readDto.setUserId(userId);
            readDto.setIsAllRead(true);
//            readDto.setMessageType(1 == msgType ? MsgMessageType.ACTIVITY : MsgMessageType.NOTICE);
            readDto.setCallbackUrl(configParamBean.getMsgReadManagerUrl());
            iMsgMessageFacadeService.read(readDto);
            return null;
        }else{//消息/公告详情
			SysMessageVo messageVo = new SysMessageVo();
        	if (1 == msgType){ //消息详情
				List<Integer> ids = Lists.newArrayList();
				ids.add(msgId);
				MessageResultDto<List<MsgMessageDto>> resultDto = iMsgMessageFacadeService.getByIds(ids, configParamBean.getMsgReadManagerUrl());
				if(resultDto.isSuccess()){
					MsgMessageDto message = resultDto.getData().get(0);
					messageVo.setTitle(message.getTitle());
					messageVo.setContent(message.getContent());
					messageVo.setPubMan(message.getOperator());
					messageVo.setPubDate(message.getCreateTime());
				}else{
					log.error("读取消息中心失败,用户id"+userId+",错误码"+resultDto.getCode()+"错误原因"+resultDto.getMsg());
				}

			}else if (2 == msgType){ //公告详情
				SysMessageVo sysMessageVo = sysNoticeMapper.noticeDetailById(msgId);
				if (sysMessageVo != null){
					messageVo = sysMessageVo;
					messageVo.setContent(sysMessageVo.getContent());
				}
			}
			return messageVo;
        }
    }

    public Integer unreadMessageNumber(String userId) {
        try {
            MessageResultDto<Integer> resultDto = iMsgMessageFacadeService.unReadCount(userId);
            return resultDto.getData();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return 0;
        }
    }
}