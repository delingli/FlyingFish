package com.dc.commonlib.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dc.commonlib.R;


public class PermisionDialog extends DialogFragment {
    public static String KEY_CONTEXT = "key_context";
    private String context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments!=null){
            context = arguments.getString(KEY_CONTEXT, null);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.common_permision_dialog, container, false);
    }

    private TextView tvDesc;
    private Button btnCacel;
    private Button btnToSetting;



    private void initParams() {
        getDialog().getWindow().setLayout( WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
/*        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            //设置透明度
            params.dimAmount = 0.3f;
            //设置dialog 宽度
            params.width = UIUtils.getScreenWidth(getContext()) - (2 * UIUtils.dip2px(getContext(), 37));
            //设置dialog 高度
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }*/
        setCancelable(false);
        if (!TextUtils.isEmpty(context)) {
            tvDesc.setText(context);
        }
    }

    private OnSettingClickListener onSettingClickListener;

    public void addOnSettingClickListener(OnSettingClickListener onSettingClickListener) {
        this.onSettingClickListener = onSettingClickListener;
    }

    public interface OnSettingClickListener {
        void onSettingClick();
        void onCacelClick();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initParams();
        tvDesc = view.findViewById(R.id.tv_desc);
        btnCacel = view.findViewById(R.id.tv_cacel);
        btnToSetting = view.findViewById(R.id.tv_toSetting);
        btnCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (null != onSettingClickListener) {
                    onSettingClickListener.onCacelClick();
                }
            }
        });


        btnToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onSettingClickListener) {
                    onSettingClickListener.onSettingClick();
                }
            }
        });
    }
}
