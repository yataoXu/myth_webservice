package websvc.utils;

import com.zdmoney.models.sys.SysLog;
import com.zdmoney.service.SysLogService;
import com.zdmoney.utils.JSONUtils;
import org.springframework.web.context.ContextLoader;

import java.util.Date;

public class SysLogUtil {

    public static void save(Object obj, String logType, String modelName) {
//        SysLogService sysLogService = ContextLoader.getCurrentWebApplicationContext().getBean(SysLogService.class);
//        SysLog sysLog = new SysLog();
//        sysLog.setLogType(logType);
//        if (obj != null) {
//            String content = "";
//            if (obj instanceof String) {
//                content = obj.toString();
//            } else {
//                content = JSONUtils.toJSON(obj);
//            }
//            if (content.length() > 1500) {
//                content = content.substring(0,1500);
//            }
//            sysLog.setContent(content);
//        }
//        sysLog.setCreateDate(new Date());
//        sysLog.setModelName(modelName);
//        sysLog.setOperator("customer");
//        sysLogService.insert(sysLog);
    }
}
