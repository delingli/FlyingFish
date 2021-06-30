package module_guangzhou_station.playinfo;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.viewpager.widget.ViewPager;

import com.dc.baselib.mvvm.AbsLifecycleActivity;

import java.util.List;

import module_guangzhou_station.R;

public class PlayInfoActivity extends AbsLifecycleActivity<PlayInfoViewModel> {
    public static String PLAYINFO_TAG = "playInfo_tag";

    private ViewPager mViewPager;
    private List<AbsPlayInfo> mPlayInfoList;
    private CustomPagerAdapter mCustomPagerAdapter;

    @Override
    protected Class<PlayInfoViewModel> getViewModel() {
        return PlayInfoViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_playinfo;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mViewPager = findViewById(R.id.viewPager);
        if (getIntent() != null) {
            mPlayInfoList = getIntent().getParcelableArrayListExtra(PLAYINFO_TAG);
        }
        mViewPager.setAdapter(mCustomPagerAdapter = new CustomPagerAdapter(this, mPlayInfoList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (null != mCustomPagerAdapter) {
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override

    protected void initData() {

    }
}
