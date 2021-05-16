package com.dc.module_main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dc.commonlib.weiget.courselist.DLCourseListView;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalItem;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalRecycleView;

import java.util.ArrayList;
import java.util.List;


public class TestActivity extends AppCompatActivity {
    List<DLHorizontalItem> ltest, topList;

    public static void startActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TestActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ltest = new ArrayList<>();
        topList = new ArrayList<>();
        DLCourseListView dLCourseListView = findViewById(R.id.dLCourseListView);

        for (int i = 0; i < 3; ++i) {
            DLHorizontalItem horizontalItem = new DLHorizontalItem();
            horizontalItem.name = i + "季文";
            topList.add(horizontalItem);
        }
        for (int i = 0; i < 20; ++i) {
            DLHorizontalItem horizontalItem = new DLHorizontalItem();
            horizontalItem.name = i + "季文";
            ltest.add(horizontalItem);
        }
        dLCourseListView.fillData(topList, ltest, 2, 3);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


    }
}
