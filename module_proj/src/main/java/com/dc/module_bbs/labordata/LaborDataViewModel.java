package com.dc.module_bbs.labordata;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dc.baselib.baseEntiry.User;
import com.dc.baselib.mvvm.AbsViewModel;
import com.dc.baselib.mvvm.EventUtils;
import com.dc.baselib.utils.JsonUtil;
import com.dc.baselib.utils.UserManager;
import com.dc.baselib.websocket.AbsWebSocketViewModel;
import com.dc.baselib.websocket.SocketResponse;
import com.dc.commonlib.common.WSAPI;
import com.zld.websocket.WebSocketHandler;
import com.zld.websocket.response.ErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaborDataViewModel extends AbsWebSocketViewModel<LaborDataRespository> {
    public String EVENT_TEAM;
    public String EVENT_WORKER;

    public LaborDataViewModel(@NonNull Application application) {
        super(application);
        EVENT_TEAM = EventUtils.getEventKey();
        EVENT_WORKER = EventUtils.getEventKey();
    }

    public static String UNIQUE_TEAM = "teampath";
    public static String UNIQUE_WORKER = "workerpath";

    @Override
    protected void onProcessedMessage(String path, SocketResponse<String> socketresponse) {
        if (null != socketresponse && TextUtils.equals(path, WSAPI.REALTIMEPEOPLENUM
        )) {
            if (TextUtils.equals(UNIQUE_TEAM, socketresponse.command.unique)) {
// todo 解
                TeamDataBean teamdatabean = JsonUtil.fromJson(socketresponse.data, TeamDataBean.class);
                conversionTeamDataBean(teamdatabean);
            } else if (TextUtils.equals(UNIQUE_WORKER, UNIQUE_WORKER)) {
                WokerDataBean wokerDataBean = JsonUtil.fromJson(socketresponse.data, WokerDataBean.class);
                conversionTWokerDataBean(wokerDataBean);

            }

        }
    }

    @Override
    protected void onErrorMessage(String path, SocketResponse<String> socketresponse) {

    }

    private void conversionTWokerDataBean(WokerDataBean wokerDataBean) {

        List<IAbsLaborData> ll = new ArrayList<>();
        if (wokerDataBean != null) {
            List<WokerDataBean.Actual> actualList = wokerDataBean.actual;
            List<WokerDataBean.TotalBean> totalList = wokerDataBean.total;
            List<LaborDataItem> laborDataItems = new ArrayList<>();
            if (totalList != null && !totalList.isEmpty()) {
                LaborDataItem laborDataItem;
                for (WokerDataBean.TotalBean totalBean : totalList) {
                    laborDataItem = new LaborDataItem();
                    laborDataItem.allNumber = totalBean.worktype_id__count;
                    laborDataItem.title = totalBean.worktype__name;
                    laborDataItem.id = totalBean.worktype_id;
                    laborDataItem.isTeam = false;
                    laborDataItems.add(laborDataItem);
                }

            }
            TabLaborDataItem tabLaborDa;
            tabLaborDa = new TabLaborDataItem();
            tabLaborDa.title = "工种出勤信息";
            ll.add(tabLaborDa);
            if (null != actualList) {
                for (WokerDataBean.Actual actual : actualList) {
                    for (LaborDataItem ld : laborDataItems) {
                        if (actual.worktype_id == ld.id) {
                            ld.attendanceNumber = actual.worktype_id__count;
                            break;
                        }

                    }
                }
            }


            if (null != laborDataItems) {
                ll.addAll(laborDataItems);
            }
            postData(EVENT_WORKER, ll);
        }

    }

    private void conversionTeamDataBean(TeamDataBean teamdatabean) {
        List<IAbsLaborData> ll = new ArrayList<>();
        if (teamdatabean != null) {
            List<TeamDataBean.Actual> actualList = teamdatabean.actual;
            List<TeamDataBean.TotalBean> totalList = teamdatabean.total;
            RegisteredNumberLaborDataItem registeredNumberLaborDataItem = new RegisteredNumberLaborDataItem();
            registeredNumberLaborDataItem.title = "在册人数";


            List<LaborDataItem> laborDataItems = new ArrayList<>();
            if (totalList != null && !totalList.isEmpty()) {
                int x = 0;
                LaborDataItem laborDataItem;
                for (TeamDataBean.TotalBean totalBean : totalList) {
                    x = x + totalBean.team_id__count;
                    laborDataItem = new LaborDataItem();
                    laborDataItem.allNumber = totalBean.team_id__count;
                    laborDataItem.title = totalBean.team__name;
                    laborDataItem.id = totalBean.team_id;
                    laborDataItem.isTeam = true;
                    laborDataItems.add(laborDataItem);
                }
                registeredNumberLaborDataItem.count = x + "";

            } else {
                registeredNumberLaborDataItem.count = 0 + "";

            }
            ll.add(registeredNumberLaborDataItem);
            TabLaborDataItem tabLaborDa;
            tabLaborDa = new TabLaborDataItem();
            tabLaborDa.title = "班组出勤信息";
            ll.add(tabLaborDa);
            if (null != actualList) {
                for (TeamDataBean.Actual actual : actualList) {
                    for (LaborDataItem ld : laborDataItems) {
                        if (actual.team_id == ld.id) {
                            ld.attendanceNumber = actual.team_id__count;
                        }

                    }
                }
            }


            if (null != laborDataItems) {
                ll.addAll(laborDataItems);
            }
            postData(EVENT_TEAM, ll);
        }
    }


    public void toGetWorkerCount(int project_id) {
        try {
            JSONObject params = new JSONObject();
            JSONObject command = new JSONObject();
            JSONObject parameters = new JSONObject();
            command.put("path", WSAPI.REALTIMEPEOPLENUM);
            command.put("unique", UNIQUE_WORKER);
            if (UserManager.getInstance().getUserInfo(getApplication()) != null) {
                String sid = UserManager.getInstance().getToken();
                parameters.put("sid", sid);
            }
            parameters.put("project_id", project_id);
            parameters.put("statistics_type", "worktype");
            params.put("command", command);

            params.put("parameters", parameters);
            WebSocketHandler.getDefault().send(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void toGetTeamCount(int project_id) {
        try {
            JSONObject params = new JSONObject();
            JSONObject command = new JSONObject();
            JSONObject parameters = new JSONObject();
            command.put("path", WSAPI.REALTIMEPEOPLENUM);
            command.put("unique", UNIQUE_TEAM);
            if (UserManager.getInstance().getUserInfo(getApplication()) != null) {
                String sid = UserManager.getInstance().getToken();
                parameters.put("sid", sid);
            }
            parameters.put("project_id", project_id);
            parameters.put("statistics_type", "team");
            params.put("command", command);

            params.put("parameters", parameters);
            WebSocketHandler.getDefault().send(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onSendDataError(ErrorResponse error) {

    }
}
