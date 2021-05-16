package com.dc.module_home.homamain;

import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.VideoAccountBean;
import com.dc.commonlib.utils.ApiService;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IHomeMainService {
    @GET(ApiService.OWNER_COMPANY_BOARD)
    Flowable<ResponseBody> getownerCompanyBoard(@Path("id") int id);

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
