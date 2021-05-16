package com.dc.commonlib.weiget.bottomsheetlayout;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dc.baselib.BaseApplication;
import com.dc.baselib.utils.ToastUtils;
import com.dc.commonlib.R;
import com.dc.commonlib.utils.UIUtils;

public class BottomSheetLoyalBar {

    private View mRootView;
    private RichEditText mEditText;
    private Context mContext;


    private Button btn_release;
    private Button mBtnCommit;
    private BottomDialog mDialog;
    //    private EmojiView mEmojiView;

    private BottomSheetLoyalBar(Context context) {
        this.mContext = context;
    }

    public static BottomSheetLoyalBar delegation(Context context) {
        BottomSheetLoyalBar bar = new BottomSheetLoyalBar(context);
        bar.mRootView = LayoutInflater.from(context).inflate(R.layout.common_bottom_sheet_comment_bar, null, false);
        bar.initView();
        return bar;
    }

    private void initView() {
        mEditText = mRootView.findViewById(R.id.et_comment);
        btn_release = mRootView.findViewById(R.id.btn_release);
//        mTvRelease.setEnabled(false);
        //mDialog = new BottomDialog(mContext,false);
        mDialog = new BottomDialog(mContext, R.style.BottomSheetLoyalBar);
        mDialog.setContentView(mRootView);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                TDevice.closeKeyboard(mEditText);
                //mFrameLayout.setVisibility(View.GONE);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_release.setEnabled(s.length() > 0);
            }
        });
    }

    private OnDelectListener onDelectListener;

    public void addOnDeleteListener(OnDelectListener onDelectListener) {
        this.onDelectListener = onDelectListener;
    }

    public interface OnDelectListener {
        void onDeleteListener();
    }

    public void showEmoji() {
        //mSyncToTweetView.setText(R.string.tweet_publish_title);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void show(String hint) {
        mDialog.show();
        if (!TextUtils.isEmpty(hint) && !UIUtils.getString(BaseApplication.getsInstance(), R.string.write_comment).equals(hint)) {
            mEditText.setHint(hint + " ");
        }
        mRootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                TDevice.showSoftKeyboard(mEditText);
            }
        }, 50);
    }

    public void dismiss() {
        TDevice.closeKeyboard(mEditText);
        //清除所有
        mEditText.setText("");
        mDialog.dismiss();
    }

    public void seTextReleaseListener(View.OnClickListener listener) {
        btn_release.setOnClickListener(listener);
        //清理
    }

    public RichEditText getEditText() {
        return mEditText;
    }

    public String getCommentText() {
        String str = mEditText.getText().toString();
        return TextUtils.isEmpty(str) ? "" : str.trim();
    }
}
