package websvc.utils;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.BussErrorCode;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonException extends Exception {
    private static final long serialVersionUID = -3827862017476307468L;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private String code;

    private String msg;

    private String msgEx;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgEx() {
        return msgEx;
    }

    public void setMsgEx(String msgEx) {
        this.msgEx = msgEx;
    }

    public static final String ERROR_0102 = JsonException.toJson(BussErrorCode.ERROR_CODE_0102, null);

    public JsonException(BussErrorCode bussErrorCode) {
        super("[" + bussErrorCode.getErrorcode() + "]" + bussErrorCode.getErrordesc());
        this.code = bussErrorCode.getErrorcode();
        this.msg = bussErrorCode.getErrordesc();
        this.msgEx = bussErrorCode.getErrordesc();

    }

    public JsonException(String code, String msg) {
        super("[" + code + "]" + msg);
        this.code = code;
        this.msg = msg;
        this.msgEx = msg;
    }

    public JsonException(String msg) {
        super(msg);
    }

    public static String toJson(JsonException je, Long logId) {
        log.info(logId + "返回:" + je.getCode() + "->" + je.getMsg());
        if (logId != null) {

        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", "0001");
        param.put("msg", je.getMsg());

        Map<String, Object> returnMsg = new HashMap<String, Object>();
        returnMsg.put("status", je.getCode());
        returnMsg.put("respDesc", je.getMsg());
        returnMsg.put("infos", null);

        param.put("msgEx", returnMsg);

        String returnStr = JSONObject.fromObject(param).toString();

        log.info(logId + returnStr);

        return returnStr;
    }


    public static String toJson(BussErrorCode bussErrorCode) {
        return toJson(bussErrorCode, null);
    }

    public static String toJson(BussErrorCode bussErrorCode, Long logId) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", bussErrorCode.getErrorcode());

        Map<String, Object> returnMsg = new HashMap<String, Object>();
        returnMsg.put("status", bussErrorCode.getErrorcode());
        returnMsg.put("respDesc", bussErrorCode.getErrordesc());
        returnMsg.put("infos", new HashMap<String, Object>());

        param.put("msgEx", returnMsg);

        String returnStr = JSONObject.fromObject(param).toString();

        log.info(logId + returnStr);

        return returnStr;
    }

    public static String toBusiExceptionJsonStr(String desc) {
        return JsonException.toJsonStr(BussErrorCode.ERROR_CODE_0000.getErrorcode(),
                AppConstants.Status.EXCEPTION,
                desc, null, null);
    }

    public static String toSuccessJsonStr(String desc, Object obj) {
        return JsonException.toJsonStr(BussErrorCode.ERROR_CODE_0000.getErrorcode(),
                AppConstants.Status.SUCCESS,
                desc, obj, null);
    }


    public static String toJsonStr(String code, String status, String desc, Object obj, Long logId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("status", status);
        msg.put("respDesc", desc);
        msg.put("infos", obj != null ? obj : new HashMap<String, Object>());

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", code);
        param.put("msgEx", msg);

        String returnStr = JSON.toJSONString(param);
        log.info("----" + dateFormat.format(new Date()) + "  " + code + "返回->" + returnStr);

        return returnStr;
    }

    public static String toJsonStr(Result result){
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("status", result.getSuccess()?"0":"-1");
        msg.put("respDesc", result.getMessage());
        msg.put("infos", result.getData() != null ? result.getData() : new HashMap<String, Object>());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("code", BussErrorCode.ERROR_CODE_0000.getErrorcode());
        param.put("msgEx", msg);
        return JSONUtils.toJSON(param);
    }

}
