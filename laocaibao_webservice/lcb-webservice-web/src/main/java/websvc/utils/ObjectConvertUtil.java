package websvc.utils;

import com.google.common.collect.Maps;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import websvc.req.ReqHeadParam;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 00225181 on 2015/10/9.
 * 对象转换工具类
 */
public class ObjectConvertUtil {
    public static  <T> T convertClass(Object obj,Class<T> cla) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Map<String,Object> maps = Maps.newTreeMap();
        if(null==obj) {
            return null;
        }
        Class<?> cls = obj.getClass();
        T dataBean = cla.newInstance();
        Field[] fields = cls.getDeclaredFields();
        Field[] beanFields = cla.getDeclaredFields();
        for(Field field:fields){
            String fieldName = field.getName();
            String strGet = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
            Method methodGet = cls.getDeclaredMethod(strGet);
            Object object = methodGet.invoke(obj);
            maps.put(fieldName,object==null?"":object);
        }

        for(Field field:beanFields){
            field.setAccessible(true);
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            String fieldValue = maps.get(fieldName)==null?null:maps.get(fieldName).toString();
            if(fieldValue!=null){
                try {
                    if(String.class.equals(fieldType)){
                        field.set(dataBean, fieldValue);
                    }else if(byte.class.equals(fieldType)){
                        field.setByte(dataBean, Byte.parseByte(fieldValue));

                    }else if(Byte.class.equals(fieldType)){
                        field.set(dataBean, Byte.valueOf(fieldValue));

                    }else if(boolean.class.equals(fieldType)){
                        field.setBoolean(dataBean, Boolean.parseBoolean(fieldValue));

                    }else if(Boolean.class.equals(fieldType)){
                        field.set(dataBean, Boolean.valueOf(fieldValue));

                    }else if(short.class.equals(fieldType)){
                        field.setShort(dataBean, Short.parseShort(fieldValue));

                    }else if(Short.class.equals(fieldType)){
                        field.set(dataBean, Short.valueOf(fieldValue));

                    }else if(int.class.equals(fieldType)){
                        field.setInt(dataBean, Integer.parseInt(fieldValue));

                    }else if(Integer.class.equals(fieldType)){
                        field.set(dataBean, Integer.valueOf(fieldValue));

                    }else if(long.class.equals(fieldType)){
                        field.setLong(dataBean, Long.parseLong(fieldValue));

                    }else if(Long.class.equals(fieldType)){
                        field.set(dataBean, Long.valueOf(fieldValue));

                    }else if(float.class.equals(fieldType)){
                        field.setFloat(dataBean, Float.parseFloat(fieldValue));

                    }else if(Float.class.equals(fieldType)){
                        field.set(dataBean, Float.valueOf(fieldValue));

                    }else if(double.class.equals(fieldType)){
                        field.setDouble(dataBean, Double.parseDouble(fieldValue));

                    }else if(Double.class.equals(fieldType)){
                        field.set(dataBean, Double.valueOf(fieldValue));

                    }else if(Date.class.equals(fieldType)){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        field.set(dataBean, sdf.parse(fieldValue));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        return dataBean;

    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> clazz) {

        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();

            // 获取自定义clazz类的全部字段
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {

                // 获取字段修饰符 需要排除静态及final修饰的字段
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                // 强制设置为 true 即可以对字段值进行设置 防止一些方法阻碍我们对属性进行赋值
                field.setAccessible(true);

                // map 中如果包含字段名称的 key 则将对应的值放入 clazz 对象对应的字段上
                if (map.containsKey(field.getName())){
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }



}
