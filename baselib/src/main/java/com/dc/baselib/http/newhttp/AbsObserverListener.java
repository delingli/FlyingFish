package com.dc.baselib.http.newhttp;


import androidx.annotation.Nullable;

import com.dc.baselib.http.exception.ApiException;
import com.dc.baselib.http.response.HttpResponse;

//统一监听,暂时不用
public abstract class AbsObserverListener<T> implements ObserverListener<T> {
    @Override
    public void onChanged(@Nullable HttpResponse<T> tHttpResponse) {
        if (tHttpResponse.getStatus().equals(StatusCode.SUCESSCODE)) {
            onSucess(tHttpResponse.getData());
        } else {
            onFial(new ApiException(tHttpResponse.getStatus(), tHttpResponse.getStatus()));
        }
    }
}
