package com.dc.baselib.http.exception;


public class ApiException extends Exception {
    private String code;
    private String mes;
    public ApiException(Throwable throwable, String code) {
        super(throwable);
        this.code = code;
    }
    public ApiException(String code, String mes) {
        this.code=code;
        this.mes=mes;
    }
    public ApiException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public ApiException(String code, String message, String mes) {
        super(message);
        this.code = code;
        this.mes=mes;
    }

}
