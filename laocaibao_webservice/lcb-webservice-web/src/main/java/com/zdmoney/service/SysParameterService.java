package com.zdmoney.service;

import com.google.common.collect.Iterables;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.sys.SysParameterMapper;
import com.zdmoney.models.sys.SysParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ******声明*************
 * <p/>
 * 版权所有：zendaimoney
 * <p/>
 * 项目名称：laocaibao_webservice
 * 类    名称：SysParameterService
 * 功能描述：
 * <p/>
 * 创建人员：joe
 * 创建时间：2015年6月17日
 *
 * @version *******修改记录************
 *          修改人员：
 *          修改时间：
 *          修改描述：
 */
@Service
public class SysParameterService extends BaseService<SysParameter, Long> {


    private SysParameterMapper getSysParameterMapper() {
        return (SysParameterMapper) baseMapper;
    }

    public SysParameter findOneByPrType(String prType){
        List<SysParameter> parameterList = findByPrType(prType);
        return Iterables.getFirst(parameterList, null);
    }

    public List<SysParameter> findByPrType(String prType){
        SysParameter sysParameter = new SysParameter();
        sysParameter.setPrType(prType);
        return findByEntity(sysParameter);
    }

    public List<SysParameter> findByPrTypeWithoutCache(String prType){
        List<SysParameter> sysParameters = getSysParameterMapper().getSysParameterWithoutCache(prType);
        return sysParameters;
    }

    public SysParameter findOneByPrTypeWithoutCache(String prType){
        List<SysParameter> parameterList = findByPrTypeWithoutCache(prType);
        return Iterables.getFirst(parameterList, null);
    }

    public SysParameter findOneByPrTypeDefaultWithoutCache(String prType){
        List<SysParameter> parameterList = getSysParameterMapper().getDefaultSysParameterWithoutCache(prType);
        return Iterables.getFirst(parameterList, null);
    }

    public SysParameter getSysParameterPrs(Map<String, Object> map){
        return getSysParameterMapper().getSysParameterPrs(map);
    }
}




	 
	