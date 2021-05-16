package com.dc.commonlib.utils.video;

import android.view.SurfaceView;

import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.utils.LogUtil;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.sdk.VMSNetSDK;
import com.hikvision.sdk.net.bean.CameraInfo;
import com.hikvision.sdk.net.bean.RecordInfo;
import com.hikvision.sdk.net.business.OnVMSNetSDKBusiness;

import java.util.Calendar;

/**
 * playback manger
 */
public class VideoPlayBackManager {
    private static final String TAG = VideoPlayBackManager.class.getSimpleName();
    private VideoPlayBackManager(){
    }
    private static class PlayBackManagerHolder {
        private static VideoPlayBackManager manager = new VideoPlayBackManager();
    }

    public static VideoPlayBackManager getInstance() {
        return PlayBackManagerHolder.manager;
    }

    public VMSNetSDK getPlayerSdk() {
        return VMSNetSDK.getInstance();
    }

    /**
     * 获取摄像头信息
     * @param cameraInfo
     * @param callBack
     */
    public void getCameraInfo(final CameraInfoListBean.ListBean cameraInfo, int window, final OnQueryCameraInfoCallBack callBack) {
        if (cameraInfo == null) {
            LogUtil.e(TAG, " getCameraInfo() onFailure....cameraInfo is null" );
            return;
        }
        VMSNetSDK.getInstance().getPlayBackCameraInfo(window, cameraInfo.getCameraUuid(), new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                LogUtil.e(TAG, " getCameraInfo() onFailure...." + cameraInfo.getCameraUuid());
                if (callBack != null) {
                    callBack.onFailure();
                }
            }
            @Override
            public void onSuccess(Object obj) {
                LogUtil.e(TAG, " getCameraInfo() onSuccess...cameraUuid is..." + cameraInfo.getCameraUuid());
                if (obj instanceof CameraInfo) {
                    if (callBack != null) {
                        callBack.onSuccess((CameraInfo) obj);
                    }
                }
            }
        });
    }

    /**
     * 查询录像片段信息
     */
    public void queryRecordSegment(CameraInfo cameraInfo, int window, Calendar startTime, Calendar endTime, int storageType, String guid, final OnQueryRecordSegmentCallBack callBack) {
        if (null == cameraInfo) {
            return;
        }

        VMSNetSDK.getInstance().queryRecordSegment(window, cameraInfo, startTime, endTime, storageType, guid, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof RecordInfo && callBack != null) {
                    callBack.onSuccess(((RecordInfo) obj));
                }
            }
        });
    }

    public void startPlayback(int playWindowOne, SurfaceView mDisplayView, final String segmentListPlayUrl, final Calendar startTime, final Calendar endTime, final OnStarPlayCallBack callBack) {
        VMSNetSDK.getInstance().startPlayBackOpt(playWindowOne, mDisplayView, segmentListPlayUrl, startTime, endTime, new OnVMSNetSDKBusiness() {
            @Override
            public void onFailure() {
                LogUtil.e(TAG, "startPlayBackOpt..onFailure..url..." + segmentListPlayUrl);
                LogUtil.e(TAG, "startPlayBackOpt..onFailure..startTime..." + startTime);
                LogUtil.e(TAG, "startPlayBackOpt..onFailure..endTime..." + endTime);
                if (callBack != null) {
                    callBack.onFailure();
                }
            }

            @Override
            public void onSuccess(Object obj) {
                LogUtil.e(TAG, "startPlayBackOpt....onSuccess..url..." + segmentListPlayUrl);

                if (callBack != null) {
                    callBack.onSuccess(obj);
                }
            }

            @Override
            public void onStatusCallback(int status) {
                LogUtil.e(TAG, "startPlayBackOpt....onStatusCallback...onStatusCallback.." + status);
                //录像片段回放结束
                if (status == RtspClient.RTSPCLIENT_MSG_PLAYBACK_FINISH) {
                    if (callBack != null) {
                        callBack.onFinish();
                    }
                }
            }
        });
    }

    /**
     * 查询camera信息
     */
    public interface OnQueryCameraInfoCallBack {
        void onFailure();
        void onSuccess(CameraInfo cameraInfo);
    }

    /**
     * 查询camera下的录像片段
     */
    public interface OnQueryRecordSegmentCallBack {
        void onFailure();
        void onSuccess(RecordInfo recordInfo);
    }

    public interface OnStarPlayCallBack {
        /**
         * 播放失败
         */
        void onFailure();

        /**
         * 播放成功
         * @param object
         */
        void onSuccess(Object object);

        /**
         * 播放结束
         */
        void onFinish();
    }



    public void release() {
        PlayBackManagerHolder.manager = null;
    }
}
