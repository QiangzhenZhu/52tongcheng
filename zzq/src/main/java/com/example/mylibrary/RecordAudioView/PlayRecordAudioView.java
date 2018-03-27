package com.example.mylibrary.RecordAudioView;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.R;
import com.example.mylibrary.View.CircleImageView;
import com.example.mylibrary.record.AudioRecorder;
import com.example.mylibrary.record.RecordStrategy;

import java.io.IOException;
import java.util.Date;

import static com.example.mylibrary.RecordAudioView.Constant.changeDeleteView;
import static com.example.mylibrary.RecordAudioView.Constant.changeDeleteViewBkToCommon;
import static com.example.mylibrary.RecordAudioView.Constant.changeDeleteViewBkToGray;
import static com.example.mylibrary.RecordAudioView.Constant.changePlayView;
import static com.example.mylibrary.RecordAudioView.Constant.changePlayViewBkToCommon;
import static com.example.mylibrary.RecordAudioView.Constant.changePlayViewBkToGray;
import static com.example.mylibrary.RecordAudioView.Constant.recoveryDeleteViewSeze;
import static com.example.mylibrary.RecordAudioView.Constant.recoveryPlayViewSeze;


/**
 * Created by 赵自强 on 2017/8/23 ${Time}.
 * 这个类的用处
 */
public class PlayRecordAudioView extends RelativeLayout {
    private CircleImageView record;
    private RecordTimeView recordTimeTextView;

    private TextView send_view, cancle_view;
    private PlayRecordListener aListener;
    private Context context;
    private Toast toast = null;
    private int playType = 0;//播放状态0>暂停1>播放
    private long time;
    public PlayRecordAudioView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        context = mContext;
        LayoutInflater.from(context).inflate(R.layout.play_voice, this);
        initView();
    }

    private void initView() {

        record = (CircleImageView) findViewById(R.id.record);
        recordTimeTextView = (RecordTimeView) findViewById(R.id.recordTimeTextView);
        recordTimeTextView.setVisibility(VISIBLE);
        send_view = (TextView) findViewById(R.id.send);
        cancle_view = (TextView) findViewById(R.id.cancle);
        recordTimeTextView.setDownInterfaceListener(downTimeListener);
        recordTimeTextView.setTime(time);
        record.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aListener != null) {
                    Log.i("看", "开始播放");
                    if (playType == 0) {
                        playType = 1;
                        aListener.OnStartRecord();
                        showAnimation();
                        Log.i("看", time+"时间");
                        recordTimeTextView.DownTime(time);
                        recordTimeTextView.refreshAnimation();
                        record.setImageResource(R.drawable.btn_chact_record_stop);
                    } else {
                        playType = 0;
                        aListener.OnStopRecord();
                        recordTimeTextView.stopDownTime();
                        recordTimeTextView.stopefreshAnimation();
                        showAnimation();
                        record.setImageResource(R.drawable.btn_chact_record_play);
                    }
                }
            }
        });
        send_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                aListener.OnSendRecordSeleted();
            }
        });
        cancle_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                aListener.OnCancleRecordSeleted();
            }
        });
    }


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            initView();
        }
    }

    private void showCenterTextView(String showingText) {
        recordTimeTextView.setVisibility(GONE);
    }

    private void showCenterTimingView() {
        recordTimeTextView.setVisibility(VISIBLE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case changePlayViewBkToCommon:
                    showCenterTimingView();
                    break;
                case changeDeleteViewBkToCommon:
                    showCenterTimingView();
                    break;
            }
        }
    };

    private void showAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        animationSet.addAnimation(scaleAnimation);
        // soundPool.play(music, 1, 1, 0, 0, 1);
        record.startAnimation(animationSet);
    }

    public void setRecordTime(long time){
        this.time = time;

    }
    public void setRecordInterfaceListener(PlayRecordListener recordInterfaceListener) {
        this.aListener = recordInterfaceListener;
    }
    DownTimeListener downTimeListener = new DownTimeListener() {
        @Override
        public void OnStopDown() {
            playType = 0;
            recordTimeTextView.setTime(time);
            aListener.OnStopRecord();
            recordTimeTextView.stopDownTime();
            recordTimeTextView.stopefreshAnimation();
            showAnimation();
            record.setImageResource(R.drawable.btn_chact_record_play);
        }
    };
}
