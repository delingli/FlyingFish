package com.dc.commonlib.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.common.ConfigUtils;

public class ActivityUtils {
/*    public static void startActivity(String targetUrl) {
        if (UserManager.getInstance().isLogin()) {
            ARouter.getInstance().build(targetUrl).navigation();
        } else {
            ARouter.getInstance().build(ArounterManager.FORWARD_TO_LOGIN_URL).navigation();
        }
    }

    public static void startActivity(String targetUrl, Bundle bundle) {
        if (TextUtils.isEmpty(targetUrl)) {
            return;
        }
        Postcard postcard = ARouter.getInstance().build(targetUrl);
        if (bundle != null) {
            postcard.with(bundle);
        }
        postcard.navigation();
    }*/




        public static void switchTo(Activity activity, Class < ? extends Activity> targetActivity){
            switchTo(activity, new Intent(activity, targetActivity));
        }

        public static void switchTo (Activity activity, Intent intent){
            activity.startActivity(intent);
/*            if (UserManager.getInstance().isLogin()) {
                activity.startActivity(intent);
            } else {
                ARouter.getInstance().build(ArounterManager.FORWARD_TO_LOGIN_URL).navigation();
            }*/

        }

/*        public static void startPersonHomePageActivity (User user){
            if (user == null) {
                return;
            }
            Postcard postcard = ARouter.getInstance().build(ArounterManager.PERSONALHOMEPAGE_ACTIVITY_URL)
                    .withParcelable(CommonConstant.USER_KEY, user);
            if (!TextUtils.isEmpty(user.id) && user.id.equals(UserManager.getInstance().getUserId())) {
                postcard.withBoolean(CommonConstant.IS_MYSELF_KEY, true);
            }
            postcard.navigation();
        }*/
    }
