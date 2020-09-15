package com.wllt.filecrawling.util;

/**
 * @program: file-crawling
 * @description:
 * @author: wllt
 * @create: 2020-09-15 17:56
 **/
public class HttpResult<T> {

    private int status;
    private String result;
    private T resultObject;

    public HttpResult() {
    }

    public HttpResult(int status, String result, T resultObject) {
        this.status = status;
        this.result = result;
        this.resultObject = resultObject;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public T getResultObject() {
        return resultObject;
    }

    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }
}
