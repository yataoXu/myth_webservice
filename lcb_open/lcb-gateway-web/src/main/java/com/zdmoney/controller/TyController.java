package com.zdmoney.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zdmoney.conf.OpenConfig;
import com.zdmoney.webservice.api.dto.wdty.RebateReqDto;
import com.zdmoney.webservice.api.dto.wdty.RebateResVo;
import com.zdmoney.webservice.api.dto.wdty.WdtyReqDto;
import com.zdmoney.webservice.api.dto.wdty.WdtyResVo;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * @ClassName TyController
 * @Description 网贷天眼接口
 * @Author huangcy
 * @Date 2018/9/3 9:25
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/openapi/wdty")
public class TyController {

	@Autowired
	private ILcbGatewayFacade lcbGatewayFacade;

	private static HashSet[] hashSets = new HashSet[4];

	@Autowired
	private OpenConfig openConfig;

	private final Gson gson = new GsonBuilder()
			.serializeNulls() //当字段值为空或null时，依然对该字段进行转换
			.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS") //时间转化为特定格式
			.setPrettyPrinting() //对结果进行格式化，增加换行
			.disableHtmlEscaping() //防止特殊字符出现乱码
			.create();

	/**
	 * @description 网贷天眼 - 借款数据接口
	 *
	 * @params []
	 * @return java.lang.Object
	 * @date: 2018/9/3 10:33
	 * @author: huangcy
	 */
	@RequestMapping("/borrowData")
	public Object borrowData(@Valid WdtyReqDto req, BindingResult result) {
		if (result.hasErrors()) {
			return WdtyResVo.FAIL(result.getFieldError().getDefaultMessage());
		}
		String s = lcbGatewayFacade.wdtyBorrowData(req);
		return gson.fromJson(s, WdtyResVo.class);
	}

	/**
	 * @description 网贷天眼 - 投资数据接口
	 *
	 * @params []
	 * @return java.lang.Object
	 * @date: 2018/9/3 10:34
	 * @author: huangcy
	 */
	@RequestMapping("/investData")
	public Object investData(WdtyReqDto req, BindingResult result) {
		if (result.hasErrors()) {
			return WdtyResVo.FAIL(result.getFieldError().getDefaultMessage());
		}
		String s = lcbGatewayFacade.wdtyInvestData(req);
		return gson.fromJson(s, WdtyResVo.class);
	}

	/**
	 * @description 网贷天眼 - 提前还款数据接口
	 *
	 * @params []
	 * @return java.lang.Object
	 * @date: 2018/9/3 10:37
	 * @author: huangcy
	 */
	@RequestMapping("/prepaymentData")
	public Object prepaymentData(WdtyReqDto req, BindingResult result) {
		if (result.hasErrors()) {
			return WdtyResVo.FAIL(result.getFieldError().getDefaultMessage());
		}
		String s = lcbGatewayFacade.prepaymentData(req);
		return gson.fromJson(s, WdtyResVo.class);
	}

