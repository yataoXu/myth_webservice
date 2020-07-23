package com.zdmoney.facade;/**
 * Created by pc05 on 2017/11/21.
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.mapper.CustomerRelationshipMapper;
import com.zdmoney.mapper.customer.CustomerLevelChangeLogMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.wdty.WdtyLoanInfoMapper;
import com.zdmoney.mapper.wdty.WdtyLoanUserMapper;
import com.zdmoney.mapper.wdzj.LoanInfoMapper;
import com.zdmoney.models.BusiDimissionStaffWhilte;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.flw.Order;
import com.zdmoney.models.flw.Orders;
import com.zdmoney.models.wdty.WdtyEarlySettlement;
import com.zdmoney.models.wdty.WdtyLoanInfo;
import com.zdmoney.models.wdty.WdtyLoanUser;
import com.zdmoney.service.BusiRebateService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.lycj.LycjService;
import com.zdmoney.service.order.ViewOrderService;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.staffWhilte.IBusiDimissionStaffWhilteService;
import com.zdmoney.service.wdzj.LoanInfoService;
import com.zdmoney.vo.BusiRebateVo;
import com.zdmoney.vo.CustomerMainInfoVo;
import com.zdmoney.webservice.api.common.dto.PageResultVo;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.customer.CustomerLevelChangeLogDto;
import com.zdmoney.webservice.api.dto.fl.FlwReqDto;
import com.zdmoney.webservice.api.dto.wdty.*;
import com.zdmoney.webservice.api.dto.wdzj.*;
import com.zdmoney.webservice.api.dto.ym.*;
import com.zdmoney.webservice.api.dto.ym.vo.BusiProductVo;
import com.zdmoney.webservice.api.dto.ym.vo.ViewOrderVo;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import websvc.utils.XMLUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述 : lcb_open 接口
 *
 * @author : huangcy
 * @create : 2017-11-21 14:36
 * @email : huangcy01@zendaimoney.com
 **/
@Slf4j
@Component
public class LcbGatewayFacadeImpl implements ILcbGatewayFacade {

    @Autowired
    private ProductService productService;

    @Autowired
    private BusiRebateService busiRebateService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private ViewOrderService viewOrderService;

    @Autowired
    private CustomerRelationshipMapper customerRelationshipMapper;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private CustomerLevelChangeLogMapper customerLevelChangeLogMapper;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

	@Autowired
	private WdtyLoanInfoMapper wdtyLoanInfoMapper;

	@Autowired
	private ConfigParamBean configParamBean;

	@Autowired
	private WdtyLoanUserMapper wdtyLoanUserMapper;

	@Autowired
    private BusiOrderMapper busiOrderMapper;

	@Autowired
    private LycjService lycjService;

    @Autowired
    private IBusiDimissionStaffWhilteService staffWhilteService;

    @Override
    public ResultDto<PageResultVo<BusiProductVo>> getProductInfo(BusiProductDto busiProductDto) {
        List<BusiProductVo> productInfo = productService.getProductInfo(busiProductDto);
        return new ResultDto(new PageResultVo(productInfo, (int) ((Page) productInfo).getTotal(), busiProductDto.getPageSize(), busiProductDto.getPageNo()));
    }

    @Override
    public ResultDto<PageResultVo<BusiRebateVo>> getRebateInfo(BusiRebateDto busiRebateDto) {
        List<BusiRebateVo> rebateInfo = busiRebateService.getRebateInfo(busiRebateDto);
        return new ResultDto(new PageResultVo(rebateInfo,(int)((Page) rebateInfo).getTotal(),busiRebateDto.getPageSize(),busiRebateDto.getPageNo()));
    }

    @Override
    public ResultDto<PageResultVo<CustomerMainInfoVo>> getCustomerInfo(CustomerMainInfoDto customerMainInfoDto) {
        List<CustomerMainInfoVo> customerInfo = customerMainInfoService.getCustomerInfo(customerMainInfoDto);
        return new ResultDto(new PageResultVo(customerInfo,(int)((Page) customerInfo).getTotal(),customerMainInfoDto.getPageSize(),customerMainInfoDto.getPageNo()));
    }

