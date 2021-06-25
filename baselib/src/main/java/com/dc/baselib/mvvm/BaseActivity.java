package com.dc.baselib.mvvm;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.dc.baselib.CurrentContentManager;
import com.dc.baselib.R;
import com.dc.baselib.callback.WinCallback;
import com.dc.baselib.constant.Constants;
import com.dc.baselib.statusBar.StarusBarUtils;
import com.dc.baselib.utils.SPUtils;
import com.dc.baselib.utils.TimeUtils;


public abstract class BaseActivity extends AppCompatActivity {


    private FrameLayout mFlcontiner;
    private Toolbar mToolBarlhead;
    protected LayoutInflater mInflater;
    private ImageView mIvLeftBack;
    private TextView tTvTitle;
    private ImageView mIvRightButton;
    private TextView mRightText;
    private View view_line;
    private TextView iv_left_cacel;
    private TextView tvLeftCacel;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(this);
        setContentView(R.layout.activity_base_layout);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        initStatusBar();
        tvLeftCacel = findViewById(R.id.tv_left_cacel);
        mFlcontiner = findViewById(R.id.fl_continer);
        view_line = findViewById(R.id.view_line);
        mToolBarlhead = findViewById(R.id.tool_bars);
        mIvLeftBack = findViewById(R.id.iv_left_back);
        tTvTitle = findViewById(R.id.tv_title);
        mIvRightButton = findViewById(R.id.iv_right_button);
        mRightText = findViewById(R.id.iv_right_text);
        setContentLayout(getLayout());
        initListener();
        initView(savedInstanceState);
        initData();
        Activity activity = CurrentContentManager.getmInstance().getActivity();
        if (activity == null) {
            return;
        }
        Window win = activity.getWindow();
        LogUtils.d("BaseActivity", "touchOnclick: activity=" + activity);
        win.setCallback(new WinCallback(win.getCallback()) {
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
//                        long l = System.currentTimeMillis();
//                        SPUtils.putLongData(getApplicationContext(), CURRENT_TIME, l);//最后触摸时间
//                        LogUtils.e("BaseActivity", "dispatchTouchEvent:activity窗口被触摸" + l);

                        break;
                    case MotionEvent.ACTION_UP:
                        long l = System.currentTimeMillis();
                        SPUtils.putLongData(getApplicationContext(), Constants.CURRENT_TIME, l);//最后触摸时间
                        LogUtils.d("BaseActivity", "dispatchTouchEvent最后触摸屏保时间" + TimeUtils.getDateToString(l));
                        break;

                }
                return super.dispatchTouchEvent(event);
            }
        });

    }

    protected abstract int getLayout();

    public void setTitle(String title) {
        if (tTvTitle != null) {
            tTvTitle.setText(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setBackShow(boolean isShow) {
        if (isShow) {
            mIvLeftBack.setVisibility(View.VISIBLE);
        } else {
            mIvLeftBack.setVisibility(View.GONE);
        }
    }
    @Override
    public Resources getResources() {
        boolean isHorizontalScreen = getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isHorizontalScreen)
            return AdaptScreenUtils.adaptWidth(super.getResources(), 1920);
        else
            return AdaptScreenUtils.adaptWidth(super.getResources(), 1080);

    }
    public void showLeftCacel() {
        tvLeftCacel.setVisibility(View.VISIBLE);
        mIvLeftBack.setVisibility(View.GONE);
    }

    public void setLeftBackImageResource(int resId) {
        mIvLeftBack.setImageResource(resId);
    }

    public void setTitleColor(int color) {
        tTvTitle.setTextColor(color);
    }

    public void setRightButtonListener(View.OnClickListener listener) {
        mIvRightButton.setOnClickListener(listener);
    }

    public void setmToolBarlheadBg(int color) {
        mToolBarlhead.setBackgroundColor(color);
    }

    public void setPaddingTop() {
        mToolBarlhead.setPadding(0, StarusBarUtils.getStatusBarHeight(this), 0, 0);
    }

    public void setToolBarlheadHide(boolean hide) {
        if (hide) {
            mToolBarlhead.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
        } else {
            mToolBarlhead.setVisibility(View.VISIBLE);
            view_line.setVisibility(View.VISIBLE);
        }
    }

    public void setToolBarLineOnly(boolean hide) {
        if (hide) {
            view_line.setVisibility(View.GONE);
        } else {

            view_line.setVisibility(View.VISIBLE);
        }
    }

    public void setRightTextListener(View.OnClickListener listener) {
        mRightText.setOnClickListener(listener);
    }

    public void setRightText(String rightText) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(rightText);
    }

    public void setRightTextViisiblty(boolean visible) {
        mRightText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setIvRightImageResource(int resId) {
        mIvRightButton.setVisibility(View.VISIBLE);
        mIvRightButton.setImageResource(resId);
    }

    public void setRightText(int rightText) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText(rightText);
    }

    public TextView getRightText() {
        return mRightText;
    }

    public void setRightTextEnable(boolean enable) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setEnabled(enable);
    }

    public void setRightTextColor(int color) {
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setTextColor(color);
    }

    public void setTitle(int resId) {
        if (tTvTitle != null) {
            tTvTitle.setText(resId);
        }
    }

    /**
     * 打开Activity
     */
    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void initListener() {
        mIvLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvLeftCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected abstract void initData();

    public void setContentLayout(int layoutId) {
        View rootView = mInflater.inflate(layoutId, mFlcontiner, false);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(layoutParams);
        mFlcontiner.addView(rootView);
    }


    /**
     * 处理状态栏，子类可以重写
     */
    protected void initStatusBar() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StarusBarUtils.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StarusBarUtils.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StarusBarUtils.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StarusBarUtils.setStatusBarColor(this, 0x55000000);
        }
//        setStatusColor(false, true, R.color.colorPrimary);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark));
    }

    /**
     * 初始化布局
     */
    protected abstract void initView(Bundle savedInstanceState);
    /*状态栏*/

    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏透明
     */
/*    private void setTranslucentStatus() {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }*/


}


