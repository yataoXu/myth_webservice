/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * SysFunctionService
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-25 17:34
 * Mail: haoc@zendaimoney.com
 */
public interface SysFunctionService extends FunctionService {

    /*
     * 400001 验证码
     *
     */
    Result validateCode(ReqMain reqMain) throws Exception;

    /*
     * 400012 未读消息确认
     *
     */
    Result unreadSysAnnouncement(ReqMain reqMain) throws Exception;


    /*
     * 400015 邀请用户消费信息
     *
     */
    Result invitationConsumeInfo(ReqMain reqMain) throws Exception;

    /*
     * 400017 根据sessionToken获取用户分享url
     *
     */
    Result shareUrlBySessionToken(ReqMain reqMain) throws Exception;

    /*
    * 400021 根据customerId获取用户分享url
    *
    */
    Result shareUrlByCustomerId(ReqMain reqMain) throws Exception;

    /*
     * 400019 新获取验证码
     *
     */
    Result validateCode2(ReqMain reqMain) throws Exception;

    /*
     * 500014 App首页
     *
     */
    Result appIndex(ReqMain reqMain) throws Exception;

    /*
     * 501014 PC首页
     *
     */
    Result pcIndex(ReqMain reqMain) throws Exception;

    /*
     * 600001 意见反馈
     *
     */
    Result feedback(ReqMain reqMain) throws Exception;

    /*
     * 600002 系统消息
     *
     */
    Result sysAnnouncementList(ReqMain reqMain) throws Exception;

    /*
     * 600004 查询注册banner
     *
     */
    Result getBannerList(ReqMain reqMain) throws Exception;


    /*
     * 600013 获取系统广告
     *
     */
    Result getAdvert(ReqMain reqMain) throws Exception;
    /*
     * 600014 获取消息中心列表
     *
     */
    Result getMessageOrNoticeList(ReqMain reqMain) throws Exception;
    /*
     * 600015 获取消息中心信息
     *
     */
    Result getMessageInfo(ReqMain reqMain) throws Exception;
    /*
     * 800011 资讯列表
     *
     */
    Result sysNoticeList(ReqMain reqMain) throws Exception;

    /*
     * 800012 资讯详情
     *
     */
    Result sysNoticeDetail(ReqMain reqMain) throws Exception;

    /*
     * 800013 小喇叭消息
     *
     */
    Result sysHeadList(ReqMain reqMain) throws Exception;

    /**
     *
     * 800015 统计资讯访问
     *
     */
    Result statistics(ReqMain reqMain) throws Exception;

    /*
     * 900011 验证码
     *
     */
    Result validateCode3(ReqMain reqMain) throws Exception;

    /**
     * 800021  投资者教育初始化
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result investEduInit(ReqMain reqMain) throws Exception;
    /*
     * 600016 获取短信链接指定页
     *
     */
    Result getSmsUrl(ReqMain reqMain) throws Exception;


    /**
     * 800022  投资者教育列表
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result investEduList(ReqMain reqMain) throws Exception;

    /**
     * 800023  投资者教育详情
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result investEduDetail(ReqMain reqMain) throws Exception;



}