    @Override
    public ResultDto<PageResultVo<ViewOrderVo>> getOrderInfo(ViewOrderDto viewOrderDto) {
        List<ViewOrderVo> orderInfo = viewOrderService.getOrderInfo(viewOrderDto);
        return new ResultDto(new PageResultVo(orderInfo,(int)((Page) orderInfo).getTotal(),viewOrderDto.getPageSize(),viewOrderDto.getPageNo()));
    }

    @Override
    public ResultDto selectCountByCmNumber(String cmNumber) {
        return new ResultDto(customerRelationshipMapper.selectCountByCmNumber(cmNumber));
    }

    @Override
    public ResultDto updateByPrimaryKey(CustomerRelationShipDto customerRelationShipDto) {
        return new ResultDto(customerRelationshipMapper.updateByPrimaryKey(customerRelationShipDto));
    }

    @Override
    public ResultDto insertSelective(CustomerRelationShipDto customerRelationShipDto) {
        return new ResultDto(customerRelationshipMapper.insertSelective(customerRelationShipDto));
    }

    @Override
    public ResultDto selectTotalCount() {
        return new ResultDto(customerRelationshipMapper.selectTotalCount());
    }

    @Override
    public ResultDto getUserInfo(String cmNumber) {
        return new ResultDto(customerMainInfoMapper.getUserInfo(cmNumber));
    }

    @Override
    public ResultDto saveCustomerLevelLog(CustomerLevelChangeLogDto customerLevelChangeLogDto) {
        return new ResultDto(customerLevelChangeLogMapper.saveCustomerLevelChangeLog(customerLevelChangeLogDto));
    }

    @Override
    public ResultDto getUserIdByInvideCode(String invideCode) {
        return new ResultDto(customerMainInfoMapper.getUserIdByInvideCode(invideCode));
    }

    @Override
    public ResultDto updateByCustomerId(com.zdmoney.webservice.api.dto.customer.CustomerMainInfoVo user) {
        return new ResultDto(customerMainInfoMapper.updateUserLevel(user));
    }

    @Override
    public ResultDto selectCustomerByIdNum(String idNum) {
        return new ResultDto(customerMainInfoMapper.selectCustomerByIdNum(idNum));
    }

    @Override
    public ResultDto getLoanInfo(LoanInfoDto loanInfoDto) {
        ResultDto resultDto = new ResultDto(loanInfoService.getLoanInfo(loanInfoDto));
        return resultDto;
    }

    @Override
    public int countSplitInfo(Integer dataType) {
        return loanInfoMapper.countSplitInfo(dataType);
    }

    @Override
    public int countSplitUser(Integer dataType) {
        return loanInfoMapper.countSplitUser(dataType);
    }

    @Override
    public int countWdzjData() {
        return loanInfoMapper.countWdzjData();
    }

    @Override
    public ResultDto getLoanInfoData(LoanSplitDto loanSplitDto) {
        Map<String,Object> params = Maps.newHashMap();
        com.zdmoney.utils.Page<LoanInfoData> page = new com.zdmoney.utils.Page<>();
        page.setPageNo(loanSplitDto.getPageNo());
        page.setPageSize(loanSplitDto.getPageSize());
        params.put("page",page);
//        params.put("isFirst",loanSplitDto.getIsFirst());
//        params.put("isSplit",loanSplitDto.getIsSplit());
        ResultDto resultDto = new ResultDto(loanInfoMapper.getLoanInfoData(params));
        return resultDto;
    }

    @Override
    public ResultDto getLoanTransferData(LoanSplitDto loanSplitDto) {
        Map<String,Object> params = Maps.newHashMap();
        com.zdmoney.utils.Page<LoanInfoData> page = new com.zdmoney.utils.Page<>();
        page.setPageNo(loanSplitDto.getPageNo());
        page.setPageSize(loanSplitDto.getPageSize());
        params.put("page",page);
        params.put("isFirst",loanSplitDto.getIsFirst());
        return new ResultDto(loanInfoMapper.getLoanTransferData(params));
    }

    @Override
    public ResultDto getPreferenceData(LoanSplitDto loanSplitDto) {
        Map<String,Object> params = Maps.newHashMap();
        com.zdmoney.utils.Page<LoanInfoData> page = new com.zdmoney.utils.Page<>();
        page.setPageNo(loanSplitDto.getPageNo());
        page.setPageSize(loanSplitDto.getPageSize());
        params.put("page",page);
        params.put("isFirst",loanSplitDto.getIsFirst());
        return new ResultDto(loanInfoMapper.getPreferenceData(params));
    }

