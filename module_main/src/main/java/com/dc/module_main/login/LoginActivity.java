package com.dc.module_main.login;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.http.Environment;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.baselib.utils.ToastUtils;
import com.dc.commonlib.common.WSAPI;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.commonlib.utils.GlideUtils;
import com.dc.commonlib.utils.UIUtils;
import com.dc.commonlib.utils.UUIDUtils;
import com.dc.commonlib.weiget.CountDownButton;
import com.dc.module_main.MainActivity;
import com.dc.module_main.R;

@Route(path = ArounterManager.LOGINACTIVITY_URL)
public class LoginActivity extends AbsLifecycleActivity<LoginViewModel> implements View.OnClickListener {

    private EditText et_phone, et_verification_code, et_captcha_code;
    private ImageView img_captcha;
    private CountDownButton mCountDownButton;
    private Button btn_logins;
    private LinearLayout ll_verify_root;
    private String randomKey;
    private ImageView iv_del;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.main_activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setmToolBarlheadHide(true);
        btn_logins = findViewById(R.id.btn_logins);
        iv_del = findViewById(R.id.iv_del);
        btn_logins.setOnClickListener(this);
        iv_del.setOnClickListener(this);
        et_phone = findViewById(R.id.et_phone);
        mCountDownButton = findViewById(R.id.btn_verification_code);
        img_captcha = findViewById(R.id.img_captcha);
        img_captcha.setOnClickListener(this);
        ll_verify_root = findViewById(R.id.ll_verify_root);
        et_verification_code = findViewById(R.id.et_verification_code);
        et_captcha_code = findViewById(R.id.et_captcha_code);
        EditTextWatcher textWatcher = new EditTextWatcher();
        et_phone.addTextChangedListener(textWatcher);
        et_verification_code.addTextChangedListener(textWatcher);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mCountDownButton.setNormalText(getResources().getString(R.string.gain_auth_code))
                .setCountDownText("秒后重发")
                .setCountDefaultColor(Color.parseColor("#3476f9"))
                .setCountdownColor(Color.parseColor("#ababbb"))
                .setCloseKeepCountDown(true) // 关闭页面保持倒计时开关
                .setCountDownClickable(false) //倒计时期间点击事件是否生效开关
                .setShowFormatTime(false)//是否格式化时间
                .setOnCountDownFinishListener(new CountDownButton.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        mCountDownButton.setEnabled(et_verification_code.getText().length() > 0);
                    }
                }).setOnClickListener(this);
        mCountDownButton.setEnabled(false);
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void dataObserver() {
        super.dataObserver();
        registerSubscriber(mViewModel.EVENT_LOGIN_SUCESS, User.class).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                // todo 跳转
                MainActivity.startActivity(LoginActivity.this);
                finish();
            }
        });
        registerSubscriber(mViewModel.EVENT_SHOW_CAPTURE, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String capture) {
                if (capture.equals(mViewModel.sucess)) {
                    ll_verify_root.setVisibility(View.VISIBLE);
                    createRandKey();
                } else {
                    ll_verify_root.setVisibility(View.GONE);

                }
            }
        });
        registerSubscriber(mViewModel.EVENT_SENDSMS_SUCESS, String.class).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String ss) {
                mCountDownButton.startCountDown(60);
            }
        });
    }

    private class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String account = et_phone.getText().toString();
            String et_verification_codes = et_verification_code.getText().toString();

            if (!TextUtils.isEmpty(account)) {
                iv_del.setVisibility(View.VISIBLE);
            } else {
                iv_del.setVisibility(View.INVISIBLE);
            }


            mCountDownButton.setEnabled(account.length() > 0);
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(et_verification_codes)) {
                if (et_verification_codes.length() < 4) {
                    btn_logins.setEnabled(false);
                } else {
                    btn_logins.setEnabled(true);
                }
            } else {
                btn_logins.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String phone = et_phone.getText().toString().trim();
        String graphicCode = et_captcha_code.getText().toString().trim();
        String verification_code = et_verification_code.getText().toString().trim();
        if (id == R.id.btn_logins) {
            if (ll_verify_root.getVisibility() == View.VISIBLE && TextUtils.isEmpty(graphicCode)) {
                ToastUtils.showToast(getResources().getString(R.string.tip_capture_format));
                return;
            }
            mViewModel.loginWithVerificationCode(phone, verification_code, graphicCode, randomKey);
        } else if (id == R.id.img_captcha) {
            if (ll_verify_root.getVisibility() != View.VISIBLE) {
                ll_verify_root.setVisibility(View.VISIBLE);
            }
            createRandKey();
        } else if (id == R.id.btn_verification_code) {
            if (TextUtils.isEmpty(phone) || !UIUtils.verifyPhoneNumber(phone)) {
                ToastUtils.showToast(getResources().getString(R.string.tip_mobile_format));
                return;
            }
            mViewModel.onVerifyClick(phone, graphicCode, randomKey);
        } else if (id == R.id.iv_del) {
            et_phone.setText("");
        }


    }

    private void createRandKey() {
        randomKey = UUIDUtils.createUUid();
        String url = Environment.getInstance().getHttpUrl() + WSAPI.CAPTCHA_CODE + randomKey + "/";
        GlideUtils.loadUrl(getApplicationContext(), url, img_captcha);
    }
}
