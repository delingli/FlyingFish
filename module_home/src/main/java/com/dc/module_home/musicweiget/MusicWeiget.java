package com.dc.module_home.musicweiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaSession2;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.dc.module_home.R;

public class MusicWeiget extends LinearLayout implements View.OnClickListener {

    private boolean autoplay;
    private LinearLayout mLlBg;
    private ImageView mIvLeft;
    private ImageView mIvPlay;
    private ImageView mIvRight;
    private ImageView mIvExplan;

    public MusicWeiget(Context context) {
        this(context, null);
    }

    public MusicWeiget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MusicWeiget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public MusicWeiget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.music_weiget_player, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MusicWeigetLayout);
        if (null != typedArray && typedArray.hasValue(R.styleable.MusicWeigetLayout_autoplay)) {
            autoplay = typedArray.getBoolean(R.styleable.MusicWeigetLayout_autoplay, false);
        }
        typedArray.recycle();
        mLlBg = findViewById(R.id.ll_bg);
        mIvLeft = findViewById(R.id.iv_left);
        mIvPlay = findViewById(R.id.iv_play);
        mIvRight = findViewById(R.id.iv_right);
        mIvExplan = findViewById(R.id.iv_explan);
        this.isAutoplay = autoplay;
        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);
        mIvExplan.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
    }

    public static int STATE_PLAYING = 1;
    public static int STATE_PAUSE = 2;
    public int CURRENT_STATE = STATE_PAUSE;

    private boolean isAutoplay = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                break;
            case R.id.iv_play: {
                if (CURRENT_STATE == STATE_PLAYING) {
                    CURRENT_STATE = STATE_PAUSE;
                } else {
                    CURRENT_STATE = STATE_PLAYING;
                }
                changeButton(CURRENT_STATE);
                toPlay();
            }
            break;
            case R.id.iv_right:
                break;
            case R.id.iv_explan:
                break;

        }
    }

    private void toPlay() {
    }

    private void changeButton(int current_state) {
        if (current_state == STATE_PAUSE) {
            mIvPlay.setImageResource(R.drawable.pauses);
        } else if (CURRENT_STATE == STATE_PLAYING) {
            mIvPlay.setImageResource(R.drawable.plays);
        }
    }
}
