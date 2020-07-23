/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant;

import com.zdmoney.data.agent.api.base.OpenResponse;
import websvc.models.Model_900000;
import websvc.servant.base.FunctionService;

/**
 *  @Description: 审计回调
 *  @author huangcy
 *  @date 2017/9/19
*/
public interface AuditFunctionService extends FunctionService {

    /*
     * 900011 验证码
     *
     */
    OpenResponse auditCallback(Model_900000 brDto) throws Exception;
}
