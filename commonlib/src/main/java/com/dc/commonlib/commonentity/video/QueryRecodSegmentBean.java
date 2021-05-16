package com.dc.commonlib.commonentity.video;

import com.hikvision.sdk.net.bean.RecordSegment;

import java.io.Serializable;
import java.util.Calendar;

public class QueryRecodSegmentBean extends RecordSegment implements Serializable {

    public QueryRecodSegmentBean(Calendar beginTime, Calendar endTime, String playURL) {

    }
}
