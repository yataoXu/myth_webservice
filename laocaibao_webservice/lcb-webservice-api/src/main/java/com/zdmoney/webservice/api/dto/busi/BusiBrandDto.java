package com.zdmoney.webservice.api.dto.busi;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 品牌文案宣传dto
 * Created by qinz on 2019/3/15.
 */
@Data
public class BusiBrandDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(serialize=false)
    private Long id;

    /**
     * 文案
     */
    private String content;

    /**
     * 引用
     */
    private String quote;

    /**
     * 图片URL
     */
    private String imgUrl;

    /**
     * 创建日期
     */
    @JSONField(serialize=false)
    private Date createDate;

    /**
     * 显示日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date displayDate;

    /**
     * 创建人
     */
    @JSONField(serialize=false)
    private String addBy;

    /**
     *宣传类型：0文案；1图片
     */
    private String brandType;

    /**
     * 修改时间
     */
    @JSONField(serialize=false)
    private Date modifyDate;

    /**
     * 修改人
     */
    @JSONField(serialize=false)
    private String modifyBy;

    /**
     * 获取当前星期几
     */
    private String week;

}
