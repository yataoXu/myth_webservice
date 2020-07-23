package com.zdmoney.component.eventListener;

import com.alibaba.fastjson.JSON;
import com.zdmoney.match.dto.MatchPartResult;
import com.zdmoney.match.dto.ResourceMatchResult;
import com.zdmoney.match.exception.MatchException;
import com.zdmoney.match.listener.IMatchResultListener;
import com.zdmoney.service.*;
import com.zdmoney.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by user on 2019/2/26.
 */
public class MatchResultListener implements IMatchResultListener {

    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchResultListener.class);

    @Autowired
    private MatchResultService matchService;


    public void onComplete(List<ResourceMatchResult> list) {
        LOGGER.info("接收到匹配结果：{}",JSON.toJSONString(list));
        //查询 子产品、标的信息、循环 创建子订单并落地 投标 满标通知
        matchService.processMatchResult(list, true);
    }

    @Override
    public void onError(MatchPartResult matchPartResult, MatchException e) {
        LOGGER.error("appId-{},poolId-{},matchId-{},status-{}",
                matchPartResult.getAppId(),matchPartResult.getPoolId(),matchPartResult.getMatchPartId(),matchPartResult.getStatus());
        LOGGER.error(e.getMessage(), e);
        MailUtil.sendMail("监听到撮合出现异常", JSON.toJSONString(matchPartResult)+":"+e.getMessage());
    }

}
