package com.dc.module_bbs.projshow;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.commonlib.utils.ArounterManager;
import com.dc.module_bbs.R;
import com.dc.module_bbs.labordata.LaborDataActivity;
import com.dc.module_bbs.monitoringlist.MonitoringListActivity;
import com.dc.module_bbs.projectoverview.ProjectOverviewActivity;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.List;

/**
 * "项目展示
 */
@Route(path = ArounterManager.PROJ_SHOW_URL)
public class ProjectShowActivity extends AbsLifecycleActivity<ProjectShowViewModel> {
    public static String KEY_PROJECTID = "projectid";
    private RecyclerView mRecyclerView;
    @Autowired
    public int project;
    private ProjectShowAdapter mProjectShowAdapter;

    //    proj_show_projinfo
//    proj_show_projj_pie  proj_show_projprogress  proj_show_item_banner
    @Override
    protected int getLayout() {
        return R.layout.common_refresh_layout;
    }

    public static void startActivity(Context context, int projectId) {
        Intent intent = new Intent(context, ProjectShowActivity.class);
        intent.putExtra(KEY_PROJECTID, projectId);
        context.startActivity(intent);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            project = getIntent().getIntExtra(KEY_PROJECTID, 0);
        }
        setTitle(R.string.project_show);
        ARouter.getInstance().inject(this);
        SmartRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.white));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProjectShowAdapter = new ProjectShowAdapter(this, null, -1);
        mViewModel.getProjectList(project);
        mRecyclerView.setAdapter(mProjectShowAdapter);
        mProjectShowAdapter.addOnProjectColumnListener(new ProjectShowAdapter.OnProjectColumnListener() {
            @Override
            public void onProjOverviewClick(ProjectItemBean itemBean) {
                ProjectOverviewActivity.startActivity(ProjectShowActivity.this, itemBean);
            }

            @Override
            public void onVideoMonitoring(ProjectItemBean itemBean) {
                MonitoringListActivity.startActivity(ProjectShowActivity.this,itemBean.getProject());
            }

            @Override
            public void onLaborClick(ProjectItemBean itemBean) {
                LaborDataActivity.startActivity(ProjectShowActivity.this, itemBean.getProject());
            }

            @Override
            public void onTowerCraneMonitoringClick(ProjectItemBean itemBean) {

            }
        });
    }

    @Override
    protected void dataObserver() {
        super.dataObserver();
        registerSubscriber(mViewModel.mRepository.EVENT_SUCESS, List.class).observe(this, new Observer<List>() {
            @Override
            public void onChanged(@Nullable List sstr) {
                mProjectShowAdapter.setList(sstr);
            }
        });
//
    }

    @Override
    protected void initData() {

    }
/*

    private void initPieChart() {
        Description description = pieChart.getDescription();
        description.setText("Assets View"); //设置描述的文字
        pieChart.setHighlightPerTapEnabled(true); //设置piecahrt图表点击Item高亮是否可用
        pieChart.animateX(2000);
        initPieChartData();
        pieChart.setUsePercentValues(true);//设置使用百分比（后续有详细介绍）

        pieChart.setDrawEntryLabels(true); // 设置entry中的描述label是否画进饼状图中
        pieChart.setEntryLabelColor(Color.parseColor("#666666"));//设置该文字是的颜色
        pieChart.setEntryLabelTextSize(12f);//设置该文字的字体大小

        pieChart.setDrawHoleEnabled(true);//设置圆孔的显隐，也就是内圆
        pieChart.setHoleRadius(51f);//设置内圆的半径。外圆的半径好像是不能设置的，改变控件的宽度和高度，半径会自适应。
        pieChart.setHoleColor(Color.WHITE);//设置内圆的颜色
        pieChart.setDrawCenterText(true);//设置是否显示文字
        pieChart.setCenterText("0亿\n总投资额(亿)");//设置饼状图中心的文字
        pieChart.setCenterTextSize(16f);//设置文字的消息
        pieChart.setCenterTextColor(Color.parseColor("#333333"));//设置文字的颜色
        pieChart.setTransparentCircleRadius(25f);//设置内圆和外圆的一个交叉园的半径，这样会凸显内外部的空间
        pieChart.setTransparentCircleColor(Color.BLACK);//设置透明圆的颜色
        pieChart.setTransparentCircleAlpha(50);//设置透明圆你的透明度


        Legend legend = pieChart.getLegend();//图例
        legend.setEnabled(true);//是否显示
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//对齐
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);//对齐
        legend.setForm(Legend.LegendForm.DEFAULT);//设置图例的图形样式,默认为圆形
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);//设置图例的排列走向:vertacal相当于分行
        legend.setFormSize(6f);//设置图例的大小
        legend.setTextSize(8f);//设置图注的字体大小
        legend.setFormToTextSpace(4f);//设置图例到图注的距离

        legend.setDrawInside(true);//不是很清楚
        legend.setWordWrapEnabled(false);//设置图列换行(注意使用影响性能,仅适用legend位于图表下面)，我也不知道怎么用的
        legend.setTextColor(Color.BLACK);


    }

    private void initPieChartData() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(70f, "cash banlance : 1500"));
        pieEntries.add(new PieEntry(30f, "consumption banlance : 500"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, null);
        pieDataSet.setColors(Color.parseColor("#36b365"), Color.parseColor("#cfdef9"));
        pieDataSet.setSliceSpace(3f);//设置每块饼之间的空隙
        pieDataSet.setSelectionShift(10f);//点击某个饼时拉长的宽度

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLUE);
        pieChart.setCenterTextSize(16);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }*/
}
