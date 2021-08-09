package com.guangzhou.station.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dc.baselib.constant.Constants;
import com.dc.baselib.utils.ToastUtils;
import com.guangzhou.station.R;
import com.tencent.mmkv.MMKV;

public class ConfigSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEt_ip;
    private EditText mEt_RabbitPort;
    private EditText mSetting_port_et;
    private EditText mEtRabbitUserName;
    private EditText etRabbitUserPassword;
    private MMKV mmkv;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ConfigSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_activity_configsetting);
        initView();
        mmkv = MMKV.defaultMMKV();
        fillData();
    }

    private void fillData() {
        mEt_ip.setText(Constants.getmConstants().GET_SERVER_HOST());
        mSetting_port_et.setText(Constants.getmConstants().GET_SERVER_PORT() + "");
        mEt_RabbitPort.setText(Constants.getmConstants().GET_RABBIT_PORT() + "");
        mEtRabbitUserName.setText(Constants.getmConstants().GET_RABBIT_NAME());
        etRabbitUserPassword.setText(Constants.getmConstants().GET_RABBIT_PASSWORD());
    }

    private void initView() {
        mEt_ip = findViewById(R.id.et_ip);
        mSetting_port_et = findViewById(R.id.setting_port_et);
        mEt_RabbitPort = findViewById(R.id.et_port);
        mEtRabbitUserName = findViewById(R.id.tv_user_name);
        etRabbitUserPassword = findViewById(R.id.tv_user_password);
        findViewById(R.id.tv_commit).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_commit) {
            String ip = mEt_ip.getText().toString();
            String setting_port_et = mSetting_port_et.getText().toString();
            String rabbitport = mEt_RabbitPort.getText().toString();
            String rabbitusername = mEtRabbitUserName.getText().toString();
            String rabbituserpassword = etRabbitUserPassword.getText().toString();
            if (!TextUtils.isEmpty(ip)
                    && !TextUtils.isEmpty(rabbitusername)
                    && !TextUtils.isEmpty(rabbituserpassword)
                    && !TextUtils.isEmpty(rabbitport)
                    && !TextUtils.isEmpty(setting_port_et)
            ) {
                Constants.getmConstants().SET_RABBIT_NAME(rabbitusername);
                Constants.getmConstants().SET_RABBIT_PASSWORD(rabbituserpassword);
                Constants.getmConstants().SET_SERVER_HOST(ip);
                Constants.getmConstants().SET_SERVER_PORT(setting_port_et);
                Constants.getmConstants().SET_RABBIT_PORT(rabbitport);

                ToastUtils.showToast("恭喜你重置成功，请杀掉进程后重启!");
                finish();
            } else {
                ToastUtils.showToast("相关输入框不能为空!");
            }
        } else if (id == R.id.tv_cancel) {
            finish();
        }
    }
}
