package com.dc.baselib.websocket;

/**
 * 对应Socket 传输回来的实体
 *
 * @param <T>
 */
public class SocketResponse<T> {
    public int code;
    public String msg;
    public T data;
    public CommandBean command;


    public static class CommandBean {
        public String path;
        public String unique;

    }
}
