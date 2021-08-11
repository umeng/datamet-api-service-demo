package com.umeng.datamet.api.demo;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * @author erniu.wzh
 * @date 2021/8/9 11:48
 */
public class TestOne {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        JSONObject object = new JSONObject();
        /**
         * 发起一次API请求，安全认证方式：APPCODE
         * @param appcode 购买商品后，云市场控制台显示appcode
         * @param url 商品页显示url
         * @param object http body
         * @param stage 预发："PRE"；线上："RELEASE"
         * @return
         * @throws IOException
         */
        ApiUtils.onePostWithAppcode("#appcode",
                "#url",
                object,
                "RELEASE");

        /**
         * 发起一次API请求，安全认证方式：签名
         *
         * @param appKey    购买商品后，云市场控制台显示appkey
         * @param appSecret 购买商品后，云市场控制台显示appsecret
         * @param url       商品页显示url
         * @param object    http body
         * @param stage     预发："PRE"；线上："RELEASE"
         */
        ApiUtils.onePostWithSign("#appkey",
                "#appsecret",
                "#url",
                object,
                "RELEASE");
    }
}
