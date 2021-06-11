package debug;

import com.dc.baselib.BaseApplication;

import org.qiyi.video.svg.Andromeda;

public class HomeApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Andromeda.init(getApplicationContext());
    }
}
