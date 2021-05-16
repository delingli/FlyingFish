package com.dc.module_main.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.baselib.statusBar.StarusBarUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.dialog.PermisionDialog;
import com.dc.module_main.MainActivity;
import com.dc.module_main.R;
import com.dc.module_main.login.LoginActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.lang.ref.WeakReference;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AbsLifecycleActivity<SplashViewModel> implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static int finalDelayMillis = 1000;
    private int RC_READ_AND_WRITE = 2;
    private String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected int getLayout() {
        return R.layout.main_splash;
    }

    private PostHandler mPostHandler = new PostHandler(this);

    @Override
    protected void initData() {

    }

    ////申请成功时调用
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//： 申请成功就直接做该做的事就行
        mPostHandler.postDelayed(new BetterRunnable(), finalDelayMillis);
    }

    //我们点了不在询问并拒绝，会弹出对话框，告诉用户这个权限时干嘛的
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // This will display a dialog directing them to enable the permission in app settings.
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms) {
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
//        if (requestCode == RC_READ_AND_WRITE) {
//            Toast.makeText(this, "已拒绝WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限" + perms.get(0), Toast.LENGTH_SHORT).show();
//        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问", Toast.LENGTH_SHORT).show();
            new AppSettingsDialog
                    .Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("是")
                    .setNegativeButton("否")
                    .build()
                    .show();
        } else {
            Toast.makeText(this, "已拒绝WRITE_EXTERNAL_STORAGE和WRITE_EXTERNAL_STORAGE权限" + perms.get(0), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            if (EasyPermissions.hasPermissions(this, perms)) {
//已经申请过权限，做想做的事
                mPostHandler.postDelayed(new BetterRunnable(), finalDelayMillis);
            } else {
                finish();
            }
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }

    private static class PostHandler extends Handler {
        private final WeakReference<Activity> weakReference;

        public PostHandler(Activity activity) {
            this.weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() != null) {

            } else {
            }
        }
    }

    private class BetterRunnable implements Runnable {

        @Override
        public void run() {
            if (UserManager.getInstance().isLogin()) {
                MainActivity.startActivity(SplashActivity.this);
            } else {
                LoginActivity.startActivity(SplashActivity.this);

            }
            finish();
        }
    }

    private void applyForPermission() {
        AndPermission.with(this).runtime().permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {//统一进去授权成功
                    @Override
                    public void onAction(List<String> data) {
                        mPostHandler.postDelayed(new BetterRunnable(), finalDelayMillis);

                    }
                }).onDenied(new Action<List<String>>() {
            //权限拒绝
            @Override
            public void onAction(List<String> data) {
                //不同意怎么办？
                if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, Permission.Group.STORAGE)) {
                    // 打开权限设置页
                    showPermissionDialog();
                } else {
                    finish();
                }
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void showPermissionDialog() {
        final PermisionDialog permisionDialog = new PermisionDialog();
        permisionDialog.addOnSettingClickListener(new PermisionDialog.OnSettingClickListener() {
            @Override
            public void onSettingClick() {
                startActivity(getAppDetailSettingIntent());
                permisionDialog.dismiss();
                finish();
            }

            @Override
            public void onCacelClick() {
                finish();
            }
        });
        permisionDialog.show(getSupportFragmentManager(), "permissionDialog");
    }

    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setmToolBarlheadHide(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        StarusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));
//        applyForPermission();
        if (EasyPermissions.hasPermissions(this, perms)) {
//已经申请过权限，做想做的事
            mPostHandler.postDelayed(new BetterRunnable(), finalDelayMillis);
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, getString(R.string.tip_read_write),
                    RC_READ_AND_WRITE, perms);
        }

    }

    @Override
    protected void onDestroy() {
        mPostHandler.removeCallbacksAndMessages(null);
        mPostHandler = null;
        super.onDestroy();
    }
}
