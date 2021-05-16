package com.dc.commonlib.weiget.courselist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.dc.commonlib.R;
import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.utils.UIUtils;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalItem;
import com.dc.commonlib.weiget.horizontalrecycle.DLHorizontalRecycleView;

import java.util.List;

public class DLCourseListView extends LinearLayout {

    private DLHorizontalRecycleView dl_top_horizontalRecycleView;
    private DLHorizontalRecycleView dl_bottom_horizontalRecycleView;
    private ImageView iv_open;
    private PopupWindow mPopupWindow;

    public DLCourseListView(Context context) {
        this(context, null);
    }

    public DLCourseListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);

    }

    public DLCourseListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public DLCourseListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.common_dl_course_list, this, true);
/*        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.Header);
        titleText=a.getString(R.styleable.Header_titleText);
        titleTextColor=a.getColor(R.styleable.Header_titleTextColor, Color.WHITE);
        titleTextSize=a.getDimension(R.styleable.Header_titleTextSize,20f);

        //回收资源，这一句必须调用
        a.recycle();*/

    }

    private int topPos = 0;
    private String bottomId;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取子控件
        dl_top_horizontalRecycleView = findViewById(R.id.dl_top_HorizontalRecycleView);
        dl_bottom_horizontalRecycleView = findViewById(R.id.dl_bottom_HorizontalRecycleView);
        iv_open = findViewById(R.id.iv_open);
        dl_top_horizontalRecycleView.addOnDLHorizontalRecycleViewListener(new DLHorizontalRecycleView.OnDLHorizontalRecycleViewListener() {
            @Override
            public void onItemClick(int pos, String id) {
                if (!TextUtils.isEmpty(id)) {
                    bottomId = id;
                }
                topPos = pos;
                if (null != ondlcourselistlistener) {
                    ondlcourselistlistener.onItemClick(topPos, bottomId);
                }
            }
        });
        iv_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onOpenClickListener) {
                    onOpenClickListener.onOpenClick();

                }
                showPopupWindow();
            }
        });
        dl_bottom_horizontalRecycleView.addOnDLHorizontalRecycleViewListener(new DLHorizontalRecycleView.OnDLHorizontalRecycleViewListener() {
            @Override
            public void onItemClick(int pos, String id) {
                if (!TextUtils.isEmpty(id)) {
                    bottomId = id;
                }
                if (null != ondlcourselistlistener) {
                    ondlcourselistlistener.onItemClick(pos, bottomId);
                }
            }
        });

    }

    public void fillData(List<DLHorizontalItem> topDataList, List<DLHorizontalItem> bottomDataList, int topPosition, int bottomPosition) {
        dl_top_horizontalRecycleView.fillData(topDataList, topPosition);
        dl_bottom_horizontalRecycleView.fillData(bottomDataList, bottomPosition);
        topPos = topPosition;
    }

    public interface OnOpenClickListener {
        void onOpenClick();
    }

    private OnDLCourseListListener ondlcourselistlistener;
    private OnOpenClickListener onOpenClickListener;

    public void addOnOpenClickListener(OnOpenClickListener onOpenClickListener) {
        this.onOpenClickListener = onOpenClickListener;
    }

    public void addOnDLCourseTopListListener(OnDLCourseListListener onDLCourseListListener) {
        this.ondlcourselistlistener = onDLCourseListListener;
    }

    public interface OnDLCourseListListener {
        void onItemClick(int topPos, String bottomId);
    }

    private DlPopupWindowAdapter mDlPopupWindowAdapter;

    private void showPopupWindow() {
        if (mPopupWindow != mPopupWindow && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.common_dl_course_popupwindow, null, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        recyclerView.setAdapter(mDlPopupWindowAdapter = new DlPopupWindowAdapter(getContext(), dl_bottom_horizontalRecycleView.getList(), -1));
        mDlPopupWindowAdapter.setSelection(dl_bottom_horizontalRecycleView.getSelectPosition());
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
//第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        mPopupWindow.showAsDropDown(dl_top_horizontalRecycleView, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
// 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
// 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
// window.showAtLocation(parent, gravity, x, y)

        mDlPopupWindowAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                dl_bottom_horizontalRecycleView.setSelection(position);
                if (mDlPopupWindowAdapter.getList() != null) {
                    DLHorizontalItem horizontalItem = mDlPopupWindowAdapter.getList().get(position);
                    if (!TextUtils.isEmpty(horizontalItem.id)) {
                        bottomId = horizontalItem.id;
                    }
                    if (null != ondlcourselistlistener) {
                        ondlcourselistlistener.onItemClick(topPos, bottomId);
                    }
                }
                mPopupWindow.dismiss();
            }
        });
    }


}
