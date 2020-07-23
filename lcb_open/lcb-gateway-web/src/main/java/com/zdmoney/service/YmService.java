package com.zdmoney.service;/**
 * Created by pc05 on 2017/12/1.
 */

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zdmoney.conf.OpenConfig;
import com.zdmoney.utils.SignUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.customer.CustomerLevelChangeLogDto;
import com.zdmoney.webservice.api.dto.customer.CustomerMainInfoVo;
import com.zdmoney.webservice.api.dto.ym.CustomerRelationShipDto;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-12-01 16:40
 * @email : huangcy01@zendaimoney.com
 **/
@Service
@Slf4j
public class YmService {

    @Autowired
    private OpenConfig openConfig;

    @Autowired
    private ILcbGatewayFacade lcbGatewayFacade;

    public void saveAndUpdateCustomer(){
        long startTime = DateUtil.date().getTime();
        log.info(">>>>>>>>>>>>>>>更新客户关系数据开始!");
        String pageNo = "1";
        String pageSize = "2000";
        Map<String, Object> reqParams = Maps.newTreeMap();
        ResultDto resultDto = lcbGatewayFacade.selectTotalCount();
        if(resultDto.isSuccess() && resultDto.getData() != null && StringUtils.isNotBlank(resultDto.getData().toString()) && !StringUtils.equals(resultDto.getData().toString(),"0")){
            reqParams.put("startTime", DateUtil.format(DateUtil.offset(DateUtil.date(), DateField.MINUTE,-60),"yyyy-MM-dd HH:mm:ss"));
            reqParams.put("endTime",DateUtil.format(DateUtil.date(),"yyyy-MM-dd HH:mm:ss"));
        }else {
            reqParams.put("startTime", DateUtil.format(DateUtil.offset(DateUtil.date(), DateField.YEAR,-15),"yyyy-MM-dd HH:mm:ss"));
            reqParams.put("endTime",DateUtil.now());
        }
        reqParams.put("pageSize",pageSize);
        while (true){
            reqParams.put("pageNo",pageNo);
            reqParams.put("sign", SignUtils.getSign(reqParams, openConfig.getCreditReqKey()));
            if(saveOrUpdateRelationShip(reqParams)<0){
                break;
            }
            pageNo=(Integer.parseInt(pageNo)+1)+"";
        }
        log.info(">>>>>>>>>>>>>>>更新客户关系数据已结束!耗时 [ "+(DateUtil.date().getTime()-startTime)+" ] 毫秒!");
    }

    public int saveOrUpdateRelationShip(Map<String, Object> reqParams) {
        List<CustomerRelationShipDto> ymCustomers = Lists.newArrayList();
        String body = HttpRequest.get(openConfig.getYmUrl()).form(reqParams).execute().body();
        log.info("YMRES : method=[{}], params=[{}], result=[{}]", "saveOrUpdateRelationShip", reqParams, body);
        try {
            int totalPage = (int) JSONUtil.parseObj(body).get("totalPage");
            if(Integer.parseInt(reqParams.get("pageNo")+"")>totalPage){
                return -1;
            }
            ymCustomers = JSONUtil.parseArray(JSONUtil.parseObj(body).get("results")).toList(CustomerRelationShipDto.class);
        } catch (Exception e) {
            log.info(">>>>>>>>>>>>>>>>>>调用ym获取客户关系数据解析异常:" + body);return -1;
        }
        if (ymCustomers.size() > 0) {
            //更新数据库数据
            log.info(">>>>>>>>>>>>>>>开始更新数据,pageNo [ " + reqParams.get("pageNo") + " ]共[ " + ymCustomers.size() + " ]条");
            int num = 0;
            for (CustomerRelationShipDto ymCustomer : ymCustomers) {
                if (StringUtils.isBlank(ymCustomer.getCustNumber())) {
                    continue;
                }
                if (StringUtils.isBlank(ymCustomer.getInviteCode()) || StringUtils.equalsIgnoreCase("null", ymCustomer.getInviteCode())) {
                    ymCustomer.setInviteCode("");
                }
                sysCustomerLevel(ymCustomer);
//                checkAndUpdateCustomerLevel(ymCustomer);// 更新用户已经用户所属用户的等级
                ymCustomer.setModifyDate(new Date());
                try {
                    ResultDto resultDto = lcbGatewayFacade.selectCountByCmNumber(ymCustomer.getCustNumber());
                    if (resultDto.isSuccess()) {
                        if (resultDto.getData() != null) { //更新
                            CustomerRelationShipDto customerRelationShipDto = (CustomerRelationShipDto)resultDto.getData();
                            if(ymCustomer.getCustType() != customerRelationShipDto.getCustType()){
                                log.info("用户等级变更>>>>>>>>>>用户编号[{}],原等级[{}],变更之后等级[{}]",ymCustomer.getCustNumber(),customerRelationShipDto.getCustType(),ymCustomer.getCustType());
                                ymCustomer.setId(customerRelationShipDto.getId());
                                ResultDto resultDto1 = lcbGatewayFacade.updateByPrimaryKey(ymCustomer);
                                if (resultDto1.isSuccess()) {
                                    num += Integer.parseInt(resultDto1.getData().toString());
                                }
                            }

                        } else {
                            ResultDto resultDto2 = lcbGatewayFacade.insertSelective(ymCustomer);
                            if (resultDto2.isSuccess()) {
                                num += Integer.parseInt(resultDto2.getData().toString());
                            }
                        }
                    }

                } catch (Exception e) {
                    log.error(">>>>>>>>>>>>>>>同步数据失败! [{}],[{}]", ymCustomer.toString(), e.getMessage());
                }
            }
            log.info(">>>>>>>>>>>>>>>更新数据完成,共[ " + ymCustomers.size() + " ]条,更新[ " + num + " ]条");
        } else if (ymCustomers.size() == 0) {
            return -1;
        }
        return 1;
    }


