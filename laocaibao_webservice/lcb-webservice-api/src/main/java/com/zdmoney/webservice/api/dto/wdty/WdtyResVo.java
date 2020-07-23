package com.zdmoney.webservice.api.dto.wdty;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WdtyResVo
 * @Description 网贷天眼 返回dto
 * @Author huangcy
 * @Date 2018/9/3 11:10
 * @Version 1.0
 **/
@Data
public class WdtyResVo<T> implements Serializable {
	private static final long serialVersionUID = -5268847656336359270L;

	private Integer result_code;

	private String result_msg;

	private Integer page_count;

	private Integer page_index;

	private List<T> loans;

	public WdtyResVo(String result_msg){
		this.result_code=-1;
		this.result_msg=result_msg;
		this.page_index=0;
		this.page_count=0;
		this.loans=null;
	}

	public WdtyResVo(Integer page_index,Integer page_count,List loans){
		this.result_code=1;
		this.result_msg="获取数据成功";
		this.page_index=page_index;
		this.page_count=page_count;
		this.loans=loans;
	}

	public static WdtyResVo SUCCESS(Integer page_index,Integer page_count,List loans){
		return new WdtyResVo<>(page_index, page_count, loans);
	}

	public static WdtyResVo FAIL(String result_msg){
		return new WdtyResVo<>(result_msg);
	}

}
