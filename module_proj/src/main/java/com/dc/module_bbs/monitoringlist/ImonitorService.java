package com.dc.module_bbs.monitoringlist;

import com.dc.commonlib.utils.ApiService;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImonitorService {
    /**
     * 视频中心---用户登录
     *
     * @param projectId 工程id
     */
    @GET(ApiService.SURVEILLANCE_USER)
    Flowable<ResponseBody> getVideoLoginInfo(@Query("project_id") int projectId);


    /**
     * 视频中心---获取摄像头列表
     *
     * @param projectId 工程id
     */
    @GET(ApiService.SURVEILLANCE_CAMERAS)
    Flowable<ResponseBody> getVideoListInfo(
            @Query("project_id") int projectId,
            @Query("page") int page,
            @Query("page_size") int pageSize);
}
