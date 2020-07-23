package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.service.BusiArchiveService;
import com.zdmoney.service.OperationsStatisticsService;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiArchiveVo;
import com.zdmoney.webservice.api.dto.sundry.OperationsResultStatisticsDto;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_600020;
import websvc.models.Model_600021;
import websvc.req.ReqMain;
import websvc.servant.SundryFunctionService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.zdmoney.vo.OperationsResultStatisticsVo;
//import com.zdmoney.webservice.api.dto.sundry.BusiArchiveDto;
//import websvc.models.Model_600022;

/**
 * Created by user on 2018/1/18.
 */
@Service
public class SundryFunctionServiceImpl implements SundryFunctionService {

    @Autowired
    private BusiArchiveService archiveService;

    @Autowired
    private OperationsStatisticsService operationsStatisticsService;

    @FunctionId("600022")
    @Override
    public Result queryOperationsStatisticsList(ReqMain reqMain) throws Exception {
        //Model_600022 model = (Model_600022) reqMain.getReqParam();
        /*Page<OperationsResultStatisticsVo> page = new Page<>();
        page.setPageSize(model.getPageSize());
        page.setPageNo(model.getPageNo());
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        List<OperationsResultStatisticsVo> list = operationsStatisticsService.selectMonthList(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for(OperationsResultStatisticsVo vo : list){
            vo.setMonth(sdf.format(vo.getAnalysisTime()));
        }
        page.setResults(list);*/
        Map<String,Object> map = new HashMap<>();
        List<String> list = operationsStatisticsService.selectSimpleMonthList(map);
        JSONObject json = new JSONObject();
        json.put("results",list);
        return Result.success(json);
    }

    @FunctionId("600021")
    @Override
    public Result queryOperationsStatisticsDetail(ReqMain reqMain) throws Exception {
        Model_600021 model = (Model_600021) reqMain.getReqParam();
        String analysisTime = model.getMonth();
        Map<String,Object> map = new HashMap<>();
        map.put("analysisTime",analysisTime);
        List<OperationsResultStatisticsDto> list = operationsStatisticsService.selectListByMap(map);
        OperationsResultStatisticsDto dto = null;
        if(list.size()>0) dto = list.get(0);
        //OperationsResultStatisticsDto dto = operationsStatisticsService.selectById(model.getId());
        JSONObject json = dto == null ? new JSONObject().element("isPresent",0) : transform2Json(dto);
        return Result.success(json);
    }

