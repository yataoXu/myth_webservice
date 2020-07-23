package com.zdmoney.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

/**
 * Created by 00250968 on 2017/9/12
 **/
@Slf4j
public class JwtToken {

    /**
     * 秘钥 ZDATLCB-WS  MD5 32位小写
     */
    private static final byte[] SECRET = "3198e6c48a941bdfe252aa0646cac772".getBytes();

    /**
     * 初始化head部分的数据为
     * {
     * "alg":"HS256",
     * "type":"JWT"
     * }
     */
    private static final JWSHeader header=new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null);

    /**
     * 生成token，该方法只在用户登录成功后调用
     *
     * @param payload Map集合，可以存储自定义字段
     * @return token字符串
     */
    public static String createToken(Map<String, Object> payload) {
        log.info("创建Token参数" + JSON.toJSONString(payload));
        String tokenString=null;
        JWSObject jwsObject = new JWSObject(header, new Payload(new JSONObject(payload)));
        try {
            // 将jwsObject 进行HMAC签名
            jwsObject.sign(new MACSigner(SECRET));
            tokenString=jwsObject.serialize();
        } catch (JOSEException e) {
            System.err.println("签名失败:" + e.getMessage());
            e.printStackTrace();
        }
        return tokenString;
    }

    /**
     * 校验token是否合法
     *
     * @param token
     * @return
     */
    public static String validToken(String token) {
        String cmNumber = null;
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            JWSVerifier verifier = new MACVerifier(SECRET);

            if (jwsObject.verify(verifier)) {
                JSONObject jsonObj = payload.toJSONObject();
                cmNumber = jsonObj.get("cmNumber").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cmNumber;
    }

    public static void main(String[] args) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("cmNumber", "01150710000001887");
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbU51bWJlciI6IjAxMTcwMzExMDAwMDA0NTUxIn0.DACUrA2eYy4knPbvMrftQNZXAJ_qZoanbxzI1o406Js";
        //System.out.println(createToken(payload));
        System.out.println(validToken(token));
    }
}
