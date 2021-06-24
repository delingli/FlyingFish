package com.dc.baselib.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class AbsViewModel<T extends BaseRespository> extends AndroidViewModel {
    public T mRepository;

    //    private LiveData<HttpResponse<T>> liveDataSource;
    public AbsViewModel(@NonNull Application application) {
        super(application);
        mRepository = getRepository();
    }

    protected abstract T getRepository();

    @Override
    protected void onCleared() {
        super.onCleared();
        if (null != mRepository) {
            mRepository.unDisposable();
        }
    }
}
