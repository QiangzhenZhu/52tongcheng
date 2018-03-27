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
import com.example.mylibrary.View.AvoidRepeatClick;
import com.example.mylibrary.View.CircleImageView;
import com.example.mylibrary.RecordAudioView.Constant.*;
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
public class QRecordAudioView extends RelativeLayout {
    private CircleImageView record;
    private RecordTimeView recordTimeTextView;
    private FrameLayout delete;
    private FrameLayout play;
    private TextView centerText;
    private BezierView bezierView;
    private RecordInterfaceListener aListener;
    private Context context;
    private final static int mixDp = 40;
    private final static int maxDp = 60;
    private int mixPx = 0;
    private int maxPx = 0;
    private SoundPool soundPool;
    private int music;
    private int[] playCoordinate;
    private int[] deleteCoordinate;
    private int[] location;
    private int proportionPlay = 0;
    private int proportionDelete = 0;
    private RecordStrategy mAudioRecorder;
    private long touchTime;
    private Toast toast = null;
    public QRecordAudioView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        context = mContext;
        LayoutInflater.from(context).inflate(R.layout.q_record_audio_view, this);
        initView();
    }

    private void initView() {
        mixPx = dip2px(mixDp);
        maxPx = dip2px(maxDp);
        record = (CircleImageView) findViewById(R.id.record);
        centerText = (TextView) findViewById(R.id.centerText);
        bezierView = (BezierView) findViewById(R.id.bezierView);
        recordTimeTextView = (RecordTimeView) findViewById(R.id.recordTimeTextView);
        delete = (FrameLayout) findViewById(R.id.delete);
        play = (FrameLayout) findViewById(R.id.play);
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = soundPool.load(context, R.raw.a, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        playCoordinate = new int[2];
        deleteCoordinate = new int[2];
        location = new int[2];
        mAudioRecorder = new AudioRecorder();
        mAudioRecorder.ready();
        Log.i("看", "set监听钱");

        record.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("看", "按下");
                        if (new Date().getTime() - touchTime < 1000) {
                            showTextToast("不要连续点击");
                        } else {
                            touchTime = new Date().getTime();
                            hideViewOnTounch();
                            if (aListener != null) {
                                Log.i("看时间", new Date().getTime() + "时间");
                                mAudioRecorder.ready();
                                Log.i("看", "开始录音");
                                mAudioRecorder.start();
                                aListener.OnStartRecord(record);
                                showAnimation();
                                recordTimeTextView.startTiming();
                                recordTimeTextView.refreshAnimation();
                            }

                            v.getLocationOnScreen(location);
                            play.getLocationOnScreen(playCoordinate);
                            delete.getLocationOnScreen(deleteCoordinate);
                            bezierView.setVisibility(VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("看", "移动");
                        Message message = handler.obtainMessage();
                        switch (LeftOrRight(v, event)) {
                            case Constant.right://在右边
                                handler.sendEmptyMessage(recoveryDeleteViewSeze);
                                proportionPlay = (playCoordinate[0] + mixPx / 2 - maxPx / 2 - location[0] - record.getWidth()) / (maxPx / 2 - mixPx / 2);
                                Log.i("看比例", "" + proportionPlay);
                                message.obj = (event.getRawX() - location[0] - v.getWidth()) / proportionPlay + mixPx / 2;
                                message.what = changePlayView;
                                handler.sendMessage(message);
                                if (isInPlayView(v, event)) {
                                    handler.sendEmptyMessage(changePlayViewBkToGray);
                                } else {
                                    handler.sendEmptyMessage(changePlayViewBkToCommon);
                                }
                                break;
                            case Constant.left://在左边
                                handler.sendEmptyMessage(recoveryPlayViewSeze);
                                proportionDelete = (location[0] - deleteCoordinate[0] - maxPx) / (maxPx / 2 - mixPx / 2);
                                message.obj = (location[0] - event.getRawX()) / proportionDelete + mixPx / 2;
                                message.what = changeDeleteView;
                                handler.sendMessage(message);
                                if (isInDeleteView(v, event)) {
                                    handler.sendEmptyMessage(changeDeleteViewBkToGray);
                                } else {
                                    handler.sendEmptyMessage(changeDeleteViewBkToCommon);
                                }
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context, "松手", Toast.LENGTH_SHORT).show();
                        Log.i("看", "拿开");

                        mAudioRecorder.stop();
                        bezierView.setVisibility(INVISIBLE);
                        recordTimeTextView.stopTiming();
                        recordTimeTextView.stopefreshAnimation();
                        handler.sendEmptyMessage(recoveryDeleteViewSeze);
                        handler.sendEmptyMessage(recoveryPlayViewSeze);
                        play.setBackgroundResource(R.drawable.touming);
                        delete.setBackgroundResource(R.drawable.touming);

                        centerText.setText("按住说话");
                        hideViewOnFinishTounch();
                        if (aListener != null) {
                            if (isInDeleteView(v, event)) {
                                aListener.OnDeleteRecordSeleted();
                                mAudioRecorder.deleteOldFile();
                                Log.i("看", "删除");
                            } else if (isInPlayView(v, event)) {
                                if (recordTimeTextView.getRecordedTime() < 1) {
                                    showTextToast("录音时间太短");
                                } else {
                                    aListener.OnPlayRecordSeleted(recordTimeTextView.getRecordedTime(),mAudioRecorder.getFilePath());
                                    MediaPlayer mediaPlayer = new MediaPlayer();
                                    Log.i("看",recordTimeTextView.getRecordedTime()+"");

                                    Log.i("看", "试听");
                                }

                            } else {
                                if (recordTimeTextView.getRecordedTime() < 1) {
                                    showTextToast("录音时间太短");
                                    mAudioRecorder.deleteOldFile();
                                } else {
                                    aListener.OnRecordFinish(recordTimeTextView.getRecordedTime(), mAudioRecorder.getFilePath(),mAudioRecorder.getRecordName());
                                }
                            }
                        }
                        break;
                }

                return true;

            }

        });

    }

    private boolean isInDeleteView(View v, MotionEvent event) {
        int[] a = new int[2];
        delete.getLocationOnScreen(a);
        if (event.getRawX() > a[0] && event.getRawX() < a[0] + delete.getWidth()) {
            if (event.getRawY() > a[1] && event.getRawY() < a[1] + delete.getHeight()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isInPlayView(View v, MotionEvent event) {
        int[] a = new int[2];
        play.getLocationOnScreen(a);
        if (event.getRawX() > a[0] && event.getRawX() < a[0] + play.getWidth()) {
            if (event.getRawY() > a[1] && event.getRawY() < a[1] + play.getHeight()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            initView();
        }
    }

    private void showCenterTextView(String showingText) {
        centerText.setVisibility(VISIBLE);
        recordTimeTextView.setVisibility(GONE);
        centerText.setText(showingText);
    }

    private void showCenterTimingView() {
        centerText.setVisibility(GONE);
        recordTimeTextView.setVisibility(VISIBLE);
        centerText.setText("按住说话");
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case changePlayView:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) play.getLayoutParams();
                    Float f = new Float((float) msg.obj);
                    layoutParams.height = (f.intValue()) * 2 > maxPx ? maxPx : (f.intValue()) * 2;
                    layoutParams.width = (f.intValue()) * 2 > maxPx ? maxPx : (f.intValue()) * 2;
                    play.setLayoutParams(layoutParams);
                    break;
                case changeDeleteView:
                    RelativeLayout.LayoutParams layoutParams0 = (RelativeLayout.LayoutParams) delete.getLayoutParams();
                    Float f0 = new Float((float) msg.obj);
                    layoutParams0.height = (f0.intValue()) * 2 > maxPx ? maxPx : (f0.intValue()) * 2;
                    layoutParams0.width = (f0.intValue()) * 2 > maxPx ? maxPx : (f0.intValue()) * 2;
                    delete.setLayoutParams(layoutParams0);
                    break;
                case recoveryDeleteViewSeze:
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) delete.getLayoutParams();
                    layoutParams1.height = mixPx;
                    layoutParams1.width = mixPx;
                    delete.setLayoutParams(layoutParams1);
                    break;
                case recoveryPlayViewSeze:
                    RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) play.getLayoutParams();
                    layoutParams2.height = mixPx;
                    layoutParams2.width = mixPx;
                    play.setLayoutParams(layoutParams2);
                    break;
                case changePlayViewBkToGray:
                    showCenterTextView("松手试听");
                    play.setBackgroundResource(R.drawable.gray);
                    break;
                case changeDeleteViewBkToGray:
                    showCenterTextView("松手取消发送");
                    delete.setBackgroundResource(R.drawable.gray);
                    break;
                case changePlayViewBkToCommon:
                    showCenterTimingView();
                    play.setBackgroundResource(R.drawable.touming);
                    break;
                case changeDeleteViewBkToCommon:
                    showCenterTimingView();
                    delete.setBackgroundResource(R.drawable.touming);
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

    private int LeftOrRight(View view, MotionEvent event) {
        if (event.getRawX() > location[0] + view.getWidth()) {
            return Constant.right;
        } else if (event.getRawX() < location[0]) {
            return Constant.left;
        } else {
            return Constant.other;
        }
    }

    private void hideViewOnTounch() {
        delete.setVisibility(VISIBLE);
        play.setVisibility(VISIBLE);
        recordTimeTextView.setVisibility(VISIBLE);
        centerText.setVisibility(GONE);
    }

    private void hideViewOnFinishTounch() {
        delete.setVisibility(GONE);
        play.setVisibility(GONE);
        recordTimeTextView.setVisibility(GONE);
        centerText.setVisibility(VISIBLE);
    }

    public void setRecordInterfaceListener(RecordInterfaceListener recordInterfaceListener) {
        this.aListener = recordInterfaceListener;
    }

    private int dip2px(float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
