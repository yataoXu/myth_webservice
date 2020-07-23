package websvc.servant.impl;

import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiProductVO;
import com.zdmoney.vo.BusiTransferProductVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_500036;
import websvc.req.ReqMain;
import websvc.servant.TransferFunctionService;


@Service
public class TransferFunctionServiceImpl implements TransferFunctionService {

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @Autowired
    private ProductService productService;

    /**
     * 转让初始化接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500031")
    @Override
    public Result transferInit(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.transferInit(reqMain);
    }

    /**
     * 转让初始化v4.6
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500041")
    @Override
    public Result initTransfer(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.initTransfer(reqMain);
    }

    /**
     * 查询转让利率接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500032")
    @Override
    public Result queryTransferRate(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.queryTransferRate(reqMain);
    }

    /**
     * 查询转让利率接口v4.8
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500055")
    @Override
    public Result transferRateInfo(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.transferRateInfo(reqMain);
    }
    /**
     * 转让接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500033")
    @Override
    public Result transfer(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.transfer(reqMain);
    }

    /**
     * 转让授权是否充足
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500042")
    public Result transferAuth(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.transferAuth(reqMain);
    }

    /**
     * 转让v4.6
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500044")
    @Override
    public Result transfers(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.transfers(reqMain);
    }

    /**
     * 转让详情接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500034")
    @Override
    public Result transferInfo(ReqMain reqMain) throws Exception {
        return null;
    }
    /**
     * 撤销转让接口
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("500035")
    @Override
    public Result cancelTransfer(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.cancelTransfer(reqMain);
    }


    @FunctionId("500037")
    @Override
    public Result queryBeforeTransferProtocol(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.queryBeforeTransferProtocol(reqMain);
    }

    @FunctionId("500038")
    @Override
    public Result queryAfterTransferProtocol(ReqMain reqMain) throws Exception {
        return busiDebtTransferService.queryAfterTransferProtocol(reqMain);
    }

}
