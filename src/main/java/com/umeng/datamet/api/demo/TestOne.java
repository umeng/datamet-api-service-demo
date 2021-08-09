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
         * ApiUtils.onePost 发起一次API请求
         *
         * @param appKey    购买商品后，云市场控制台显示appkey
         * @param appSecret 购买商品后，云市场控制台显示appsecret
         * @param url       商品页显示url
         * @param object    http body
         * @param stage     预发："PRE"；线上："RELEASE"
         */
        ApiUtils.onePost("#appkey",
                "#appsecret",
                "#url",
                object,
                "PRE");
    }
}