    /*
    * 1. 新入职理财师的用户归属问题处理
        仅对入职员工做理财师归属的变更，原邀请用户保持原归属关系不变，后续新增邀请关系按照入职后作为大拇指理财师进行关系处理

      2，离职理财师的用户归属问题处理
        仅对离职理财师做普通用户的归属变更，原邀请用户的归属不变，由大拇指重新分配理财师进行维护
    * */
    public void sysCustomerLevel(CustomerRelationShipDto ymCustomer){
        if(ymCustomer == null || StringUtils.isBlank(ymCustomer.getCustNumber())){
            return;
        }
        ResultDto userInfo1 = lcbGatewayFacade.getUserInfo(ymCustomer.getCustNumber());
        if (!userInfo1.isSuccess()){
            return;
        }
        CustomerMainInfoVo user = (CustomerMainInfoVo)userInfo1.getData();//根据cmNumber查询用户信息
        if (user != null && ymCustomer.getCustType()!= null){
            String userLevel = user.getUserLevel(); //用户的等级
            int tag = 0;
            //用户升级为理财师
            if(!StringUtils.equals(userLevel,"0") && ymCustomer.getCustType() == 0){
                tag = 1;
                user.setUserLabel("1");
                user.setUserLevel("0");
            }
            //用户降级为其他用户
            if(StringUtils.equals(userLevel,"0") && ymCustomer.getCustType() != 0){
                tag = 1;
//                if(ymCustomer.getCustType() <= 2){
//                    user.setUserLabel("1");//理财师客户
//                }else {
//                    user.setUserLabel("0");//互联网客户
//                }
//                user.setUserLevel(ymCustomer.getCustType()>2?"3":ymCustomer.getCustType()+"");
                user.setUserLabel("0");
                user.setUserLevel("3");
            }
            if(tag > 0){
                user.setCmModifyDate(new Date());
                ResultDto resultDto = lcbGatewayFacade.updateByCustomerId(user);
                if (resultDto.isSuccess() && StringUtils.equals(resultDto.getData().toString(),"1")) {//更新成功
                    //插入用户等级更新记录
                    CustomerLevelChangeLogDto levelLog = new CustomerLevelChangeLogDto();
                    levelLog.setCustId(user.getId());
                    levelLog.setBeforeLevel(userLevel);
                    levelLog.setAfterLevel(user.getUserLevel());
                    levelLog.setCreateDate(new Date());
                    ResultDto levelLogRes = lcbGatewayFacade.saveCustomerLevelLog(levelLog);
                    if (!levelLogRes.isSuccess() || StringUtils.equals(levelLogRes.getData().toString(), "0")) {
                        log.error(">>>>>>>>>>>>>>>插入用户更新等级日志记录失败! [{}]", BeanUtil.beanToMap(levelLog));
                    }
                }else {
                        log.error(">>>>>>>>>>>>>>>更新用户等级失败! [{}]", BeanUtil.beanToMap(user));
                }
            }
        }
    }