	/**
	 * @description 网贷天眼 - 订单返利查询接口
	 *
	 * @params [reqDto]
	 * @return java.lang.Object
	 * @date: 2018/9/14 9:59
	 * @author: huangcy
	 */
	@RequestMapping("/getOrderRebates")
	public Object getOrderRebates(RebateReqDto reqDto, HttpServletRequest request) {
		log.info("网贷天眼-订单返利reqParam:[{}]", reqDto);
		if (reqDto.getTimestamp() == null) {
			return RebateResVo.FAIL(RebateResVo.RebateResCode.TIME_OUT);
		}
		if (this.checkSignature(reqDto)) {
			return RebateResVo.FAIL(RebateResVo.RebateResCode.CHECK_SIGN_FAILED);
		}
		if (this.checkIpWhite(request)) {
			return RebateResVo.FAIL(RebateResVo.RebateResCode.CHECK_WHITELIST_FAILED);
		}
		if (StrUtil.isNotBlank(reqDto.getStart_time()) && StrUtil.isNotBlank(reqDto.getStart_time())) {
			if (DateUtil.parseDateTime(reqDto.getStart_time()).compareTo(DateUtil.parseDateTime(reqDto.getEnd_time()))
					== 1) {
				return RebateResVo.FAIL(RebateResVo.RebateResCode.ENDTIME_LESS_STARTTIME);
			}
			if (DateUtil.parseDateTime(reqDto.getStart_time()).compareTo(DateUtil.date()) == 1) {
				return RebateResVo.FAIL(RebateResVo.RebateResCode.TIME_OUT_LIST);
			}
		}
		String orderRebates;
		try {
			orderRebates = lcbGatewayFacade.getOrderRebates(reqDto);
		} catch (Exception e) {
			log.info("系统异常 : ", e);
			return RebateResVo.FAIL(RebateResVo.RebateResCode.SYS_EXCEPTION);
		}
		return gson.fromJson(orderRebates,RebateResVo.class);
	}


	/**
	 * 验证ip白名单
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private boolean checkIpWhite(HttpServletRequest request) {
		String ipReg = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(ipReg);

		String ip = request.getHeader("X-Real-IP");
		log.info("X-Real-IP={}", ip);
		if (StringUtils.isBlank(ip)) {
			ip = request.getRemoteAddr();
		}
		log.info("visit ip=[{}] whiteIpList=[{}]", ip, openConfig.getWhiteIpList());
		if (!pattern.matcher(ip).matches()){
			log.info("not ip !"); return false;
		}
		String[] split2 = StrUtil.split(ip, ",");
		if (hashSets[0]==null || hashSets[0].size() == 0){//初始化 ip set
			HashSet<String> ip1 = Sets.newHashSet();
			HashSet<String> ip2 = Sets.newHashSet();
			HashSet<String> ip3 = Sets.newHashSet();
			HashSet<String> ip4 = Sets.newHashSet();
			hashSets[0] = ip1;
			hashSets[1] = ip2;
			hashSets[2] = ip3;
			hashSets[3] = ip4;
			String[] split = StrUtil.split(openConfig.getWhiteIpList(), ",");
			for (String whiteIp : split) {
				String[] split1 = StrUtil.split(whiteIp, "."); //单个ip
				if (split1.length == 4){
					for (int i = 0; i < 4; i++) {
						hashSets[i].add(split1[i]);
					}
				}
			}
		}

		//判断用户ip
		for (String s2 : split2) {
			String[] split1 = StrUtil.split(s2, ".");//单个ip
			for (int j = 0; j < 4; j++) {
				if (!hashSets[j].contains("*") && !hashSets[j].contains(split1[j])) {//不配置的时候  //跳出单层循环
					break;
				}
				//如果走到这边说明匹配了
				if (j == 3){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 校验签名
	 * @return
	 */
	private boolean checkSignature(RebateReqDto reqDto) {
		if (StrUtil.isNotBlank(reqDto.getSignature())) {
			log.info("网贷天眼平台返利签名请求参数:[{}]", JSONUtil.parseObj(reqDto));
			StringBuilder sb = new StringBuilder();
			String append = (sb.append("end_time=").append(reqDto.getEnd_time()).append("&mobile=")
					.append(reqDto.getMobile()).append("&order_id=").append(reqDto.getOrder_id()).append("&org_code=")
					.append(reqDto.getOrg_code()).append("&public_key=").append(openConfig.getPublicKey()).append("&start_time=")
					.append(reqDto.getStart_time()).append("&timestamp=").append(reqDto.getTimestamp())).toString();
			log.info("网贷天眼平台返利签名请求参数拼接Str:[{}]", append);
			return !StrUtil.equals(reqDto.getSignature(), SecureUtil.md5(append));
		}
		return true;
	}
}