    @Override
    public ResultDto getLoanUserData(LoanSplitDto loanSplitDto) {
        Map<String,Object> params = Maps.newHashMap();
        com.zdmoney.utils.Page<LoanUserVo> page = new com.zdmoney.utils.Page<>();
        page.setPageNo(loanSplitDto.getPageNo());
        page.setPageSize(loanSplitDto.getPageSize());
        params.put("page",page);
        params.put("isFirst",loanSplitDto.getIsFirst());
        params.put("isSplit",loanSplitDto.getIsSplit());
        return new ResultDto(loanInfoMapper.getLoanUserData(params));
    }

    @Override
    public int saveLoanInfo(List<LoanInfoData> list) {
        return loanInfoMapper.saveLoanInfo(list);
    }

    @Override
    public int saveLoanUser(List<LoanUserVo> list) {
        return loanInfoMapper.saveLoanUser(list);
    }

    @Override
    public LoanInfoData getBusiProductInfo(String productId) {
        return loanInfoMapper.getBusiProductInfo(productId);
    }

    @Override
    public LoanInfoData getBusiProductSubInfo(String productId) {
        return loanInfoMapper.getBusiProductSubInfo(productId);
    }

    @Override
    public String getLastMoney() {
        return loanInfoMapper.getLastMoney();
    }

	@Override
	public String getOrderLastMoney() {
		return loanInfoMapper.getOrderLastMoney();
	}

	@Override
    public String earlyRepaymentData(Map params) {
        com.zdmoney.utils.Page<PreapyVo> page =new com.zdmoney.utils.Page<>();
        page.setPageNo(Integer.parseInt(params.get("page")+""));
        page.setPageSize(Integer.parseInt(params.get("pageSize")+""));
        params.put("page",page);
        List<PreapyVo> preapyVos = loanInfoMapper.earlyRepaymentData(params);
        page.setResults(preapyVos);
        JSONObject obj = JSONUtil.createObj();
        obj.put("totalPage",page.getTotalPage());
        obj.put("currentPage",page.getPageNo());
        obj.put("preapys",preapyVos);
        return JSONUtil.toJsonStr(obj);
    }

	@Override
	public String wdtyBorrowData(WdtyReqDto req) {
		WdtyResVo res = null;
		List borrowData = Lists.newArrayList();
		Map<String, Object> params = Maps.newHashMap();
		com.zdmoney.utils.Page page = new com.zdmoney.utils.Page<>();
		page.setPageNo(req.getPage_index());
		page.setPageSize(req.getPage_size());
		params.put("page", page);
		params.put("time_from", req.getTime_from());
		params.put("time_to", req.getTime_to());
		List<WdtyLoanInfo> wdtyLoanInfos = wdtyLoanInfoMapper.queryWdtyLoanInfo(params);
		if (CollUtil.isEmpty(wdtyLoanInfos)) {
			res = WdtyResVo.FAIL("未授权的访问!");
		} else {
			for (WdtyLoanInfo wdtyLoanInfo : wdtyLoanInfos) {
				Integer dataType = wdtyLoanInfo.getDataType();
				String url = "https://www.laocaibao.com/";
				if (dataType == 1 || dataType == 2) {
					String transferStatus = wdtyLoanInfo.getTransferStatus();
					if (StrUtil.equalsIgnoreCase(transferStatus, "0")) {
						url = configParamBean.getLoanUrl() + wdtyLoanInfo.getPlanId();
					} else {
						url = configParamBean.getLoanTransferUrl() + wdtyLoanInfo.getPlanId();
					}
				}
				wdtyLoanInfo.setUrl(url);
				JSONObject jsonObject = JSONUtil.parseObj(wdtyLoanInfo);
				jsonObject.remove("dataType");
				jsonObject.remove("planId");
				jsonObject.remove("transferStatus");
				borrowData.add(jsonObject);
			}
			res = WdtyResVo.SUCCESS(page.getPageNo(), page.getPageSize(), borrowData);
		}
		return JSONUtil.parseObj(res,false).toString();
	}

