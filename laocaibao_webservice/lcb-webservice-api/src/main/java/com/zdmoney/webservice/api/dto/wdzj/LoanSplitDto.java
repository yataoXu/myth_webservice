package com.zdmoney.webservice.api.dto.wdzj;

import lombok.Data;

import java.io.Serializable;
@Data
public class LoanSplitDto implements Serializable {

    private static final long serialVersionUID = 5252685391833240617L;

    private Integer isFirst;

    private Integer isSplit;

    private Integer pageNo;

    private Integer pageSize;
}
