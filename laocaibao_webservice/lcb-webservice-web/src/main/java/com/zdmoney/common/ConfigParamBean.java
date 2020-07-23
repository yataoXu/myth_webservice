package com.zdmoney.common;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@Slf4j
@Component
public class ConfigParamBean implements InitializingBean {

	private DefaultConversionService conversionService = new DefaultConversionService();

	@Value("${ce_rechargeMd5Key}")
	private String rechargeMd5Key;

	@Value("${ce_validate_switch}")
	private String ceValidateSwitch;

	@Value("${app_version_switch}")
	private String appVersionSwitch;

	@Value("${session_validate_switch}")
	private String sessionValidateSwitch;

	@Value("${message_push_switch}")
	private String messagePushSwitch;

	@Value("${ID5_switch}")
	private String ID5Switch;

	@Value("${pay_version3}")
	private String payVersion3;

	@Value("${pay_version4}")
	private String payVersion4;

	@Value("${pay_merchant_no}")
	private String payMerchantNo;

	@Value("${pay_private_key}")
	private String payPrivateKey;

	@Value("${cash_card_bin_url}")
	private String cashCardBinUrl;

	@Value("${get_sub_bank_url}")
	private String getSubBankUrl;

	@Value("${qrCode_url}")
	private String qrCodeurl;

	@Value("${voice_server}")
	private String voiceService;

	@Value("${voice_port}")
	private String voicePort;

	@Value("${voice_account}")
	private String voiceAccount;

	@Value("${voice_token}")
	private String voiceToken;

	@Value("${voice_appid}")
	private String voiceAppid;

	@Value("${user_token_key}")
	private String userTokenKey;

	@Value("${pushwsRootUrl}")
	private String pushwsRootUrl;

	@Value("${push_key}")
	private String pushKey;

	@Value("${message_push_switch}")
	private String pushSwitch;

	@Value("${product_detail_url}")
	private String productDetailUrl;

	@Value("${login_grant_red_pack_time}")
	private String loginGrantRedPackTime;

	@Value("${tpp_h5_save_host}")
	private String tppH5SaveHost;

	@Value("${tpp_h5_save_port}")
	private String tppH5SavePort;

	@Value("${tpp_h5_save_user}")
	private String tppH5SaveUser;

	@Value("${tpp_h5_save_password}")
	private String tppH5SavePassword;

	@Value("${tpp_h5_save_remote_path}")
	private String tppH5SaveRemotePath;

	@Value("${tpp_agreement_h5_url}")
	private String tppAgreementH5Url;

	@Value("${tpp_valicode_try_time}")
	private Short tppValicodeTryTime;

	@Value("${trade_agreement_url}")
	private String tradeAgreementUrl;

	@Value("${banner_path}")
	private String bannerPath;

	@Value("${img_path}")
	private String imgPath;

	@Value("${cashier_addr}")
	private String cashierAddr;

	@Value("${cashier_addr_lan}")
	private String cashierAddrLan;

	@Value("${mall_home_url}")
	private String mallHomeUrl;

	@Value("${task_center_url}")
	private String taskCenterUrl;

	@Value("${life_service_url}")
	private String lifeServiceUrl;

	@Value("${touch_webapp_home_url}")
	private String touchWebappHomeUrl;

	@Value("${invite_friend_url}")
	private String inviteFriendUrl;

	@Value("${subject_partner_no}")
	private String subjectPartnerNo;

	@Value("${hr.recharge.callback_url}")
	private String rechargeCallbackUrl;

	@Value("${open.account.request}")
	private String openAccountRequest;

	@Value("${touch.account.bindcard}")
	private String touchAccountBindCard;

	@Value("${guarantee.cmNumber}")
	private String guaranteeCmNumber;

	@Value("${hr.recharge.key}")
	private String hrRechargeKey;

	@Value("${touch.bindCard}")
	private String touchBindCard;

	@Value("${risk_test_url}")
	private String riskTestUrl;

	@Value("${ws_msgRead_manager_url}")
	private String msgReadManagerUrl;

	@Value("${elephant.park.url}")
	private String elephantParkUrl;

