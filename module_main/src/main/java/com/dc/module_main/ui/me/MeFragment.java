package com.dc.module_main.ui.me;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.mvvm.AbsLifecycleFragment;
import com.dc.baselib.mvvm.BaseFragment;
import com.dc.baselib.utils.ToastUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.common.ConfigUtils;
import com.dc.commonlib.commonentity.UserInfo;
import com.dc.commonlib.utils.ActivityUtils;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.commonlib.utils.GlideUtils;
import com.dc.commonlib.utils.UIUtils;
import com.dc.module_main.R;
import com.dc.module_main.TestActivity;

public class MeFragment extends BaseFragment {

    @Override
    public void initView(View view) {
//        ME_MEINFO_URL
//        fl_continerz

        getChildFragmentManager().beginTransaction().replace(R.id.fl_continerz, (BaseFragment) ARouter.getInstance().build(ArounterManager.ME_MEINFO_URL).navigation(),"MeFragmentsTag").commit();
    }

    @Override
    protected void initData() {

    }

    protected int getLayout() {
        return R.layout.common_fragment_layout;
    }
}

