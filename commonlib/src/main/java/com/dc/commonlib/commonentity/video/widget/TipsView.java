package com.dc.commonlib.commonentity.video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dc.commonlib.R;

public class TipsView extends RelativeLayout implements View.OnClickListener {
    LinearLayout mNetLayout;

    LinearLayout mErrorLayout;

    TextView mPlayBtn;

    TextView mRefreshBtn;

    TextView mConnectState;

    TextView mErrorContent;
    private Type mCurrType;
    private onTipsOperatorClickListener mListener;

    public TipsView(Context context) {
        this(context, null);
    }

    public TipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_video_tips, this, true);
        mConnectState = inflate.findViewById(R.id.tv_connect);
        mErrorContent = inflate.findViewById(R.id.tv_error_content);
        mRefreshBtn = inflate.findViewById(R.id.tv_refresh);
        mNetLayout = inflate.findViewById(R.id.ll_tips_net);
        mErrorLayout = inflate.findViewById(R.id.ll_tips_error);
        mPlayBtn = inflate.findViewById(R.id.tv_continue_play);
        mRefreshBtn.setOnClickListener(this);
        mPlayBtn.setOnClickListener(this);
    }

    public void setShowType(Type showType) {
        this.mCurrType = showType;
        if (showType == Type.Connect) {
            showView(mConnectState);
        } else if (showType == Type.Error) {
            showView(mErrorLayout);
        } else if (showType == Type.Net) {
            showView(mNetLayout);
        }
    }

    private void showView(View view) {
        mConnectState.setVisibility(GONE);
        mNetLayout.setVisibility(GONE);
        mErrorLayout.setVisibility(GONE);
        view.setVisibility(VISIBLE);
    }

    public void setShowType(Type showType, String message) {
        this.mCurrType = showType;
        if (showType == Type.Connect) {
            showView(mConnectState);
            mConnectState.setText(message);
        } else if (showType == Type.Error) {
            showView(mErrorLayout);
            mErrorContent.setText(message);
        } else if (showType == Type.Net) {
            showView(mNetLayout);
        }
    }

    public void hideAll() {
        mNetLayout.setVisibility(GONE);
        mConnectState.setVisibility(GONE);
        mErrorLayout.setVisibility(GONE);
    }
    public boolean isShow(){
        if(mConnectState.getVisibility()==View.VISIBLE){
            return true;
        }
        if(mNetLayout.getVisibility()==View.VISIBLE){
            return true;
        }
        if(mErrorLayout.getVisibility()==View.VISIBLE){
            return true;
        }
        return false;
    }



    public boolean isErrorShowing() {
        return mErrorLayout.getVisibility() == VISIBLE;
    }
    public boolean isNetTipsShowing(){return mNetLayout.getVisibility() == VISIBLE;}
    public void hideNetTips() {
        mNetLayout.setVisibility(GONE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_refresh) {
            if (mListener != null) {
                mListener.onRefresh();
            }
        } else if (id == R.id.tv_continue_play) {
            if (mListener != null) {
                mListener.onContinue();
            }
        }
    }

    public interface onTipsOperatorClickListener {
        void onRefresh();

        void onContinue();
    }

    public void setOnTipsOperatorClickListener(onTipsOperatorClickListener listener) {
        this.mListener = listener;
    }

    public enum Type {
        Connect,
        Error,
        Net
    }
}
