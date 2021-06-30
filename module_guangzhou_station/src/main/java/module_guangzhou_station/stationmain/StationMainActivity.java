package module_guangzhou_station.stationmain;

import com.dc.baselib.mvvm.AbsLifecycleActivity;

import module_guangzhou_station.R;

public class StationMainActivity extends AbsLifecycleActivity<StationMainViewModel> {
    @Override
    protected Class<StationMainViewModel> getViewModel() {
        return StationMainViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_stationmain;
    }

    @Override
    protected void initData() {

    }
}
