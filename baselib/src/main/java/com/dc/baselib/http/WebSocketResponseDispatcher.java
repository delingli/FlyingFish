package com.dc.baselib.http;

import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.utils.JsonUtil;
import com.dc.baselib.websocket.SocketResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zld.websocket.dispatcher.IResponseDispatcher;
import com.zld.websocket.dispatcher.ResponseDelivery;
import com.zld.websocket.response.ErrorResponse;
import com.zld.websocket.response.Response;
import com.zld.websocket.response.ResponseFactory;

import org.java_websocket.framing.Framedata;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;

/**
 * Created by ZhangKe on 2018/6/28.
 */
public class WebSocketResponseDispatcher implements IResponseDispatcher {

    private static final String TAG = "ResponseDispatcher";

    /**
     * JSON 数据格式错误
     */
    private static final int JSON_ERROR = 11;
    /**
     * code 码错误
     */
    private static final int CODE_ERROR = 12;

    @Override
    public void onConnected(ResponseDelivery delivery) {
        delivery.onConnected();
    }

    @Override
    public void onConnectFailed(Throwable cause, ResponseDelivery delivery) {
        delivery.onConnectFailed(cause);
    }

    @Override
    public void onDisconnect(ResponseDelivery delivery) {
        delivery.onDisconnect();
    }

    @Override
    public void onMessage(ByteBuffer byteBuffer, ResponseDelivery delivery) {
        delivery.onMessage(byteBuffer, null);
    }

    @Override
    public void onPing(Framedata framedata, ResponseDelivery delivery) {
        delivery.onPing(framedata);
    }

    @Override
    public void onPong(Framedata framedata, ResponseDelivery delivery) {
        delivery.onPong(framedata);
    }

    @Override
    public void onMessage(String message, ResponseDelivery delivery) {
        try {
            //TODO
            //统一处理成功的 发射数据
            JSONObject jsonObject = new JSONObject(message);
            int code = jsonObject.optInt("code");
            String msg = jsonObject.optString("msg");
            JSONObject dataJson = jsonObject.optJSONObject("data");
            String dataz = null;
            if (dataJson != null) {
                dataz = dataJson.toString();

            }
            JSONObject commandObj = jsonObject.optJSONObject("command");
            SocketResponse.CommandBean commandBean = new SocketResponse.CommandBean();
            commandBean.path = commandObj.optString("path");
            commandBean.unique = commandObj.optString("unique");
            SocketResponse<String> sockeJson = new SocketResponse<>();
            sockeJson.code = code;
            sockeJson.msg = msg;
            sockeJson.data = dataz;
            sockeJson.command = commandBean;
//            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//            SocketResponse<String> ss = new Gson().fromJson(message, SocketResponse.class);

//            SocketResponse socketResponse = new Gson().fromJson(message, new TypeToken<SocketResponse<JSONObject>>() {
//            }.getType());
//            Type type =TokenUtil.<SocketResponse<String>>getType();
// fromJson返回Map或者List, ClassCastException!
//            SocketResponse<String> socketResponse = new Gson().fromJson(message, type);
//            SocketResponse socketResponse = JsonUtil.fromJson(message, type);
            delivery.onMessage(message, sockeJson);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse errorResponse = ResponseFactory.createErrorResponse();
            Response<String> textResponse = ResponseFactory.createTextResponse();
            textResponse.setResponseData(message);
            errorResponse.setResponseData(textResponse);
            errorResponse.setErrorCode(JSON_ERROR);
            errorResponse.setCause(e);
            onSendDataError(errorResponse, delivery);
        }
    }

    @Override
    public void onSendDataError(ErrorResponse error, ResponseDelivery delivery) {
        switch (error.getErrorCode()) {
            case ErrorResponse.ERROR_NO_CONNECT:
                error.setDescription("网络错误");
                break;
            case ErrorResponse.ERROR_UN_INIT:
                error.setDescription("连接未初始化");
                break;
            case ErrorResponse.ERROR_UNKNOWN:
                error.setDescription("未知错误");
                break;
            case JSON_ERROR:
                error.setDescription("数据格式异常");
                break;
        }
        delivery.onSendDataError(error);
    }

}
