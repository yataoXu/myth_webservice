package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;


@Setter
@Getter
public class Model_400003 extends ReqParam{
	/**
	 * 手机号
	 */
	@NotEmpty(message = "手机号不能为空")
	private String cmCellphone;
	
	/**
	 * 注册账户名
	 */
	private String cmAccount;
	
	/**
	 * 密码
	 */
	@NotEmpty(message = "密码不能为空")
	private String cmPassword;
	
	/**
	 * 验证码
	 */
	@NotEmpty(message = "验证码不能为空")
	private String validateCode;
	
	/**
	 * 邀请人介绍码
	 */
	private String cmIntroduceCode;

	/*
	 *红包编号
	 */
	private String redNo;

	private String ip;

	/*
	 *营业执照
	 */
	private String businessLicense;

	/*
	 *组织机构代码
	 */
	private String orgCode;

	/*
	 *企业名称
	 */
	private String enterpriseName;

	/*
	 *客户类型
	 */
	private String customerType;

	/**
	 * 用户属性：1.出借人2.借款人3.营销户 4.手续费户5.代偿户6.消费金融商家7.平台自有资金账户 9.担保方手续费户
	 */
	private String accountType;

	private String wacai_flag;

}
