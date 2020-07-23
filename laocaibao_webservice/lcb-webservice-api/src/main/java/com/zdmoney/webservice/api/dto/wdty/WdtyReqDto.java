package com.zdmoney.webservice.api.dto.wdty;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName WdtyReqDto
 * @Description 网贷天眼请求参数dto
 * @Author huangcy
 * @Date 2018/9/3 10:39
 * @Version 1.0
 **/
@Data
public class WdtyReqDto implements Serializable {

	private static final long serialVersionUID = 3484333380161277622L;

	/**
	 * 起始时间如:2018-03-08 06:10:00,平台计息结束或提前还款时间字段的值检索.
	 *
	 */
	@NotBlank(message = "起始时间不能为空!")
	private String time_from;

	/**
	 * 	截止时间如:2018-03-09 06:10:00,平台计息结束或提前还款时间字段的值检索.
	 */
	@NotBlank(message = "截止时间不能为空!")
	private String time_to;

	/**
	 * 请求的页码.
	 */
	@NotNull(message = "页码不能为空!")
	private Integer page_index;

	/**
	 * 每页记录条数.
	 */
	@NotNull(message = "记录条数不能为空!")
	private Integer page_size;
}