	@Value("${consulting_url}")
	private String consultingUrl;

	@Value("${touch_product_detail_url}")
	private String touchProductDetailsUrl;

	@Value("${spring.redis.cluster.nodes}")
	private String spring_redis_cluster_nodes;

	@Value("${spring.redis.cluster.max-redirects}")
	private String spring_redis_cluster_max_redirects;

	@Value("${mail.smtp.to}")
	private String mail_smtp_to;

	/**
	 * 公告/消息详情url
	 */
	@Value("${messageandnoticedetail.url}")
	private String messageAndNoticeDetailUrl;


	/*************安融认证***************/
	@Value("${ar_member}")
	private String ar_member;

	@Value("${ar_member_pass}")
	private String ar_member_pass;

	@Value("${ar_ssl}")
	private String ar_ssl;

	@Value("${ar_keyStore_path}")
	private String ar_keyStore_path;

	@Value("${ar_keyStore_pass}")
	private String ar_keyStore_pass;

	@Value("${ar_authUrl}")
	private String ar_authUrl;
	/*************安融认证***************/


	/*************算话认证***************/
	@Value("${sh_member_code}")
	private String sh_member_code;

	@Value("${sh_authUrl}")
	private String sh_authUrl;

	@Value("${sh_member_key}")
	private String sh_member_key;
	/*************算话认证***************/

	@Value("${deposit_version}")
	private String depositVersion;

	@Value(value="${loan.url}")
	private String loanUrl;
	@Value(value="${loan.transfer.url}")
	private String loanTransferUrl;
	@Value(value="${loan.plan.url}")
	private String loanPlanUrl;

	@Value("${promotion_start_date}")
	private String promotionStartDate;

	@Value("${promotion_end_date}")
	private String promotionEndDate;

	@Value("${range_interest_start_date}")
	private String rangeInterestStartDate;

	@Value("${range_interest_end_date}")
	private String rangeInterestEndDate;

	@Value("${banner_img_url}")
	private String bannerImgUrl;

	@Value("${banner_redi_url}")
	private String RediUrl;

	@Value("${reinvest_networkuser_regulations}")
	private String reinvestNetworkuserRegulations;

	@Value("${reinvest_accountant_regulations}")
	private String reinvestAccountantRegulations;

	@Value("${reinvest_360days}")
	private String reinvest360days;

	@Value("${reinvest_180days}")
	private String reinvest180days;

	@Value("${rate_differ_180days}")
	private String ratediffer180days;

	@Value("${rate_differ_360days}")
	private String ratediffer360days;

	//返利网s_id
	@Value("${fanli_s_id}")
	private String sId;

	@Value("${shop_key}")
	private String shopKey;//返利网key


	@Value("${fuiou_merchant_code}")
	private String fuiouMerchantCode;

	@Value("${fuiou_key}")
	private String fuiouKey;

	@Value("${zdpay_url}")
	private String zdpayUrl;

	@Value("${zdpay_backNotify_url}")
	private String zdpayBackNotifyUrl;

	@Value("${TASK_ID_SCRATCH_CARD}")
	private String scratchCardId;

	@Value("${TASK_ID_CATCH_DOLL}")
	private String catchDollId;

	@Value("${touch.webapp.url}")
	private String touchAppUrl;

	@Value("${rocketmq.serverAddress}")
	private String nameSrvAddr;

	@Value(value = "${order.limit.time}")
	private String orderLimitTime;

	@Value(value = "${order.limit.desc}")
	private String orderLimitDesc;

	@Value(value="${invest.share.url}")
	private String shareUrl;

	@Value(value = "${coupon.no_5}")
	private String coupon5; //5元红包ID

	@Value(value = "${coupon.no_10}")
	private String coupon10;//10元红包ID

	@Value(value = "${coupon.no_50}")
	private String coupon50;//50元红包ID

	@Value(value = "${invest.alert_typeNo}")
	private String investAlertTypeNo;

	@Value(value = "#{'${depository.bypassed.code}'.split(',')}")
	private List<String> bypassedCode;


