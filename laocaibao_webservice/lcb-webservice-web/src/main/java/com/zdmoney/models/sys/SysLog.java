package com.zdmoney.models.sys;

import java.util.Date;
/**
 * ******声明*************
 *
 * 版权所有：zendaimoney
 *
 * 项目名称：laocaibao_manager_1
 * 类    名称：SysLog
 * 功能描述：系统日志，记录操作日志
 *
 * 创建人员：cj
 * 创建时间：2015年8月20日
 * @version
 ********修改记录************
 * 修改人员：
 * 修改时间：
 * 修改描述：
 */
public class SysLog {
    private Long id;

    private String modelName;

    private Date createDate;

    private String content;

    private String operator;

    private String logType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }
}