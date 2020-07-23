package websvc.servant.impl;

import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import com.zdmoney.assets.api.dto.subject.borrow.BorrowDetailDto;
import com.zdmoney.assets.api.dto.subject.borrow.MyBorrowResDto;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.service.BusiProductService;
import com.zdmoney.service.PersonLoanService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_540001;
import websvc.models.Model_540002;
import websvc.models.Model_540007;
import websvc.req.ReqMain;
import websvc.servant.PersonalLoanFunctionService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/10/21.
 */
@Service
@Slf4j
public class PersonalLoanFunctionServiceImpl implements PersonalLoanFunctionService {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private PersonLoanService personLoanService;

    @Autowired
    private BusiProductService busiProductService;

    @FunctionId("540001")
    @Override
    public Result myBorrows(ReqMain reqMain) throws Exception {
        Model_540001 plModel = (Model_540001) reqMain.getReqParam();
        try {
            CustomerMainInfo cusMainInfo = customerMainInfoMapper.selectByPrimaryKey(plModel.getCustomerId());
            if (null == cusMainInfo) {
                throw new BusinessException("customer.not.exist");
            }
            MyBorrowResDto resultDto = subjectService.queryMyBorrows(cusMainInfo.getCmIdnum(), cusMainInfo.getCmCellphone());
            return Result.success(resultDto);
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error("FunctionId:540001 fail---------->" + e.getMessage());
            return Result.fail("查询我的借款首页信息失败!");
        }
    }

    @FunctionId("540002")
    @Override
    public Result queryMyBorrowDetail(ReqMain reqMain) {
        Model_540002 model = (Model_540002) reqMain.getReqParam();
        try {
            CustomerMainInfo cusMainInfo = customerMainInfoMapper.selectByPrimaryKey(model.getCustomerId());
            if (null == cusMainInfo) {
                throw new BusinessException("customer.not.exist");
            }
            BorrowDetailDto resultDto = subjectService.queryBorrowDetail(cusMainInfo.getCmIdnum(), cusMainInfo.getCmCellphone(), model.getSubjectNo());
            return Result.success(resultDto);
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error("FunctionId:540002 fail---------->" + e.getMessage());
            return Result.fail("查询借款详情信息失败!");
        }
    }

    @FunctionId("540003")
    @Override
    public Result queryRepayDetail(ReqMain reqMain) throws Exception{
        return personLoanService.queryRepayDetail(reqMain);
    }

    @FunctionId("540004")
    @Override
    public Result signBorrow(ReqMain reqMain) throws Exception{
        return personLoanService.signBorrow(reqMain);
    }

    @FunctionId("540005")
    @Override
    public Result queryBorrowAgreement(ReqMain reqMain) throws Exception{
        return personLoanService.queryBorrowAgreement(reqMain);
    }

    @FunctionId("540006")
    @Override
    public Result queryAuthAgreement(ReqMain reqMain) throws Exception{
        return personLoanService.queryAuthAgreement(reqMain);
    }

    @FunctionId("540007")
    @Override
    public Result queryAgreementsTemplete(ReqMain reqMain) throws Exception{
        Model_540007 model = (Model_540007) reqMain.getReqParam();
        Long productId = model.getProductId();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
        paramsMap.put("productId", productId);
        BusiProduct product = busiProductService.findProductById(paramsMap);
        if(product == null){
            throw new BusinessException("产品不存在");
        }
        List<AgreementNameDto> list =  subjectService.gainPayAgreementsTemplete(product);
        return Result.success(list);
    }

    @FunctionId("540008")
    @Override
    public Result queryInvestBorrowAgreement(ReqMain reqMain) throws Exception {
        return personLoanService.queryInvestBorrowAgreement(reqMain);
    }

    @FunctionId("541000")
    @Override
    public Result queryFinPlanAgreement(ReqMain reqMain) throws Exception {
        return personLoanService.queryFinPlanAgreement(reqMain);
    }

    @FunctionId("541001")
    @Override
    public Result queryFinPlanReInvest(ReqMain reqMain) throws Exception {
        return personLoanService.queryFinPlanReInvest(reqMain);
    }
}
