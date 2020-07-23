package com.zdmoney.service.customer;

import cn.hutool.json.JSONUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectBorrowerDetailDto;
import com.zdmoney.assets.api.dto.subject.SubjectBorrowerDetailReqDto;
import com.zdmoney.assets.api.dto.subject.SubjectDiscloseInfoDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.customer.CustomerAddressMapper;
import com.zdmoney.mapper.financePlan.DebtDetailMapper;
import com.zdmoney.mapper.payChannel.BusiPayBankLimitMapper;
import com.zdmoney.mapper.payChannel.BusiPayBankMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.models.customer.CustomerAddress;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.payChannel.BusiPayBank;
import com.zdmoney.models.payChannel.BusiPayBankLimit;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.service.BusiFinancePlanService;
import com.zdmoney.service.BusiProductService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.vo.BusiBorrowerInfoVO;
import com.zdmoney.web.dto.BusiBankQuotaDTO;
import com.zdmoney.web.dto.CustomerAddressDTO;
import com.zdmoney.webservice.api.dto.plan.BusiDebtDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import websvc.models.*;
import websvc.req.ReqMain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/11/10.
 */
@Service
@Slf4j
public class CustomerAddressService {

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiPayBankMapper busiPayBankMapper;

    @Autowired
    private BusiPayBankLimitMapper busiPayBankLimitMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private BusiFinancePlanService busiFinancePlanService;

    @Autowired
    private BusiProductService busiProductService;

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;

    @Autowired
    private BusiProductSubMapper busiProductSubMapper;

    @Autowired
    private DebtDetailMapper debtDetailMapper;

    /**
     * 获取用户地址列表
     * @param reqMain
     * @return
     */
    public Result queryCustomerAddressList(ReqMain reqMain){
        List<CustomerAddressDTO> addressList = null;
        Model_550001 model = (Model_550001) reqMain.getReqParam();
        try {
            // 校验客户是否存在
            CustomerMainInfo customerInfo = customerMainInfoService.findOne(model.getCustomerId());
            if(null == customerInfo) throw new BusinessException("customer.not.exist");
            addressList = customerAddressMapper.queryCustomerAddressList(customerInfo.getId());
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error("FunctionId:550001-->查询客户ID:" + model.getCustomerId() + "的收货地址列表失败:" + e.getMessage());
        }
        return Result.success(addressList);
    }

    /**
     * 保存或修改收获地址
     * @param reqMain
     * @return
     */
    public Result saveOrUpdateAddress(ReqMain reqMain){
        Long res = 0L;
        Model_550002 model = (Model_550002) reqMain.getReqParam();
        try {
            // 校验客户是否存在
            CustomerMainInfo customerInfo = customerMainInfoService.findOne(model.getCustomerId());
            if(null == customerInfo) throw new BusinessException("customer.not.exist");

            CustomerAddress address = new CustomerAddress();
            address.setCustomerId(customerInfo.getId());
            address.setContactName(model.getContactName());
            address.setCellPhone(model.getCellPhone());
            address.setProvince(model.getProvince());
            address.setCity(model.getCity());
            address.setArea(model.getArea());
            address.setStreet(model.getStreet());
            address.setStatus(1);// 默认1:生效   0:删除
            if(null == model.getId()){
                address.setCreateTime(new Date());
                // id为空时,保存收货地址信息
                res = customerAddressMapper.saveCustomerAddress(address);
            }else{
                address.setId(model.getId());
                address.setUpdateTime(new Date());
                // 更新收货地址信息
                res = customerAddressMapper.updateCustomerAddress(address);
            }
            if (res == 0) throw new BusinessException("更新或修改ID:" + customerInfo.getId() + "的收获地址失败");
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error("FunctionId:550002-->更新或修改收获地址失败:" + e.getMessage());
        }
        return Result.success(res);
    }

