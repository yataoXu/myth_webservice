package com.zdmoney.service;

import com.zdmoney.models.product.BusiProductContract;

/**
 * Created by user on 2019/3/5.
 */
public interface BusiProductContractService {

    BusiProductContract selectBySubjectNo(String subjectNo);
}
