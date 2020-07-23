package com.zdmoney.service.staffWhilte;

import com.zdmoney.models.BusiStaffWhilte;
import java.util.List;
import java.util.Map;

/**
 * @date 2019-03-13 11:08:53
 */
public interface IBusiStaffWhilteService {

    List<BusiStaffWhilte> queryBusiStaffWhilte(Map<String, Object> paramsMap);

    int saveBusiStaffWhilte(BusiStaffWhilte busiStaffWhilte);

    int updateBusiStaffWhilte(BusiStaffWhilte busiStaffWhilte);

    int removeBusiStaffWhilteById(long id);

}

