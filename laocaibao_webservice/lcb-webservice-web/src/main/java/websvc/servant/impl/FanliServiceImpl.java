package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.models.flw.Order;
import com.zdmoney.webservice.api.dto.fl.FlwReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import websvc.req.ReqMain;
import websvc.servant.FanliService;
import websvc.utils.XMLUtil;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 15:44
 * @Mail : xuyt@zendaimoney.com
 */
public class FanliServiceImpl implements FanliService {

    @Autowired
    BusiOrderMapper busiOrderMapper;

    @FunctionId("450001")
    public Result getOrderList(FlwReqDto flwReqDto) throws Exception {

        Order order = new Order();
        String dd = XMLUtil.beanToXml(order, Order.class);
        System.out.println(dd);
        return null;
    }
}
