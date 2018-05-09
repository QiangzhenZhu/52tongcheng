package cn.xcom.banjing.fragment.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.baidu.mapapi.map.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bolts.CancellationTokenSource;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.WXpay.Constants;
import cn.xcom.banjing.activity.DisplayAdViewPagerActivivty;
import cn.xcom.banjing.activity.DisplayAdvertisingActivity;
import cn.xcom.banjing.activity.DisplayVideoAdvertisingActivity;
import cn.xcom.banjing.activity.LoginActivity;
import cn.xcom.banjing.activity.ReportActivity;
import cn.xcom.banjing.adapter.AdCommentListAdapter;
import cn.xcom.banjing.adapter.ViewPageAdapter;
import cn.xcom.banjing.bean.ADInfo;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.bean.CommentInfo;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.OkHttpUtils;
import cn.xcom.banjing.share.ShareHelper;
import cn.xcom.banjing.temp.MediaplayerController;
import cn.xcom.banjing.utils.Config;
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
import okhttp3.Call;
import okhttp3.Callback;

import static cn.xcom.banjing.R.id.time;
import static cn.xcom.banjing.R.id.tv_avaliable_scores;
import static cn.xcom.banjing.utils.ToastUtils.mToast;
import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;


/**
 * Created by 10835 on 2018/1/9.
 */

public class DisplayAdFragment extends Fragment implements View.OnClickListener{
    private Context mContext;
    private static final String TAG = "DisplayAdFragment";
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private UserInfo userInfo;
    private ImageView imageView1,redstate,btnlike,imageView2;
    private RoundImageView user_photo;
    private SmoothImageView imageView;
    private String imgurl,title,userphoto,username,imageurl1;
    private String content;
    private List addViewList;//添加图片的list
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private RelativeLayout rl_back;
    private LinearLayout ll_down;
    private TextView down_time,tvContent,timetext,tv_username,tvRedpacket ,tvLikeText,tvPriase ;
    private Convenience AdInfo;
    private LinearLayout rl_share;
    private RecyclerView chatAdRecycleView;
    private LinearLayout rl_comment ,rl_like;
    private InputMethodManager inputMethodManager;
    SharePopupWindow takePhotoPopWin;
    private int flag = 2, wxflag = 1;
    IWXAPI msgApi;
    Resources res;
    Bitmap bitmap;
    String thumbPath;
    public static Tencent mTencent;
    private BaseUiListener listener;
    private Collection collection;
    private List<Collection> addList;
    private ArrayList<String> bImgsList=new ArrayList<>();
    private PopupWindow commentPopupWindow;
    private AdCommentListAdapter adapter;
    private List<CommentInfo> commentInfos;
    //图片轮播的Url
    private ArrayList<String> imageUrls;
    private List<ImageView> imageViews;
    private int packetTime;
    //private LinearLayout layout_view_play;
    private String veidoUrl;
    //private MediaplayerController mController;
    private int type;
    private View mView;
    private int position;
    private final static String CURPOSITION = "curPosition";
    private LinearLayout ll_bottom;
    private RelativeLayout rl_top;
    private ScrollView scrollView;
    private boolean hasPlayed;
    private Button callPhone;
    private PLVideoTextureView mVedioView;
    private cn.xcom.banjing.utils.MediaController mMediaController;
    private FrameLayout mVedioFrameLayout;
    private static int[] mVedioLocation = new int[2];
    private boolean isViewCreated = false;
    private boolean isUiVisible = false;
    private KProgressHUD hud;
    private Convenience convenience;
    private LinearLayout ll_convenience_collection;
    private ImageView convenience_collection;
    private TextView trend_item_tv_collection;
    private TextView btn_report;
    private Button btnReward;
    private View.OnClickListener onListner;
    private int rewardPoints = 0;
    private String mid;
    private CountDownTimer timer ;
    private boolean isFirstIn = false;


    public static DisplayAdFragment newInstance(Convenience adInfo,int type) {

        Bundle args = new Bundle();
        args.putSerializable("adinfo",adInfo);
        args.putInt("type",type);
        args.putString("Mid",adInfo.getMid());
        DisplayAdFragment fragment = new DisplayAdFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        type=getArguments().getInt("type");
        mContext =getContext();
        hasPlayed=true;
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("正在加载...")
                .setCancellable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_ad_image_detail,container,false);
        mView=view;
        inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //视频控件
        mVedioView = (PLVideoTextureView) view.findViewById(R.id.PLVideoTextureView);
        userInfo = new UserInfo();
        userInfo.readData(mContext);

