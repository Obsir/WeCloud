package com.obser.wecloud.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/7/29.
 */

public class NetUtils {

    public static final String LOGIN_URL = "http://192.168.1.103:8083/login";
    public static final String REGISTER_URL = "http://192.168.1.103:8083/regist";
    public static final String FIND_ALL_USER = "http://192.168.1.103:8083/findAllUser";
    //保证OkHttpClient是唯一的
    private static OkHttpClient okHttpClient;

    static {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
    }

    /**
     * GET请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(String url, Callback callback) {
        //2.构造request对象
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        //3.将request封装为call
        Call call = okHttpClient.newCall(request);
        //4.执行call
        call.enqueue(callback);
    }

    /**
     * Post请求
     *
     * @param url
     * @param params   参数
     * @param callback 回调函数
     */
    public static void doPost(String url, Map<String, String> params, Callback callback) {
        if (callback == null) throw new NullPointerException("callback is null");
        if (params == null) throw new NullPointerException("params is null");

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String value = params.get(key);
            Log.d("okHttp", key + " : " + value);
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    public static String UsersOfGet(String UsersList) {
        HttpURLConnection conn = null;
        try {
            Log.e("传过来的列表地址", UsersList);
            URL url = new URL(UsersList);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");        // get或者post必须得全大写
            conn.setConnectTimeout(10000); // 连接的超时时间
            conn.setReadTimeout(5000); // 读数据的超时时间

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String state = getStringFromInputStream(is);
                if (state != null) {
                    Log.e("能正常得到列表state", state);
                }
                return state;
            } else {
                Log.i(TAG, "访问失败: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();        // 关闭连接
            }
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();

        String html = baos.toString();    // 把流中的数据转换成字符串, 采用的编码是: utf-8

        baos.close();
        return html;
    }
}
