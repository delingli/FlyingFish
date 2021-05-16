package com.dc.module_home.homamain;

import android.content.Intent;
import android.text.TextUtils;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.http.newhttp.StrAbsHttpSubscriber;
import com.dc.baselib.mvvm.BaseRespository;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.utils.UserManager;
import com.dc.commonlib.commonentity.HomeBean;
import com.dc.commonlib.commonentity.video.CameraInfoListBean;
import com.dc.commonlib.commonentity.video.VideoAccountBean;
import com.dc.commonlib.commonentity.video.VideoAccountInfoManager;
import com.dc.commonlib.utils.JsonUtil;
import com.dc.commonlib.utils.LogUtil;
import com.dc.commonlib.utils.video.PlayerManager;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class HomeMainRespositorys extends BaseRespository {
    public String EVENT_KEY_RELEASE_FAILURE;
    public String EVENT_FINISHREFRESH;
    public String EVENT_VIDEOLIST, EVENT_LOGIN_SUCESS;

    public HomeMainRespositorys() {
        EVENT_KEY_RELEASE_FAILURE = EventUtils.getEventKey();
        EVENT_FINISHREFRESH = EventUtils.getEventKey();
        EVENT_LOGIN_SUCESS = EventUtils.getEventKey();
        EVENT_VIDEOLIST = EventUtils.getEventKey();

    }

    public OnVideoInfoCallBackListener onVideoInfoCallBackListener;

    public interface OnVideoInfoCallBackListener {
        void onVideoList(CameraInfoListBean.ListBean listBean);
    }

    int page = 1;

    /**
     * 获取视频列表
     *
     * @param projectId
     */
    public void getVideoListInfo(final int projectId, final OnVideoInfoCallBackListener onVideoInfoCallBackListener) {
        addDisposable(mRetrofit.create(IHomeMainService.class)
                .getVideoListInfo(projectId, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            CameraInfoListBean response = JsonUtil.fromJson(s, CameraInfoListBean.class);
                            LogUtil.e(TAG, "onSuccessResponse...response data .. " + response);
                            if (null != response && response.getList() != null && !response.getList().isEmpty()) {
                                CameraInfoListBean.ListBean listBean = response.getList().get(0);
                                listBean.setProjectId(projectId);
                                if (null != onVideoInfoCallBackListener) {
                                    onVideoInfoCallBackListener.onVideoList(listBean);
                                }
//                                postData(EVENT_VIDEOLIST, listBean);
                            } else {
                                postData(EVENT_FINISHREFRESH, "finsh");
                            }
                        } else {
                            postData(EVENT_FINISHREFRESH, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_FINISHREFRESH, "finsh");
                    }
                }));
    }

    public void loginAccount(VideoAccountBean videoAccountBean) {
        PlayerManager.getPlayerInstance().login(videoAccountBean, new PlayerManager.LoginResultCallBack() {
            @Override
            public void onSuccess() {
                //todo 刷新
                postData(EVENT_LOGIN_SUCESS, "sucess");
                //登陆了 更新
//            VideoAccountBean videoAccountInfo = VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId));
//            videoAccountInfo.setLogin(true);
//            VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId), videoAccountInfo);

//                                        getVideoListInfo(projectId);
            }

            @Override
            public void onFailure() {
          /*      VideoAccountBean videoAccountInfo = VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId));
                if (null != videoAccountInfo) {
                    videoAccountInfo.setLogin(false);
                    VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId), videoAccountInfo);

                }*/

            }
        });

    }

    public void getHkPlayerAccount(final int projectId) {
        addDisposable(mRetrofit.create(IHomeMainService.class)
                .getVideoLoginInfo(projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<VideoAccountBean> response = JsonUtil.fromJson(s, new TypeToken<List<VideoAccountBean>>() {
                            }.getType());
                            if (response == null
                                    || response.isEmpty()
                                    || (TextUtils.isEmpty(response.get(0).getLoginAccount()) && TextUtils.isEmpty(response.get(0).getPassword()))) {
//                                closeRoundProgressDialog();
//                                startActivity(new Intent(getActivity(), VideoIntroduceActivity.class));
                            } else {

                                VideoAccountBean videoAccountBean = response.get(0);
                                //保存
                                VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId), videoAccountBean);
                                loginAccount(videoAccountBean);
//                                PlayerManager.getPlayerInstance().login(videoAccountBean, new PlayerManager.LoginResultCallBack() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                        //登陆了 更新
//                                        VideoAccountBean videoAccountInfo = VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId));
//                                        videoAccountInfo.setLogin(true);
//                                        VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId), videoAccountInfo);
//
////                                        getVideoListInfo(projectId);
//                                    }
//
//                                    @Override
//                                    public void onFailure() {
//                                        VideoAccountBean videoAccountInfo = VideoAccountInfoManager.getInstance().getVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId));
//                                        if (null != videoAccountInfo) {
//                                            videoAccountInfo.setLogin(false);
//                                            VideoAccountInfoManager.getInstance().saveVideoAccountInfo(BaseApplication.getsInstance(), String.valueOf(projectId), videoAccountInfo);
//
//                                        }
//
//                                    }
//                                });
                            }

                        } else {
                            postData(EVENT_FINISHREFRESH, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_FINISHREFRESH, "finsh");
                    }
                }));

    }

    PlayerManager.LoginResultCallBack callBack = new PlayerManager.LoginResultCallBack() {
        @Override
        public void onSuccess() {
//            closeRoundProgressDialog();
//            Intent intent = new Intent(getActivity(), VideoCenterActivity.class);
//            intent.putExtra(INTENT_ARG_01, true);
//            startActivity(intent);
            // Toast.makeText(getActivity(),"海康登录回调成功",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure() {
//            closeRoundProgressDialog();
//            Intent intent = new Intent(getActivity(), VideoCenterActivity.class);
//            intent.putExtra(INTENT_ARG_01, false);
//            startActivity(intent);
            //Toast.makeText(getActivity(),"海康登录回调失败",Toast.LENGTH_LONG).show();
        }
    };

    public void toGetownerCompanyBoard(int company_id) {
        addDisposable(mRetrofit.create(IHomeMainService.class)
                .getownerCompanyBoard(company_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new StrAbsHttpSubscriber() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            HomeBean homebean = JsonUtil.fromJson(s, HomeBean.class);
                            conversionData(homebean);
                        } else {
                            postData(EVENT_FINISHREFRESH, "finsh");
                        }

                    }

                    @Override
                    public void onFailure(String msg, String code) {
//                        ToastUtils.showToast(msg);
                        postData(EVENT_FINISHREFRESH, "finsh");
                    }
                }));
    }


    private void conversionData(HomeBean homebean) {
        if (null != homebean) {
            List<IAbsHomeItem> list = new ArrayList<>();
            ProjectOverviewHomeItem projectOverviewHomeItem = new ProjectOverviewHomeItem();

            ProjectOverviewHomeItem.ProjectOverviewHomeItemBean allPro = new ProjectOverviewHomeItem.ProjectOverviewHomeItemBean();
            allPro.projectCount = homebean.project_cnt;
            allPro.projectTitle = "项目总数(个)";

            ProjectOverviewHomeItem.ProjectOverviewHomeItemBean projectUnderConstruction = new ProjectOverviewHomeItem.ProjectOverviewHomeItemBean();
            projectUnderConstruction.projectCount = homebean.construction_project_cnt;
            projectUnderConstruction.projectTitle = "在建(个)";

            ProjectOverviewHomeItem.ProjectOverviewHomeItemBean noWorkProject = new ProjectOverviewHomeItem.ProjectOverviewHomeItemBean();
            noWorkProject.projectCount = homebean.prepare_project_cnt;
            noWorkProject.projectTitle = "未开工(个)";


            projectOverviewHomeItem.projectAll = allPro;
            projectOverviewHomeItem.projectUnderConstruction = projectUnderConstruction;
            projectOverviewHomeItem.noWorkProject = noWorkProject;
            list.add(projectOverviewHomeItem);

            if (homebean.region_projects != null) {
                List<ProjectAreaHomeItem.ProjectAreaItems> llAreas = new ArrayList<>();
                if (null != homebean.region_projects.yinhu) {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects = homebean.region_projects.yinhu.get(1);
                    areaItems1.totalProjects = homebean.region_projects.yinhu.get(1)+homebean.region_projects.yinhu.get(0);
                    areaItems1.projectAdress = "银湖";
                    areaItems1.click=true;
                    llAreas.add(areaItems1);
                }else {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects =0;
                    areaItems1.totalProjects = 0;
                    areaItems1.projectAdress = "银湖";
                    areaItems1.click=false;
                    llAreas.add(areaItems1);
                }
                if (null != homebean.region_projects.xindeng) {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects = homebean.region_projects.xindeng.get(1);
                    areaItems1.totalProjects = homebean.region_projects.xindeng.get(1)+homebean.region_projects.xindeng.get(0);
                    areaItems1.projectAdress = "新登";
                    areaItems1.click=true;

                    llAreas.add(areaItems1);
                }else {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects =0;
                    areaItems1.totalProjects = 0;
                    areaItems1.projectAdress = "新登";
                    areaItems1.click=false;
                    llAreas.add(areaItems1);
                }
                if (null != homebean.region_projects.changkou) {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects = homebean.region_projects.changkou.get(1);
                    areaItems1.totalProjects = homebean.region_projects.changkou.get(1)+ homebean.region_projects.changkou.get(0);
                    areaItems1.projectAdress = "场口";
                    areaItems1.click=true;

                    llAreas.add(areaItems1);
                }else {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects =0;
                    areaItems1.totalProjects = 0;
                    areaItems1.projectAdress = "场口";
                    areaItems1.click=false;
                    llAreas.add(areaItems1);
                }

                if (null != homebean.region_projects.dongzhou) {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects = homebean.region_projects.dongzhou.get(1);
                    areaItems1.totalProjects = homebean.region_projects.dongzhou.get(1)+ homebean.region_projects.dongzhou.get(0);
                    areaItems1.projectAdress = "东洲";
                    areaItems1.click=true;
                    llAreas.add(areaItems1);
                }else {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects =0;
                    areaItems1.totalProjects = 0;
                    areaItems1.projectAdress = "东洲";
                    areaItems1.click=false;
                    llAreas.add(areaItems1);
                }

                if (null != homebean.region_projects.jinqiao) {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects = homebean.region_projects.jinqiao.get(1);
                    areaItems1.totalProjects = homebean.region_projects.jinqiao.get(1)+homebean.region_projects.jinqiao.get(0);
                    areaItems1.projectAdress = "金桥";
                    areaItems1.click=true;
                    llAreas.add(areaItems1);
                }else {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects =0;
                    areaItems1.totalProjects = 0;
                    areaItems1.projectAdress = "金桥";
                    areaItems1.click=false;
                    llAreas.add(areaItems1);
                }
                if (null != homebean.region_projects.lushan) {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects = homebean.region_projects.lushan.get(1);
                    areaItems1.totalProjects = homebean.region_projects.lushan.get(1)+homebean.region_projects.lushan.get(0);
                    areaItems1.projectAdress = "鹿山";
                    areaItems1.click=true;
                    llAreas.add(areaItems1);
                }else {
                    ProjectAreaHomeItem.ProjectAreaItems areaItems1 = new ProjectAreaHomeItem.ProjectAreaItems();
                    areaItems1.completedProjects =0;
                    areaItems1.totalProjects = 0;
                    areaItems1.projectAdress = "鹿山";
                    areaItems1.click=false;
                    llAreas.add(areaItems1);
                }
                ProjectAreaHomeItem areaHomeItem = new ProjectAreaHomeItem();
                areaHomeItem.projectAreas = llAreas;
                list.add(areaHomeItem);
            }

            LabHomeItem labHomeItem2 = new LabHomeItem();
            labHomeItem2.title = "视频监控";
            list.add(labHomeItem2);
            if (homebean.projects != null && !homebean.projects.isEmpty()) {

            }

            if (homebean.video_urls != null && !homebean.video_urls.isEmpty()) {
                VideoMonitoringHomeItem videoMonitoringHomeItem;
                int x = 0;
                for (HomeBean.VideoUrlsBean item : homebean.video_urls) {
                    x++;
                    videoMonitoringHomeItem = new VideoMonitoringHomeItem();
                    videoMonitoringHomeItem.name = item.name;
                    videoMonitoringHomeItem.projectId = item.project;
//                    videoMonitoringHomeItem.longitude = item.longitude;
                    videoMonitoringHomeItem.placeHolder = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3405795056,3874822847&fm=26&gp=0.jpg";
                    if (x == 1) {
                        videoMonitoringHomeItem.isFirst = true;
                    } else {
                        videoMonitoringHomeItem.isFirst = false;
                    }
                    list.add(videoMonitoringHomeItem);

                }

     /*       if (homebean.projects != null && !homebean.projects.isEmpty()) {
                VideoMonitoringHomeItem videoMonitoringHomeItem;
                for (HomeBean.ProjectsBean item : homebean.projects) {
                    videoMonitoringHomeItem = new VideoMonitoringHomeItem();
                    videoMonitoringHomeItem.name = item.name;
                    videoMonitoringHomeItem.projectId = item.id ;
                    videoMonitoringHomeItem.longitude = item.longitude;
                    list.add(videoMonitoringHomeItem);
                }
            }*/
                postData(EVENT_KEY_RELEASE_FAILURE, list);

            } else {
                postData(EVENT_FINISHREFRESH, "finish");
            }
        }
    }
}
