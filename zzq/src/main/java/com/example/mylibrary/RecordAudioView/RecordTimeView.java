package com.example.mylibrary.RecordAudioView;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵自强 on 2017/8/23 ${Time}.
 * 这个类的用处
 */

public class RecordTimeView extends RelativeLayout {
    private ImageView a1;
    private ImageView a2;
    private ImageView a3;
    private ImageView a4;
    private ImageView a5;
    private ImageView a6;
    private ImageView a7;
    private ImageView a8;
    private ImageView a9;
    private ImageView a10;
    private TextView timeView;
    private ImageView a11;
    private ImageView a12;
    private ImageView a13;
    private ImageView a14;
    private ImageView a15;
    private ImageView a16;
    private ImageView a17;
    private ImageView a18;
    private ImageView a19;
    private ImageView a20;
    private Context context;
    private int showPositionLeft = 9;
    private int showPositionRight = 10;
    private final static int startTime = 0;
    private int recordedTime = startTime;
    private CountDownTimer timer;
    private Handler handler = new Handler();
    private List<ImageView> list;
    private Runnable recordTime = new Runnable() {
        @Override
        public void run() {
            timeView.setText(showTime(++recordedTime));
            handler.postDelayed(this, 1000);
        }
    };
    private DownTimeListener aListener;
    public int getRecordedTime() {
        return recordedTime;
    }

    private Runnable Animation = new Runnable() {
        @Override
        public void run() {
           if (showPositionLeft == 9){
               list.get(0).setImageResource(R.drawable.yin_liang_view_xiao);
               list.get(9).setImageResource(R.drawable.yin_liang_view_da);
               showPositionLeft--;
           }else if (showPositionLeft ==0){
               list.get(showPositionLeft).setImageResource(R.drawable.yin_liang_view_da);
               list.get(showPositionLeft+1).setImageResource(R.drawable.yin_liang_view_xiao);
               showPositionLeft = 9;
           }else {
               list.get(showPositionLeft).setImageResource(R.drawable.yin_liang_view_da);
               list.get(showPositionLeft+1).setImageResource(R.drawable.yin_liang_view_xiao);
                showPositionLeft--;
           }
           if (showPositionRight ==10){
               list.get(19).setImageResource(R.drawable.yin_liang_view_xiao);
               list.get(10).setImageResource(R.drawable.yin_liang_view_da);
               showPositionRight++;
           }else if (showPositionRight == 19){
               list.get(19).setImageResource(R.drawable.yin_liang_view_da);
               list.get(showPositionRight-1).setImageResource(R.drawable.yin_liang_view_xiao);
               showPositionRight = 10;
           }else {
               list.get(showPositionRight).setImageResource(R.drawable.yin_liang_view_da);
               list.get(showPositionRight-1).setImageResource(R.drawable.yin_liang_view_xiao);
               showPositionRight++;
           }
            handler.postDelayed(this,100);
        }
    };
    public void startTiming(){
        recordedTime =0;
        handler.postDelayed(recordTime, 1000);//每两秒执行一次runnable.
    }
    public void stopTiming(){
        timeView.setText("00:00");
        handler.removeCallbacks(recordTime);
    }
    public void setTime(long time){
        timeView.setText(showTime(time-1)+"");
    }
    public void DownTime(long time){

        timer = new CountDownTimer(time*1000+1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText(showTime(millisUntilFinished / 1000-1)+"");
            }

            @Override
            public void onFinish() {
                timeView.setEnabled(true);
                stopefreshAnimation();
                aListener.OnStopDown();
            }
        };
        timer.start();
    }
    public void stopDownTime(){

        timer.cancel();
    }
    private String showTime(long v) {
        long fen = v /60 == 0 ? 0 : v / 60 ;
        long miao = v %60 ==0 ? 0: v % 60;
//        Log.i("看 第 " +v  + "秒","fen "+ fen + "   秒  "+ miao);
        String result = "";
        if (fen == 0) {
            if (miao == 0){
                result ="00:00";
            }else if (miao!=0 && miao<10){
                result ="00:0"+miao;
            }else {
                result ="00:"+miao;
            }
        }else if (fen != 0 && fen <10){
            if (miao == 0){
                result ="0"+fen+":00";
            }else if (miao!=0 && miao<10){
                result ="0"+fen+":0"+miao;
            }else {
                result ="0"+fen+":"+miao;
            }
        }else {
            if (miao == 0){
                result =fen+":00";
            }else if (miao!=0 && miao<10){
                result =fen+":0"+miao;
            }else {
                result =fen+":"+miao;
            }
        }
        return result;
    }
    public void refreshAnimation(){
        showPositionLeft = 9;
        showPositionRight = 10;
        handler.postDelayed(Animation, 100);//每两秒执行一次runnable.
    }
    public void stopefreshAnimation(){
        list.get(showPositionLeft+1).setImageResource(R.drawable.yin_liang_view_xiao);
        list.get(showPositionRight-1).setImageResource(R.drawable.yin_liang_view_xiao);
        handler.removeCallbacks(Animation);
    }


    public RecordTimeView(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.record_time_view, this);
        a1 = (ImageView) findViewById(R.id.a1);
        a2 = (ImageView) findViewById(R.id.a2);
        a3 = (ImageView) findViewById(R.id.a3);
        a4 = (ImageView) findViewById(R.id.a4);
        a5 = (ImageView) findViewById(R.id.a5);
        a6 = (ImageView) findViewById(R.id.a6);
        a7 = (ImageView) findViewById(R.id.a7);
        a8 = (ImageView) findViewById(R.id.a8);
        a9 = (ImageView) findViewById(R.id.a9);
        a10 = (ImageView) findViewById(R.id.a10);
        a11 = (ImageView) findViewById(R.id.a11);
        a12 = (ImageView) findViewById(R.id.a12);
        a13 = (ImageView) findViewById(R.id.a13);
        a14 = (ImageView) findViewById(R.id.a14);
        a15 = (ImageView) findViewById(R.id.a15);
        a16 = (ImageView) findViewById(R.id.a16);
        a17 = (ImageView) findViewById(R.id.a17);
        a18 = (ImageView) findViewById(R.id.a18);
        a19 = (ImageView) findViewById(R.id.a19);
        a20 = (ImageView) findViewById(R.id.a20);
        timeView = (TextView) findViewById(R.id.timeView);
        list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);
        list.add(a5);
        list.add(a6);
        list.add(a7);
        list.add(a8);
        list.add(a9);
        list.add(a10);
        list.add(a11);
        list.add(a12);
        list.add(a13);
        list.add(a14);
        list.add(a15);
        list.add(a16);
        list.add(a17);
        list.add(a18);
        list.add(a19);
        list.add(a20);
    }
    public void setDownInterfaceListener(DownTimeListener recordInterfaceListener) {
        this.aListener = recordInterfaceListener;
    }
}
