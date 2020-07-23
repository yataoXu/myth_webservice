package com.zdmoney.service.lycj;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.mapper.lycj.LycjLoanInfoMapper;
import com.zdmoney.mapper.lycj.LycjLoanUserMapper;
import com.zdmoney.mapper.lycj.LycjPrepaymentMapper;
import com.zdmoney.models.lycj.LycjLoanInfo;
import com.zdmoney.models.lycj.LycjLoanUser;
import com.zdmoney.models.lycj.LycjPrepayment;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.vo.lycj.LycjLoanInfoVo;
import com.zdmoney.vo.lycj.LycjLoanUserVo;
import com.zdmoney.vo.lycj.LycjPrepaymentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 2018/12/25 0025.
 */
@Slf4j
@Service
public class LycjService {

    @Autowired
    private LycjLoanInfoMapper lycjLoanInfoMapper;

    @Autowired
    private LycjLoanUserMapper lycjLoanUserMapper;

    @Autowired
    private LycjPrepaymentMapper lycjPrepaymentMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    /**
     * 零壹财经 标的数据推送
     *
     * @param dateTime
     * @param pageNo
     * @param pageSize
     */
    public String sendLoanInfoData(String dateTime, Integer pageNo, Integer pageSize) {
        Map params = Maps.newHashMap();
        StrBuilder loanInfoBuilder = StrBuilder.create();
        while (true) {
            params.put("dateTime", dateTime);
            params.put("pageNo", pageNo);
            params.put("pageSize", pageSize);
            List<LycjLoanInfo> list = lycjLoanInfoMapper.queryLycjLoanInfo(params);
            if (list.size() <= 0) {
                break;
            }
            log.info(" <--- 零壹财经标的数据组装开始 ---> dateTime[{}],pageNo[{}],pageSize[{}],resSize[{}]", dateTime, pageNo, pageSize, list.size());
            List<LycjLoanInfoVo> lycjLoanInfoVos = Lists.newArrayList();

            for (LycjLoanInfo lycjLoanInfo : list) {
                LycjLoanInfoVo lycjLoanInfoVo = new LycjLoanInfoVo();
                /*数据组装*/
                lycjLoanInfoVo.setId(lycjLoanInfo.getId());
                lycjLoanInfoVo.setTitle(lycjLoanInfo.getTitle());
                lycjLoanInfoVo.setUsername(lycjLoanInfo.getUsername());
                lycjLoanInfoVo.setUserid(lycjLoanInfo.getUserid());
                lycjLoanInfoVo.setAmount(lycjLoanInfo.getAmount());
                lycjLoanInfoVo.setBorrow_period(lycjLoanInfo.getBorrowPeriod());
                lycjLoanInfoVo.setInterest(lycjLoanInfo.getInterest());
                lycjLoanInfoVo.setAsset_type(lycjLoanInfo.getAssetType());
                lycjLoanInfoVo.setRepay_type(lycjLoanInfo.getRepayType());
                lycjLoanInfoVo.setPercentage(lycjLoanInfo.getPercentage());
                lycjLoanInfoVo.setBid_state(lycjLoanInfo.getBidState());
                lycjLoanInfoVo.setVerify_time(lycjLoanInfo.getVerifyTime());
                lycjLoanInfoVo.setReverify_time(lycjLoanInfo.getReverifyTime());
                StrBuilder strBuilder = StrBuilder.create();
                if (lycjLoanInfo.getProductType() == 1) { //散标
                    lycjLoanInfoVo.setLink(strBuilder.append(configParamBean.getPcUrl()).append("/products/").append(getProductId(lycjLoanInfo.getPlanId())).append("?productTagVo=0").toString());
                } else if (lycjLoanInfo.getProductType() == 2) {//智投宝
                    lycjLoanInfoVo.setLink(strBuilder.append(configParamBean.getPcUrl()).append("/financial_products/").append(getProductId(lycjLoanInfo.getPlanId())).append("?productTagVo=0").toString());
                } else if (lycjLoanInfo.getProductType() == 3) {//转让
                    lycjLoanInfoVo.setLink(strBuilder.append(configParamBean.getPcUrl()).append("/trans_products/").append(getProductId(lycjLoanInfo.getPlanId())).append("?productTagVo=0").toString());
                }
                /*添加到list*/
                lycjLoanInfoVos.add(lycjLoanInfoVo);
            }
            log.info(" <--- 零壹财经标的数据组装结束 ---> dateTime[{}],pageNo[{}],pageSize[{}],resSize[{}],totalLoanInfoSize[{}]", dateTime, pageNo, pageSize, list.size(), lycjLoanInfoVos.size());

            //向零壹财经推送数据
            if (lycjLoanInfoVos.size() > 0) {
                Map form = new HashMap<>();
                Map jsonData = Maps.newHashMap();
                jsonData.put("data", lycjLoanInfoVos);
                String data = JSONUtil.toJsonStr(jsonData);
                String time = String.valueOf(System.currentTimeMillis());
                form.put("visit_key", configParamBean.getVisitKey());
                form.put("data", data);
                form.put("time", time);
                form.put("sign", getSignInfo(configParamBean.getVisitKey(), time, data));
                String body = HttpUtil.createPost(configParamBean.getLycjLoanUrl()).form(form).execute().body();
                //邮件通知
                isFailedSendMail("sendLoanInfoData",dateTime,body);
                log.info(" <--- 零壹财经推送标的数据完成 ---> dateTime:[{}],pageNo[{}],pageSize[{}],totalNum[{}],res:[{}]", dateTime, pageNo, pageSize, lycjLoanInfoVos.size(), body);
                loanInfoBuilder.append(" <--- 零壹财经推送标的数据完成 ---> dateTime:[").append(dateTime).append("],pageNo[").append(pageNo).append("],pageSize[").append(pageSize).append("],totalNum[").append(lycjLoanInfoVos.size()).append("],res[").append(body).append("]</br>");
                loanInfoBuilder.append("*********************************************************************</br>");
            }
            //查询下一页
            pageNo += 1;
        }
        return loanInfoBuilder.toString();
    }

