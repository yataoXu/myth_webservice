package com.zdmoney.facade;

import com.zdmoney.assets.api.utils.BeanUtil;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.ResultBorrowCertificateService;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.vo.UserUnReceiveAsset;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateDto;
import com.zdmoney.webservice.api.dto.busi.BusiBorrowCertificateSearchDto;
import com.zdmoney.webservice.api.facade.IBusiBorrowCertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Created by qinzhuang on 2018/6/12.
 */
@Component
@Slf4j
public class BusiBorrowCertificateService implements IBusiBorrowCertificateService {
    @Autowired
    private ResultBorrowCertificateService resultBorrowCertificateService;
    @Autowired
    CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    AccountOverview520003Service accountOverview520003Service;

    @Override
    public PageResultDto<BusiBorrowCertificateDto> searchList(BusiBorrowCertificateSearchDto searchDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(searchDto);
        PageResultDto<BusiBorrowCertificateDto> resultDto = resultBorrowCertificateService.selectListByMap(map);
        return resultDto;
    }

    @Override
    public ResultDto create(BusiBorrowCertificateDto model) {
        ResultDto resultDto = ResultDto.SUCCESS();
        try {
            resultBorrowCertificateService.insertBorrowCertificate(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto = ResultDto.FAIL("发送失败");
        }
        return resultDto;
    }

    @Override
    public ResultDto<String> getHoldAsset(String customerId) {
        String holdAsset = "";
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
        if (customerMainInfo == null) {
            return ResultDto.FAIL("查询不到用户信息！");
        }
        UserUnReceiveAsset unReceiveAsset = null;
        try {
            unReceiveAsset = accountOverview520003Service.getHoldAsset(customerMainInfo);
            holdAsset = CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceivePrinciple());
        } catch (Exception e) {
            e.printStackTrace();
            ResultDto.FAIL("查询在投本金失败：" + e.getMessage());
        }
        return new ResultDto<String>(holdAsset);
    }


}
