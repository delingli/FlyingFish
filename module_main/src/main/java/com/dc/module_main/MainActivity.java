package com.dc.module_main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.mvvm.BaseActivity;
import com.dc.baselib.mvvm.BaseFragment;
import com.dc.baselib.statusBar.StarusBarUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.commonentity.video.DisplayVideoPlayerManager;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.commonlib.utils.UIUtils;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

@Route(path = ArounterManager.MAIN_MAIN_HOME)
public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private BottomNavigationView mNavView;
    private ViewPager mViewPager;
    private List<BaseFragment> mList;


    protected void initData() {

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        if (null != getSupportActionBar()) {
            getSupportActionBar().hide();
        }
        setBackShow(false);

        mNavView = findViewById(R.id.nav_view);
        mViewPager = findViewById(R.id.viewPager);
        mNavView.setOnNavigationItemSelectedListener(this);
        mViewPager.addOnPageChangeListener(this);

        mList = new ArrayList<>();
        mList.add((BaseFragment) ARouter.getInstance().build(ArounterManager.HOME_HOMEMAINFRAGMENT_URL).navigation());
        mList.add((BaseFragment) ARouter.getInstance().build(ArounterManager.PROJ_PROJECTFRAGMENT_URL).navigation());
        mList.add((BaseFragment) ARouter.getInstance().build(ArounterManager.ME_MEINFO_URL).navigation());
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mList));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        StarusBarUtils.setRootViewFitsSystemWindows(this, true);
//        setPaddingTop();
        mNavView.setItemIconTintList(null);
        mNavView.setSelectedItemId(R.id.navigation_home);//根据具体情况调用
        setmToolBarlheadHide(true);
        StarusBarUtils.setTranslucentStatus(this);
    }

    @Override
    protected void initStatusBar() {
//        super.initStatusBar();
//
//        setPaddingTop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != DisplayVideoPlayerManager.instance().getCurrentVideoPlayer()) {
            DisplayVideoPlayerManager.instance().getCurrentVideoPlayer().onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != DisplayVideoPlayerManager.instance().getCurrentVideoPlayer()) {
            DisplayVideoPlayerManager.instance().getCurrentVideoPlayer().onStop();
        }
    }

    @Override
    public void onBackPressed() {
        if (DisplayVideoPlayerManager.instance().onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.navigation_home) {
            setTitle(R.string.home_main_desc);
            mViewPager.setCurrentItem(0, true);
            return true;
        } else if (itemId == R.id.navigation_bbs) {
            setTitle(R.string.project);
            StarusBarUtils.setStatusBarDarkTheme(this, true);
            mViewPager.setCurrentItem(1, true);
            return true;

        } else if (itemId == R.id.navigation_me) {
            setTitle(R.string.me_title);
            StarusBarUtils.setStatusBarDarkTheme(this, true);
            mViewPager.setCurrentItem(2, true);
            return true;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mNavView.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                mNavView.setSelectedItemId(R.id.navigation_bbs);
                break;
            case 2:
                mNavView.setSelectedItemId(R.id.navigation_me);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
