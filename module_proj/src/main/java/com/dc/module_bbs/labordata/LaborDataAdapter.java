package com.dc.module_bbs.labordata;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.BaseViewHolder;
import com.dc.commonlib.common.MultiTypeSupport;
import com.dc.commonlib.utils.MoneyUtils;
import com.dc.commonlib.weiget.HorizontalProgressBar;
import com.dc.module_bbs.R;

import java.util.List;

public class LaborDataAdapter extends BaseRecyclerAdapter<IAbsLaborData> implements MultiTypeSupport<IAbsLaborData> {
    /**
     * @param context
     * @param list
     * @param itemLayoutId
     */
    public LaborDataAdapter(Context context, @Nullable List<IAbsLaborData> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
        this.multiTypeSupport = this;
    }

    @Override
    protected void convert(BaseViewHolder holder, IAbsLaborData item, int position, List<Object> payloads) {
        if (item instanceof LaborDataItem) {
            TextView tv_supervision_unit = holder.getView(R.id.tv_supervision_unit);
            TextView tv_number_registered = holder.getView(R.id.tv_number_registered);
            HorizontalProgressBar proj_horizontalprogressbar = holder.getView(R.id.proj_horizontalprogressbar);
            LaborDataItem laborDataItem = (LaborDataItem) item;
            tv_supervision_unit.setText(laborDataItem.title + ":" + laborDataItem.allNumber + "人");
            tv_number_registered.setText(laborDataItem.attendanceNumber + "人");
            int progress = MoneyUtils.percentageInt(laborDataItem.allNumber , laborDataItem.attendanceNumber );
            proj_horizontalprogressbar.setProgress(progress);
            if (laborDataItem.isTeam) {
                proj_horizontalprogressbar.setBgColor(getContext().getResources().getColor(R.color.bg_color_cfdef9));
                proj_horizontalprogressbar.setProgressColor(getContext().getResources().getColor(R.color.bg_color_3a50f7));
            } else {
                proj_horizontalprogressbar.setBgColor(getContext().getResources().getColor(R.color.bg_color_cef7de));
                proj_horizontalprogressbar.setProgressColor(getContext().getResources().getColor(R.color.text_color_36b365));
            }
        } else if (item instanceof TabLaborDataItem) {
            TabLaborDataItem tabLaborDataItem = (TabLaborDataItem) item;
            TextView tv_rehist_number = holder.getView(R.id.tv_rehist_number);
            tv_rehist_number.setText(tabLaborDataItem.title);
        } else if (item instanceof RegisteredNumberLaborDataItem) {
            RegisteredNumberLaborDataItem registeredNumberLaborDataItem = (RegisteredNumberLaborDataItem) item;
            TextView tv_lab = holder.getView(R.id.tv_lab);
            TextView tv_number_registered = holder.getView(R.id.tv_number_registered);
            tv_lab.setText(registeredNumberLaborDataItem.title);
            tv_number_registered.setText(registeredNumberLaborDataItem.count + "人");
        }
    }

    @Override
    public int getLayoutId(IAbsLaborData item, int position) {
        if (item instanceof LaborDataItem) {
            return R.layout.proj_labordata_item_progress;
        } else if (item instanceof TabLaborDataItem) {
            return R.layout.proj_labordata_item_lab;
        } else if (item instanceof RegisteredNumberLaborDataItem) {
            return R.layout.proj_labordata_item;
        }
        return 0;
    }
}
