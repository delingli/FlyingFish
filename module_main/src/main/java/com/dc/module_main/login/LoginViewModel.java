package com.dc.module_main.login;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.websocket.AbsWebSocketViewModel;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.baselib.websocket.SocketResponse;
import com.dc.commonlib.common.CommonErrorCode;
import com.dc.commonlib.common.WSAPI;
import com.dc.commonlib.utils.JsonUtil;
import com.dc.commonlib.utils.LogUtil;
import com.dc.module_main.R;
import com.zld.websocket.WebSocketHandler;
import com.zld.websocket.response.ErrorResponse;
import com.zld.websocket.response.TextResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginViewModel extends AbsWebSocketViewModel<LoginRespository> {
    public String EVENT_LOGIN_SUCESS;
    public String EVENT_SHOW_CAPTURE;
    public String EVENT_SENDSMS_SUCESS;
    private boolean needVerifyCodeCaptcha = false;
    private boolean needLoginaptcha = false;
    public static String sucess = "sucess";
    public static String hide = "hide";

    public LoginViewModel(@NonNull Application application) {
        super(application);
        EVENT_LOGIN_SUCESS = EventUtils.getEventKey();
        EVENT_SHOW_CAPTURE = EventUtils.getEventKey();
        EVENT_SENDSMS_SUCESS = EventUtils.getEventKey();
    }


    @Override
    protected void onProcessedMessage(String path, SocketResponse<String> socketresponse) {
        if (TextUtils.equals(path, WSAPI.LOGIN_PATH)) {
            if (null != socketresponse && socketresponse.data != null) {
                User user = JsonUtil.fromJson(socketresponse.data, User.class);
                needLoginaptcha = user.captcha;
                if (user.is_owner) {
                    UserManager.getInstance().saveUserInfo(getApplication(), user);
                    postData(EVENT_LOGIN_SUCESS, user);
                } else {
                    // todo 不能进主页权限
                    ToastUtils.showToast(getApplication().getResources().getString(R.string.no_permissions_login));
                    return;
                }
                if (needLoginaptcha) {
                    postData(EVENT_SHOW_CAPTURE, sucess);

                } else {
                    postData(EVENT_SHOW_CAPTURE, hide);
                }
                //弹出提示
                ToastUtils.showToast(socketresponse.msg);
            }
        } else if (TextUtils.equals(WSAPI.LoginVerifyCode, path)) {
            if (socketresponse != null && socketresponse.data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(socketresponse.data);
                    boolean captcha = jsonObject.optBoolean("captcha");
                    needVerifyCodeCaptcha = captcha;
                    if (needVerifyCodeCaptcha) {
                        postData(EVENT_SHOW_CAPTURE, sucess);
                    } else {
                        postData(EVENT_SHOW_CAPTURE, hide);

                    }

                    ToastUtils.showToast(socketresponse.msg);
                    postData(EVENT_SENDSMS_SUCESS, "captures");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    @Override
    protected void onErrorMessage(String path, SocketResponse<String> socketresponse) {
        ToastUtils.showToast(socketresponse.msg);
        if (TextUtils.equals(WSAPI.LOGIN_PATH, path)) {
            if (socketresponse.data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(socketresponse.data);
                    needLoginaptcha = jsonObject.optBoolean("captcha");
                    if(needLoginaptcha){
                        postData(EVENT_SHOW_CAPTURE, sucess);

                    }else {
                        postData(EVENT_SHOW_CAPTURE, hide);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (TextUtils.equals(WSAPI.LoginVerifyCode, path)) {
            if (socketresponse.code == CommonErrorCode.graphic_verification_code) {
                if (socketresponse.data != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(socketresponse.data);
                        needVerifyCodeCaptcha= jsonObject.optBoolean("captcha");
                        if(needVerifyCodeCaptcha){
                            postData(EVENT_SHOW_CAPTURE, sucess);

                        }else {
                            postData(EVENT_SHOW_CAPTURE, hide);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public void loginWithVerificationCode(String account, String sms, String captcha_code, String randomKey) {
        try {
            JSONObject params = new JSONObject();
            JSONObject command = new JSONObject();
            JSONObject parameters = new JSONObject();
            command.put("path", WSAPI.LOGIN_PATH);
            parameters.put("username", account);
            parameters.put("sms", sms);
            if (needLoginaptcha && !TextUtils.isEmpty(captcha_code) && !TextUtils.isEmpty(randomKey)) {
                parameters.put("rand_captcha_key", randomKey);
                parameters.put("captcha_code", captcha_code);
            }
            params.put("command", command);
            params.put("parameters", parameters);
            LogUtil.d("ldl", params.toString());
            WebSocketHandler.getDefault().send(params.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onVerifyClick(String account, String captcha_code, String rand_captcha_key) {
        try {
            JSONObject params = new JSONObject();
            JSONObject command = new JSONObject();
            JSONObject parameters = new JSONObject();
            command.put("path", WSAPI.LoginVerifyCode);
            parameters.put("username", account.trim());

            if (needVerifyCodeCaptcha) {
                if (TextUtils.isEmpty(captcha_code)) {
                    ToastUtils.showToast(getApplication().getString(R.string.tip_capture_format));
                    postData(EVENT_SHOW_CAPTURE, sucess);
                    return;
                }

                if (!TextUtils.isEmpty(rand_captcha_key)) {
                    parameters.put("rand_captcha_key", rand_captcha_key);
                }
                if (!TextUtils.isEmpty(captcha_code)) {
                    parameters.put("captcha_code", captcha_code);
                }
            }

            params.put("command", command);
            params.put("parameters", parameters);
            LogUtil.d("login", params.toString());
            WebSocketHandler.getDefault().send(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSendDataError(ErrorResponse error) {
        ToastUtils.showToast(error.getDescription());
        if (error.getResponseData() instanceof TextResponse) {
            TextResponse textResponse = (TextResponse) error.getResponseData();
            if (!TextUtils.isEmpty(textResponse.getResponseData())) {
                try {
                    User user = JsonUtil.fromJson(textResponse.getResponseData(), User.class);
                    if (null != user && user.captcha) {
                        postData(EVENT_SHOW_CAPTURE, sucess);
                    }
          /*          WSCommonEntity<User> entity = JSON.parseObject(textResponse.getResponseData(), new TypeReference<CommonEntity<UserInfo>>() {
                    });
                    if (entity != null
                            && entity.getCommand() != null
                            && TextUtils.equals(Api.LOGIN_PATH, entity.getCommand().getPath())
                            && entity.getData() != null
                            && entity.getData().isCaptcha()) {
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("LoginActiivity", e.getMessage());
                }
            }
        }
    }


}