    private JSONObject transform2Json(OperationsResultStatisticsDto dto){
        JSONObject json = new JSONObject();

        json.put("isPresent",1);
        json.put("analysisTime",dto.getAnalysisTime()==null ? "" : new SimpleDateFormat("yyyy-MM").format(dto.getAnalysisTime()));
        json.put("cumulativeLoanAmount",new JSONObject().element("value",dto.getCumulativeLoanAmount()).element("name","累计交易金额"));
        json.put("cumulativeLoanCount",new JSONObject().element("value",dto.getCumulativeLoanCount()).element("name","累计借贷笔数"));
        json.put("creditBalance",new JSONObject().element("value",dto.getCreditBalance()).element("name","借贷余额"));
        json.put("creditBalanceCount",new JSONObject().element("value",dto.getCreditBalanceCount()).element("name","借贷余额笔数"));
        json.put("intrestBalance",new JSONObject().element("value",dto.getIntrestBalance()).element("name","利息余额"));
        json.put("cumulativeBorrowersAmount",new JSONObject().element("value",dto.getCumulativeBorrowersAmount()).element("name","累计借款人数量"));
        json.put("currentBorrowersAmount",new JSONObject().element("value",dto.getCurrentBorrowersAmount()).element("name","当前借款人数量"));
        json.put("topTenBorrowersDebtAmount",
                new JSONObject().element("value",
                        dto.getTopTenBorrowersDebtAmount()!=null && dto.getTopTenBorrowersDebtAmount().contains("%")?dto.getTopTenBorrowersDebtAmount().substring(0,dto.getTopTenBorrowersDebtAmount().indexOf("%")):dto.getTopTenBorrowersDebtAmount())
                        .element("name","前十大借款人待还金额占比"
                                + (dto.getTopTenBorrowersDebtAmount()==null?"":(dto.getTopTenBorrowersDebtAmount().contains("%")?dto.getTopTenBorrowersDebtAmount():dto.getTopTenBorrowersDebtAmount()+"%"))));
        json.put("biggestLoanDebtAmount",
                new JSONObject().element("value",
                        dto.getBiggestLoanDebtAmount()!=null && dto.getBiggestLoanDebtAmount().contains("%")?dto.getBiggestLoanDebtAmount().substring(0,dto.getBiggestLoanDebtAmount().indexOf("%")):dto.getBiggestLoanDebtAmount())
                        .element("name","最大单一借款人待还金额占比"
                                + (dto.getBiggestLoanDebtAmount()==null?"":(dto.getBiggestLoanDebtAmount().contains("%")?dto.getBiggestLoanDebtAmount():dto.getBiggestLoanDebtAmount()+"%"))));
        json.put("relatedCreditBalance",new JSONObject().element("value",dto.getRelatedCreditBalance()).element("name","关联关系借款余额"));
        json.put("relatedCreditBalanceCount",new JSONObject().element("value",dto.getRelatedCreditBalanceCount()).element("name","关联关系借款余额笔数"));
        json.put("cumulativeLendersAmount",new JSONObject().element("value",dto.getCumulativeLendersAmount()).element("name","累计出借人数量"));
        json.put("currentLendersAmount",new JSONObject().element("value",dto.getCurrentLendersAmount()).element("name","当前出借人数量"));
        json.put("perCapitaLendAmount",new JSONObject().element("value",dto.getPerCapitaLendAmount()).element("name","人均累计出借金额"));
        json.put("biggestLoanBalanceAmount",
                new JSONObject().element("value",
                        dto.getBiggestLoanBalanceAmount()!=null && dto.getBiggestLoanBalanceAmount().contains("%")?dto.getBiggestLoanBalanceAmount().substring(0,dto.getBiggestLoanBalanceAmount().indexOf("%")):dto.getBiggestLoanBalanceAmount())
                        .element("name","最大单户出借余额占比"
                                + (dto.getBiggestLoanBalanceAmount()==null?"":(dto.getBiggestLoanBalanceAmount().contains("%")?dto.getBiggestLoanBalanceAmount():dto.getBiggestLoanBalanceAmount()+"%"))));
        json.put("topTenBalanceAmount",
                new JSONObject().element("value",
                        dto.getTopTenBalanceAmount()!=null && dto.getTopTenBalanceAmount().contains("%")?dto.getTopTenBalanceAmount().substring(0,dto.getTopTenBalanceAmount().indexOf("%")):dto.getTopTenBalanceAmount())
                        .element("name","最大十户出借余额占比"
                                + (dto.getTopTenBalanceAmount()==null?"":(dto.getTopTenBalanceAmount().contains("%")?dto.getTopTenBalanceAmount():dto.getTopTenBalanceAmount()+"%"))));
        json.put("overdueAmount",new JSONObject().element("value",dto.getOverdueAmount()).element("name","逾期金额（出借人到期未收到）"));
        json.put("overdueDealsCount",new JSONObject().element("value",dto.getOverdueDealsCount()).element("name","逾期笔数（出借人到期未收到）"));
        json.put("overdueProjectsPercent",
                new JSONObject().element("value",
                        dto.getOverdueProjectsPercent()!=null &&dto.getOverdueProjectsPercent().contains("%")?dto.getOverdueProjectsPercent().substring(0,dto.getOverdueProjectsPercent().indexOf("%")):dto.getOverdueProjectsPercent())
                        .element("name","项目逾期率"
                                + (dto.getOverdueProjectsPercent()==null?"":(dto.getOverdueProjectsPercent().contains("%")?dto.getOverdueProjectsPercent():dto.getOverdueProjectsPercent()+"%"))));
        json.put("overduePercent",
                new JSONObject().element("value",
                        dto.getOverduePercent()!=null && dto.getOverduePercent().contains("%")?dto.getOverduePercent().substring(0,dto.getOverduePercent().indexOf("%")):dto.getOverduePercent())
                        .element("name","金额逾期率"
                                + (dto.getOverduePercent()==null?"":(dto.getOverduePercent().contains("%")?dto.getOverduePercent():dto.getOverduePercent()+"%"))));
        json.put("over90daysOverdueAmount",new JSONObject().element("value",dto.getOver90daysOverdueAmount()).element("name","逾期90天（不含）以上金额（出借人到期未收到）"));
        json.put("over90daysOverdueCount",new JSONObject().element("value",dto.getOver90daysOverdueCount()).element("name","逾期90天（不含）以上笔数（出借人到期未收到）"));
        json.put("cumulativeSubstituteAmount",new JSONObject().element("value",dto.getCumulativeSubstituteAmount()).element("name","累计代偿金额（非借款人、非网贷机构的第三方代偿）"));
        json.put("cumulativeSubstituteCount",new JSONObject().element("value",dto.getCumulativeSubstituteCount()).element("name","累计代偿笔数（非借款人、非网贷机构的第三方代偿）"));
        json.put("serviceCharge",new JSONObject().element("value", dto.getServiceCharge()).element("name","借款人平台服务费"));
        json.put("transferServiceCharge",new JSONObject().element("value", dto.getTransferServiceCharge()).element("name","出借人债权转让服务费"));
        json.put("customerReg",new JSONObject().element("value", dto.getCustomerReg()).element("name","用户注册"));
        json.put("recharge",new JSONObject().element("value", dto.getRecharge()).element("name","充值"));
        json.put("depositAccount",new JSONObject().element("value", dto.getDepositAccount()).element("name","开通存管账户"));
        json.put("repayment",new JSONObject().element("value", dto.getRepayment()).element("name","回款到账"));
        json.put("withdraw",new JSONObject().element("value", dto.getWithdraw()).element("name","提现"));
        json.put("lenderProductServiceCharge",new JSONObject().element("value", dto.getLenderProductServiceCharge()).element("name","出借人产品服务费"));
        json.put("accumulativeLenderNum",new JSONObject().element("value", dto.getAccumulativeLenderNum()).element("name","累计出借笔数"));
        json.put("accumulatedLoanAmount",new JSONObject().element("value", dto.getAccumulatedLoanAmount()).element("name","累计借贷金额"));

        return json;
    }

    @FunctionId("600020")
    @Override
    public Result queryArchiveList(ReqMain reqMain) throws Exception {
        Model_600020 model = (Model_600020) reqMain.getReqParam();
        Page<BusiArchiveVo> page = new Page<>();
        page.setPageSize(model.getPageSize());
        page.setPageNo(model.getPageNo());
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        List<BusiArchiveVo> list = archiveService.selectSimpleList(map);
        page.setResults(list);
        return Result.success(page);
    }

}
