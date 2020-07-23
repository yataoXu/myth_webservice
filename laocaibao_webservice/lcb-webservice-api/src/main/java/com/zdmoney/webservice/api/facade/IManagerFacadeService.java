package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.UserBindCardDTO;
import com.zdmoney.webservice.api.dto.busi.BusiBrandDto;
import com.zdmoney.webservice.api.dto.busi.CheckAutoPayingFeeAllowedDto;
import com.zdmoney.webservice.api.dto.busi.CheckIfPurchaseAllowedDto;
import com.zdmoney.webservice.api.dto.customer.CashCouponDto;
import com.zdmoney.webservice.api.dto.customer.CustomerGrantInfoDTO;
import com.zdmoney.webservice.api.dto.customer.CustomerGrantInfoSearchDTO;
import com.zdmoney.webservice.api.dto.customer.CustomerRatingConfigDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.QueryOrderReqDto;
import com.zdmoney.webservice.api.dto.sys.SysIconDto;

import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

/**
 * Created by 00250968 on 2018/2/8
 **/
public interface IManagerFacadeService {

    /**
     * 注册
     * @param cellPhone
     * @return
     * @throws Exception
     */
    ResultDto register(String cellPhone);

    /**
     * 实名认证
     * @param customerId
     * @param idNum
     * @param realName
     * @return
     */
    ResultDto realNameAuth(Long customerId, String idNum, String realName);

    /**
     * 绑定银行卡
     * @param userBindCardDTO
     * @return
     */
    ResultDto bindCard(UserBindCardDTO userBindCardDTO);

    /**
     * 建标
     *
     * @param subjectNo
     * @param productId
     * @return
     */
    ResultDto bidBuild(String subjectNo, Long productId);

    /**
     * 授权缴费额度是否足够
     * @param checkDto
     * @return
     */
    ResultDto<Boolean> checkIfAutoPayingAllowed(CheckAutoPayingFeeAllowedDto checkDto);
    /**
     * 授权缴费额度是否足够
     * @param checkDto
     * @return
     */
    ResultDto<Boolean> checkHRAutoPayingAllowed(CheckAutoPayingFeeAllowedDto checkDto);

    /**
     * 授权购买额度是否足够
     * @param checkDto
     * @return
     */
    ResultDto<Boolean> checkIfAuthedPurchaseAllowed(CheckIfPurchaseAllowedDto checkDto);

    /**
     * 查询用户授权信息
     * @param searchDTO
     * @return
     */
    PageResultDto<CustomerGrantInfoDTO> searchCustomerGrantInfo(CustomerGrantInfoSearchDTO searchDTO);

    /**
     * 回款发放现金券
     * @param cashCouponDto
     * @return
     */
    ResultDto sendRepayCashCoupon(CashCouponDto cashCouponDto);

	/**
	 * 查询app icons
	 * @return
	 */
	ResultDto<List<SysIconDto>> querySysIcons();

	/**
	 * 更新app icons
	 * @param sysIconDto
	 * @return
	 */
	ResultDto updateSysIcon(SysIconDto sysIconDto);

	/**
	 * 删除app icons
	 * @return
	 */
	ResultDto deleteIcon();

    /**
     * 获取再投本金
     * @param customerId
     * @return
     */
	ResultDto getHoldAsset(String customerId);

    ResultDto<List<CustomerRatingConfigDto>> findCustomerRatingConfigs();

    /**
     * 获取会员列表
     * @return
     */
	ResultDto<List<CustomerRatingConfigDto>> getCustomerRatingConfigList();

    /**
     * 更新CustomerRatingConfigDto
     * @return
     */
	ResultDto<Integer> updateCustomerRatingConfig(CustomerRatingConfigDto customerRatingConfigDto);

    /**
     * 获取品牌文案宣传列表
     */
    ResultDto<List<BusiBrandDto>> queryBusiBrand(Map<String,Object> params);

    /**
     * 新增品牌文案宣传
     * @param brandDto
     * @return
     */
    ResultDto insertBusiBrand(BusiBrandDto brandDto);

    /**
     * 修改品牌文案宣传
     */
    ResultDto updateBusiBrand(BusiBrandDto brandDto);

    /**
     * 删除品牌文案宣传
     */
    ResultDto deleteBusiBrand(Long id);

    /**
     * 删除品牌文案宣传
     */
    ResultDto<BusiBrandDto> getBusiBrandById(Long id);

    PageResultDto<BusiOrderDto> findMatchedOrders(QueryOrderReqDto reqDto);

    PageResultDto<BusiOrderDto> queryWacaiSubOrders(QueryOrderReqDto reqDto);

    ResultDto<String> matchOrderAndCredit(String orderNum);

    ResultDto<String> rematchOrderAndCredit(String orderNum);

    ResultDto<String> processMatchResultBySubject(String subjectNo);

    ResultDto<String> tenderSpecificOrders(List<String> orderNos);

    ResultDto notifyBidApp(String subjectNo);

}
