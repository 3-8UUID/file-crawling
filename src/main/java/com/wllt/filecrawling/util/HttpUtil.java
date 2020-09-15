package com.wllt.filecrawling.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @program: file-crawling
 * @description:
 * @author: wllt
 * @create: 2020-09-15 17:56
 **/
public class HttpUtil {

    private static Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();

    /**
     * get请求
     */
    public static String get(HttpParam restParam) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null) {
            url = url+restParam.getApiPath();
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return exec(restParam, request).getResult();
    }

    /**
     * get请求
     */
    public static <T> HttpResult<T> get(HttpParam restParam, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();

        if (restParam.getApiPath() != null) {
            url = url+restParam.getApiPath();
        }
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求json数据
     */
    public static <T> HttpResult<T> post(HttpParam restParam, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();
        if (restParam.getApiPath() != null) {
            url = url + restParam.getApiPath();
        }
        Request request = new Request.Builder().url(url).build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求json数据
     */
    public static <T> HttpResult<T> post(HttpParam restParam, String reqJsonData, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();
        if (restParam.getApiPath() != null) {
            url = url+restParam.getApiPath();
        }
        RequestBody body = RequestBody.create(restParam.getMediaType(), reqJsonData);
        Request request = new Request.Builder()
                .url(url).post(body).build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求map数据
     */
    public static <T> HttpResult<T> post(HttpParam restParam, Map<String, String> parms, Class<T> tClass) throws Exception {
        String url = restParam.getApiUrl();
        if (restParam.getApiPath() != null) {
            url = url+restParam.getApiPath();
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (parms != null) {
            for (Map.Entry<String, String> entry : parms.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return exec(restParam, request, tClass);
    }

    /**
     * POST请求map数据 返回结果
     */
    public static <T> HttpResult<T> post(HttpParam restParam,  String reqJsonData) throws Exception {
        String url = restParam.getApiUrl();
        if (restParam.getApiPath() != null) {
            url = url+restParam.getApiPath();
        }
        RequestBody body = RequestBody.create(restParam.getMediaType(), reqJsonData);
        Request request = new Request.Builder()
                .url(url).post(body).build();
        return exec(restParam, request);
    }

    /**
     * 返回值封装成对象
     */
    private static <T> HttpResult<T> exec(
            HttpParam restParam,
            Request request,
            Class<T> tClass) throws Exception {

        HttpResult clientResult = exec(restParam, request);
        String result = clientResult.getResult();
        int status = clientResult.getStatus();

        T t = null;
        if (status == 200) {
            if (result != null && "".equalsIgnoreCase(result)) {
                t = gson.fromJson(result, tClass);
            }
        } else {
            try {
                result = gson.fromJson(result, String.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new HttpResult<>(clientResult.getStatus(), result, t);
    }

    /**
     * 执行方法
     */
    private static HttpResult exec(
            HttpParam restParam,
            Request request) throws Exception {

        HttpResult result = null;

        okhttp3.OkHttpClient client = null;
        ResponseBody responseBody = null;
        try {
            client = new okhttp3.OkHttpClient();

            client.newBuilder()
                    .connectTimeout(restParam.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .readTimeout(restParam.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(restParam.getWriteTimeout(), TimeUnit.MILLISECONDS);

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseBody = response.body();
                if (responseBody != null) {
                    String responseString = responseBody.string();

                    result = new HttpResult<>(response.code(), responseString, null);
                }
            } else {
                throw new Exception(response.message());
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            if (responseBody != null) {
                responseBody.close();
            }
            if (client != null) {
                client.dispatcher().executorService().shutdown();   //清除并关闭线程池
                client.connectionPool().evictAll();                 //清除并关闭连接池
                try {
                    if (client.cache() != null) {
                        client.cache().close();                         //清除cache
                    }
                } catch (IOException e) {
                    throw new Exception(e.getMessage());
                }
            }
        }
        return result;
    }
}

