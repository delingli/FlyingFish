package com.dc.module_main.ui.home;

import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.mvvm.BaseFragment;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.module_main.R;

public class HomeFragment extends BaseFragment {
    @Override
    public void initView(View view) {
        getChildFragmentManager().beginTransaction().replace(R.id.fl_continerz, (BaseFragment) ARouter.getInstance().build(ArounterManager.HOME_HOMEMAINFRAGMENT_URL).navigation(), "HomeFragmentTag").commit();
    }

    @Override
    protected void initData() {


    }

    @Override
    protected int getLayout() {
        return R.layout.common_fragment_layout;
    }
}