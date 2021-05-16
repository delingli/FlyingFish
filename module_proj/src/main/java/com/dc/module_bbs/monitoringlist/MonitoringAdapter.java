package com.dc.module_bbs.monitoringlist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.widget.DisplayMode;
import com.dc.commonlib.commonentity.video.widget.VideoDisplayView;
import com.dc.module_bbs.R;

import java.util.List;

public class MonitoringAdapter extends BaseRecyclerAdapter<CameraInfoListBean.ListBean> {
    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public MonitoringAdapter(Context context, @Nullable List<CameraInfoListBean.ListBean> list, int itemLayoutId) {
        super(context, list, R.layout.proj_video_list_monitor);
    }

    @Override
    protected void convert(BaseViewHolder holder, CameraInfoListBean.ListBean listBean, int position, List<Object> payloads) {
        if (listBean != null) {
            VideoDisplayView videoPlayers = holder.getView(R.id.videoPlayers);
            TextView tv_name = holder.getView(R.id.tv_name);
            tv_name.setText(listBean.getCameraName());
            videoPlayers.setDisplayMode(DisplayMode.LIVE);
            videoPlayers.startPreview(listBean);

        }
    }
}
