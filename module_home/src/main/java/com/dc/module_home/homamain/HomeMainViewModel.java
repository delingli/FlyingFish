package com.dc.module_home.homamain;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.commonentity.video.VideoAccountBean;

public class HomeMainViewModel extends AbsViewModel<HomeMainRespositorys> {
    public HomeMainViewModel(@NonNull Application application) {
        super(application);
    }

    public void toGetownerCompanyBoard() {
        if (UserManager.getInstance().isLogin()) {
            User userInfo = UserManager.getInstance().getUserInfo(getApplication());
            if (userInfo != null) {
                mRepository.toGetownerCompanyBoard(userInfo.company_id);
            }
        }
    }


    /**
     * 获取账号密码
     */
    public void getHkPlayerAccount(int projectId) {
        mRepository.getHkPlayerAccount(projectId);
    }
    public void loginAccount(VideoAccountBean videoAccountBean){
        mRepository.loginAccount(videoAccountBean);
    }
    public void getVideoListInfo(int projectId, HomeMainRespositorys.OnVideoInfoCallBackListener onVideoInfoCallBackListener) {
        mRepository.getVideoListInfo(projectId,onVideoInfoCallBackListener);
    }
}
