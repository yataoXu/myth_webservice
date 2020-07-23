package com.zdmoney.service.payChannel;

import com.zdmoney.models.payChannel.BusiPayBankLimit;

import java.util.List;

/**
 * Created by 00225181 on 2015/12/21.
 */
public interface BusiPayBankService {
    List<BusiPayBankLimit> getPayBankLimit();
}
