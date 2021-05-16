package com.dc.commonlib.utils.video;

import android.app.Application;

import com.dc.baselib.BaseApplication;
import com.dc.commonlib.commonentity.video.VideoAccountBean;
import com.dc.commonlib.commonentity.video.VideoAccountInfoManager;
import com.dc.commonlib.utils.LogUtil;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.consts.HttpConstants;
import com.hikvision.sdk.consts.SDKConstant;
import com.hikvision.sdk.net.bean.CustomRect;
import com.hikvision.sdk.net.bean.RootCtrlCenter;
import com.hikvision.sdk.net.bean.SubResourceNodeBean;
import com.hikvision.sdk.net.bean.SubResourceParam;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;

import java.util.List;

public class PlayerManager {
    private static final String TAG = PlayerManager.class.getSimpleName();
    private LoginResultCallBack callBack;

    private static class PlayerManagerHolder {
        private static PlayerManager playerManager = new PlayerManager();
    }

    private PlayerManager() {
    }

    public static PlayerManager getPlayerInstance() {
        return PlayerManagerHolder.playerManager;
    }

    /**
     * get video playback manager
     *
     * @return
     */
    public static VideoPlayBackManager getPlaybackManager() {
        return VideoPlayBackManager.getInstance();
    }

    public static void init(Application context) {
        MCRSDK.init();
        // 初始化RTSP
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
//        // 初始化语音对讲
//        TalkClientSDK.initLib();
        // SDK初始化
        VMSNetSDK.init(context);
    }

    public void login(VideoAccountBean videoAccountInfo, final LoginResultCallBack callBack) {
        this.callBack = callBack;
        if (videoAccountInfo == null) {
            return;
        }
        String macAddress = MacAddressUtils.getMacAddressVersion(BaseApplication.getsInstance());
        String videoAddress = HttpConstants.HTTPS + videoAccountInfo.getServerIp() + ":" + videoAccountInfo.getMobilePort();
        VMSNetSDK.getInstance().Login(videoAddress, videoAccountInfo.getLoginAccount(), videoAccountInfo.getPassword(), macAddress, new OnVMSNetSDKBusiness() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtil.e(TAG, "PlayerManager::: Login: Success");
//                getPlayerInstance().getRootControlCenter();
                //存进去
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                LogUtil.e(TAG, "PlayerManager::: Login: onFailure...");
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

        });
        LogUtil.e(TAG, "video account info : " + videoAccountInfo.getLoginAccount() + ", password: " + videoAccountInfo.getPassword() + ", macAddress: " + macAddress);
    }

//    private OnVMSNetSDKBusiness business = ;

    public void logOut() {
//        VMSNetSDK.getInstance().Logout(business);
    }

    /**
     * 获取根控制中心
     */
    private void getRootControlCenter() {
        VMSNetSDK.getInstance().getRootCtrlCenterInfo(VideoConfig.Root.CTRL_CURR_PAGE, SDKConstant.SysType.TYPE_VIDEO, VideoConfig.Root.CTRL_NUM_PER_PAGER, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                LogUtil.e(TAG, "PlayerManager:::getRootControlCenter: onFailure");
            }

            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                LogUtil.e(TAG, "PlayerManager:::getRootControlCenter: onSuccess");
                if (obj instanceof RootCtrlCenter) {
                    RootCtrlCenter rootCtrlCenter = (RootCtrlCenter) obj;
                    int parentNodeType = Integer.parseInt(rootCtrlCenter.getNodeType());
                    int parentId = ((RootCtrlCenter) obj).getId();
                    VideoConfig.Root.PARENT_NODE_TYPE = parentNodeType;
                    VideoConfig.Root.PARENT_ID = parentId;
                }
            }
        });
    }

    public void zoomLiveOpt(int window, boolean b, CustomRect customRect1, CustomRect customRect2) {
        VMSNetSDK.getInstance().zoomLiveOpt(window, b, customRect1, customRect2);

    }

    public void zoomPlayBackOpt(int window, boolean b, CustomRect customRect1, CustomRect customRect2) {
        VMSNetSDK.getInstance().zoomPlayBackOpt(window, b, customRect1, customRect2);
    }

    /**
     * 获取父节点资源列表
     *
     * @param parentNodeType 父节点类型
     * @param pId            父节点ID
     */
    public void getSubResourceList(int parentNodeType, int pId, final OnGetSubResourceListCallback callback) {
        VMSNetSDK.getInstance().getSubResourceList(VideoConfig.SubNode.CURR_PAGE, VideoConfig.SubNode.NUM_PER_PAGE, SDKConstant.SysType.TYPE_VIDEO, parentNodeType, String.valueOf(pId), new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                super.onFailure();
                LogUtil.e(TAG, "PlayerManager:::getSubResourceList: onFailure ");
                if (callback != null) {
                    callback.onFailure("onFailure", "onFailure");
                }
            }

            @Override
            public void onSuccess(Object obj) {
                super.onSuccess(obj);
                LogUtil.e(TAG, "PlayerManager:::getSubResourceList: onSuccess ");
                if (obj instanceof SubResourceParam) {
                    List<SubResourceNodeBean> list = ((SubResourceParam) obj).getNodeList();

                    if (list != null && list.size() > 0) {
                        SubResourceNodeBean subResourceNodeBean = list.get(0);
                        if (subResourceNodeBean.getNodeType() == SDKConstant.NodeType.TYPE_CAMERA_OR_DOOR) {
                            if (callback != null) {
                                callback.onSuccess(list);
                            }
                        } else {
                            getSubResourceList(subResourceNodeBean.getNodeType(), subResourceNodeBean.getId(), callback);
                        }
                    }
                }
            }
        });
    }

    /**
     * 云台控制命令
     *
     * @param window
     * @param isNormal
     * @param action
     * @param command
     * @param controlSpeed
     * @param presetIndex
     */
    public void sendCommand(int window, boolean isNormal, String action, int command, int controlSpeed, int presetIndex, final OnControlResultCallBack callBack) {
        VMSNetSDK.getInstance().sendPTZCtrlCommand(window, isNormal, action, command, controlSpeed, presetIndex, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                super.onFailure();
                LogUtil.e(TAG, "PlayerManager ::: sendCommand :: onFailure");
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtil.e(TAG, "PlayerManager ::: sendCommand :: onSuccess");
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }
        });
    }

    /**
     * 暂停播放
     */
    public void pausePlayBack(int playWindow) {
        VMSNetSDK.getInstance().pausePlayBackOpt(playWindow);
    }

    /**
     * 恢复播放
     */
    public void resumePlayBackOpt(int playWindow) {
        VMSNetSDK.getInstance().resumePlayBackOpt(playWindow);
    }

    public interface OnGetSubResourceListCallback {
        /**
         * 获取子节点数据列表成功
         *
         * @param list 子节点数据
         */
        void onSuccess(List<SubResourceNodeBean> list);

        /**
         * 获取失败
         *
         * @param errorCode 错误码
         * @param errorMsg  错误信息
         */
        void onFailure(String errorCode, String errorMsg);
    }

    public interface OnControlResultCallBack {
        /**
         * 云台控制成功
         */
        void onSuccess();

        /**
         * 云台控制失败
         */
        void onFailure();
    }

    public interface LoginResultCallBack {
        void onSuccess();

        void onFailure();
    }
}
