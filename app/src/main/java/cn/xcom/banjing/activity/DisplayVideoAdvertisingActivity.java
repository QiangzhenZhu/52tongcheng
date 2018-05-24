package cn.xcom.banjing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.WXpay.Constants;
import cn.xcom.banjing.adapter.AdCommentListAdapter;
import cn.xcom.banjing.adapter.ViewPageAdapter;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.bean.CommentInfo;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.ConvenienceAd;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.share.ShareHelper;
import cn.xcom.banjing.temp.MediaplayerController;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.RoundImageView;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.SmoothImageView;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.utils.ToastUtils;
import cn.xcom.banjing.view.CommentPopupWindow;
import cn.xcom.banjing.view.SharePopupWindow;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

public class DisplayVideoAdvertisingActivity extends Activity implements OnClickListener,
        OnTouchListener {
    /**
     * 视频播放部分
     */
    private RelativeLayout layout_video_play;

    private RelativeLayout rl_top;
    private ScrollView scrollView;
    private LinearLayout layout_view_play;
    private LinearLayout ll_bottom;
    /**
     * 用于视频播放
     */
    private MediaPlayer mPlayer;
    private SurfaceView surfaceview_video;
    private MediaplayerController mController;
    private SurfaceHolder.Callback mCallback;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private MediaplayerController.ControlOper mPlayerControl;
    private UserInfo user;
    private Context context;
    /**
     * 视频播放连接
     */
    private String newsRadioUrl;
    private final static String TAG = "DisplayVideoAdvertisingActivity";
    private ImageView img_video_error;
    private int position;
    private final static String CURPOSITION = "curPosition";
    private Convenience AdInfo;
    ///
    private UserInfo userInfo;
    private ImageView imageView1,redstate,btnlike;
    private RoundImageView user_photo;
    private SmoothImageView imageView;
    private String imgurl,title,userphoto,username;
    private List addViewList;//添加图片的list
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private RelativeLayout rl_back;
    private LinearLayout ll_down;
    private TextView down_time,tvContent,timetext,tv_username,tvLikeText;
    private LinearLayout rl_share;
    private LinearLayout rl_commennt;
    SharePopupWindow takePhotoPopWin;
    private int flag = 2, wxflag = 1;
    IWXAPI msgApi;
    Resources res;
    Bitmap bitmap;
    String thumbPath;
    private Tencent mTencent;
    private BaseUiListener listener;
    private Collection collection;
    private List<Collection> addList;
    private ArrayList<String> bImgsList=new ArrayList<>();
    private String content;
    private CommentPopupWindow commentPopupWindow;
    private AdCommentListAdapter adapter;
    private List<ConvenienceAd.CommentBean> commentInfos;
    private RecyclerView chatAdRecycleView;
    private TextView tvPriase, tvPacketCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 屏幕无标题 */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_video_advertising);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(CURPOSITION);
        }
        AdInfo = (Convenience) getIntent().getSerializableExtra("adinfo");
        if (AdInfo!=null){
//            LogUtils.e(TAG, "--AdInfo-?" + AdInfo.toString());
//            newsRadioUrl = "http://radio.canka168.com/27856ed7e9caa1e9d0173a2285cdbaa2";

            LogUtils.e(TAG, "--newsRadioUrl-?" + newsRadioUrl);
        newsRadioUrl = NetConstant.NET_DISPLAY_IMG + AdInfo.getVideo();
//        LogUtils.e(TAG, "--newsRadioUrl-?" + newsRadioUrl);
        }else {
            finish();
        }
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        user = new UserInfo();
        user.readData(context);
        initListeners();
        initView();

    }


    private void initView() {
        getLayoutVideoPlay();
        tvPacketCount= (TextView) findViewById(R.id.tv_packet_count);
        chatAdRecycleView= (RecyclerView) findViewById(R.id.rv_ad_chat_vedio);
        rl_commennt= (LinearLayout) findViewById(R.id.rl_chat_vedio);
        rl_commennt.setOnClickListener(this);
        rl_top= (RelativeLayout) findViewById(R.id.rl_top);
        scrollView= (ScrollView) findViewById(R.id.sv);
        ll_bottom= (LinearLayout) findViewById(R.id.ll_bottom);
        layout_video_play.setOnTouchListener(this);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvContent=(TextView)findViewById(R.id.tvContent);
        user_photo=(RoundImageView)findViewById(R.id.riv_userphoto);
        tv_username=(TextView)findViewById(R.id.tv_username);
        context=this;
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        mTencent = Tencent.createInstance("1106382893", this.getApplicationContext());
        listener = new BaseUiListener();
        res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_gf_default_photo);
        thumbPath = convertIconToString(bitmap);
        userInfo = new UserInfo();
        userInfo.readData(context);
        addViewList=new ArrayList();
        addList = new ArrayList<>();
