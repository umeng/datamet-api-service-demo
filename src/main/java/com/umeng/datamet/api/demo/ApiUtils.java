package com.umeng.datamet.api.demo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApiUtils {
    private static String getSignString(HttpPost httpPost) {
        Header[] headers = httpPost.getAllHeaders();
        Map<String, String> map = new HashMap<>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }
        String query = httpPost.getURI().getQuery() == null ? "" : "?" + httpPost.getURI().getQuery();
        return httpPost.getMethod() + "\n" +
                map.get("Accept") + "\n" +
                map.get("Content-MD5") + "\n" +
                map.get("Content-Type") + "\n" +
                map.get("Date") + "\n" +
                "X-Ca-Key:" + map.get("X-Ca-Key") + "\n" +
                "X-Ca-Stage:" + map.get("X-Ca-Stage") + "\n" +
                "X-Ca-Timestamp:" + map.get("X-Ca-Timestamp") + "\n" +
                "X-Ca-Version:" + map.get("X-Ca-Version") + "\n" +
                httpPost.getURI().getPath() + query;
    }

    /**
     * 发起一次API请求，安全认证方式：签名
     *
     * @param appKey    购买商品后，云市场控制台显示appkey
     * @param appSecret 购买商品后，云市场控制台显示appsecret
     * @param url       商品页显示url
     * @param object    http body
     * @param stage     预发："PRE"；线上："RELEASE"
     */
    public static void onePostWithSign(String appKey, String appSecret, String url, Object object, String stage) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        HttpPost httpPost = new HttpPost(url);
        /**
         * body
         */
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(object), StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        /**
         * header
         */
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        httpPost.setHeader("X-Ca-Version", "1");
        httpPost.setHeader("X-Ca-Signature-Headers", "X-Ca-Version,X-Ca-Stage,X-Ca-Key,X-Ca-Timestamp");
        httpPost.setHeader("X-Ca-Stage", stage);
        httpPost.setHeader("X-Ca-Key", appKey);
        httpPost.setHeader("X-Ca-Timestamp", String.valueOf(System.currentTimeMillis()));
        httpPost.setHeader("X-Ca-Nonce", UUID.randomUUID().toString());
        httpPost.setHeader("Content-MD5", Base64.encodeBase64String(DigestUtils.md5(JSON.toJSONString(object))));
        /**
         * sign 如果报错签名错误，把此处的stringToSign打印出来，换行用#代替，与网关返回的内容对比修正
         */
        String stringToSign = getSignString(httpPost);
        // System.out.println(stringToSign);
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        byte[] keyBytes = appSecret.getBytes(StandardCharsets.UTF_8);
        hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));
        String sign = new String(Base64.encodeBase64(hmacSha256.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8))));
        httpPost.setHeader("X-Ca-Signature", sign);
        /**
         * execute
         */
        CloseableHttpClient httpclient = HttpClients.createDefault();
        long a = System.currentTimeMillis();
        CloseableHttpResponse response = httpclient.execute(httpPost);
        long b = System.currentTimeMillis();
        System.out.println("返回状态：" + response.getStatusLine());
        System.out.println("请求耗时：" + (b - a) + "ms （提示：此时间包含DNS查询、TCP握手、SSL握手、服务端处理、网络延迟、Response下载等）");
        System.out.println("返回Headers：" + Arrays.toString(response.getAllHeaders()));
        System.out.println("返回Body：" + EntityUtils.toString(response.getEntity()));
    }

    /**
     * 发起一次API请求，安全认证方式：APPCODE
     *
     * @param appcode 购买商品后，云市场控制台显示appcode
     * @param url     商品页显示url
     * @param object  http body
     * @param stage   预发："PRE"；线上："RELEASE"
     * @return
     * @throws IOException
     */
    public static void onePostWithAppcode(String appcode, String url, Object object, String stage) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        /**
         * body
         */
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(object), StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        /**
         * header
         */
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("X-Ca-Stage", stage);
        httpPost.setHeader("X-Ca-Nonce", UUID.randomUUID().toString());
        httpPost.setHeader("Authorization", String.format("APPCODE %s", appcode));
        /**
         * execute
         */
        CloseableHttpClient httpclient = HttpClients.createDefault();
        long a = System.currentTimeMillis();
        CloseableHttpResponse response = httpclient.execute(httpPost);
        long b = System.currentTimeMillis();
        System.out.println("返回状态：" + response.getStatusLine());
        System.out.println("请求耗时：" + (b - a) + "ms （提示：此时间包含DNS查询、TCP握手、SSL握手、服务端处理、网络延迟、Response下载等）");
        System.out.println("返回Headers：" + Arrays.toString(response.getAllHeaders()));
        System.out.println("返回Body：" + EntityUtils.toString(response.getEntity()));
    }
}

