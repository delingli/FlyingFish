package com.dc.module_home.homamain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dc.baselib.BaseApplication;
import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.dc.commonlib.common.MultiTypeSupport;
import com.dc.commonlib.commonentity.video.CameraType;
import com.dc.commonlib.commonentity.video.DisplayVideoPlayerManager;
import com.dc.commonlib.commonentity.video.VideoAccountBean;
import com.dc.commonlib.commonentity.video.VideoAccountInfoManager;
import com.dc.commonlib.commonentity.video.widget.DisplayMode;
import com.dc.commonlib.commonentity.video.widget.VideoDisplayView;
import com.dc.commonlib.utils.GlideUtils;
import com.dc.commonlib.utils.LogUtil;
import com.dc.commonlib.utils.video.PlayerManager;
import com.dc.module_home.R;

import java.util.List;

public class HomeMainAdapter extends BaseRecyclerAdapter<IAbsHomeItem> implements MultiTypeSupport<IAbsHomeItem> {


    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public HomeMainAdapter(Context context, @Nullable List<IAbsHomeItem> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
        this.multiTypeSupport = this;

    }

    private OnAreaItemClickListener onAreaItemClickListener;

    public void addOnAreaItemClickListener(OnAreaItemClickListener onAreaItemClickListener) {
        this.onAreaItemClickListener = onAreaItemClickListener;
    }

    public interface OnAreaItemClickListener {
        void onAreaItemClick(String areaAdress);
    }

    private OnVideoClickItemClickListener onVideoClickItemClickListener;

    public void addOnVideoClickItemClickListener(OnVideoClickItemClickListener onVideoClickItemClickListener) {
        this.onVideoClickItemClickListener = onVideoClickItemClickListener;
    }

    public interface OnVideoClickItemClickListener {
        void onVideoItemClick(String areaAdress);
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getTag() instanceof VideoDisplayView) {
            VideoDisplayView videoDisplayView = (VideoDisplayView) holder.getTag();
            videoDisplayView.onStop();
            videoDisplayView.release();
        }
//        onStop
    }

    @Override
    protected void convert(BaseViewHolder holder, IAbsHomeItem iAbsHomeItem, int position, List<Object> payloads) {
        if (iAbsHomeItem instanceof LabHomeItem) {
            LabHomeItem labHomeItem = (LabHomeItem) iAbsHomeItem;
            TextView tv_lab_title = holder.getView(R.id.tv_lab_title);
            tv_lab_title.setText(labHomeItem.title);
        } else if (iAbsHomeItem instanceof ProjectAreaHomeItem) {
            ProjectAreaHomeItem projectareahomeitem = (ProjectAreaHomeItem) iAbsHomeItem;
            RecyclerView recyclerView = holder.getView(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            recyclerView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            final HomeAreaAdapter homeAreaAdapter = new HomeAreaAdapter(getContext(), projectareahomeitem.projectAreas, -1);
            homeAreaAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClickListener(View v, int position) {
                    if (null != homeAreaAdapter.getList()) {
                        ProjectAreaHomeItem.ProjectAreaItems areaItem = homeAreaAdapter.getList().get(position);
                        if (null != onAreaItemClickListener&&areaItem.click) {
                            onAreaItemClickListener.onAreaItemClick(areaItem.projectAdress);
                        }
                    }

                }
            });
            recyclerView.setAdapter(homeAreaAdapter);
        } else if (iAbsHomeItem instanceof VideoMonitoringHomeItem) {
            final VideoMonitoringHomeItem videomonitoringhomeitem = (VideoMonitoringHomeItem) iAbsHomeItem;
            TextView tv_title = holder.getView(R.id.tv_title);
            final VideoDisplayView videodisplayview = holder.getView(R.id.videoPlayers);
            videodisplayview.addOnItemClickListener(new VideoDisplayView.OnItemClickListener() {
                @Override
                public void OnItemClick() {
                    if (null != videomonitoringhomeitem.listBean) {
                        LogUtil.d("LDL", "播放执行调用..");
                        videodisplayview.startPreview(videomonitoringhomeitem.listBean);
                    }
                }
            });
//            GlideUtils.loadUrl(getContext(), videomonitoringhomeitem.placeHolder, videodisplayview.getIv_placeholder());

   /*         if (holder.getTag() != null) {
                VideoDisplayView videoDisplayView = (VideoDisplayView) holder.getTag();
                videoDisplayView.onResume();
            }else {
                holder.setTag(videodisplayview);
                videodisplayview.setDisplayMode(DisplayMode.LIVE);
                if (null != videomonitoringhomeitem.listBean) {
                    LogUtil.d("LDL","播放执行调用..");
                    videodisplayview.startPreview(videomonitoringhomeitem.listBean);
                }
            }*/
            holder.setTag(videodisplayview);
            videodisplayview.setDisplayMode(DisplayMode.LIVE);
            if (null != videomonitoringhomeitem.listBean ) {
                if( videomonitoringhomeitem.isFirst){
                    LogUtil.d("LDL", "播放执行调用..");

                    videodisplayview.startPreview(videomonitoringhomeitem.listBean);
                }
            }
            tv_title.setText(videomonitoringhomeitem.name);
        } else if (iAbsHomeItem instanceof ProjectOverviewHomeItem) {
            ProjectOverviewHomeItem projectOverviewHomeItem = (ProjectOverviewHomeItem) iAbsHomeItem;
            TextView tv_proj_count = holder.getView(R.id.tv_proj_count);

            TextView tv_proj_count_desc = holder.getView(R.id.tv_proj_count_desc);

            TextView tv_work_count = holder.getView(R.id.tv_work_count);

            TextView tv_work_count_desc = holder.getView(R.id.tv_work_count_desc);

            TextView tv_no_work = holder.getView(R.id.tv_no_work);

            TextView tv_no_work_desc = holder.getView(R.id.tv_no_work_desc);

            tv_proj_count.setText(projectOverviewHomeItem.projectAll.projectCount + "");
            tv_proj_count_desc.setText(projectOverviewHomeItem.projectAll.projectTitle);
            tv_work_count.setText(projectOverviewHomeItem.projectUnderConstruction.projectCount + "");
            tv_work_count_desc.setText(projectOverviewHomeItem.projectUnderConstruction.projectTitle);
            tv_no_work.setText(projectOverviewHomeItem.noWorkProject.projectCount + "");
            tv_no_work_desc.setText(projectOverviewHomeItem.noWorkProject.projectTitle);
        }
    }

    @Override
    public int getLayoutId(IAbsHomeItem item, int position) {
        if (item instanceof LabHomeItem) {
            return R.layout.home_item_lab;
        } else if (item instanceof ProjectAreaHomeItem) {
            return R.layout.common_recycle_view;
        } else if (item instanceof VideoMonitoringHomeItem) {
            return R.layout.home_item_video_monitoring;
        } else if (item instanceof ProjectOverviewHomeItem) {
            return R.layout.home_item_top;
        }
        return 0;
    }
}
