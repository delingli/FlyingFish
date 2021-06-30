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
    protected void postData(Object eventKey, String tag, Object t) {
        LiveBus.getDefault().postEvent(eventKey, tag, t);
    }

    protected void postData(Object eventKey, Object t) {
        postData(eventKey, null, t);
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