    /*更新用户等级-弃用*/
    public void checkAndUpdateCustomerLevel(CustomerRelationShipDto ymCustomer){
        if(StringUtils.isBlank(ymCustomer.getCustNumber())){
            return;
        }
        ResultDto userInfo1 = lcbGatewayFacade.getUserInfo(ymCustomer.getCustNumber());
        if (!userInfo1.isSuccess()){
            return;
        }
        CustomerMainInfoVo user = (CustomerMainInfoVo)userInfo1.getData();//根据cmNumber查询用户信息
        if (ymCustomer != null && user != null && ymCustomer.getCustType()!= null &&
                StringUtils.isNotBlank(user.getUserLabel()) && StringUtils.isNotBlank(user.getUserLevel()) && StringUtils.equalsIgnoreCase("1",user.getUserLabel())) {
            if((Integer.parseInt(user.getUserLevel())-ymCustomer.getCustType()) < 0){ //用户等级有变更 需要更新用户以及下属等级
                //lcbGatewayFacade.update //理财师 插入更新记录
                String userLevel = user.getUserLevel();
                user.setUserLabel(ymCustomer.getCustType()>2?"0":"1");
                user.setUserLevel(ymCustomer.getCustType()>2?"3":ymCustomer.getCustType()+"");
                ResultDto resultDto = lcbGatewayFacade.updateByCustomerId(user);
                if (resultDto.isSuccess() && StringUtils.equals(resultDto.getData().toString(),"1")){//更新成功
                    //插入用户等级更新记录
                    CustomerLevelChangeLogDto levelLog = new CustomerLevelChangeLogDto();
                    levelLog.setCustId(user.getId());
                    levelLog.setBeforeLevel(userLevel);
                    levelLog.setAfterLevel(user.getUserLevel());//互联网用户
                    levelLog.setCreateDate(new Date());
                    ResultDto levelLogRes = lcbGatewayFacade.saveCustomerLevelLog(levelLog);
                    if(!levelLogRes.isSuccess() || StringUtils.equals(levelLogRes.getData().toString(),"0")){
                        log.error(">>>>>>>>>>>>>>>插入用户更新等级日志记录失败! [{}]", BeanUtil.beanToMap(levelLog));
                    }
                    ResultDto userIdByInvideCode = lcbGatewayFacade.getUserIdByInvideCode(user.getCmInviteCode());
                    if(userIdByInvideCode.isSuccess()){
                        Set<CustomerMainInfoVo> users = Sets.newHashSet();
                        List<CustomerMainInfoVo> data = (List<CustomerMainInfoVo>) userIdByInvideCode.getData();//查询需要更新的一级客户
                        if(data.size() > 0){
                            for (CustomerMainInfoVo datum : data) {
                                users.add(datum);
                                ResultDto userIdByInvideCode1 = lcbGatewayFacade.getUserIdByInvideCode(datum.getCmInviteCode());
                                if(userIdByInvideCode1.isSuccess()){
                                    List<CustomerMainInfoVo> data1 = (List<CustomerMainInfoVo>) userIdByInvideCode1.getData();//查询需要更新的二级客户
                                    if(data1.size()>0){
                                        for (CustomerMainInfoVo customerMainInfoVo : data1) {
                                            users.add(customerMainInfoVo);
                                        }
                                    }
                                }
                            }
                        }
                        //更新用户等级 以及 插入 更新记录表
                        if(users.size() > 0){
                            for (CustomerMainInfoVo userInfo : users) {
                                if (userInfo.getUserLabel()!=null && org.apache.commons.lang3.StringUtils.equalsIgnoreCase("0",userInfo.getUserLabel())
                                        &&userInfo.getUserLevel()!=null && org.apache.commons.lang3.StringUtils.equalsIgnoreCase("3",userInfo.getUserLevel())){
                                    continue;
                                }
                                String userLevel1 = userInfo.getUserLevel();
                                userInfo.setUserLevel("3");
                                userInfo.setUserLabel("0");
                                ResultDto resultDto1 = lcbGatewayFacade.updateByCustomerId(userInfo);
                                if(resultDto1.isSuccess() && StringUtils.equals(resultDto1.getData().toString(),"1")){
                                    CustomerLevelChangeLogDto customerLevelChangeLogDto = new CustomerLevelChangeLogDto();
                                    customerLevelChangeLogDto.setCustId(userInfo.getId());
                                    customerLevelChangeLogDto.setBeforeLevel(userLevel1);
                                    customerLevelChangeLogDto.setAfterLevel("3");//互联网用户
                                    customerLevelChangeLogDto.setCreateDate(new Date());
                                    ResultDto resultDto2 = lcbGatewayFacade.saveCustomerLevelLog(customerLevelChangeLogDto);
                                    if(!resultDto2.isSuccess() || StringUtils.equals(resultDto1.getData().toString(),"0")){
                                        log.error(">>>>>>>>>>>>>>>插入用户更新等级日志记录失败! [{}]", BeanUtil.beanToMap(customerLevelChangeLogDto));
                                    }
                                }else{
                                    log.error(">>>>>>>>>>>>>>>更新理财师用户等级失败! [{}]", BeanUtil.beanToMap(userInfo));
                                }

                            }
                        }
                    }
                }else {
                    log.error(">>>>>>>>>>>>>>>更新用户等级失败! [{}]", BeanUtil.beanToMap(user));
                }
            }
        }
    }

