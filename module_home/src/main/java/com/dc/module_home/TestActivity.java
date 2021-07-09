package com.dc.module_home;


import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dc.module_home.marqueen.MarqueeFramelayout;
import com.dc.module_home.marqueen.SimpleMF;
import com.dc.module_home.marqueen.SimpleMarqueeView;
import com.dc.module_home.othermarqueen.MarqueeFrameLayouts;
import com.dc.module_home.othermarqueen.MarqueeView;

import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private SimpleMarqueeView marqueeView6;
    private final List<String> datas = Arrays.asList("离离原上草，一岁一枯荣。吃饱穿暖摩擦声ad啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊巴巴爸爸是是是","离离原上草，一岁一枯荣。");
    private MarqueeFramelayout marqueeframelayout;
    private MarqueeView mMarqueeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_test);
        mMarqueeView = findViewById(R.id.mMarqueeView);
        MarqueeFrameLayouts marqueeframelayoutz = findViewById(R.id.marqueeframelayoutz);
        marqueeframelayoutz.setText("依据赫兹接触强度计算理论，着重研究了圆柱滚子轴承内、外圈及滚动体的接触应力");
        marqueeframelayoutz.startScroll();

        marqueeView6 = findViewById(R.id.marqueeView6);
        marqueeframelayout = findViewById(R.id.marqueeframelayout);
//        marqueeBackgroundColor
        TextClock textclock=  findViewById(R.id.tv_time);
        final SimpleMF<String> marqueeFactory = new SimpleMF<>(this);
        marqueeFactory.setData(datas);
//        marqueeView6.setOnItemClickListener(onSimpleItemClickListener);
        marqueeView6.setMarqueeFactory(marqueeFactory);
        marqueeView6.startFlipping();
        marqueeframelayout.startMarquee(datas);

        mMarqueeView.setOnMargueeListener(new MarqueeView.OnMargueeListener() {
            @Override
            public void onRollOver() {
                Toast.makeText(TestActivity.this, "滚动完毕", Toast.LENGTH_LONG).show();
            }
        });

    }
}
