package module_guangzhou_station.stationmain;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;

import org.jetbrains.annotations.NotNull;

public class StationMainViewModel extends AbsViewModel<StationMainRepository> {
    public StationMainViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    @Override
    protected StationMainRepository getRepository() {
        return null;
    }
}