    /**
     * 理财师/员工入职
     * @param username
     * @param idNum
     * @param entryTime
     * @return
     */
    public Object entry(String username, String idNum, String entryTime,String isStaff) {
        if (StrUtil.isBlank(isStaff)){
            isStaff="0";
        }
        HashMap<Object, Object> resultMap = Maps.newHashMap();
        String code = "0000";
        String msg = "操作成功!";
        log.info("理财师入职请求参数>>>>username="+username+" idNum="+idNum+" entryTime="+entryTime+" isStaff="+isStaff);
        if(StrUtil.isBlank(idNum)){
            code="0001";
            msg="身份证不能为空!";
        }else{
            ResultDto resultDto = lcbGatewayFacade.selectCustomerByIdNum(idNum);
            if (resultDto.isSuccess()){
                CustomerMainInfoVo user = (CustomerMainInfoVo) resultDto.getData();
                if(user == null){
                    code="0101";
                    msg = "该用户不存在,身份证号:"+idNum;
                }else {
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.isBlank(user.getCmRealName())){
                        code="0102";
                        msg = "该用户没有实名认证,身份证号:"+idNum;
                    }
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.isNotBlank(username) && !StrUtil.equalsIgnoreCase(user.getCmRealName(),username)){
                        code="0103";
                        msg = "该用户身份信息与系统不匹配,用户名:"+username+" 身份证号:"+idNum;
                    }
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.equalsIgnoreCase(user.getUserLevel(),"0") && StrUtil.equalsIgnoreCase(isStaff,"0")){
                        code="0104";
                        msg = "该用户已经是理财师用户无需变更,身份证号:"+idNum;
                    }
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.equalsIgnoreCase(user.getUserLevel(),"4") && StrUtil.equalsIgnoreCase(isStaff,"1")){
                        code="0105";
                        msg = "该用户已经是员工用户无需变更,身份证号:"+idNum;
                    }
                    //将用户变更为理财师用户
                    if(StrUtil.equalsIgnoreCase(code,"0000")){
                        String userLevel = user.getUserLevel();//变更之前等级
                        if(StrUtil.equalsIgnoreCase(isStaff,"0")){
                            user.setUserLabel("1");
                            user.setUserLevel("0");
                        }else {
                            user.setUserLabel("0");
                            user.setUserLevel("4");
                        }
                        user.setCmModifyDate(new Date());
                        ResultDto resultDto1 = lcbGatewayFacade.updateByCustomerId(user);
                        if (resultDto1.isSuccess() && StringUtils.equals(resultDto1.getData().toString(),"1")){
                            //插入用户等级更新记录
                            CustomerLevelChangeLogDto levelLog = new CustomerLevelChangeLogDto();
                            levelLog.setCustId(user.getId());
                            levelLog.setBeforeLevel(userLevel);
                            levelLog.setAfterLevel(user.getUserLevel());
                            levelLog.setCreateDate(new Date());
                            levelLog.setIsQuit(1);
                            levelLog.setChangeTime(entryTime);
                            ResultDto levelLogRes = lcbGatewayFacade.saveCustomerLevelLog(levelLog);
                            if(!levelLogRes.isSuccess() || StringUtils.equals(resultDto1.getData().toString(),"0")){
                                log.error(">>>>>>>>>>>>>>>插入用户更新等级日志记录失败! [{}]", BeanUtil.beanToMap(levelLogRes));
                            }
                        }else{
                            code="0301";
                            msg="更新用户DB失败!,身份证号:"+idNum;
                        }
                    }
                }
            }else{
                code="0201";
                msg = "操作失败,身份证号:"+idNum;
            }
        }
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        log.info("理财师入职请求结果 : 身份证号码[{}] , 请求结果[{}]",idNum,resultMap);
        return resultMap;
    }


    /**
     * 理财师离职
     * @param username
     * @param idNum
     * @param quitTime
     * @return
     */
    public Object quit(String username, String idNum, String quitTime) {
        HashMap<Object, Object> resultMap = Maps.newHashMap();
        String code = "0000";
        String msg = "操作成功!";
        log.info("理财师离职职请求参数>>>>username="+username+" idNum="+idNum+" quitTime="+quitTime);
        if(StrUtil.isBlank(idNum)) {
            code = "0001";
            msg = "身份证不能为空!";
        }else {
            ResultDto resultDto = lcbGatewayFacade.selectCustomerByIdNum(idNum);
            if (resultDto.isSuccess()){
                CustomerMainInfoVo user = (CustomerMainInfoVo) resultDto.getData();
                if(user == null){
                    code="0101";
                    msg = "该用户不存在,身份证号:"+idNum;
                }else {
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.isBlank(user.getCmRealName())){
                        code="0102";
                        msg = "该用户没有实名认证,身份证号:"+idNum;
                    }
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.isNotBlank(username) && !StrUtil.equalsIgnoreCase(user.getCmRealName(),username)){
                        code="0103";
                        msg = "该用户身份信息与系统不匹配,用户名:"+username+" 身份证号:"+idNum;
                    }
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.equalsIgnoreCase(user.getUserLevel(),"3")){
                        code="0106";
                        msg = "该用户已经是互联网用户无需变更,身份证号:"+idNum;
                    }

                    //若用户为理财师，则保存到白名单中
                    if(StrUtil.equalsIgnoreCase(code,"0000") && StrUtil.equals(user.getUserLevel(),"0")){
                        ResultDto staffWhilteRes = lcbGatewayFacade.insertStaffWhilte(idNum,quitTime);
                        if(!staffWhilteRes.isSuccess() || StringUtils.equals(staffWhilteRes.getData().toString(),"0")){
                            log.error(">>>>>>>>>>>>>>>插入员工白名单失败! 身份证号:[{}]", idNum);
                        }
                    }
                    //将用户变更为互联网用户
                    if(StrUtil.equalsIgnoreCase(code,"0000")){
                        String userLevel = user.getUserLevel();//变更之前等级
                        user.setUserLabel("0");
                        user.setUserLevel("3");
                        user.setCmModifyDate(new Date());
                        ResultDto resultDto1 = lcbGatewayFacade.updateByCustomerId(user);
                        if (resultDto1.isSuccess() && StringUtils.equals(resultDto1.getData().toString(),"1")){
                            //插入用户等级更新记录
                            CustomerLevelChangeLogDto levelLog = new CustomerLevelChangeLogDto();
                            levelLog.setCustId(user.getId());
                            levelLog.setBeforeLevel(userLevel);
                            levelLog.setAfterLevel(user.getUserLevel());//互联网用户
                            levelLog.setCreateDate(new Date());
                            levelLog.setIsQuit(0);
                            levelLog.setChangeTime(quitTime);
                            ResultDto levelLogRes = lcbGatewayFacade.saveCustomerLevelLog(levelLog);
                            if(!levelLogRes.isSuccess() || StringUtils.equals(resultDto1.getData().toString(),"0")){
                                log.error(">>>>>>>>>>>>>>>插入用户更新等级日志记录失败! [{}]", BeanUtil.beanToMap(levelLogRes));
                            }
                        }else{
                            code="0301";
                            msg="更新用户DB失败!,身份证号:"+idNum;
                        }

                    }
                }
            }else{
                code="0201";
                msg = "操作失败,身份证号:"+idNum;
            }
        }
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        log.info("理财师离职请求结果 : 身份证号码[{}] , 请求结果[{}]",idNum,resultMap);
        return resultMap;
    }
}
