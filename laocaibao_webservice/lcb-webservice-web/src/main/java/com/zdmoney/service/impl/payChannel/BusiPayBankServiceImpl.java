package com.zdmoney.service.impl.payChannel;

import com.zdmoney.models.payChannel.BusiPayBankLimit;
import com.zdmoney.service.payChannel.BusiPayBankService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 00225181 on 2015/12/21.
 */
@Service
public class BusiPayBankServiceImpl implements BusiPayBankService {
    @Override
    public List<BusiPayBankLimit> getPayBankLimit() {
        return null;
    }
}