    /**
     * 获取签名信息
     *
     * @return
     */
    private String getSignInfo(String visit_key, String time, String data) {
        String signInfo = "";
        try {
            String sign = StrBuilder.create().append(configParamBean.getLycjKey()).append("&").append("visit_key=").append(visit_key).append("&time=").append(time).append("&data=").append(data.getBytes("UTF-8").length).toString();
            log.info("<--- 零壹财经 签名字符串 : [{}] --->", sign);
            signInfo = DigestUtil.md5Hex(sign).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signInfo;
    }

    /**
     * 如果零壹财经返回错误码 邮件通知
     */
    private void isFailedSendMail(String method,String dateTime,String body){
        StrBuilder strBuilder = StrBuilder.create();
        if (StrUtil.isBlank(body)){
            strBuilder.append("零壹财经返回数据为空,method:[").append(method).append("]");
        }else {
            JSONObject res = JSONUtil.parseObj(body);
            Object result_code = res.get("result_code");
            if (result_code != null && StrUtil.isNotBlank(result_code.toString()) && Integer.parseInt(result_code.toString())==1){

            }else {
                strBuilder.append("零壹财经数据报送错误,dateTime[").append(dateTime).append("],res[").append(body).append("]");
            }
        }
        String content = strBuilder.toString();
        if (StrUtil.isNotBlank(content)){
            MailUtil.sendMail("零壹财经数据报送错误",content);
        }
    }

    /**
     * 零壹财经 投标数据推送
     *
     * @param dateTime
     * @param pageNo
     * @param pageSize
     */
    public String sendLoanUserData(String dateTime, Integer pageNo, Integer pageSize) {
        Map params = Maps.newHashMap();
        StrBuilder loanUserBuilder = StrBuilder.create();
        while (true) {
            params.put("dateTime", dateTime);
            params.put("pageNo", pageNo);
            params.put("pageSize", pageSize);
            List<LycjLoanUser> list = lycjLoanUserMapper.queryLycjLoanUser(params);
            if (list.size() <= 0) {
                break;
            }
            log.info(" <--- 零壹财经投标数据组装开始 ---> dateTime[{}],pageNo[{}],pageSize[{}],resSize[{}]", dateTime, pageNo, pageSize, list.size());
            List<LycjLoanUserVo> lycjLoanUserVos = Lists.newArrayList();

            for (LycjLoanUser lycjLoanUser : list) {
                LycjLoanUserVo lycjLoanUserVo = new LycjLoanUserVo();
                /*数据组装*/
                lycjLoanUserVo.setId(lycjLoanUser.getId());
                lycjLoanUserVo.setInvest_id(lycjLoanUser.getInvestId());
                lycjLoanUserVo.setUsername(lycjLoanUser.getUsername());
                lycjLoanUserVo.setUserid(lycjLoanUser.getUserid());
                lycjLoanUserVo.setMoney(lycjLoanUser.getMoney());
                lycjLoanUserVo.setAccount(lycjLoanUser.getAccount());
                lycjLoanUserVo.setStatus(lycjLoanUser.getStatus());
                lycjLoanUserVo.setAdd_time(lycjLoanUser.getAddTime());
                StrBuilder strBuilder = StrBuilder.create();
                if (lycjLoanUser.getProductType() == 1) { //散标
                    lycjLoanUserVo.setLink(strBuilder.append(configParamBean.getPcUrl()).append("/products/").append(getProductId(lycjLoanUser.getPlanId())).append("?productTagVo=0").toString());
                } else if (lycjLoanUser.getProductType() == 2) {//智投宝
                    lycjLoanUserVo.setLink(strBuilder.append(configParamBean.getPcUrl()).append("/financial_products/").append(getProductId(lycjLoanUser.getPlanId())).append("?productTagVo=0").toString());
                } else if (lycjLoanUser.getProductType() == 3) {//转让
                    lycjLoanUserVo.setLink(strBuilder.append(configParamBean.getPcUrl()).append("/trans_products/").append(getProductId(lycjLoanUser.getPlanId())).append("?productTagVo=0").toString());
                }
                /*添加到list*/
                lycjLoanUserVos.add(lycjLoanUserVo);
            }
            log.info(" <--- 零壹财经投标数据组装结束 ---> dateTime[{}],pageNo[{}],pageSize[{}],resSize[{}],totalLoanInfoSize[{}]", dateTime, pageNo, pageSize, list.size(), lycjLoanUserVos.size());

            //向零壹财经推送数据
            if (lycjLoanUserVos.size() > 0) {
                Map form = Maps.newHashMap();
                Map jsonData = Maps.newHashMap();
                jsonData.put("data", lycjLoanUserVos);
                String data = JSONUtil.toJsonStr(jsonData);
                String time = String.valueOf(System.currentTimeMillis());
                form.put("visit_key", configParamBean.getVisitKey());
                form.put("data", data);
                form.put("time", time);
                form.put("sign", getSignInfo(configParamBean.getVisitKey(), time, data));
                String body = HttpUtil.createPost(configParamBean.getLycjUserUrl()).form(form).execute().body();
                //邮件通知
                isFailedSendMail("sendLoanUserData",dateTime,body);
                log.info(" <-- 零壹财经推送投标数据完成 --> dateTime:[{}],pageNo[{}],pageSize[{}],totalNum[{}],res:[{}]", dateTime, pageNo, pageSize, lycjLoanUserVos.size(), body);
                loanUserBuilder.append("<-- 零壹财经推送投标数据完成 --> dateTime[").append(dateTime).append("],pageNo[").append(pageNo).append("],pageSize[").append(pageSize).append("],totalNum[").append(lycjLoanUserVos.size()).append("],res[").append(body).append("]</br>");
                loanUserBuilder.append("*********************************************************************</br>");
            }
            //查询下一页
            pageNo += 1;
        }
        return loanUserBuilder.toString();
    }

    /**
     * 生成pc的productId
     *
     * @param planId
     * @return
     */
    private String getProductId(String planId) {
        StrBuilder strBuilder = StrBuilder.create();
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        return strBuilder.append(tlr.nextInt(10, 99)).append(planId).append(tlr.nextLong(1000000000, 9999999999L)).toString();
    }

    /**
     * 提前结清数据
     *
     * @param dateTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    public String getPrepayment(String dateTime, Integer pageNo, Integer pageSize) {
        Map<String, Object> map = Maps.newTreeMap();
        StrBuilder prepaymentBuilder = StrBuilder.create();
        while (true) {
            map.put("dateTime", dateTime);
            map.put("pageNo", pageNo);
            map.put("pageSize", pageSize);
            List<LycjPrepayment> list = lycjPrepaymentMapper.queryLycjPrepayment(map);

            if (list.size() <= 0) {
                log.info(" <--- 零壹财经提前结清无数据 ---> dateTime[{}]", dateTime);
                break;
            }

            log.info(" <--- 零壹财经提前结清组装开始 ---> dateTime[{}],prepaySize[{}]", dateTime, list.size());
            List<LycjPrepaymentVo> lycjPrepaymentVoList = Lists.newArrayList();
            for (LycjPrepayment lycjPrepayment : list) {
                LycjPrepaymentVo lycjPrepaymentVo = new LycjPrepaymentVo();
                lycjPrepaymentVo.setId(lycjPrepayment.getId());
                lycjPrepaymentVo.setRepay_id(lycjPrepayment.getRepayId());
                lycjPrepaymentVo.setActual_period(lycjPrepayment.getActualPeriod());
                lycjPrepaymentVo.setAdvanced_amount(lycjPrepayment.getAdvancedAmount());
                lycjPrepaymentVo.setAdvanced_time(lycjPrepayment.getAdvancedTime());
                lycjPrepaymentVoList.add(lycjPrepaymentVo);
            }
            log.info(" <--- 零壹财经提前结清组装结束 ---> dateTime[{}],prepaySize[{}]", dateTime, list.size());

            if (lycjPrepaymentVoList.size() > 0) {
                Map jsonData = Maps.newHashMap();
                jsonData.put("data", lycjPrepaymentVoList);
                String data = JSONUtil.toJsonStr(jsonData);
                String time = String.valueOf(System.currentTimeMillis());
                Map form = new HashMap<>();
                form.put("visit_key", configParamBean.getVisitKey());
                form.put("data", data);
                form.put("time", time);
                form.put("sign", getSignInfo(configParamBean.getVisitKey(), time, data));
                String body = HttpUtil.createPost(configParamBean.getLycjPrepaymentUrl()).form(form).execute().body();
                //邮件通知
                isFailedSendMail("getPrepayment",dateTime,body);
                log.info(" <-- 零壹财经提前结清数据推送完成 --> dateTime:[{}],res:[{}]", dateTime, body);
                prepaymentBuilder.append("<-- 零壹财经提前结清数据推送完成 --> dateTime[").append(dateTime).append("],pageNo[").append(pageNo).append("],pageSize[").append(pageSize).append("],totalNum[").append(lycjPrepaymentVoList.size()).append("],res[").append(body).append("]</br>");
                prepaymentBuilder.append("*********************************************************************</br>");
            }
            //查询下一页
            pageNo += 1;
        }
        return prepaymentBuilder.toString();
    }
}