	@Override
	public String wdtyInvestData(WdtyReqDto req) {
		WdtyResVo res = null;
		List investData = Lists.newArrayList();
		Map<String, Object> params = Maps.newHashMap();
		params.put("pageSize", req.getPage_size());
		params.put("pageNo", req.getPage_index());
		params.put("time_from", req.getTime_from());
		params.put("time_to", req.getTime_to());
		List<WdtyLoanUser> wdtyLoanUsers = wdtyLoanUserMapper.queryWdtyLoanUserByInfo(params);
		if (CollUtil.isEmpty(wdtyLoanUsers)) {
			res = WdtyResVo.FAIL("未授权的访问!");
		} else {
			for (WdtyLoanUser wdtyLoanUser : wdtyLoanUsers) {
				JSONObject jsonObject = JSONUtil.parseObj(wdtyLoanUser);
				jsonObject.remove("dataType");
				investData.add(jsonObject);
			}
			res = WdtyResVo.SUCCESS(req.getPage_index(), req.getPage_size(), investData);
		}
		return JSONUtil.parseObj(res,false).toString();
	}

	@Override
	public String prepaymentData(WdtyReqDto req) {
		WdtyResVo res = null;
		List investData = Lists.newArrayList();
		Map<String, Object> params = Maps.newHashMap();
		com.zdmoney.utils.Page page = new com.zdmoney.utils.Page<>();
		page.setPageNo(req.getPage_index());
		page.setPageSize(req.getPage_size());
		params.put("page", page);
		params.put("time_from", req.getTime_from());
		params.put("time_to", req.getTime_to());
		List<WdtyEarlySettlement> wdtyEarlySettlements = wdtyLoanInfoMapper.earlySettlementByDate(params);
		if (CollUtil.isEmpty(wdtyEarlySettlements)) {
			res = WdtyResVo.FAIL("未授权的访问!");
		} else {
			for (WdtyEarlySettlement wdtyEarlySettlement : wdtyEarlySettlements) {
				Integer dataType = wdtyEarlySettlement.getDataType();
				String url = "https://www.laocaibao.com/";
				if (dataType == 1 || dataType == 2) {
					String transferStatus = wdtyEarlySettlement.getTransferStatus();
					if (StrUtil.equalsIgnoreCase(transferStatus, "0")) {
						url = configParamBean.getLoanUrl() + wdtyEarlySettlement.getPlanId();
					} else {
						url = configParamBean.getLoanTransferUrl() + wdtyEarlySettlement.getPlanId();
					}
				}
				wdtyEarlySettlement.setUrl(url);
				JSONObject jsonObject = JSONUtil.parseObj(wdtyEarlySettlement);
				jsonObject.remove("dataType");
				jsonObject.remove("planId");
				jsonObject.remove("transferStatus");
				investData.add(jsonObject);
			}
			res = WdtyResVo.SUCCESS(page.getPageNo(), page.getPageSize(), investData);
		}
		return JSONUtil.parseObj(res,false).toString();
	}

	@Override
	public int countWdzyInfoByDate() {
		return wdtyLoanInfoMapper.countWdzyInfoByDate();
	}

	@Override
	public int countWdzyPrepaymentByDate() {
		return wdtyLoanInfoMapper.countWdzyPrepaymentByDate();
	}

	@Override
	public int countWdzyUserByDate() {
		return wdtyLoanUserMapper.countWdzyUserByDate();
	}

	@Override
	public int insertLastDayWdzjInfoData() {
		return wdtyLoanInfoMapper.insertLastDayWdzjInfoData();
	}

	@Override
	public int insertLastDayWdzjUserData() {
		return wdtyLoanUserMapper.insertLastDayWdzjUserData();
	}

	@Override
	public int insertLastDayWdzjPrepaymentData() {
		return wdtyLoanInfoMapper.insertLastDayWdzjPrepaymentData();
	}

