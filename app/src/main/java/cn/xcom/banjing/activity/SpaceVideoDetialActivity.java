package cn.xcom.banjing.activity;

import android.content.Context;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.kaopiz.kprogresshud.KProgressHUD;

import cn.xcom.banjing.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static cn.xcom.banjing.R.id;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class SpaceVideoDetialActivity extends BaseActivity {

    //    private JCVideoPlayerStandard videoView;
    private VideoView videoView;
    private LinearLayout ll_down;
    private TextView down_time,timetext,money,title;
    ImageView videoback,redstate;
    private String videoUri;
    SensorManager sensorManager;
    JCVideoPlayer.JCAutoFullscreenListener sensorEventListener;
    private MediaController mediaController;
    Context context;
    private KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        context = this;

        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        videoView = (VideoView) findViewById(id.space_videoplayer);
        ll_down = (LinearLayout) findViewById(id.ll_down_timer);
        down_time = (TextView) findViewById(id.down_timer);
        timetext = (TextView) findViewById(id.time_text);
        redstate = (ImageView) findViewById(id.red_image);
        title = (TextView) findViewById(id.title);

        videoView.pause();
        videoUri = getIntent().getStringExtra("videoUrl");
        videoback = (ImageView) findViewById(id.video_back);
        videoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!videoUri.equals("")) {
            play(videoUri);
        }
        hud.show();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                //播放结束后的动作
                ll_down.setVisibility(View.GONE);
//                red.setVisibility(View.VISIBLE);
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()

        {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//                            videoView.setBackgroundColor(Color.TRANSPARENT);
                            hud.dismiss();
                            }
                        if (what==MediaPlayer.MEDIA_INFO_BUFFERING_END){
                            ll_down.setVisibility(View.VISIBLE);
                            timer.start();
                        }
                            return true;

                    }

                });

            }
        });
    }
    private CountDownTimer timer = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            down_time.setText(millisUntilFinished/1000+"''");
        }

        @Override
        public void onFinish() {
            timetext.setVisibility(View.GONE);
           down_time.setText("+0.1");
            redstate.setImageResource(R.mipmap.ic_red_open);
        }
    };
    private void play(final String path) {

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setKeepScreenOn(true);

        videoView.setMediaController(mediaController);
        Uri uri = null;
        uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}