    /**
     * 删除收获地址
     * @param reqMain
     * @return
     */
    public Result deleteCustomerAddressById(ReqMain reqMain){
        Long res = 0L;
        try {
            Model_550003 model = (Model_550003) reqMain.getReqParam();
            res = customerAddressMapper.deleteCustomerAddressById(model.getId());
            if(res == 0) throw new BusinessException("删除地址ID:" + model.getId() + "失败");
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return Result.success(res);
    }

    /**
     * 获取各个银行限额数据
     * @return
     */
    public Result getBankQuota(){
        try {
            List<BusiPayBank> banks = busiPayBankMapper.selectValidBankList();
            Example example = new Example(BusiPayBankLimit.class);
            example.createCriteria().andEqualTo("bankStatus", "1").andEqualTo("payChannel", AppConstants.PayChannelCodeContants.FUIOU);
            List<BusiPayBankLimit> limits = busiPayBankLimitMapper.selectByExample(example);
            List<BusiBankQuotaDTO> payBanks = Lists.newArrayList();
            if(null == banks || null == limits)  throw new BusinessException("查询银行数据失败!");

            for (BusiPayBank bank : banks) {
                for (BusiPayBankLimit limit : limits) {
                    if (bank.getCode().equals(limit.getCode())) {
                        BusiBankQuotaDTO dto = new BusiBankQuotaDTO();
                        dto.setBankName(bank.getName());
                        dto.setBankCode(bank.getCode());
                        dto.setSingleQuota(limit.getSingleAmt());
                        dto.setDayQuota(limit.getDayAmt());
                        dto.setMonthQuota(limit.getMonthAmt());
                        dto.setRemark(limit.getRemark());
                        payBanks.add(dto);
                    }
                }
            }
            return Result.success(payBanks);
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.fail("查询银行限额数据失败:" + e.getMessage());
        }
    }

    /**
     * 解除绑定
     * 安全退出
     * @param reqMain
     * @return
     */
    public Result unBind(ReqMain reqMain){
        Model_550005 model = (Model_550005) reqMain.getReqParam();
        int res = 0;
        try {
            // 校验客户是否存在
            CustomerMainInfo customerInfo = customerMainInfoService.findOne(model.getCustomerId());
            if(null == customerInfo) throw new BusinessException("customer.not.exist");

            res = customerMainInfoService.unBind(customerInfo.getId());
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error("FunctionId:550005-->客户ID:" + model.getCustomerId() + "解除绑定失败:" + e.getMessage());
        }
        return Result.success(res);
    }

    /**
     * 查询借款人信息
     * @param reqMain
     * @return
     * @throws Exception
     */
    public Result queryBorrowerInfo(ReqMain reqMain) throws Exception {
        SubjectDiscloseInfoDto resDto = null;
        Model_550006 model = (Model_550006) reqMain.getReqParam();
        if (model.getStatus()==0){//普通产品借款人信息
            BusiBorrowerInfoVO borrowerInfo = productService.queryBorrowerInfo(model.getProductId());
            if(null != borrowerInfo){
                resDto = getSubjectDiscloseInfo(borrowerInfo.getSubjectNo());
            }
        }
        if (model.getStatus()==1){//理财计划产品借款人信息
            if (StringUtil.isEmpty(model.getId())){
                throw new BusinessException("id编号不能为空!");
            }
            BusiDebtDetailVO busiDebtDetailVO = busiFinancePlanService.selectDebtDetailByPrimaryKey(Long.parseLong(model.getId()));
            if (null!=busiDebtDetailVO){
                String initSubjectNo=busiDebtDetailVO.getInitSubjectNo();
                resDto = getSubjectDiscloseInfo(initSubjectNo);
                //resDto=packageProductInfo(initSubjectNo,resDto,model.getProductId());
            }
        }
        if (model.getStatus()==2){//理财计划订单产品借款人信息
            if (StringUtil.isEmpty(model.getId())){
                throw new BusinessException("id编号不能为空!");
            }
            BusiOrderSub busiOrderSub = busiFinancePlanService.selectOrderSubByPrimaryKey(Long.parseLong(model.getId()));
            if (null!=busiOrderSub){
                String subjectNo=busiOrderSub.getSubjectNo();
                resDto = getSubjectDiscloseInfo(subjectNo);
                // resDto=packageProductInfo(subjectNo,resDto,model.getProductId());
            }
        }
        return Result.success(resDto);
    }

    private SubjectBorrowerDetailDto getSubjectBorrowerDetail(String subjctNo){
        SubjectBorrowerDetailDto resDto = null;
        SubjectBorrowerDetailReqDto reqDto = new SubjectBorrowerDetailReqDto();
        reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
        reqDto.setSubjectNo(subjctNo);
        try {
            AssetsResultDto<SubjectBorrowerDetailDto> resultDto =  lcbSubjectFacadeService.getSubjectBorrowerDetail(reqDto);
            if(resultDto.isSuccess())  resDto = resultDto.getData();
        }catch (Exception e){
             //throw new BusinessException("无借款信息!");
            log.error("调用标的系统查询标的借款人信息失败：【标的编号:{}】",subjctNo);
        }
        return resDto;
    }


    /**
     *v4.7 获取标的披露信息
     * @return
     */
    private SubjectDiscloseInfoDto getSubjectDiscloseInfo(String subjctNo){
        SubjectDiscloseInfoDto resDto = null;
        SubjectBorrowerDetailReqDto reqDto = new SubjectBorrowerDetailReqDto();
        reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
        reqDto.setSubjectNo(subjctNo);
        try {
            AssetsResultDto<SubjectDiscloseInfoDto>   resultDto =  lcbSubjectFacadeService.getSubjectDiscloseInfo(reqDto);
            if(resultDto.isSuccess())  resDto = resultDto.getData();

        }catch (Exception e){
            log.error("调用标的系统查询标的借款人信息失败：【标的编号:{}】",subjctNo,e);
        }
        return resDto;
    }

    private  SubjectBorrowerDetailDto  packageProductInfo(String subjectNo,SubjectBorrowerDetailDto resDto,Long productId){
        Map map=new HashMap();
        map.put("subjectNo",subjectNo);
        List<BusiProductContract> busiProductContracts = busiProductContractMapper.selectBusiProductContract(map);
        if (CollectionUtils.isNotEmpty(busiProductContracts)) {
            BusiProductContract productContract=busiProductContracts.get(0);
            String workNature = productContract.getWorkNature() == null ? "" : productContract.getWorkNature();
            String burrowIndustry = productContract.getBurrowIndustry() == null ? productContract.getTradeInfo() : productContract.getBurrowIndustry();
            burrowIndustry = burrowIndustry == null ? "" : burrowIndustry;
            String borrowPurpose = productContract.getBorrowUse() == null ? productContract.getBorrowPurpose() : productContract.getBorrowUse();
            borrowPurpose = borrowPurpose == null ? "" : borrowPurpose;
           // String itemInfo = "借款人为【" + workNature + "】，从事【" + burrowIndustry + "】，工作收入稳定，借款用于【" + borrowPurpose + "】。经审核，该借款人符合捞财宝借款人融资准入标准，准予为其提供线上融资服务并进行还款管理";
            String itemInfo = "借款人工作收入稳定，借款用于【" + borrowPurpose + "】。经审核，该借款人符合捞财宝借款人融资准入标准，准予为其提供线上融资服务并进行还款管理";
            resDto.setItemInfo(itemInfo);
            resDto.setBorrowPurpose(borrowPurpose);
            resDto.setRepaySource(AppConstants.REPAYSOURCE);
            resDto.setRepaymentGuarantee(AppConstants.REPAYMENTGUARANTEE);
            resDto.setCooperativeDesc(AppConstants.COOPERATIVEDESC);
        }
        map = new HashMap<>();
        map.put("productId", productId);
        map.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
        BusiProduct busiProduct = busiProductService.findProductById(map);
        if (busiProduct!=null){
            if (busiProduct.getSaleEndDate() != null && busiProduct.getSaleStartDate() != null) {
                int collectPeriod = DateUtil.getIntervalDays2(busiProduct.getSaleEndDate(), busiProduct.getSaleStartDate());
                resDto.setSaleTerm(collectPeriod + 1);//募集期限
            }
        }
        return resDto;
    }

    /**
     * 查询借款信息-网贷之家
     * projectId  项目ID
     * status 0-子产品编号 1-标的编号
     */
    public String queryBorrowerInfo(String projectId,Long planId,String status) throws Exception {
        String subjectNo=projectId;
        if("0".equals(status)){
            BusiProductSub bps = busiProductSubMapper.selectByPrimaryKey(Long.valueOf(projectId));
            if(bps!= null){
                subjectNo = bps.getSubjectNo();
            }
        }
        SubjectBorrowerDetailDto resDto = getSubjectBorrowerDetail(subjectNo);
        if(resDto!=null){
            resDto.setBorrowerName(getCmRealNameDesc(resDto.getBorrowerName()));
            resDto.setIdNo(getCmIdnumDesc(resDto.getIdNo()));
            resDto.setBorrowerPhone("");
            resDto=packageProductInfo(subjectNo,resDto,planId);
        }
        return JSONUtil.toJsonStr(resDto);
    }

    public String getCmRealNameDesc(String cmRealName) {
        if(StringUtils.isEmpty(cmRealName)){
            return "";
        }
        String[] split = cmRealName.split("");
        StringBuilder sb = new StringBuilder();
        for (int i =0;i<split.length;i++){
            if(split[i].length() == 0){
                continue;
            }
            if(sb.length()==0){
                sb.append(split[i]);continue;
            }
            if(!StringUtils.isEmpty(split[i])){
                sb.append("*");
            }
        }
        return sb.toString();
    }

    public String getCmIdnumDesc(String cmIdnum) {
        if(StringUtils.isEmpty(cmIdnum)){
            return "";
        }
        String[] split = cmIdnum.trim().split("");
        StringBuilder sb = new StringBuilder();
        for (int i =0;i<split.length;i++){
            if(sb.length() < 3 || i==split.length-1){
                sb.append(split[i]);
            }else{
                sb.append("*");
            }
        }
        return sb.toString();
    }

}
