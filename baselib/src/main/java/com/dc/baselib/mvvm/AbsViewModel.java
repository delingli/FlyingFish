package com.dc.baselib.mvvm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class AbsViewModel<T extends BaseRespository> extends AndroidViewModel {
    public T mRepository;

    //    private LiveData<HttpResponse<T>> liveDataSource;
    public AbsViewModel(@NonNull Application application) {
        super(application);
        mRepository = TUtil.getNewInstance(this, 0);//也可以留给子类New
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (null != mRepository) {
            mRepository.unDisposable();
        }
    }
}
