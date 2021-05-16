package com.dc.module_bbs.projectlist;

import com.dc.baselib.http.response.HttpResponse;
import com.dc.commonlib.utils.ApiService;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IProjectListService {

    @GET(ApiService.PROJECT_LIST)
    Flowable<ResponseBody> getProjectList(@Query("page") int page,
                                          @Query("project__status") String project__status,
                                          @Query("region") String region
    );

    @GET(ApiService.PROJECT_LIST)
    Flowable<ResponseBody> getSearchProjectList(@Query("page") int page,
                                                @Query("project__status") String project__status,
                                                @Query("region") String region,
                                                @Query("search") String search
    );
}
