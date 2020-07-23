package com.zdmoney.mapper.transfer;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.models.transfer.DebtTransferDetail;

import java.util.List;
import java.util.Map;

public interface BusiDebtTransferMapper extends JdMapper<BusiDebtTransfer, Long> {

    List<BusiDebtTransfer> getTransfersByParam(Map<String,Object> map);

    List<BusiDebtTransfer> selectDebtTransferByCondition(Map<String,Object> map);

    int getMaxId();

    int getNewNo(Long originProductId);

    int updateTransferDebtStatus(Map<String,Object> map);

    int saveDebtTransfer(BusiDebtTransfer debtTransfer);

    int updateByMap(Map<String,Object> params);

    long getTransfersNumByInitOrderNum(Map<String,Object> params);

    DebtTransferDetail getBuyInfoAndTransferInfo(Long deptTransferId);
}