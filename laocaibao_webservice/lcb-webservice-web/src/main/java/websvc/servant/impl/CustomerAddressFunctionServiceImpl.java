package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.service.customer.CustomerAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.req.ReqMain;
import websvc.servant.CustomerAddressFunctionService;

/**
 * Created by user on 2016/11/10.
 */
@Service
public class CustomerAddressFunctionServiceImpl implements CustomerAddressFunctionService{

    @Autowired
    private CustomerAddressService customerAddressService;

//    @FunctionId("550001")
    @Override
    public Result queryCustomerAddressList(ReqMain reqMain) {
        return customerAddressService.queryCustomerAddressList(reqMain);
    }

//    @FunctionId("550002")
    @Override
    public Result saveOrUpdateAddress(ReqMain reqMain) {
        return customerAddressService.saveOrUpdateAddress(reqMain);
    }

//    @FunctionId("550003")
    @Override
    public Result deleteCustomerAddressById(ReqMain reqMain) {
        return customerAddressService.deleteCustomerAddressById(reqMain);
    }

    @FunctionId("550004")
    @Override
    public Result getBankQuota(ReqMain reqMain) {
        return customerAddressService.getBankQuota();
    }

    @FunctionId("550005")
    @Override
    public Result unBind(ReqMain reqMain) {
        return customerAddressService.unBind(reqMain);
    }

    @FunctionId("550006")
    @Override
    public Result queryBorrowerInfo(ReqMain reqMain) throws Exception {
        return customerAddressService.queryBorrowerInfo(reqMain);
    }
}
