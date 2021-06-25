package com.dc.baselib.http.exception;


public class ApiException extends Exception {
    private int code;
    private String mes;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ApiException(int code, String mes) {
        super(mes);
        this.code = code;
        this.mes = mes;
    }

    public ApiException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }


}
