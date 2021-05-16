package com.dc.commonlib.commonentity.video.player;

import com.dc.commonlib.utils.LogUtil;
import com.dc.commonlib.utils.video.PlayerManager;

public class VideoCloudControlManager {
    private static final String TAG = VideoCloudControlManager.class.getSimpleName();
    private static  String ACTION_START = "START";
    private static  String ACTION_STOP = "STOP";
    private String mCurrAction;
    private static class CloudControlManagerHolder {
        private static VideoCloudControlManager controlManager = new VideoCloudControlManager();
    }
    public static VideoCloudControlManager getInstance() {
        return CloudControlManagerHolder.controlManager;
    }

    public void sendCommand(int window, int command, String action) {
        this.mCurrAction = action;
        PlayerManager.getPlayerInstance().sendCommand(window, true, action, command, 2, 256, new PlayerManager.OnControlResultCallBack() {
            @Override
            public void onSuccess() {
                LogUtil.e(TAG, "PlayerManager ::: sendCommand :: onSuccess");
            }

            @Override
            public void onFailure() {
                LogUtil.e(TAG, "PlayerManager ::: sendCommand :: onFailure");
            }
        });
    }

    public interface CommandConstant {
        String ACTION_START = "START";
        String ACTION_STOP = "STOP";
        // 自动巡航
        int CUSTOM_CMD_AUTO = 29;
        // 云台转上
        int CUSTOM_CMD_UP = 21;
        // 云台转下
        int CUSTOM_CMD_DOWN = 22;
        // 云台转左
        int CUSTOM_CMD_LEFT = 23;
        // 云台转右
        int CUSTOM_CMD_RIGHT = 24;
        // 云台转左上
        int CUSTOM_CMD_LEFT_UP = 25;
        // 云台转右上
        int CUSTOM_CMD_RIGHT_UP = 26;
        // 云台转左下
        int CUSTOM_CMD_LEFT_DOWN= 27;
        // 云台转右下
        int CUSTOM_CMD_RIGHT_DOWN = 28;

    }
}
