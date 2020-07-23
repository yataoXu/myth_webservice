package com.zdmoney.models.gameCenter;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_GC_DAILY_TASK_RECORD")
@Getter
@Setter
public class DailyTaskRecord extends AbstractEntity<Long>{

    @Id
    private Long id;

    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    public Date createTime;

    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    public Date modifyTime;

    public String operator;

    /**客户编号 */
    private String cmNumber;
    /**活动编号 */
    private String activityNo;
    /**活动类型 0:刮刮卡 1:抓公仔 */
    private String activityType;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long aLong) {
        this.id=aLong;
    }
}