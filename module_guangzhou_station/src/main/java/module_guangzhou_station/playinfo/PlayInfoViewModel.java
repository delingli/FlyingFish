package module_guangzhou_station.playinfo;

import android.app.Application;

import androidx.annotation.NonNull;

import com.dc.baselib.mvvm.AbsViewModel;

import org.jetbrains.annotations.NotNull;


public class PlayInfoViewModel extends AbsViewModel<PlayInfoRepository> {
    public PlayInfoViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    @Override
    protected PlayInfoRepository getRepository() {
        return null;
    }
}