//        int ID=getIntent().getIntExtra("position", 0);
        ll_down = (LinearLayout) findViewById(R.id.ll_down_timer);
        down_time = (TextView) findViewById(R.id.down_timer);
        redstate = (ImageView) findViewById(R.id.red_image);
        timetext = (TextView) findViewById(R.id.time_text);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        rl_share = (LinearLayout) findViewById(R.id.rl_share);
        tvLikeText= (TextView) findViewById(R.id.trend_item_tv_like);
        tvLikeText.setText(AdInfo.getLikeInfo()+"");
        rl_share.setOnClickListener(this);
        btnlike=(ImageView)findViewById(R.id.iv_like);
        btnlike.setOnClickListener(this);
        tvContent.setText(AdInfo.getContent());
        userphoto=AdInfo.getPhoto();
        username=AdInfo.getName();
        tv_username.setText(username);
        tvPriase= (TextView) findViewById(R.id.trend_item_tv_praise);
        tvPriase.setText(AdInfo.getRedpacket()+"");
        //tvPacketCount.setText(AdInfo.getRedpacket());
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + userphoto, user_photo);
        /*if (AdInfo.getComment()!=null&&AdInfo.getComment().size()>0){
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            chatAdRecycleView.setLayoutManager(linearLayoutManager);
            commentInfos=  AdInfo.getComment();
            adapter=new AdCommentListAdapter(commentInfos);
            chatAdRecycleView.setAdapter(adapter);
        }*/
        //解决未知布局嵌套的原因致使 ScrollView 滑到中部，现在滑动会顶部
        tv_username.setFocusable(true);
        tv_username.setFocusableInTouchMode(true);
        tv_username.requestFocus();
    }
    private CountDownTimer timer = new CountDownTimer(9000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            down_time.setText(millisUntilFinished/1000+"''");
        }

        @Override
        public void onFinish() {
            timetext.setVisibility(View.GONE);
            //没有红包则不抢
            if ("0".equals(AdInfo.getRedpacket())){
                down_time.setText("");
            }else {
                touchPacket();
            }

        }
    };
    /**
     * 视频播放部分
     */
    private void getLayoutVideoPlay() {
        layout_video_play = (RelativeLayout) findViewById(R.id.layout_video_play);
        layout_view_play = (LinearLayout) findViewById(R.id.layout_view_play);
        surfaceview_video = (SurfaceView) findViewById(R.id.surfaceview_video);
        img_video_error = (ImageView) findViewById(R.id.img_video_error);
        img_video_error.setOnClickListener(this);
        SurfaceHolder surfaceHolder = surfaceview_video.getHolder();
        surfaceHolder.addCallback(mCallback);
        mPlayer = new MediaPlayer();
        mController = new MediaplayerController(context);
        netTypeVideoPlayer();
    }

    /**
     * 判断网络是移动网络还是 WIFI 还是 没网操作
     */
    private void netTypeVideoPlayer() {
        try {
            img_video_error.setVisibility(View.GONE);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(newsRadioUrl);
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.prepareAsync();
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** 释放资源 */
        timer.cancel();
        releaseMediaPlayer();
    }

    /**
     * 释放资源
     */
    private void releaseMediaPlayer() {
        mController.removeHandlerCallback();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_video_error:
                netTypeVideoPlayer();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.rl_share:
                showPopFormBottom(view);
                break;
            case R.id.iv_like:
                //setHadLikePost(AdInfo,AdInfo.getComment().size());
                break;
            case R.id.rl_chat_vedio:
                showPopComment();
                break;



        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.layout_video_play:
                mController.show();
                break;

        }
        return false;
    }
    /**
     * 回调EditText中的内容
     */
    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            content=s.toString().trim();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public void showPopComment(){
        //弹出Popup时让背景变暗
        setBackGroundAlpha(1);
        commentPopupWindow=new CommentPopupWindow(this,onClickListener,textWatcher,userInfo, AdInfo);
        commentPopupWindow.showAtLocation(findViewById(R.id.ll_bottom),Gravity.BOTTOM,0,0);
        commentPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(2);
            }
        });
    }
    /**
     * 发表评论
     */
    public void AddAdComment(){
        if (TextUtils.isEmpty(content)){
            Toast.makeText(DisplayVideoAdvertisingActivity.this,"输入内容不能为空，请重新输入",Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String url = NetConstant.ADD_AD_COMMENT;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(DisplayVideoAdvertisingActivity.this, "评论成功", Toast.LENGTH_LONG).show();
                        commentPopupWindow.dismiss();
                        setBackGroundAlpha(2);

                        //刷新评论列表
                        if (commentInfos == null){
                            commentInfos=new ArrayList<>();
                        }
                        if (adapter == null){
                            adapter=new AdCommentListAdapter(commentInfos);
                        }
                        ConvenienceAd.CommentBean info=new ConvenienceAd.CommentBean();
                        info.setContent(content);
                        info.setAdd_time(String.valueOf(new Date().getTime()));
                        info.setName(userInfo.getUserName());
                        info.setPhoto(userInfo.getUserImg());
                        int size=commentInfos.size();
                        commentInfos.add(info);
                        if (adapter.getItemCount() == 0){
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyItemInserted(commentInfos.size()
                        );

                    } else {

                        Toast.makeText(DisplayVideoAdvertisingActivity.this, "评论失败，请稍后重试", Toast.LENGTH_LONG).show();
                        commentPopupWindow.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("id",AdInfo.getMid() );
        request.putValue("type", "1");
        request.putValue("content",content);
//        request.putValue("title", shopGoodInfo.getGoodsname());
//        request.putValue("description", shopGoodInfo.getDescription());
        SingleVolleyRequest.getInstance(DisplayVideoAdvertisingActivity.this).addToRequestQueue(request);
    }

    /**
     * 倒计时结束，抢红包
     */
    public void touchPacket(){
        String url = NetConstant.TOUCH_PACKET;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        JSONObject object=jsonObject.getJSONObject("data");
                        down_time.setText(object.getString("money"));
                        redstate.setImageResource(R.mipmap.ic_red_open);
                    }
                    if (status.equals("error")){
                        down_time.setText("");
                        Toast.makeText(context,jsonObject.getString("data"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(DisplayVideoAdvertisingActivity.this,"发生未知错误，请重试",Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid", userInfo.getUserId());
        //request.putValue("packetid",AdInfo.getPacketId());
        SingleVolleyRequest.getInstance(DisplayVideoAdvertisingActivity.this).addToRequestQueue(request);
    }
    /**
     * 设置Popup弹出时的背景变暗，或变亮
     */
    public void setBackGroundAlpha(int type){
        if (type == 1) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.4f;
            getWindow().setAttributes(lp);
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 1.0f;
            getWindow().setAttributes(lp);
        }
    }
    private void initListeners() {
        mCallback = new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                mPlayer.setDisplay(holder);
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        };
        mPreparedListener = new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {

                mController.setMediaPlayer(mPlayerControl);
                mController
                        .setAnchorView((FrameLayout) findViewById(R.id.video_surfacecontainer));
                mPlayer.start();
                if (position > 0) {
                    mPlayer.seekTo(position);
                }
            }
        };
        mPlayerControl = new MediaplayerController.ControlOper() {
            public void start() {
                mPlayer.start();
            }

            public void pause() {
                mPlayer.pause();
            }

            public int getDuration() {
                return mPlayer.getDuration();
            }

            public int getCurPosition() {
                return mPlayer.getCurrentPosition();
            }

            public void seekTo(int pos) {
                mPlayer.seekTo(pos);
            }

            public boolean isPlaying() {
                return mPlayer.isPlaying();
            }

            public int getBufPercent() {
                return 0;
            }

            public boolean canPause() {
                return true;
            }

            public boolean canSeekBackward() {
                return true;
            }

            public boolean canSeekForward() {
                return true;
            }

            public boolean isFullScreen() {
                if (screenOrient() == 0) {
                    return true;
                }
                return false;
            }

            /** 判断横竖屏 @return 1：竖 | 0：横 */
            @SuppressWarnings("deprecation")
            public int screenOrient() {
                int orient = DisplayVideoAdvertisingActivity.this.getRequestedOrientation();
                if (orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    WindowManager windowManager = DisplayVideoAdvertisingActivity.this
                            .getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    int screenWidth = display.getWidth();
                    int screenHeight = display.getHeight();
                    orient = screenWidth < screenHeight ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
                return orient;
            }

            /** 使屏幕处于竖屏 */
            public void verticalScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                setPortraitWidthHeight();

            }

            /** 使屏幕处于横屏 */
            public void fullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                setLandscapeWidthHeight();

            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt(CURPOSITION, mPlayer.getCurrentPosition());
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.e(TAG, "onConfigurationChanged()");
        // 检测屏幕的方向：纵向或横向
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.out.println("当前屏幕=》》》》横屏");
            // 当前为横屏， 在此处添加额外的处理代码
            setLandscapeWidthHeight();
            rl_top.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("当前屏幕=》》》》竖屏");
            // 当前为竖屏， 在此处添加额外的处理代码
            setPortraitWidthHeight();
            rl_top.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.VISIBLE);
        }
        // 检测实体键盘的状态：推出或者合上
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            // 实体键盘处于推出状态，在此处添加额外的处理代码
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            // 实体键盘处于合上状态，在此处添加额外的处理代码
        }
    }

    /**
     * 当用户按下返回键的时候判断当前手机状态是横屏还是竖屏
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mPlayerControl.screenOrient() == 0) {
                mPlayerControl.fullScreen();
                return false;
            } else {
                DisplayVideoAdvertisingActivity.this.finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置竖屏时候播放器的宽高
     */
    private void setPortraitWidthHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = dm.heightPixels;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = mSurfaceViewWidth;
        lp.height = mSurfaceViewHeight * 1 / 3;
        layout_view_play.setLayoutParams(lp);
    }

    /**
     * 设置横屏时候播放器的宽高
     */
    private void setLandscapeWidthHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mSurfaceViewWidth = dm.widthPixels;
        int mSurfaceViewHeight = dm.heightPixels;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.width = mSurfaceViewWidth;
        lp.height = mSurfaceViewHeight-50;
        layout_view_play.setLayoutParams(lp);
    }

    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(findViewById(R.id.ll_bottom), Gravity.BOTTOM, 0, 0);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.showToast(DisplayVideoAdvertisingActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(DisplayVideoAdvertisingActivity.this, "微信朋友圈");
                    history();
                    break;
                case R.id.qq:
                    ToastUtils.showToast(DisplayVideoAdvertisingActivity.this, "QQ");
                    shareToQQ();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(DisplayVideoAdvertisingActivity.this, "QQ空间");
                    shareToQzone();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(DisplayVideoAdvertisingActivity.this, "支付宝");
                    toAlipay();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.bt_ad_submit:
                    AddAdComment();
                    break;
            }
        }
    };

    /**
     * 微信分享网页
     */
    private void shareWX() {
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetConstant.SHARE_AD + AdInfo.getMid();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = ShareHelper.AdShareTitle;
        msg.description = ShareHelper.AdShareContent;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "weiyi";
        req.message = msg;
        req.scene = wxflag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        msgApi.sendReq(req);
    }

    /**
     * 分享到微信好友
     */
    private void setting() {
        //ToastUtils.ToastShort(this, "分享到微信好友");
        wxflag = 0;
        shareWX();
        takePhotoPopWin.dismiss();

    }

    /**
     * 分享到微信朋友圈
     */
    private void history() {
        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
        wxflag = 1;
        shareWX();
        takePhotoPopWin.dismiss();
    }
    //点击收藏商品
    public void collection() {
        String url = NetConstant.GOOD_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        flag = 1;
                        btnlike.setImageResource(R.mipmap.ad_btn_praise);
                        Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();

                    } else {
                        flag = 2;
                        Toast.makeText(context, "收藏失败", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("goodsid",AdInfo.getMid() );
        request.putValue("type", "4");
//        request.putValue("title", shopGoodInfo.getGoodsname());
//        request.putValue("description", shopGoodInfo.getDescription());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }
    //判断该商品是否被收藏
    public void judgeIsCollection() {
        String url = NetConstant.GOOD_IS_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("==sou", s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        flag = 1;
                        btnlike.setImageResource(R.mipmap.ad_btn_praise);

                    } else {
                        flag = 2;
                        btnlike.setImageResource(R.mipmap.ad_btn_praise);
                    }
                    btnlike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag == 1) {
                                cancleCollection();
                            } else if (flag == 2) {
                                collection();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("refid", AdInfo.getMid());
        request.putValue("type", "4");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //获取收藏列表
    public void collectionList() {
        String url = NetConstant.HAS_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    Log.d("==用户data", data);
                    if (data == null) {
                        collection = null;
                        Log.d("==用户是否收藏", "用户没有收藏");
                    } else {
                        Log.d("==用户是否收藏", "用户有收藏");
                        Gson gson = new Gson();
                        addList = gson.fromJson(data,
                                new TypeToken<ArrayList<Collection>>() {
                                }.getType());
                        for (int i = 0; i < addList.size(); i++) {
                            collection = addList.get(i);
                        }
                        judgeIsCollection();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);

    }
    /**
     * 点赞
     * @param convenience
     */
    public  void SetLikePost(Convenience convenience , final int count){
        String url = NetConstant.SETLIKE_POST;

        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        ToastUtil.showShort(context, "点赞成功");
                        int likeCount=count+1;
                        tvLikeText.setText(likeCount+"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
            }
        });
        request.putValue("userid", userInfo.getUserId());

        request.putValue("refid", convenience.getMid());
        request.putValue("type", "1");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 先判断受否点赞过，没有点过赞再调用SetLikePost（）进行点赞；
     */
    public  void setHadLikePost(final Convenience con,int count){
        final Convenience convenience =con;
        String url = NetConstant.CHECK_HAD_LIKE;
        final int likeCount=count;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String isLike=jsonObject.getString("data");
                        if (!"had".equals(isLike)){
                            SetLikePost(convenience,likeCount);
                        }else {
                            Toast.makeText(context,"你已经点过赞了",Toast.LENGTH_SHORT).show();
                        }
                    }if (status.equals("error")&&"no".equals(jsonObject.getString("data"))){
                        SetLikePost(convenience,likeCount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(context, "网络错误，请检查");
            }
        });
        request.putValue("userid", userInfo.getUserId());

        request.putValue("refid", convenience.getMid());
        request.putValue("type", "1");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }

    //取消收藏
    public void cancleCollection() {
        String url = NetConstant.GOOD_CANCLE_COLLECTION;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("==sou", s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        flag = 2;
                        btnlike.setImageResource(R.mipmap.ad_btn_praise);
                        Toast.makeText(context, "取消收藏", Toast.LENGTH_LONG).show();
                        Log.d("===quxiao", "取消收藏成功");
                    } else {
                        flag = 1;
                        btnlike.setImageResource(R.mipmap.ad_btn_praise);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("goodsid", AdInfo.getMid());
        request.putValue("type", "4");
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }


    private void toAlipay() {
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), "2016083001821606", false);
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = NetConstant.SHARE_AD + AdInfo.getMid();

        //组装分享消息对象
        APMediaMessage mediaMessage = new APMediaMessage();
        mediaMessage.title = ShareHelper.AdShareTitle;
        mediaMessage.description = ShareHelper.AdShareContent;
        mediaMessage.mediaObject = webPageObject;
        mediaMessage.setThumbImage(bitmap);
        //将分享消息对象包装成请求对象
        SendMessageToZFB.Req req = new SendMessageToZFB.Req();
        req.message = mediaMessage;
        req.transaction = "WebShare" + String.valueOf(System.currentTimeMillis());
        //发送请求
        api.sendReq(req);

    }


    private void shareToQQ() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, ShareHelper.AdShareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ShareHelper.AdShareContent);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://banjing.xiaocool.net/uploads/ic_logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "52半径");
        mTencent.shareToQQ(this, params, listener);
    }

    private void shareToQzone() {
        Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, ShareHelper.AdShareTitle);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, ShareHelper.AdShareContent);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_AD + AdInfo.getMid());//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://banjing.xiaocool.net/uploads/ic_logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(DisplayVideoAdvertisingActivity.this, params, listener);
    }
    public String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
            Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(DisplayVideoAdvertisingActivity.this, uiError.errorMessage + "\n" + uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
        }
    }


}