        final View mLoadingView = view.findViewById(R.id.LoadingView);
        mLoadingView.setVisibility(View.VISIBLE);
        mVedioView.setBufferingIndicator(mLoadingView);
        mVedioView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mVedioFrameLayout = (FrameLayout) view.findViewById(R.id.vedio_FrameLayout);
        btnReward = (Button) view.findViewById(R.id.btn_reward);
        //打赏按钮
        btnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getAvaliableReward();

            }
        });
        AdInfo= (Convenience) getArguments().getSerializable("adinfo");
        mid = getArguments().getString("Mid");
        btn_report = (TextView) view.findViewById(R.id.bt_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReportActivity.class);
                intent.putExtra("refid",AdInfo.getMid());
                startActivity(intent);
            }
        });
        viewPager= (ViewPager) view.findViewById(R.id.view_pager);
        //layout_view_play= (LinearLayout) view.findViewById(R.id.layout_view_play);
        //layout_video_play= (RelativeLayout) view.findViewById(R.id.layout_video_play);
        //layout_video_play.setOnTouchListener(clickListener);
        ll_convenience_collection = (LinearLayout) view.findViewById(R.id.ll_convenience_collection);
        convenience_collection = (ImageView) view.findViewById(R.id.convenience_collection);
        trend_item_tv_collection = (TextView) view.findViewById(R.id.trend_item_tv_collection);
        ll_bottom= (LinearLayout) view.findViewById(R.id.ll_bottom);
        scrollView= (ScrollView) view.findViewById(R.id.sv);
        user_photo=(RoundImageView)view.findViewById(R.id.riv_userphoto);
        rl_top= (RelativeLayout) view.findViewById(R.id.tl_top);
        callPhone = (Button) view.findViewById(R.id.bt_ad_callPhone);
        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:"+AdInfo.getPhone()));
                startActivity(intent);
            }
        });
        if (type == 1){
            viewPager.setVisibility(View.GONE);
            //layout_view_play.setVisibility(View.VISIBLE);
            mVedioFrameLayout .setVisibility(View.VISIBLE);
            if (savedInstanceState != null) {
                position = savedInstanceState.getInt(CURPOSITION);
            }
            AVOptions options = new AVOptions();
            // the unit of timeout is ms
            options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
            options.setInteger(AVOptions.KEY_MEDIACODEC, 2);
            options.setInteger(AVOptions.KEY_LOG_LEVEL, 0);
            options.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR);
            mVedioView.setAVOptions(options);
            mVedioView.setDebugLoggingEnabled(false);
            mMediaController = new cn.xcom.banjing.utils.MediaController(container.getContext(),true,false);
            mVedioView.setMediaController(mMediaController);
            mVedioView.setLooping(true);
            mVedioView.setOnInfoListener(mOnInfoListener);
            mVedioView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            mVedioView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mVedioView.setOnCompletionListener(mOnCompletionListener);
            mVedioView.setOnErrorListener(mOnErrorListener);
            veidoUrl=NetConstant.NET_DISPLAY_IMG + AdInfo.getVideo();
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            /*initListeners();
            getLayoutVideoPlay(mView);*/
            mVedioView.setVideoPath(veidoUrl);
            //mVedioView.start();



        }
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(CURPOSITION);
        }
        rl_back = (RelativeLayout)view. findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });
        tvContent=(TextView)view.findViewById(R.id.tvContent);

        tv_username=(TextView)view.findViewById(R.id.tv_username);
        tvRedpacket= (TextView) view.findViewById(R.id.tv_packet_count);
        msgApi = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
        mTencent = Tencent.createInstance("1106382893", getActivity().getApplicationContext());
        listener = new BaseUiListener();
        res = getResources();
        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_gf_default_photo);
        thumbPath = convertIconToString(bitmap);

        addViewList=new ArrayList();
        addList = new ArrayList<>();