	@Value("${auth.scale}")
	private String authScale; //授权乘以比例

	@Value("${auth.term}")
	private String authTerm; //授权增加天数

	@Value("${auth.rate}")
	private String authRate; //授权缴费比例

	@Value("${auth.limit.amt}")
	private String authLimitAmt; //授权出借金额比例

	@Value("${auth.borrow.scale}")
	private String authBorrowScale; //借款授权乘以比例

	@Value("${auth.titile}")
	private String authTitle; //授权标题

	@Value("${auth.order.msg}")
	private String authOrderMsg; //下单授权msg

	@Value("${auth.transfer.msg}")
	private String authTransferMsg; //转让授权msg


	private Map<String,Field> fieldAutowiredKeyMap;

	@Value(value = "${manager.url}")
	private String managerUrl;

	@Value(value = "${fuiou.lender.autoLendAmt}")
	private String fuiouLenderAutoLendAmt;

	@Value(value = "${fuiou.lender.autoFeeAmt}")
	private String fuiouLenderAutoFeeAmt;

	@Value(value = "${offline.recharge.deposit.bank}")
	private String offlineRechargeDepositBank;

	@Value(value = "${offline.recharge.accountName}")
	private String offlineRechargeAccountName;

	@Value(value = "${offline.recharge.accountNo}")
	private String offlineRechargeAccountNo;

	@Value(value = "${offline.recharge.operation}")
	private String offlineRechargeOperation;

	@Value(value = "${offline.recharge.attions}")
	private String offlineRechargeAttions;

	@Value(value = "${withdrawals_limit_times}")
	private String withdrawalsLimitTimes;

	@Value(value = "${repay.amt.limit}")
	private String repayAmtLimit;

	@Value("${guarantee_cm_number}")
	private String guarantee_cm_number;

	@Value("${wx_appid}")
	private String wx_appid;

	@Value("${wx_secret}")
	private String wx_secret;

	@Value("${rating_last_days}")
	private int rating_last_days;

	@Value("${rating_up_credit_code}")
	private String rating_up_credit_code;

	@Value("${withdraw_rate}")
	private String withdraw_rate;

	/*  零壹财经  start*/
	@Value("${web.pc.url}")
	private String pcUrl;
	@Value("${lycj.visit.key}")
	private String visitKey;
	@Value("${lycj.key}")
	private String lycjKey;
	@Value("${lycj.loan.url}")
	private String lycjLoanUrl;
	@Value("${lycj.user.url}")
	private String lycjUserUrl;
	@Value("${lycj.prepayment.url}")
	private String lycjPrepaymentUrl;
	/*  零壹财经  end*/

	@Value("${member.desc.url}")
	private String memberDescUrl;

	//贴心邀请礼v4.10
	@Value("${level_gift_url1}")
	private String levelGiftUrl1;

	//购买转转礼
	@Value("${level_gift_url2}")
	private String levelGiftUrl2;

	//月度贴心礼
	@Value("${level_gift_url3}")
	private String levelGiftUrl3;

	//升级关怀
	@Value("${level_gift_url4}")
	private String levelGiftUrl4;

	//专属客服
	@Value("${level_gift_url5}")
	private String levelGiftUrl5;

	//专属产品
	@Value("${level_gift_url6}")
	private String levelGiftUrl6;

	//线下活动
	@Value("${level_gift_url7}")
	private String levelGiftUrl7;

    @Value("${month_gift_date}")
    private String monthGiftDate;

	@Value("${level_expire_day}")
	private String levelExpireDay;


	@Value("${rating_up_msg}")
	private String ratingUpMsg;

	@Value("${rating_down_msg}")
	private String ratingDownMsg;

	/*员工专属产品配置 start */
	@Value("${staff_level}")
	private String staffLevel;

	@Value("${ip_white_list}")
	private String ipWhiteList;

	@Value("${staff_payment_rate}")
	private String  staffPaymentRate;

	@Value("${expiry_days}")
	private int expiryDays;

	@Value("${product_limit_type}")
	private String productLimitType;

	/*员工专属产品配置 end */

