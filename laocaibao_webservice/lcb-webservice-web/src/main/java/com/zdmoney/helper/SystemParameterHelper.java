package com.zdmoney.helper;

import com.zdmoney.exception.BusinessException;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.SysParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

import java.util.*;
import java.util.Map.Entry;

/**
 * 用于对系统参数parameter进行读取和缓存
 *
 * @ author zhanghao
 * @ create date 2012-11-9 上午09:32:41
 * @ version 1.0
 */
@Component
@Slf4j
public class SystemParameterHelper {

    private static SysParameterService parameterService = ContextLoader.getCurrentWebApplicationContext().getBean(SysParameterService.class);

    private static LinkedHashMap<Long, SysParameter> parameterMap = new LinkedHashMap<Long, SysParameter>();

    static {
        init(loadParameterData());
    }


    /**
     * 初始化数据,将参数进行缓存
     *
     * @param loadParameterData
     * @author zhanghao
     * @date 2012-11-9,上午09:57:57
     */
    protected static void init(List<SysParameter> loadParameterData) {
        for (SysParameter parameter : loadParameterData) {
            parameterMap.put(parameter.getId().longValue(), parameter);
        }
    }

    /**
     * 加载参数数据,从数据库表里读取所有参数.
     *
     * @return 参数列表集合
     * @author zhanghao
     * @date 2012-11-9,上午09:57:22
     */
    protected static List<SysParameter> loadParameterData() {
        return parameterService.findAll();
    }

    /**
     * 重新加载系统参数数据到缓存中
     *
     * @author zhanghao
     * @date 2012-11-9,上午10:03:48
     */
    public static void reloadParameterData() {
        parameterMap.clear();
        init(loadParameterData());
        //UcHelper.initUcStaffMap();
        //CreditHelper.initCreditMap();
    }


    /**
     * 根据参数类型查询参数
     *
     * @param prType 参数类型
     * @return 符合条件的参数
     * @author zhanghao
     * @date 2012-11-13,下午02:00:05
     */
    public static List<SysParameter> findParameterByPrType(String prType) {
        HashMap<Object, Object> condition = new HashMap<Object, Object>();
        condition.put("prType", prType);
        return findParameterByCondition(condition);
    }

    public static String findOneParameterByPrType(String prType) {
        List<SysParameter> params = findParameterByPrType(prType);
        if (params.isEmpty()) {
            log.error("没有配置数据字典：" + prType);
            throw new BusinessException("系统异常，请稍后重试！");
        } else {
            return params.get(0).getPrValue();
        }
    }

    /**
     * 根据条件查询参数
     *
     * @param condition 条件,键值对形式传入
     * @return 符合条件的参数
     * @author zhanghao
     * @date 2012-11-13,下午01:53:15
     */
    protected static List<SysParameter> findParameterByCondition(HashMap<Object, Object> condition) {
        LinkedHashMap<Long, SysParameter> parameters = new LinkedHashMap<Long, SysParameter>();
        parameters.putAll(parameterMap);
        Iterator<Entry<Long, SysParameter>> iterator = parameterMap.entrySet().iterator();
        SysParameter parameter;
        while (iterator.hasNext()) {
            parameter = iterator.next().getValue();
            if (condition.containsKey("id") && parameter.getId() != condition.get("id")) {
                parameters.remove(parameter.getId());
            }
            if (condition.containsKey("prTypename") && null != parameter.getPrTypename() && !parameter.getPrTypename().equals(condition.get("prTypename"))) {
                parameters.remove(parameter.getId());
            }
            if (condition.containsKey("prType") && null != parameter.getPrType() && !parameter.getPrType().equals(condition.get("prType"))) {
                parameters.remove(parameter.getId());
            }
            if (condition.containsKey("prName") && null != parameter.getPrName() && !parameter.getPrName().equals(condition.get("prName"))) {
                parameters.remove(parameter.getId());
            }
            if (condition.containsKey("prValue") && null != parameter.getPrValue() && !parameter.getPrValue().equals(condition.get("prValue"))) {
                parameters.remove(parameter.getId());
            }
            if (condition.containsKey("prIsedit") && null != parameter.getPrIsedit() && !parameter.getPrIsedit().equals(condition.get("prIsedit"))) {
                parameters.remove(parameter.getId());
            }
            if (condition.containsKey("prState") && null != parameter.getPrState() && !parameter.getPrState().equals(condition.get("prState"))) {
                parameters.remove(parameter.getId());
            }
        }
        return convertMapToList(parameters);
    }

    /**
     * Map转List
     *
     * @param parameterMap
     * @return
     * @author zhanghao
     * @date 2012-11-13,下午01:54:14
     */
    protected static List<SysParameter> convertMapToList(LinkedHashMap<Long, SysParameter> parameterMap) {
        List<SysParameter> parameterList = new ArrayList<SysParameter>();
        Iterator<Entry<Long, SysParameter>> parameterIterator = parameterMap.entrySet().iterator();
        while (parameterIterator.hasNext()) {
            SysParameter vo = new SysParameter();
            SysParameter parameter = parameterIterator.next().getValue();
            vo.setPrName(parameter.getPrName());
            vo.setPrValue(parameter.getPrValue());
            vo.setPrTypename(parameter.getPrTypename());
            vo.setId(parameter.getId());
            vo.setPrState(parameter.getPrState());
            parameterList.add(vo);
        }
        return parameterList;
    }


}
