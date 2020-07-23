package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.mapper.payment.PaymentCalendarMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.payment.PaymentPlanService;
import com.zdmoney.vo.AssetCalendar;
import com.zdmoney.vo.AssetCalendarVO;
import com.zdmoney.vo.AssetDetailVO;
import com.zdmoney.vo.AssetInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.list.TreeList;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_500028;
import websvc.models.Model_500029;
import websvc.models.Model_500030;
import websvc.req.ReqMain;
import websvc.servant.PaymentPlanFunctionService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentPlanFunctionServiceImpl implements PaymentPlanFunctionService {

    @Autowired
    private PaymentPlanService paymentPlanService;

    @Autowired
    private PaymentCalendarMapper paymentCalendarMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @FunctionId("500028")
    public Result getPaymentPlanList(ReqMain reqMain) throws Exception {
        Model_500028 cdtModel = (Model_500028) reqMain.getReqParam();
        return Result.success(paymentPlanService.getPaymentPlanList(cdtModel.getOrderNum()));
    }

    @FunctionId("500029")
    @Override
    public Result getAssetCalendar(ReqMain reqMain) throws Exception {
        Model_500029 model = (Model_500029) reqMain.getReqParam();
        customerMainInfoService.findOneByCustomerId(model.getCustomerId());
        Map paramsMap = new HashMap<>();
        paramsMap.put("customerId", model.getCustomerId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String repayDate = StringUtils.isNotBlank(model.getRepayDate()) ? model.getRepayDate() : sdf.format(new Date());
        paramsMap.put("repayDate", sdf.format(sdf.parse(repayDate)));
        AssetCalendarVO assetCalendar = new AssetCalendarVO();

        // 待回款
        List<AssetCalendar> waitReceiveList = paymentCalendarMapper.queryAssetCalendarByWaitReceive(paramsMap);
        Set<Integer> waitReceiveSet = new TreeSet();
        BigDecimal totalWaitReceiveAmt = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(waitReceiveList)) {
            for (AssetCalendar asset : waitReceiveList) {
                waitReceiveSet.add(asset.getDay());
                if (asset.getPrincipalInterest() != null) {
                    totalWaitReceiveAmt = totalWaitReceiveAmt.add(asset.getPrincipalInterest());
                }
            }
        }
        assetCalendar.setWaitReceiveList(convertAssetCalendar(waitReceiveList, waitReceiveSet));
        assetCalendar.setTotalWaitReceiveAmt(totalWaitReceiveAmt);
        assetCalendar.setWaitReceiveCount(waitReceiveList.size());

        // 已回款
        List<AssetCalendar> receivableList = paymentCalendarMapper.queryAssetCalendarByReceivable(paramsMap);
        Set<Integer> receivableSet = new TreeSet();
        BigDecimal totalReceivableAmt = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(receivableList)) {
            for (AssetCalendar asset : receivableList) {
                receivableSet.add(asset.getDay());
                if (asset.getPrincipalInterest() != null) {
                    totalReceivableAmt = totalReceivableAmt.add(asset.getPrincipalInterest());
                }
            }
        }
        assetCalendar.setTotalReceivableAmt(totalReceivableAmt);
        List tempList = convertAssetCalendar(receivableList, receivableSet);
        Collections.reverse(tempList);
        assetCalendar.setReceivableList(tempList);
        assetCalendar.setReceivableCount(receivableList.size());
        return Result.success(assetCalendar);
    }

    private List convertAssetCalendar(List<AssetCalendar> assetCalendarList, Set<Integer> set){
        List tempList = new TreeList();
        for(Iterator iter = set.iterator(); iter.hasNext(); ) {
            Integer day = (Integer) iter.next();
            List<AssetCalendar> assetList = null;
            for (AssetCalendar asset : assetCalendarList) {
                if (CollectionUtils.isEmpty(assetList)) assetList = new ArrayList<>();
                if (day == asset.getDay()) assetList.add(asset);
            }
            tempList.add(assetList);
        }
        return tempList;
    }

    @FunctionId("500030")
    @Override
    public Result getAssetDetailInfo(ReqMain reqMain) throws Exception {
        Model_500030 model = (Model_500030) reqMain.getReqParam();
        AssetDetailVO assetDetailVO = new AssetDetailVO();
        BigDecimal totalAsset = new BigDecimal(0);
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCustomerId(model.getCustomerId());

        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(customerMainInfo);
        assetDetailVO.setAccountBalance(accountBalance);

        List<AssetInfo> assetInfoList = paymentCalendarMapper.queryAssetDetail(model.getCustomerId());
        for (AssetInfo asset : assetInfoList){
            totalAsset = totalAsset.add(asset.getInterset()).add(asset.getPrincipal());
        }
        assetDetailVO.setAssetList(assetInfoList);
        assetDetailVO.setTotalAsset(totalAsset.add(accountBalance));
        return Result.success(assetDetailVO);
    }
}