	/*快速提现限制时段和描述*/
	@Value("${quick_withdraw_limit_time}")
	private String  quickWithdrawLimitTime;

	@Value("${quick_withdraw_limit_desc}")
	private String  quickWithdrawLimitDesc;
	/*快速提现限制时段和描述*/

	@Value("${wacai_product_remaining_part_buyer}")
	private String wacaiProductRemainingPartBuyer;

	@Value("${zdqq_md5_key}")
    private String zdqqMd5Key;

	@Value("${zdqq_notify_url}")
    private String zdqqNotifyUrl;


	/*债券转让通知配置 */
	@Value("${debt_transfer_sm_msg}")
	private String debtTransferSmMsg;

	@Value("${wsRootUrl}")
	private String wsRootUrl;

	@Value("${message_callBack}")
	private String messageCallBack;

	@Value("${dept_transfer_msg}")
	private String 	deptTransferMsg;
	/*债券转让通知配置 */

	/*挖财兜底金额告警配置*/
	@Value("${alarm_amount_wacai}")
	private String alarmAmountWacai;

	/*挖财兜底金额告警邮箱配置*/
	@Value("${alarm_amount_maill_wacai}")
	private String alarmAmountMaillWacai;


	@Value("${transfer_min_rate}")
	private String transfer_min_rate;

	@Value("${transfer_max_rate}")
	private String transfer_max_rate;

	@Value("${register_white_list}")
	private String registerWhiteList;


	@Value("${quick_withdraw_limit_customer_register_date}")
	private String quickWithdrawLimitCustomerRegisterDate;


	// 捞财宝app发布日期
	@Value("${lcb_release_date}")
	private String lcbReleaseDate;

	/*5.0 小象生活URL*/
	@Value("${store_goods_list_url}")
	private String goodsListUrl;

	@Value("${store_goods_detail_url}")
	private String goodsDetailUrl;
	/*5.0 小象生活URL*/

	/**
	 * 了解捞财宝
	 */
	@Value("${understand_url}")
	private String understandUrl;

	/**
	 * 新手运营位
	 */
	@Value("${newcomer_url}")
	private String newcomerUrl;

	@Override
	public void afterPropertiesSet() throws Exception {
		synchronized (this){
			fieldAutowiredKeyMap = new HashMap<>();
			Field[] fields = getClass().getDeclaredFields();
			for(Field field : fields){
				Value fieldAnnotation = field.getAnnotation(Value.class);
				if(fieldAnnotation != null){
					String autowiredKey = fieldAnnotation.value();
					String propKey = autowiredKey;
					if(autowiredKey.startsWith("$")){
						propKey = autowiredKey.substring(2,autowiredKey.length()-1);
					}
					fieldAutowiredKeyMap.put(propKey,field);
				}
			}
		}
	}

	@ApolloConfigChangeListener
	public void onPropChange(ConfigChangeEvent changeEvent){
		for (String key : changeEvent.changedKeys()) {
			ConfigChange change = changeEvent.getChange(key);
			log.info(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
			processChangeEvent(change);
		}
	}

	private void processChangeEvent(ConfigChange change){
		switch (change.getChangeType()){
			case MODIFIED: onModified(change);break;
			case ADDED: break;
			case DELETED: break;
			default:
		}
	}

	private void onModified(ConfigChange change){
		Field field = fieldAutowiredKeyMap.get(change.getPropertyName());
		try{
			Class<?> type = field.getType();
			Object newValue = change.getNewValue();
			Object convertedValue = newValue;
			if (type.isPrimitive()){
				convertedValue = conversionService.convert(newValue,type);
                /*if(type.equals(Integer.class)) convertedValue = Integer.valueOf(newValue.toString());
                if(type.equals(Long.class)) convertedValue = Long.valueOf(newValue.toString());
                if(type.equals(Double.class)) convertedValue = Double.valueOf(newValue.toString());*/
			}
			field.set(this,convertedValue);
		}catch (Exception e){
			log.error(String.format("set class com.zdmoney.common.ConfigPropsBean, field %s failed",change.getPropertyName()),e);
		}
	}
}
