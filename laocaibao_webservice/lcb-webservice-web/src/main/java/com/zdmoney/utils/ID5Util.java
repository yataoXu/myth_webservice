package com.zdmoney.utils;

import com.zdmoney.mapper.customer.CustomerAuthenticationMapper;
import com.zdmoney.models.customer.CustomerAuthentication;
import com.zdmoney.models.IdCardInfo;
import com.zdmoney.service.QueryValidatorServices;
import com.zdmoney.service.QueryValidatorServicesProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.context.ContextLoader;
import websvc.utils.SpringContextHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * ID5验证工具类
 *
 * @author aohj@zendaimoney.com
 *
 */
public class ID5Util {
    private static final Log log = LogFactory.getLog(ID5Util.class);

    private static CustomerAuthenticationMapper customerAuthenticationMapper;

    public static String id5Validate(String name, String id_card) {
        String resutl = null;
        try {
            QueryValidatorServicesProxy proxy = new QueryValidatorServicesProxy();

            proxy.setEndpoint("http://gboss.id5.cn/services/QueryValidatorServices");
            QueryValidatorServices service = proxy.getQueryValidatorServices();
            // 对调用的参数进行加密
            String key = "12345678";
            String userName = Des2.encode(key, "zhengdajiekou");
            String password = Des2.encode(key, "zhengdajiekou_CDG3_@l(");
            // 查询返回结果
            String resultXML = "";

			/*------身份信息核查比对-------*/
            String datasource = Des2.encode(key, "1A020201");
            // 单条
            String param = new String(name + "," + id_card);
            System.out.println(param);
            System.out.println(Des2.encode(key, param));
            log.info("param  " + param);
            log.info("Des2 " + Des2.encode(key, param));
            resultXML = service.querySingle(userName, password, datasource,
                    Des2.encode(key, param));
            log.info("resultXML1" + resultXML);
            resultXML = Des2.decodeValue(key, resultXML);
            System.out.println(resultXML);
            log.info("resultXML2" + resultXML);
            resutl = resultXML;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resutl;
    }

    public static boolean resultValidate(String name, String id_card){
        try{
            log.info("验证人："+name+",验证身份证："+id_card);
            customerAuthenticationMapper = ContextLoader.getCurrentWebApplicationContext().getBean(CustomerAuthenticationMapper.class);
            Document document = DocumentHelper.parseText(ID5Util.id5Validate(
                    name, id_card));// ID5认证
            Element root = document.getRootElement();
            String status = root.element("message").elementTextTrim("status");
            if (status == null || !"0".equals(status)) { // 查询失败
                log.info("身份验证查询失败");
                return false;
            }
            Element policeCheckInfoe = root.element("policeCheckInfos")
                    .element("policeCheckInfo");
            String compStatus = policeCheckInfoe.elementTextTrim("compStatus"); // 比对状态
            String compResult = policeCheckInfoe.elementTextTrim("compResult"); // 比对结果
            CustomerAuthentication auth=new CustomerAuthentication();
            auth.setCmIdnum(id_card);
            auth.setRealName(name);
            auth.setAuMsg(compResult);
            auth.setCmInputDate(new Date());
            auth.setPlatform("APP");
            auth.setOperMan("System");
            auth.setChannelId(1);
            if("3".equals(compStatus)){
                auth.setAuStatus(Short.valueOf("0"));
            }else{
                auth.setAuStatus(Short.parseShort("1"));
            }
             customerAuthenticationMapper.insert(auth);

            if (!"3".equals(compStatus)) {
                log.info("身份验证失败:" + compResult);
                return false;
            }else{
                return true;
            }
        }catch (Exception ex){
            log.info("获取身份证信息失败" + ex);
            return false;
        }

    }

    /**
     * 获取身份证信息
     *
     * @param idNo
     * @param realName
     * @return
     */
    public static IdCardInfo getIdCardInfo(String realName, String idNo) {
        try {
            Document document = DocumentHelper.parseText(ID5Util.id5Validate(
                    realName, idNo));// ID5认证
            Element root = document.getRootElement();
            String status = root.element("message").elementTextTrim("status");
            if (status == null || !"0".equals(status)) { // 查询失败
                return null;
            }
            Element policeCheckInfoe = root.element("policeCheckInfos")
                    .element("policeCheckInfo");
            String compStatus = policeCheckInfoe.elementTextTrim("compStatus"); // 比对状态
            String compResult = policeCheckInfoe.elementTextTrim("compResult"); // 比对结果
            String policeadd = policeCheckInfoe.elementTextTrim("policeadd"); // 原始发证地
            String birthday2 = policeCheckInfoe.elementTextTrim("birthday2"); // 出生日期2
            String sex2 = policeCheckInfoe.elementTextTrim("sex2"); // 性别2
            if (!"3".equals(compStatus)) {
                log.info("身份验证失败:" + compResult);
                return null;
            }
            IdCardInfo idCardInfo = new IdCardInfo();
            idCardInfo.setIdno(idNo);
            idCardInfo.setName(realName);
            idCardInfo.setAddress(policeadd);
            idCardInfo.setAvatar("");
            idCardInfo.setValidTime(new Date());

            if (birthday2.length() == 6) {// 如果生日信息为六位
                birthday2 = "19" + birthday2;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            idCardInfo.setBirth(format.parse(birthday2));
            if ("男".equals(sex2)) {
                idCardInfo.setGender("MALE");
            } else {
                idCardInfo.setGender("FEMALE");
            }
            return idCardInfo;
        } catch (Exception e) {
            log.info("获取身份证信息失败" + e);
        }
        return null;
    }

    public static void main(String[] args) {
        ID5Util i = new ID5Util();
        String idCardInfo = ID5Util.id5Validate("敖沪军","360622199006024518");
        System.out.print(idCardInfo);
    }
}