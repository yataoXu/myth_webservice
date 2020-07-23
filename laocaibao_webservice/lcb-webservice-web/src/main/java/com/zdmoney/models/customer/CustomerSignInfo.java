package com.zdmoney.models.customer;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gosling on 2016/12/7.
 */
@Table(name = "CUSTOMER_SIGN_INFO")
@Data
public class CustomerSignInfo implements Serializable {

    @JSONField(serialize = false)
    @Id
    private Long id;

    @JSONField(serialize = false)
    private Long customerId;

    @JSONField(format = "yyyy-MM-dd")
    private Date signDate;

    /**
     * 0: 签到无效
     * 1: 签到成功
     * 2: 有礼物
     * 3: 签到+礼物
     */
    @JSONField(serialize = false)
    private Long status;

    /**送宝箱状态 默认0:未送 1:已送*/
    @JSONField(serialize = false)
    private Integer chestStatus;

}