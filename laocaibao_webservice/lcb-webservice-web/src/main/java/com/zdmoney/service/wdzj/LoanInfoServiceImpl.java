package com.zdmoney.service.wdzj;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.mapper.wdzj.LoanInfoMapper;
import com.zdmoney.service.customer.CustomerAddressService;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
	private ConfigParamBean configParamBean;

    @Autowired
    private CustomerAddressService customerAddressService;


    @Override
    public String getLoanInfo(LoanInfoDto loanInfoDto) {
        int pageNo = StrUtil.isBlank(loanInfoDto.getPage()) ? 1 : Integer.parseInt(loanInfoDto.getPage());
        int pageSize = StrUtil.isBlank(loanInfoDto.getPageSize()) ? 10 : Integer.parseInt(loanInfoDto.getPageSize());
        Map<String, Object> params = Maps.newHashMap();
        params.put("date",loanInfoDto.getDate());
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        List<LoanInfoVo> loanInfo = loanInfoMapper.getLoanInfo(params);
        int totalSize = loanInfoMapper.getTotalSize(params);
        BigDecimal totolAmount = new BigDecimal(0);
        List resLoan = Lists.newArrayList();
        for (LoanInfoVo loanInfoVo : loanInfo) {
            JSONObject jsonObject = JSONUtil.parseObj(loanInfoVo);
            String amount = jsonObject.get("amount").toString();
            if(StringUtils.isNotBlank(amount)){
                totolAmount=totolAmount.add(new BigDecimal(amount));
            }
            //理财计划
            if(StrUtil.equalsIgnoreCase(jsonObject.get("dataType").toString(),"3") ){
                jsonObject.put("loanUrl",configParamBean.getLoanPlanUrl()+jsonObject.getStr("projectId") +"&planId="+jsonObject.get("planId")+"&status=0");
            }
            else if (StrUtil.equalsIgnoreCase(jsonObject.get("dataType").toString(),"4") ){
                String subjectNo = jsonObject.getStr("projectId");
                if(subjectNo.length()>=16){
                    subjectNo =  subjectNo.substring(0,16);
                }
                jsonObject.put("loanUrl",configParamBean.getLoanPlanUrl()+subjectNo +"&planId="+jsonObject.get("planId")+"&status=1");
            }
            //非理财计划产品
            else {
                if (StrUtil.equalsIgnoreCase(jsonObject.get("transferType").toString(),"1")){
                    jsonObject.put("loanUrl",configParamBean.getLoanTransferUrl()+jsonObject.get("planId"));
                }else{
                    jsonObject.put("loanUrl",configParamBean.getLoanUrl()+jsonObject.get("planId"));
                }
            }
            jsonObject.remove("transferType");
            jsonObject.remove("handler");
            resLoan.add(jsonObject);
        }
        Map<String, Object> res = Maps.newHashMap();
        res.put("totalPage",Math.ceil(totalSize/Float.parseFloat(pageSize+"")));
        res.put("currentPage", pageNo);
        res.put("totalCount", totalSize);
        res.put("totalAmount", totolAmount);
        res.put("borrowList", resLoan);
        return JSONUtil.toJsonStr(res);
    }

    /**
     * 获取需要拆分的数据
     * @return
     */
    @Override
    public List<LoanInfoVo> getSplitData(){
        return null;
    }

    /**
     * 获取不需要拆分的数据
     * @return
     */
    @Override
    public List<LoanInfoVo> getNoSplitData(){




        return null;
    }

    @Override
    public String gainBorrowInfo(String projectId,Long planId, String status) throws Exception {
        return customerAddressService.queryBorrowerInfo(projectId,planId,status);
    }

}
