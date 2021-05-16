package com.dc.module_bbs.projshow;

import com.dc.commonlib.utils.ApiService;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IprojectService {
    @GET(ApiService.PROJECT_LIST + "/{project}")
    Flowable<ResponseBody> getItemProject(@Path("project") int project);
}
