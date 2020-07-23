package com.zdmoney.models.customer;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author hanxn
 * @date 2019/4/12
 */
@Table(name = "SYS_CHECK_REGISTER")
@Setter
@Getter
public class SysCheckRegister extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String illegalPhone;

    private Date createDate;

    private Date modifyDate;

}
