package com.zdmoney.webservice.api.dto.customer;

import com.zdmoney.webservice.api.dto.plan.MatchSucResult;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gaol on 2017/6/17
 **/
@Data
public class CustomerInfoDto implements Serializable {

    private String beginTime ;

    private String endTime;

    private String cmCellphone;

    private String cmRealName;

    private String cmIdnum;

    private String cmRecommend;//来源  用户推荐类型 0：未认证 1：员工 2：员工邀请 3：非员工 4：非员工邀请 5：其它

    private String cbAccount;//银行卡号

    private String cmEmployee;//是否员工

    private String renzheng;//是否认证

    private String cmIntroduceCode;//介绍人码

    private String cmInviteCode ;//邀请码

    private String introducer;//介绍人

    private String cmNumber;//用户编号

    private String cmOrigin;//用户类型

    private String merchantCode;//商户码

    private String channelCode;//渠道码

    private String memberType;//会员类型

    private String minBuyTimes;//最小购买次数

    private String maxBuyTimes;//最大购买次数

    private String openAccountFlag;//是否开户

    private String registerSource;//注册来源

    private String riskTestType;//风险测评类型

    private String accountType;//账户类型

    private String userLevel;//用户类型

    private String memberLevel;//会员等级

    private int pageSize = 20;

    private int pageNo = 1;

}
