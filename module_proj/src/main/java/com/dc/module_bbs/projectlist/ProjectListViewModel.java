package com.dc.module_bbs.projectlist;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalItem;
import com.dc.module_bbs.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectListViewModel extends AbsViewModel<ProjectListRespository> {
    public ProjectListViewModel(@NonNull Application application) {
        super(application);
    }

    public void getProjectList(boolean refresh, final String project__status, String region) {
        mRepository.getProjectList(refresh, project__status, region);
    }

    public void getSearchProjectList(boolean refresh, final String project__status, String region, String search) {
        mRepository.getSearchProjectList(refresh, project__status, region, search);
    }

    public List<DLHorizontalItem> getAreaList() {
        String[] stringArray = getApplication().getResources().getStringArray(R.array.area_labs);
        String[] stringIds = getApplication().getResources().getStringArray(R.array.area_labs_ids);
        List<DLHorizontalItem> list = new ArrayList<>();
        DLHorizontalItem dlHorizontalItem1;
        for (int i = 0; i < stringArray.length; ++i) {
            dlHorizontalItem1 = new DLHorizontalItem();
            dlHorizontalItem1.id = stringIds[i];
            dlHorizontalItem1.name = stringArray[i];
            list.add(dlHorizontalItem1);
        }
        return list;
    }

    public DLHorizontalItem getDefaultArea() {
        String[] stringArray = getApplication().getResources().getStringArray(R.array.area_labs);
        String[] stringIds = getApplication().getResources().getStringArray(R.array.area_labs_ids);

        DLHorizontalItem dlHorizontalItem1 = new DLHorizontalItem();
        dlHorizontalItem1.name = stringArray[0];
        dlHorizontalItem1.id = stringIds[0];
        return dlHorizontalItem1;
    }

    public DLHorizontalItem getDefaultState() {
        String[] area_labs_status = getApplication().getResources().getStringArray(R.array.area_labs_status);
        String[] stringIds = getApplication().getResources().getStringArray(R.array.area_labs_status_ids);

        DLHorizontalItem dlHorizontalItem1 = new DLHorizontalItem();
        dlHorizontalItem1.name = area_labs_status[0];
        dlHorizontalItem1.id = stringIds[0];
        return dlHorizontalItem1;
    }

    public List<DLHorizontalItem> getAreaStateList() {
        String[] area_labs_status = getApplication().getResources().getStringArray(R.array.area_labs_status);
        String[] stringIds = getApplication().getResources().getStringArray(R.array.area_labs_status_ids);
        List<DLHorizontalItem> list = new ArrayList<>();
        DLHorizontalItem dlHorizontalItem1;
        for (int i = 0; i < area_labs_status.length; ++i) {
            dlHorizontalItem1 = new DLHorizontalItem();
            dlHorizontalItem1.id = stringIds[i];
            dlHorizontalItem1.name = area_labs_status[i];
            list.add(dlHorizontalItem1);
        }
        return list;
    }
}
