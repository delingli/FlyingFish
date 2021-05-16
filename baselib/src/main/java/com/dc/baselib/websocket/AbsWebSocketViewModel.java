package com.dc.baselib.websocket;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.dc.baselib.livedata.LiveBus;
import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;
import com.zld.websocket.SocketListener;
import com.zld.websocket.WebSocketHandler;
import com.zld.websocket.response.ErrorResponse;
import com.zld.websocket.util.WSLogUtil;

import org.java_websocket.framing.Framedata;
import org.json.JSONObject;

import java.nio.ByteBuffer;

/**
 * 封装抽象WebSocketViewModel Socket监听实现，抽离View层
 * Respository 必要时分担ViewModel的压力处理数据逻辑，
 * 由于SocketResponse 中data 数据格式不确定，我们现在统一用string返回，在具体业务层做解析
 */
public abstract class AbsWebSocketViewModel<T extends BaseRespository> extends AbsViewModel<T> implements SocketListener {

    public AbsWebSocketViewModel(@NonNull Application application) {
        super(application);
        WebSocketHandler.getDefault().addListener(this);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        WebSocketHandler.getDefault().removeListener(this);
    }

    protected void postData(Object eventKey, String tag, Object t) {
        LiveBus.getDefault().postEvent(eventKey, tag, t);
    }

    protected void postData(Object eventKey, Object t) {
        postData(eventKey, null, t);
    }

    @Override
    public void onConnected() {
        Log.i("==onConnected", "onConnected");
    }

    @Override
    public void onConnectFailed(Throwable e) {
        Log.i("==onConnectFailed", e.toString());
    }

    @Override
    public void onDisconnect() {
        Log.i("==onDisconnect", "onDisconnect");
    }

    @Override
    public <T> void onMessage(ByteBuffer bytes, T data) {

    }

    public static <T> SocketResponse<String> getEntityFromResponse(T data) {
        try {
            if (data instanceof SocketResponse) {
                return (SocketResponse<String>) data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AbsWebSocketViewModel", "getEntityFromResponse", e);
        }
        return null;
    }

    @Override
    public <T> void onMessage(String message, T data) {

        try {
            SocketResponse<String> entityFromResponse = getEntityFromResponse(data);
            if (entityFromResponse != null) {
                if (entityFromResponse.code == 2101) {
                    UserManager.getInstance().clearUser(getApplication());
                    //登陆应该在业务里做，上层不做统一处理，组件化也没法在上层依赖进行跳转
                } else if (entityFromResponse.code >= 1000 && entityFromResponse.code < 2000) {
                    onProcessedMessage(entityFromResponse.command.path, entityFromResponse);
                } else {
                    onErrorMessage(entityFromResponse.command.path, entityFromResponse);

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected abstract void onProcessedMessage(String path, SocketResponse<String> socketresponse);

    protected abstract void onErrorMessage(String path,SocketResponse<String> socketresponse);

    @Override
    public void onPing(Framedata framedata) {

    }

    @Override
    public void onPong(Framedata framedata) {

    }
}