//        int ID=getIntent().getIntExtra("position", 0);
        ll_down = (LinearLayout) view.findViewById(R.id.ll_down_timer);
        down_time = (TextView) view.findViewById(R.id.down_timer);
        redstate = (ImageView) view.findViewById(R.id.red_image);
        timetext = (TextView)  view.findViewById(R.id.time_text);
        tvLikeText= (TextView) view.findViewById(R.id.trend_item_tv_like);
        tvPriase= (TextView) view.findViewById(R.id.trend_item_tv_praise);
        rl_share = (LinearLayout) view.findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);
        rl_comment= (LinearLayout) view.findViewById(R.id.rl_chat);
        rl_comment.setOnClickListener(this);
        rl_like= (LinearLayout) view.findViewById(R.id.ll_like);
        rl_like.setOnClickListener(this);
        chatAdRecycleView= (RecyclerView)view. findViewById(R.id.rv_ad_chat);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chatAdRecycleView.setLayoutManager(linearLayoutManager);
        if(AdInfo!=null) {
            /*if (AdInfo.getComment()!=null&&AdInfo.getComment().size()>0){

                commentInfos=  AdInfo.getComment();
                adapter=new AdCommentListAdapter(commentInfos);
                chatAdRecycleView.setAdapter(adapter);
            }
            tvContent.setText(AdInfo.getContent());
            userphoto=AdInfo.getPhoto();
            username=AdInfo.getName();
            if (TextUtils.isEmpty(AdInfo.getBalance())) {
                tvPriase.setText("0");
            }else {
                tvPriase.setText(AdInfo.getBalance());
            }
            tv_username.setText(username);
            tvRedpacket.setText(AdInfo.getRedpacket());
            tvLikeText.setText(AdInfo.getLike().size()+"");
            trend_item_tv_collection.setText((Integer.parseInt(AdInfo.getFavorites_count()))+"");
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + userphoto, user_photo);
            imageUrls=new ArrayList<>();
            imageViews=new ArrayList<>();
            for (int i = 0; i <AdInfo.getPic().size() ; i++) {
                String imageUrl=AdInfo.getPic().get(i).getPictureurl();
                imageUrls.add(NetConstant.NET_DISPLAY_IMG + imageUrl);
                ImageView imageView=new ImageView(mContext);
                Bitmap bmap = imageView.getDrawingCache();
                imageView.setImageBitmap(bmap);
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + imageUrl, imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageViews.add(imageView);

            };

            viewPageAdapter=new ViewPageAdapter(imageUrls,imageViews,mContext);
            viewPager.setAdapter(viewPageAdapter);
        }else{

            ((Activity)mContext).finish();
        }*/
        }
        //解决未知布局嵌套的原因致使 ScrollView 滑到中部，现在滑动会顶部
        tv_username.setFocusable(true);
        tv_username.setFocusableInTouchMode(true);
        tv_username.requestFocus();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();

    }

    private void lazyLoad() {
        if (isViewCreated&&isUiVisible){
            getAdByMid(mid);
        }
    }

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    //showToastTips("First video render time: " + extra + "ms");
                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.i(TAG, "First audio render time: " + extra + "ms");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLMediaPlayer.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVedioView.getMetadata().toString());
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_BITRATE:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_FPS:
                    break;
                case PLMediaPlayer.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private void showToastTips(final String tips) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(getContext(), tips, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    showToastTips("IO Error !");
                    return false;
                case PLMediaPlayer.ERROR_CODE_OPEN_FAILED:
                    showToastTips("failed to open player !");
                    break;
                case PLMediaPlayer.ERROR_CODE_SEEK_FAILED:
                    showToastTips("failed to seek !");
                    break;
                default:
                    showToastTips("unknown error !");
                    break;
            }
            getActivity().finish();
            return true;
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            Log.i(TAG, "Play Completed !");
            showToastTips("Play Completed !");
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mVedioView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            mVedioView.setLayoutParams(layoutParams);
            //mVedioView.getLocationOnScreen(mVedioLocation);
            //mVedioLocation[0] += mVedioView.getMeasuredWidth();
            //mVedioLocation[1] += mVedioView.getMeasuredHeight();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mVedioView.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isUiVisible = true;
            lazyLoad();


        } else {
            if (mVedioView != null) {
                mVedioView.pause();
            }
            if (timer!=null) {
                timer.cancel();
            }
        }
    }



    @Override
    public  void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        /*if (type == 1) {
            outState.putInt(CURPOSITION, mPlayer.getCurrentPosition());
        }*/
    }





    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ((Activity) mContext).finish();
                break;
            case R.id.rl_share:
                showPopFormBottom(v);
                break;
            case R.id.ll_like:
                setHadLikePost(AdInfo, AdInfo.getComment().size());
                break;
            case R.id.rl_chat:
                showPopComment();
                break;

        }
    }




    private class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
            Toast.makeText(mContext, "取消分享", Toast.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(mContext, uiError.errorMessage + "\n" + uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
            // TODO: 2018/4/21
            shareNotify();
        }
    }
    public void showPopComment(){
        //弹出Popup时让背景变暗
        setBackGroundAlpha(1);
        commentPopupWindow=new CommentPopupWindow(mContext,onClickListener,textWatcher,userInfo, AdInfo);
        commentPopupWindow.showAtLocation(getActivity().findViewById(R.id.ll_bottom), Gravity.BOTTOM,0,0);
        commentPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackGroundAlpha(2);
            }
        });
    }
    public void showPopFormBottom(View view) {
        takePhotoPopWin = new SharePopupWindow(mContext, onClickListener);
        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
        takePhotoPopWin.showAtLocation(getActivity().findViewById(R.id.ll_bottom), Gravity.BOTTOM, 0, 0);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.haoyou:
                    ToastUtils.showToast(mContext, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(mContext, "微信朋友圈");
                    history();
                    break;
                case R.id.qq:
                    ToastUtils.showToast(mContext, "QQ");
                    shareToQQ();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(mContext, "QQ空间");
                    shareToQzone();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(mContext, "支付宝");
                    toAlipay();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.bt_ad_submit:
                    AddAdComment();
                    if (inputMethodManager != null){
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                    }
                    break;

            }
        }
    };
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
    /**
     * 发表评论
     */
    public void AddAdComment(){
        if (TextUtils.isEmpty(content)){
            Toast.makeText(mContext,"输入内容不能为空，请重新输入",Toast.LENGTH_SHORT)
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
                        Toast.makeText(mContext, "评论成功", Toast.LENGTH_LONG).show();
                        commentPopupWindow.dismiss();
                        setBackGroundAlpha(2);

                        //刷新评论列表
                        if (commentInfos == null){
                            commentInfos=new ArrayList<>();
                        }
                        if (adapter == null){
                            adapter=new AdCommentListAdapter(commentInfos);
                        }
                        CommentInfo info=new CommentInfo();
                        info.setContent(content);
                        info.setAdd_time(String.valueOf(new Date().getTime()));
                        info.setName(userInfo.getUserName());
                        info.setPhoto(userInfo.getUserImg());
                        int size=commentInfos.size();
                        commentInfos.add(info);
                        //if (adapter.getItemCount() == 0){

                            adapter.notifyDataSetChanged();
                        //}

                        adapter.notifyItemInserted(commentInfos.size());


                    } else {

                        Toast.makeText(mContext, "评论失败，请稍后重试", Toast.LENGTH_LONG).show();
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
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
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
                        Toast.makeText(mContext,jsonObject.getString("data"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mContext,"发生未知错误，请重试",Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("packetid",AdInfo.getPacketId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }

    /**
     * 设置Popup弹出时的背景变暗，或变亮
     */
    public void setBackGroundAlpha(int type){
        if (type == 1) {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.4f;
            getActivity().getWindow().setAttributes(lp);
        } else {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 1.0f;
            getActivity().getWindow().setAttributes(lp);
        }
    }
    /**
     * 微信分享网页
     */
    private void shareWX() {
        HelperApplication.shareType = 2;
        HelperApplication.refid = Integer.parseInt(mid);
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetConstant.SHARE_AD + mid;
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
                        convenience_collection.setImageResource(R.drawable.collection_enabled);
                        Toast.makeText(mContext, "收藏成功", Toast.LENGTH_LONG).show();
                        // TODO: 2018/3/22 去抖动
                        trend_item_tv_collection.setText((Integer.parseInt(AdInfo.getFavorites_count())+1)+"");

                    } else {
                        flag = 2;
                        convenience_collection.setImageResource(R.drawable.collection_uneabled);

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
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
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
                        convenience_collection.setImageResource(R.drawable.collection_enabled);

                    } else {
                        flag = 2;
                        convenience_collection.setImageResource(R.drawable.collection_uneabled);

                    }
                    ll_convenience_collection.setOnClickListener(new View.OnClickListener() {
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
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
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
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);

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
                        convenience_collection.setImageResource(R.drawable.collection_uneabled);
                        trend_item_tv_collection.setText((Integer.parseInt(AdInfo.getFavorites_count()))+"");
                        Toast.makeText(mContext, "取消收藏", Toast.LENGTH_LONG).show();
                        Log.d("===quxiao", "取消收藏成功");
                    } else {
                        flag = 1;
                        convenience_collection.setImageResource(R.drawable.collection_enabled);
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
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }


    private void toAlipay() {
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        IAPApi api = APAPIFactory.createZFBApi(mContext.getApplicationContext(), "2016083001821606", false);
        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = NetConstant.SHARE_AD + userInfo.getUserId();

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
        HelperApplication.shareType = 0;
        HelperApplication.refid = 0;
        HelperApplication.shareType = 2;
        HelperApplication.refid = Integer.parseInt(mid);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, ShareHelper.AdShareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, ShareHelper.AdShareContent);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_AD + AdInfo.getMid());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://banjing.xiaocool.net/uploads/ic_logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "52半径");
        mTencent.shareToQQ(getActivity(), params, listener);
    }

    private void shareToQzone() {
        HelperApplication.shareType = 0;
        HelperApplication.refid = 0;
        HelperApplication.shareType = 2;
        HelperApplication.refid = Integer.parseInt(mid);
        Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, ShareHelper.AdShareTitle);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, ShareHelper.AdShareContent);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_AD + AdInfo.getMid());//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://banjing.xiaocool.net/uploads/ic_logo.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(getActivity(), params, listener);
    }
    public String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

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
                        ToastUtil.showShort(mContext, "点赞成功");
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
                ToastUtil.Toast(mContext, "网络错误，请检查");
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("refid", convenience.getMid());
        request.putValue("type", "1");
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
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
                            Toast.makeText(mContext,"你已经点过赞了",Toast.LENGTH_SHORT).show();
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
                ToastUtil.Toast(mContext, "网络错误，请检查");
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("refid", convenience.getMid());
        request.putValue("type", "1");
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            timer.cancel();
        }
        mVedioView.stopPlayback();
        /*if (type == 1){
            releaseMediaPlayer();
        }*/
    }
    /**
     * 释放资源
     */
   /* private void releaseMediaPlayer() {
        mController.removeHandlerCallback();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }*/
    @Override
    public void onResume() {
        super.onResume();
        getAdByMid(mid);
        judgeIsCollection();

    }
    private void getAdByMid(String mid) {
        Log.e("zcq", "getmore");
        hud.show();
        String url = NetConstant.GET_BBSADBYID;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        Gson gson =new Gson();
                        AdInfo = gson.fromJson(jsonObject.getString("data"),Convenience.class);
                        if (TextUtils.isEmpty(AdInfo.getBalance())) {
                            tvPriase.setText("0");
                        }else {
                            tvPriase.setText(AdInfo.getRed_balance()+"");
                        }
                        tvRedpacket.setText(AdInfo.getRed_packet()+"");
                        tvLikeText.setText(AdInfo.getLike().size()+"");
                        if (adapter != null){
                            adapter.notifyDataSetChanged();
                        }else {
                            commentInfos = AdInfo.getComment();
                            adapter = new AdCommentListAdapter(commentInfos);
                            chatAdRecycleView.setAdapter(adapter);
                        }
                        // TODO: 2018/4/18

                        if (AdInfo.getComment()!=null&&AdInfo.getComment().size()>0){

                            commentInfos=  AdInfo.getComment();
                            adapter=new AdCommentListAdapter(commentInfos);
                            chatAdRecycleView.setAdapter(adapter);
                        }
                        tvContent.setText(AdInfo.getContent());
                        userphoto=AdInfo.getPhoto();
                        username=AdInfo.getName();
                        if (TextUtils.isEmpty(AdInfo.getBalance())) {
                            tvPriase.setText("0");
                        }else {
                            tvPriase.setText(AdInfo.getRed_balance()+"");
                        }
                        tv_username.setText(username);
                        tvRedpacket.setText(AdInfo.getRed_packet()+"");
                        tvLikeText.setText(AdInfo.getLike().size()+"");
                        trend_item_tv_collection.setText((Integer.parseInt(AdInfo.getFavorites_count()))+"");
                        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + userphoto, user_photo);
                        imageUrls=new ArrayList<>();
                        imageViews=new ArrayList<>();
                        for (int i = 0; i <AdInfo.getPic().size() ; i++) {
                            String imageUrl=AdInfo.getPic().get(i).getPictureurl();
                            imageUrls.add(NetConstant.NET_DISPLAY_IMG + imageUrl);
                            ImageView imageView=new ImageView(mContext);
                            Bitmap bmap = imageView.getDrawingCache();
                            imageView.setImageBitmap(bmap);
                            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + imageUrl, imageView);
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            imageViews.add(imageView);

                        };

                        viewPageAdapter=new ViewPageAdapter(imageUrls,imageViews,mContext);
                        viewPager.setAdapter(viewPageAdapter);
                        // TODO: 2018/4/19
                        if (isViewCreated&&isUiVisible){
                            mVedioView.start();
                            if (!isFirstIn) {
                                isFirstIn = true;
                                int time = 9000;
                                if (!TextUtils.isEmpty(AdInfo.getPackettime())) {
                                    time = Integer.parseInt(AdInfo.getPackettime()) * 1000;
                                } else {
                                    time = 9000;
                                }
                                timer = new CountDownTimer(time, 1000) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        down_time.setText(millisUntilFinished / 1000 + "''");
                                    }

                                    @Override
                                    public void onFinish() {
                                        timetext.setVisibility(View.GONE);
                                        //没有红包则不抢
                                        if ("0".equals(AdInfo.getRedpacket())) {
                                            down_time.setText("");
                                        } else {
                                            touchPacket();
                                        }
                                    }
                                };
                                timer.start();
                            }
                        }
                    }else{

                        ((Activity)mContext).finish();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hud.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(mContext, "网络错误，请检查");
                hud.dismiss();
            }

        });

        request.putValue("id",mid);
        request.putValue("userid",userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);

    }

    public void Reward(String score, final AlertDialog dialog){
        Uri uri = Uri.parse(NetConstant.REWARD).buildUpon()
                .appendQueryParameter("userid",userInfo.getUserId())
                .appendQueryParameter("ref_id",AdInfo.getUserid())
                .appendQueryParameter("score",score)
                .build();
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false);
        hud.show();
        OkHttpUtils.sendOkHttp(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"网络加载错误",Toast.LENGTH_SHORT).show();
                        hud.dismiss();
                        dialog.dismiss();

                    }
                });
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                try {
                    final JSONObject object = new JSONObject(result);
                    if ("success".equals(object.getString("status"))){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext,"打赏成功",Toast.LENGTH_SHORT).show();
                                hud.dismiss();
                                dialog.dismiss();
                            }
                        });

                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String error = object.getString("data");
                                    Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();
                                    hud.dismiss();
                                    dialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getAvaliableReward(){
        Uri uri = Uri.parse(NetConstant.INTE_POINTS).buildUpon()
                .appendQueryParameter("userid",userInfo.getUserId())
                .build();
        OkHttpUtils.sendOkHttp(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"网络加载错误",Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                try {
                    final JSONObject object = new JSONObject(result);
                    if ("success".equals(object.getString("status"))){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String data = object.getString("data");
                                    initReward(data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String error = object.getString("data");
                                    Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void initReward(String avaliableScores){
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        final View rewardView = LayoutInflater.from(mContext).inflate(R.layout.reward_dialog_view,null);
        final Button GridButton1 = (Button) rewardView.findViewById(R.id.grid_button_1);
        final Button GridButton2 = (Button) rewardView.findViewById(R.id.grid_button_2);
        final Button GridButton3 = (Button) rewardView.findViewById(R.id.grid_button_3);
        final Button GridButton4 = (Button) rewardView.findViewById(R.id.grid_button_4);
        final Button GridButton5 = (Button) rewardView.findViewById(R.id.grid_button_5);
        final EditText GridButton6 = (EditText) rewardView.findViewById(R.id.grid_button_6);
        TextView textView = (TextView) rewardView.findViewById(R.id.tv_avaliable_scores);
        textView.setText(getResources().getString(R.string.avaliable_scores,avaliableScores));
        GridButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridButton1.setSelected(!GridButton1.isSelected());
                if (!GridButton1.isSelected()) {
                    rewardPoints = 0;

                }
                else {
                    GridButton2.setSelected(!GridButton1.isSelected());
                    GridButton3.setSelected(!GridButton1.isSelected());
                    GridButton4.setSelected(!GridButton1.isSelected());
                    GridButton5.setSelected(!GridButton1.isSelected());
                    GridButton6.setSelected(!GridButton1.isSelected());
                    GridButton6.setText("");
                    rewardPoints = 5;
                }

            }
        });

        GridButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridButton2.setSelected(!GridButton2.isSelected());
                if (!GridButton2.isSelected()){
                    rewardPoints = 0;
                }else {
                    GridButton1.setSelected(!GridButton2.isSelected());
                    GridButton3.setSelected(!GridButton2.isSelected());
                    GridButton4.setSelected(!GridButton2.isSelected());
                    GridButton5.setSelected(!GridButton2.isSelected());
                    GridButton6.setSelected(!GridButton2.isSelected());
                    GridButton6.setText("");
                    rewardPoints = 10;
                }


            }
        });
        GridButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridButton3.setSelected(!GridButton3.isSelected());
                if (!GridButton3.isSelected()){
                    rewardPoints = 0;
                }else {
                    GridButton1.setSelected(!GridButton3.isSelected());
                    GridButton2.setSelected(!GridButton3.isSelected());
                    GridButton4.setSelected(!GridButton3.isSelected());
                    GridButton5.setSelected(!GridButton3.isSelected());
                    GridButton6.setSelected(!GridButton3.isSelected());
                    GridButton6.setText("");
                    rewardPoints = 20;
                }



            }
        });

        GridButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GridButton4.setSelected(!GridButton4.isSelected());
                if (!GridButton4.isSelected()){
                    rewardPoints = 0;
                }else {
                    GridButton1.setSelected(!GridButton4.isSelected());
                    GridButton2.setSelected(!GridButton4.isSelected());
                    GridButton3.setSelected(!GridButton4.isSelected());
                    GridButton5.setSelected(!GridButton4.isSelected());
                    GridButton6.setSelected(!GridButton4.isSelected());
                    GridButton6.setText("");
                    rewardPoints = 50;
                }

            }
        });

        GridButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GridButton5.setSelected(!GridButton5.isSelected());
                if (!GridButton5.isSelected()){
                    rewardPoints = 0;
                }else {
                    GridButton6.setSelected(!GridButton5.isSelected());
                    GridButton1.setSelected(!GridButton5.isSelected());
                    GridButton2.setSelected(!GridButton5.isSelected());
                    GridButton3.setSelected(!GridButton5.isSelected());
                    GridButton4.setSelected(!GridButton5.isSelected());
                    GridButton6.setText("");
                    rewardPoints = 100;
                }
            }
        });
        GridButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GridButton6.setSelected(!GridButton6.isSelected());
                GridButton1.setSelected(!GridButton6.isSelected());
                GridButton2.setSelected(!GridButton6.isSelected());
                GridButton3.setSelected(!GridButton6.isSelected());
                GridButton4.setSelected(!GridButton6.isSelected());
                GridButton5.setSelected(!GridButton6.isSelected());
            }
        });
        Button BtnReward = (Button) rewardView.findViewById(R.id.btn_reward_dialog);
        dialog.setView(rewardView);
        dialog.show();
        BtnReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(GridButton6.getText().toString().trim())){
                    Reward(GridButton6.getText().toString().trim(),dialog);
                    return;
                } else if (rewardPoints!=0){
                    Reward(String.valueOf(rewardPoints),dialog);
                    return;
                }

            }
        });
    }

    public void shareNotify(){
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("正在加载...")
                .setCancellable(true);
        if(hud!= null) {
            hud.show();
        }
        StringPostRequest request = new StringPostRequest(NetConstant.SHARE_SUCCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (hud != null) {
                    hud.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        String data = jsonObject.getString("data");

                    }
                    else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }


            }
        });
        request.putValue("userid",userInfo.getUserId());
        request.putValue("type", String.valueOf(HelperApplication.shareType));
        request.putValue("ref_id",String.valueOf(HelperApplication.refid));
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
