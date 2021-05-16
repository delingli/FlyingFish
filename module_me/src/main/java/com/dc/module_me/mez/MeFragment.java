package com.dc.module_me.mez;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.mvvm.BaseFragment;
import com.dc.baselib.statusBar.StarusBarUtils;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.module_me.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArounterManager.ME_MEINFO_URL)

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private SmartRefreshLayout mRefreshLayout;
    private TextView tv_phone, tv_names;

    @Override
    public void initView(View view) {

        tv_names = view.findViewById(R.id.tv_names);
        view.findViewById(R.id.iv_left_back).setVisibility(View.GONE);
       TextView tv_title= view.findViewById(R.id.tv_title);
       tv_title.setText(getResources().getString(R.string.me_title));
        view.findViewById(R.id.tv_login_out).setOnClickListener(this);
        tv_phone = view.findViewById(R.id.tv_phone);
        if (UserManager.getInstance().isLogin()) {
            User user = UserManager.getInstance().getUserInfo(getContext());
            tv_names.setText(user.realname);
            if (user.username != null && user.username.contains("@")) {
                String phone = user.username.substring(0,user.username.lastIndexOf("@"));
                tv_phone.setText(phone);

            } else {
                tv_phone.setText(user.username);

            }
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayout() {
        return R.layout.me_fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_login_out) {

            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("退出程序")
                    .setMessage("是否退出程序")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UserManager.getInstance().clearUser(getContext());
                            ARouter.getInstance().build(ArounterManager.LOGINACTIVITY_URL).navigation();
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            return;
                        }
                    }).create();
            alertDialog.show();

        }

    }
}
