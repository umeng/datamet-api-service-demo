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
        ApiUtils.onePost("#appkey",
                "#appsecret",
                "#url",
                object,
                "PRE");
    }
}
