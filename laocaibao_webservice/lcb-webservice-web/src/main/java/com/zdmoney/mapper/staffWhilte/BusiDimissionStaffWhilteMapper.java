package com.zdmoney.mapper.staffWhilte;

import com.zdmoney.models.BusiDimissionStaffWhilte;
import java.util.List;
import java.util.Map;

/**
 * @date 2019-02-28 09:25:53
 */
public interface BusiDimissionStaffWhilteMapper {

    List<BusiDimissionStaffWhilte> queryBusiDimissionStaffWhilte(Map<String, Object> paramsMap);

    int saveBusiDimissionStaffWhilte(BusiDimissionStaffWhilte busiDimissionStaffWhilte);

    int updateBusiDimissionStaffWhilte(BusiDimissionStaffWhilte busiDimissionStaffWhilte);

    int removeBusiDimissionStaffWhilteById(long id);

    BusiDimissionStaffWhilte getStaffByCmNumber(String cmNumber);
}
