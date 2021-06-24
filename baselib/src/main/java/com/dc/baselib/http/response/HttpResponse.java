package com.dc.baselib.http.response;

import com.dc.baselib.http.newhttp.StatusCode;

public class HttpResponse<T> {
    private String status;
    private String message;
    private String timestamp;
    private T data;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return StatusCode.SUCESSCODE.equals(status);
    }
}
