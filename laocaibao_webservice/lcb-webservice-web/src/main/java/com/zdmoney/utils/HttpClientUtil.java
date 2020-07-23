package com.zdmoney.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HttpClient工具类
 * 支持https, http, 证书
 *
 * Created by gaol on 2017年1月17日
 **/
@Slf4j
public class HttpClientUtil {

    private static int SocketTimeout = 3000;//3秒
    private static int ConnectTimeout = 3000;//3秒
    private static Boolean SetTimeOut = true;

    /**
     * 创建的httpClient实例
     *
     * 如果请求中需要携带证书,则必须传入以下参数; 用不到证书则不需要
     * SSL : true
     * path : 密钥库路径
     * pwd : 密钥库密码
     *
     * 关于证书的一些命令, 这里以cer为例
     * 1.创建keystore,并将证书testServer.cer 别名my server cert导入到my.keystore中
     *   keytool -import -alias "my server cert" -file testServer.cer -keystore my.keystore
     * 2.修改keystore的默认密码(changeit) 为123456
     *   keytool -storepasswd -new 123456 -keystore my.keystore
     * 3.查看keystore信息(输入修改后的密码,即123456)
     *   keytool -list -keystore my.keystore
     *
     * @param params
     * @return
     */
    private static CloseableHttpClient getHttpClient(Map<String, String> params) {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        SSLContext sslContext;

        boolean flag = "true".equals(params.get("SSL")) ? true : false;
        String path = params.get("path");
        String pwd = params.get("pwd");
        if(flag & (StringUtils.isBlank(path) || StringUtils.isBlank(pwd))){
            throw new RuntimeException("请传入密钥库路径和密钥库密码参数信息");
        }
        try {
            if(flag){
                sslContext = custom(path, pwd);
            }else{
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                TrustStrategy anyTrustStrategy = new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        return true;
                    }
                };
                sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            }
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    /**
     * 设置信任自签名证书
     *
     * @param keyStorePath 密钥库路径
     * @param keyStorePwd 密钥库密码
     * @return
     */
    public static SSLContext custom(String keyStorePath, String keyStorePwd){
        SSLContext sc = null;
        FileInputStream instream = null;
        KeyStore trustStore;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(keyStorePath));
            trustStore.load(instream, keyStorePwd.toCharArray());
            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
        } catch (KeyStoreException | NoSuchAlgorithmException| CertificateException | IOException | KeyManagementException e) {
            e.printStackTrace();
        } finally {
            try {
                instream.close();
            } catch (IOException e) {
            }
        }
        return sc;
    }

    /**
     * GET请求
     * @param url 请求的url
     * @param params 提交的参数
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String, String> params) throws IOException {
        if(StringUtils.isBlank(url)) return null;
        String responseBody = "";
        CloseableHttpClient httpClient = getHttpClient(params);

        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList(params.size());
            for(Map.Entry<String,String> entry : params.entrySet()){
                String key = entry.getKey().toUpperCase();
                if (!"SSL".equals(key) && !"PWD".equals(key) && !"PATH".equals(key)) {
                    String value = entry.getValue();
                    if(value != null){
                        pairs.add(new BasicNameValuePair(entry.getKey(),value));
                    }
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, Consts.UTF_8));
        }
        log.info("---------->HTTP CLIENT GET URL : " + url);
        HttpGet httpGet = new HttpGet(url);
        if (SetTimeOut) {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout).setConnectTimeout(ConnectTimeout).build();//设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
        }
        try {
            // 请求数据
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpClient.close();
        }
        return responseBody;
    }

    /**
     * POST请求
     * @param url     请求的url
     * @param params  提交的参数
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params) throws IOException {
        if(StringUtils.isBlank(url)) return null;
        String responseBody = "";
        CloseableHttpClient httpClient = getHttpClient(params);

        //指定url,和http方式
        HttpPost httpPost = new HttpPost(url);
        if (SetTimeOut) {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout).setConnectTimeout(ConnectTimeout).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
        }
        //添加参数
        List<NameValuePair> nvps = new ArrayList<>();
        if (params != null && params.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        //请求数据
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        return responseBody;
    }

    public static void main(String[] args) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", "文曦");
            params.put("cid", "432301197901202512");
            params.put("member", "1290");
            params.put("sign", "82C1E4D45D19BBD19AE42E2FC2C94C23");
            // 需要加载密钥库
            /*params.put("SSL", "true");
            params.put("path", "D:/testService/anrong.keystore");
            params.put("pwd", "123456");*/
            System.out.println(get("https://p2ptest.creditdata.cn:10000/yl/rest/relation/identityConfirm", params));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}