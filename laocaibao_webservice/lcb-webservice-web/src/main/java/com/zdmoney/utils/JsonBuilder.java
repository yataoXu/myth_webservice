
package com.zdmoney.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class JsonBuilder
{

    /****************************************************************
     * 
     * 静态方法与构造函数
     * 
     * 
     ****************************************************************/
 
    /**
     * @return 
     * json字符串转对象
     * 
     * @author [谈唯雷]
     * @method [getObjectFromJsonString]
     * @param jsonStr
     * @param clz
     * @return
     * @retruntype [Object]
     * @exception
     */
    public static Object getObjectFromJsonString(String jsonStr , Class<?> clz)
    {
        return com.alibaba.fastjson.JSON.parseObject(jsonStr, clz);
    }

    /**
     * json字符串转对象数组
     * 
     * @author [谈唯雷]
     * @method [getObjectLstFromJsonString]
     * @param jsonStr
     * @param clz
     * @return
     * @retruntype [List<?>]
     * @exception
     */
    public static <E> List<E> getObjectLstFromJsonString(String jsonStr , Class<E> clz)
    {
        return com.alibaba.fastjson.JSON.parseArray(jsonStr, clz);
    }

    /**
     * 工厂类得到一个JsonBuilder
     * 
     * @author [谈唯雷]
     * @method [build]
     * @return
     * @retruntype [JsonBuilder]
     * @exception
     */
    public static JsonBuilder build()
    {
        return new JsonBuilder();
    }

    private JsonBuilder()
    {
        json = new JSONObject();
    }

    private JSONObject json;

    /****************************************************************
     * 
     * json部分的公共方法
     * 
     * 
     ****************************************************************/
    /**
     * json构建完毕之后得到json对象
     * 
     * @author [谈唯雷]
     * @method [commit]
     * @return
     * @retruntype [JSONObject]
     * @exception
     */
    public JSONObject commit()
    {
        return json;
    }

    /**
     * 向json对象中加入值
     * 
     * @author [谈唯雷]
     * @method [append]
     * @param key
     * @param value
     * @return
     * @
     * @retruntype [JsonBuilder]
     * @exception
     */
    public JsonBuilder append(String key , String value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , int value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , long value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , double value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , float value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , boolean value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , Object value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(String key , List<Map<String, Object>> value)
    {
        json.put(key, value);
        return this;
    }

    public JsonBuilder append(Map<? extends String, ? extends Object> value)
    {
        json.putAll(value);
        return this;
    }
}