	@Override
	public String getOrderRebates(RebateReqDto reqDto) {
		List investData = Lists.newArrayList();
		RebateResVo rebateResVo = RebateResVo.SUCCESS(null);
		List<RebateOrderVo> orderRebates = wdtyLoanInfoMapper.getOrderRebates(reqDto);
		if (CollUtil.isEmpty(orderRebates)){
			if(StrUtil.isNotBlank(reqDto.getOrder_id())){
				rebateResVo = RebateResVo.FAIL(RebateResVo.RebateResCode.NO_ORDER);
			}
		}else {
			for (RebateOrderVo orderRebate : orderRebates) {
				JSONObject jsonObject = JSONUtil.parseObj(orderRebate,false);
				if (orderRebate.getStatus() == 1 || orderRebate.getStatus() == 3){
					jsonObject.put("start_time",null);
				}else {
					if(orderRebate.getStart_time()!=null){
						jsonObject.put("start_time",(orderRebate.getStart_time().getTime()/1000));
					}
				}
				if (orderRebate.getTrade_time() != null){
					jsonObject.put("trade_time",(orderRebate.getTrade_time().getTime()/1000));
				}
				if (orderRebate.getReg_time()!=null){
					jsonObject.put("reg_time",(orderRebate.getReg_time().getTime()/1000));
				}

				investData.add(jsonObject);
			}
			rebateResVo= RebateResVo.SUCCESS(investData);
		}
		return JSONUtil.parseObj(rebateResVo,false).toString();
	}

    @Override
    public String getOrderFlw(FlwReqDto reqDto) {
        String sId = configParamBean.getSId();
        if (sId ==null){
            throw new RuntimeException("没有找到相应的shopId");
        }
        Map map = Maps.newTreeMap();
        map.put("startDate",reqDto.getBegin_date());
        map.put("endDate",reqDto.getEnd_date());
        map.put("status",reqDto.getUpdate());
        map.put("sId",sId);
       Orders orders = busiOrderMapper.getOrderListForFlw(map);
        String ordersXml = null;
        try {
            ordersXml = XMLUtil.beanToXml(orders, Orders.class);
            log.info("xml文本为"+ordersXml);
        } catch (Exception e) {
            log.error("对象转xml异常");
            e.printStackTrace();
        }
        return ordersXml;
    }

    @Override
    public String sendLycjInfoData(String dateTime, int pageNo, int pageSize) {
        return lycjService.sendLoanInfoData(dateTime,pageNo,pageSize);
    }

    @Override
    public String sendLycjUserData(String dateTime, int pageNo, int pageSize) {
        return lycjService.sendLoanUserData(dateTime,pageNo,pageSize);
    }

    @Override
    public String sendLycjPrepaymentData(String dateTime, int pageNo, int pageSize) {
        return lycjService.getPrepayment(dateTime,pageNo,pageSize);
    }

    @Override
    public ResultDto insertStaffWhilte(String idNum, String quitTime){
        log.info("离职员工插入白名单，请求参数idNum: [{}],quitTime: [{}]",idNum ,quitTime);
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByIdNum(idNum);
        if (customerMainInfo == null){
            log.error("没有查询到该用户! 身份证号 : {}", idNum);
            return ResultDto.FAIL("没有查询到该用户!");
        }
        BusiDimissionStaffWhilte staffByCmNumber = staffWhilteService.getStaffByCmNumber(customerMainInfo.getCmNumber());

        if (staffByCmNumber != null && DateUtil.date().compareTo(staffByCmNumber.getExpiryDate()) <= 0){
            log.error("该用户已插入用户白名单，失效日期为："+DateUtil.format(staffByCmNumber.getExpiryDate(),"yyyy-MM-dd HH:mm:ss"));
            return new ResultDto("1111","添加失败","该用户已插入用户白名单，失效日期为："+DateUtil.format(staffByCmNumber.getExpiryDate(),"yyyy-MM-dd HH:mm:ss"));
        }
        BusiDimissionStaffWhilte staffWhilte = new BusiDimissionStaffWhilte();
        staffWhilte.setCmNumber(customerMainInfo.getCmNumber());
        staffWhilte.setInsertDate(new Date());
        staffWhilte.setQuitTime(DateUtil.parse(quitTime).toJdkDate());
        DateTime expiryDateTime = DateUtil.offsetDay(staffWhilte.getQuitTime(), configParamBean.getExpiryDays());
        staffWhilte.setExpiryDate(expiryDateTime.toJdkDate());
        staffWhilte.setExpiryDays(Long.valueOf(configParamBean.getExpiryDays()));
        int flag = staffWhilteService.saveBusiDimissionStaffWhilte(staffWhilte);
        if(flag != 1){
            log.error("插入失败 请求参数idNum: [{}],quitTime: [{}]",idNum ,quitTime);
            return new ResultDto("1111","添加失败",flag);
        }
        log.info("插入成功 请求参数idNum: [{}],quitTime: [{}]",idNum ,quitTime);
        return new ResultDto("添加成功",flag);
    }
}
