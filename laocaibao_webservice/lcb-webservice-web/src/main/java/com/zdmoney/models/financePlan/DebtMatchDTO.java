package com.zdmoney.models.financePlan;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DebtMatchDTO  implements Serializable {

    @NotBlank(message = "理财计划id不能为空")
    private Long productId;

    private List<DebtDetailDTO> debtDetailDTOList = new ArrayList<>();

    @NotBlank(message = "匹配结果不能为空")//0-失败 1-成功
    private String status;

    @NotBlank(message = "项目本金不能为空")
    private BigDecimal productPrincipal;

    @NotBlank(message = "产品星标不能为空")
    private Long productRank; // 10-五星 9-四星半 8-四星 7-三星半 6-三星


    @NotBlank(message = "借款人负债比不能为空")
    private BigDecimal liabilitiesRate;
}
