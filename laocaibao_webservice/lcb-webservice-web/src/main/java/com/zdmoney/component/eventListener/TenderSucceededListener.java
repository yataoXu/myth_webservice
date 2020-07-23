package com.zdmoney.component.eventListener;

import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectCollectResultDto;
import com.zdmoney.assets.api.dto.subject.SubjectOrderItemDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.service.BusiOrderService;
import com.zdmoney.service.BusiProductContractService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.utils.ApplicationEventSupport;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2019/3/5.
 */
@Component
public class TenderSucceededListener implements ApplicationListener<ApplicationEventSupport.TenderSucceededEvent> {

    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(TenderSucceededListener.class);

    @Autowired
    private BusiProductSubMapper productSubMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @Autowired
    private BusiProductContractService productContractService;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private SubjectService subjectService;

    @Override
    @Async("matchMainOrderThreadExecutor")
    public void onApplicationEvent(ApplicationEventSupport.TenderSucceededEvent event) {
        String orderNo = event.getSource();
        BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(orderNo);
        updateProduct(orderSub);
        subjectService.sendGoalReachedNoticeIfNecessary(orderSub.getSubjectNo(), true);
    }

    private void updateProduct(BusiOrderSub orderSub){
        Map<String,Object> params = new HashMap<>();
        params.put("productId",orderSub.getProductId());
        params.put("amt",orderSub.getOrderAmt());
        productSubMapper.updateInvestAmt(params);
    }

}
