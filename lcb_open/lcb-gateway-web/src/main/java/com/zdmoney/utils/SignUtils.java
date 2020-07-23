package com.zdmoney.utils;/**
 * Created by pc05 on 2017/12/1.
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-12-01 17:20
 * @email : huangcy01@zendaimoney.com
 **/
@Slf4j
@Component
public class SignUtils {

    /**
     * 生成签名
     * <p>
     * a.对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）后，
     * 使用 URL 键值对的格式（即 key1=value1&key2=value2…）拼接成字符串 string1,
     * 注意：值为空的参数不参不签名
     * b.在 string1 最后拼接上 key=KEY(密钥)得到 stringSignTemp
     * 字符串，并对stringSignTemp 进行 md5 运算，再将得到的字符串所有字符转换为大写，得到 sign值
     * </p>
     *
     * @param reqParams
     * @return
     */
    public static String getSign(Map<String, Object> reqParams, String creditReqKey) {
        StringBuilder sbl = new StringBuilder();
        Iterator<String> itr = reqParams.keySet().iterator();
        String key;
        String value;
        while (itr.hasNext()) {
            key = itr.next();
            value = (String) reqParams.get(key);
            if (StringUtils.isNotBlank(value)
                    && !StringUtils.equals(key, "sign")
                    && !StringUtils.equals(key, "key")) {
                sbl.append(key).append("=").append(value).append("&");
            }
        }
        String stringSignTemp = sbl.append("key=").append(creditReqKey).toString();
        log.info(stringSignTemp);
        return MD5Utils.encode(stringSignTemp, "UTF-8").toUpperCase();
    }
}
