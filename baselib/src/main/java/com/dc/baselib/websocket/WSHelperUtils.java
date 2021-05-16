package com.dc.baselib.websocket;
/**
 * WebSocket 接口帮助类
 * <p>
 * Created by ZhangKe on 2019/2/15.
 */
public class WSHelperUtils {

    private static final String TAG = "WSHelperUtils";

//    @SuppressWarnings("unchecked")
/*    public static <T> CommonSocketEntity<String> getEntityFromResponse(T data) {
        try {
            if (data instanceof CommonSocketEntity) {
                return (CommonSocketEntity<String>) data;
            }
        } catch (Exception e) {
            WSLogUtil.e(TAG, "getEntityFromResponse", e);
        }
        return null;
    }*/


/*    public static JSONObject getCommand(String path) {
        return getCommand(path, null);
    }*/

/*    public static JSONObject getCommand(String path, String unique) {
        JSONObject command = new JSONObject();
        command.put("path", path);
        if (!TextUtils.isEmpty(unique)) {
            command.put("unique", unique);
        }
        return command;
    }*/

    /*    *//**
     * 获取工资单详情列表
     *//*
    public static JSONObject getPayrollDetailList(int payrollId) {
*//*        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("payroll_id", payrollId);

        params.put("command", getCommand(Api.PAYROLL_DETAIL_LIST));
        params.put("parameters", parameters);
        return params;*//*
    }

    *//**
     * 添加工资单工人
     *//*
    public static JSONObject addWorkerToPayRoll(int payrollId,
                                                String payrollName,
                                                String payrollMonth,
                                                List<PaybillListEntity.ResultsBean> workerList) {
        JSONArray payrollDetailsFormArray = new JSONArray();
        for (PaybillListEntity.ResultsBean item : workerList) {
            JSONObject payrollDetailsForm = new JSONObject();
            payrollDetailsForm.put("pay_bill_id", item.getId());
            if (!TextUtils.isEmpty(item.getWagecard())) {
                payrollDetailsForm.put("acc_no", item.getWagecard());
            }
            payrollDetailsForm.put("acc_name", item.getRealname());
//            payrollDetailsForm.put("amount", 1);
            payrollDetailsForm.put("team", item.getTeam_name());
            payrollDetailsForm.put("phone", item.getPhone());
            double refAmount = 0;
            if (item.getAmount_ref() != null && !item.getAmount_ref().isEmpty()) {
                refAmount =
                        Observable.fromIterable(item.getAmount_ref())
                                .map(PaybillListEntity.ResultsBean.AmountRefBean::getAmount)
                                .reduce((d1, d2) -> d1 + d2)
                                .blockingGet();
            }
            payrollDetailsForm.put("ref_amount", refAmount);
            payrollDetailsFormArray.add(payrollDetailsForm);
        }
        return installAddOrUpdatePayroll(payrollId,
                payrollName,
                payrollMonth,
                payrollDetailsFormArray,
                null);
    }

    *//**
     * 更新工资单中的工人数据
     *//*
    public static JSONObject updateWorkerFromPayRoll(int payrollId,
                                                     String payrollName,
                                                     String payrollMonth,
                                                     List<PayrollDetailListEntity.PayRollDetailsBean> workerList) {
        JSONArray payrollDetailsFormArray = new JSONArray();
        for (PayrollDetailListEntity.PayRollDetailsBean item : workerList) {
            JSONObject payrollDetailsForm = new JSONObject();
            payrollDetailsForm.put("id", item.getId());
            payrollDetailsForm.put("pay_bill_id", item.getPay_bill_id());
            if (!TextUtils.isEmpty(item.getAcc_no())) {
                payrollDetailsForm.put("acc_no", item.getAcc_no());
            }
            if (!TextUtils.isEmpty(item.getBank_name())) {
                payrollDetailsForm.put("bank_name", item.getBank_name());
            }
            payrollDetailsForm.put("acc_name", item.getAcc_name());
            payrollDetailsForm.put("amount", item.getAmount());
            payrollDetailsForm.put("team", item.getTeam());
            payrollDetailsForm.put("phone", item.getPhone());
            double refAmount = 0;
            if (item.getRef_amount_detail() != null && !item.getRef_amount_detail().isEmpty()) {
                refAmount =
                        Observable.fromIterable(item.getRef_amount_detail())
                                .map(PayrollDetailListEntity.PayRollDetailsBean.RefAmountDetailBean::getAmount)
                                .reduce((d1, d2) -> d1 + d2)
                                .blockingGet();
            }
            payrollDetailsForm.put("ref_amount", refAmount);
            payrollDetailsFormArray.add(payrollDetailsForm);
        }
        return installAddOrUpdatePayroll(payrollId,
                payrollName,
                payrollMonth,
                payrollDetailsFormArray,
                null);
    }

    *//**
     * 删除工资单中的工人
     *//*
    public static JSONObject deleteWorkerFromPayRoll(int payrollId,
                                                     String payrollName,
                                                     String payrollMonth,
                                                     List<Integer> deleteList) {
        return installAddOrUpdatePayroll(payrollId,
                payrollName,
                payrollMonth,
                null,
                deleteList);
    }

    private static JSONObject installAddOrUpdatePayroll(int payrollId,
                                                        String payrollName,
                                                        String payrollMonth,
                                                        JSONArray payrollDetailsFormArray,
                                                        List<Integer> deleteList) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("payroll_id", payrollId);
        JSONObject payrollForm = new JSONObject();
        payrollForm.put("name", payrollName);
        payrollForm.put("month", payrollMonth);
        payrollForm.put("project_id", AppDataCache.getProjectBean().getId());
        //amount 与 num 字段可随意传值，后台会自动计算，不传可能表单验证会失败。
        payrollForm.put("amount", 1);

        if (payrollDetailsFormArray == null || payrollDetailsFormArray.isEmpty()) {
            payrollForm.put("num", 0);
        } else {
            payrollForm.put("num", payrollDetailsFormArray.size());
            parameters.put("payroll_details_form", payrollDetailsFormArray);
        }
        parameters.put("payroll_form", payrollForm);
        if (deleteList != null && !deleteList.isEmpty()) {
            parameters.put("payroll_details_delete_form", deleteList);
        }

        params.put("command", getCommand(Api.PAYROLL_ADD_UPDATE));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 获取自己的银行卡列表
     *//*
    public static JSONObject getSelfBankList(List<Integer> userId) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("user_id", userId);
        parameters.put("encrypt", false);

        params.put("command", getCommand(Api.BANK_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 获取注册页发送验证码接口
     *//*
    public static JSONObject getRegVerify(String phone,
                                          String randCaptchaKey,
                                          String captchaCode) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("username", phone);
        if (!TextUtils.isEmpty(randCaptchaKey) && !TextUtils.isEmpty(captchaCode)) {
            parameters.put("rand_captcha_key", randCaptchaKey);
            parameters.put("captcha_code", captchaCode);
        }

        params.put("command", getCommand(Api.REG_VERIFY));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 获取职员注册接口
     *//*
    public static JSONObject getEmployeeRegister(String realName,
                                                 int companyId,
                                                 String phone,
                                                 String code,
                                                 String password) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("real_name", realName);
        parameters.put("company_id", companyId);
        parameters.put("username", phone);
        parameters.put("code", code);
        parameters.put("password", password);

        params.put("command", getCommand(Api.EMPLOYEE_REG));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 重置密码模块需要的短信验证码
     *//*
    public static JSONObject sendVerifyWithResetPwd(String phone,
                                                    String randCaptchaKey,
                                                    String captchaCode) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("username", phone);
        if (!TextUtils.isEmpty(randCaptchaKey) && !TextUtils.isEmpty(captchaCode)) {
            parameters.put("rand_captcha_key", randCaptchaKey);
            parameters.put("captcha_code", captchaCode);
        }

        params.put("command", getCommand(Api.RESET_PWD_VERIFY));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 重置密码模块需要的短信验证码
     *//*
    public static JSONObject resetPassword(String phone,
                                           String code,
                                           String password) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("username", phone);
        parameters.put("password", password);
        parameters.put("code", code);

        params.put("command", getCommand(Api.RESET_PASSWORD));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 重置密码模块需要的短信验证码
     *//*
    public static JSONObject resetPhoneVerify(String phone,
                                              String randCaptchaKey,
                                              String captchaCode) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("username", phone);
        if (!TextUtils.isEmpty(randCaptchaKey) && !TextUtils.isEmpty(captchaCode)) {
            parameters.put("rand_captcha_key", randCaptchaKey);
            parameters.put("captcha_code", captchaCode);
        }

        params.put("command", getCommand(Api.RESET_PHONE_OLD_VERIFY));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 修改手机号
     *//*
    public static JSONObject changePhone(String oldPhone, String oldCode,
                                         String newPhone, String newCode) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("username", oldPhone);
        parameters.put("code", oldCode);
        parameters.put("new_username", newPhone);
        parameters.put("new_code", newCode);

        params.put("command", getCommand(Api.CHANGE_PHONE));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤日报列表
     *//*
    public static JSONObject attendResultList(String date,
                                              List<Integer> userIds,
                                              List<Integer> teamList,
                                              int page,
                                              int limit) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("project_id", AppDataCache.getProjectBean().getId());
        parameters.put("start_day", date);
        parameters.put("end_day", date);
        if (userIds != null && !userIds.isEmpty()) {
            parameters.put("user_id", userIds);
        }
        if (teamList != null && !teamList.isEmpty()) {
            parameters.put("team_id", teamList);
        }
        parameters.put("page", page);
        parameters.put("limit", limit);

        params.put("command", getCommand(Api.ATTEND_RESULT_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤进出记录
     *//*
    public static JSONObject attendanceInstanceList(String date,
                                                    List<Integer> userIds,
                                                    int contractId,
                                                    int page,
                                                    int limit) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("project_id", AppDataCache.getProjectBean().getId());
        parameters.put("start_day", date);
        parameters.put("end_day", date);
        if (contractId != -1) {
            parameters.put("contract_id", contractId);
        }
        parameters.put("user_id", userIds);
        parameters.put("page", page);
        parameters.put("limit", limit);

        params.put("command", getCommand(Api.ATTEND_INSTANCE_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤修改记录
     *//*
    public static JSONObject amendAttendRecordList(String date,
                                                   List<Integer> userIds,
                                                   int contractId,
                                                   int page,
                                                   int limit) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("start_day", date);
        parameters.put("end_day", date);
        parameters.put("contract_id", contractId);
        parameters.put("user_id", userIds);
        parameters.put("request_status", "完成");
        parameters.put("page", page);
        parameters.put("limit", limit);

        params.put("command", getCommand(Api.AMEND_RECORD_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 修改手机号模块-验证旧手机号验证码
     *//*
    public static JSONObject checkOldPhone(String phone, String code) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("username", phone);
        parameters.put("code", code);

        params.put("command", getCommand(Api.CHECK_OLD_PHONE));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤月报-工人月统计
     *//*
    public static JSONObject monthlyWorkerAttendList(String month,
                                                     String name,
                                                     int teamId,
                                                     int page,
                                                     int limit) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("month", month);
        if (!TextUtils.isEmpty(name)) {
            parameters.put("name", name);
        } else if (teamId != -1) {
            parameters.put("team_id", teamId);
        }
        parameters.put("page", page);
        parameters.put("limit", limit);
        parameters.put("project_id", AppDataCache.getProjectBean().getId());

        params.put("command", getCommand(Api.MONTHLY_WORKER_ATTEND_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤月报-职员月统计
     *//*
    public static JSONObject monthlyEmployeeAttendList(String year,
                                                       String month,
                                                       String name,
                                                       int page,
                                                       int limit) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("year", year);
        parameters.put("month", month);
        parameters.put("name", name);
        parameters.put("page", page);
        parameters.put("limit", limit);
        parameters.put("project_id", AppDataCache.getProjectBean().getId());

        params.put("command", getCommand(Api.MONTHLY_EMPLOYEE_ATTEND_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤月报-工人列表-日历
     *//*
    public static JSONObject monthlyWorkerAttendSheetDetail(int contractId,
                                                            String month) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("contract_id", contractId);
        parameters.put("project_id", AppDataCache.getProjectBean().getId());
        parameters.put("month", month);

        params.put("command", getCommand(Api.ATTEND_SHEET_DETAIL));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 考勤月报-职员月历
     *//*
    public static JSONObject monthlyEmployeeDetail(int userId,
                                                   String year,
                                                   String month) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("project_id", AppDataCache.getProjectBean().getId());
        parameters.put("user_id", userId);
        parameters.put("year", year);
        parameters.put("month", month);
        parameters.put("group_by_user", 1);

        params.put("command", getCommand(Api.EMPLOYEE_ATTEND_DETAIL));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 获取消息列表
     *//*
    public static JSONObject getMessageList(int page,
                                            int limit) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("page", page);
        parameters.put("limit", limit);

        params.put("command", getCommand(Api.MESSAGE_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 消息一键已读
     *//*
    public static JSONObject setAllMessageRead(String unique) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());

        params.put("command", getCommand(Api.MESSAGE_ALL_READ, unique));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 消息一键已读
     *//*
    public static JSONObject setMessageRead(String unique, List<Integer> idList) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("title_ids", idList);

        params.put("command", getCommand(Api.MESSAGE_ALL_READ, unique));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 获取未读消息数
     *//*
    public static JSONObject getUnreadMsgCount() {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());

        params.put("command", getCommand(Api.UNREAD_MSG_COUNT));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 读取消息内容
     *//*
    public static JSONObject readMsgContent(int msgId, String unique) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("title_id", msgId);

        params.put("command", getCommand(Api.READ_MSG_CONTENT, unique));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 工作流列表
     *//*
    public static JSONObject requestList(int requestId) {
        JSONObject params = new JSONObject();
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("request_id", requestId);
        parameters.put("limit", 1);
        parameters.put("page", 1);
        parameters.put("self", 1);

        params.put("command", getCommand(Api.REQUEST_LIST));
        params.put("parameters", parameters);
        return params;
    }

    *//**
     * 班组列表
     *//*
    public static JSONObject getTeamList() {
        JSONObject command = new JSONObject();
        command.put("path", Api.TEAM_LIST_PATH);
        JSONObject parameters = new JSONObject();

        parameters.put("sid", AppDataCache.getUserInfo().getSid());
        parameters.put("project_id", AppDataCache.getProjectBean().getId());
        parameters.put("flag", 1);

        JSONObject param = new JSONObject();
        param.put("command", command);
        param.put("parameters", parameters);
        return param;
    }

    *//**
     * 终止合同
     *//*
    public static JSONObject exitContract(int contractID) {
        JSONObject params = new JSONObject();
        JSONObject command = new JSONObject();
        JSONObject parameters = new JSONObject();

        command.put("path", Api.WORKER_CONTRACT_TERMINATE);

        parameters.put("contract_id", contractID);
        parameters.put("sid", AppDataCache.getUserInfo().getSid());

        params.put("command", command);
        params.put("parameters", parameters);
        return params;
    }*/
}
