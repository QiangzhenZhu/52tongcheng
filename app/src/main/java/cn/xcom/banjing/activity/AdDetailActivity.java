//package cn.xcom.banjing.activity;
//
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v4.view.ViewPager;
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alipay.share.sdk.openapi.APAPIFactory;
//import com.alipay.share.sdk.openapi.APMediaMessage;
//import com.alipay.share.sdk.openapi.APWebPageObject;
//import com.alipay.share.sdk.openapi.IAPApi;
//import com.alipay.share.sdk.openapi.SendMessageToZFB;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.tencent.connect.share.QQShare;
//import com.tencent.connect.share.QzoneShare;
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.xcom.banjing.R;
//import cn.xcom.banjing.WXpay.Constants;
//import cn.xcom.banjing.adapter.ViewPageAdapter;
//import cn.xcom.banjing.bean.Collection;
//import cn.xcom.banjing.bean.Convenience;
//import cn.xcom.banjing.bean.UserInfo;
//import cn.xcom.banjing.constant.NetConstant;
//import cn.xcom.banjing.utils.MyImageLoader;
//import cn.xcom.banjing.utils.RoundImageView;
//import cn.xcom.banjing.utils.SingleVolleyRequest;
//import cn.xcom.banjing.utils.SmoothImageView;
//import cn.xcom.banjing.utils.StringPostRequest;
//import cn.xcom.banjing.utils.ToastUtils;
//import cn.xcom.banjing.view.MediaplayerController;
//import cn.xcom.banjing.view.SharePopupWindow;
//
//import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
//
///**
// * Created by zhuchongkun on 2017/11/25.
// */
//
//public class AdDetailActivity extends BaseActivity implements View.OnClickListener {
//    private int mLocationX;
//    private int mLocationY;
//    private int mWidth;
//    private int mHeight;
//    private UserInfo userInfo;
//    private ImageView imageView1, redstate, btnlike;
//    private RoundImageView user_photo;
//    private SmoothImageView imageView;
//    private String imgurl, title, userphoto, username;
//    private Context context;
//    private List addViewList;//添加图片的list
//    private ViewPager viewPager;
//    private ViewPageAdapter viewPageAdapter;
//    private RelativeLayout rl_back;
//    private LinearLayout ll_down;
//    private TextView down_time, tvContent, timetext, tv_username;
//    private Convenience AdInfo;
//    private LinearLayout rl_share;
//    SharePopupWindow takePhotoPopWin;
//    private int flag = 2, wxflag = 1;
//    IWXAPI msgApi;
//    Resources res;
//    Bitmap bitmap;
//    String thumbPath;
//    private Tencent mTencent;
//    private BaseUiListener listener;
//    private Collection collection;
//    private List<Collection> addList;
//    /**
//     * 用于视频播放
//     */
//    private MediaPlayer mPlayer;
//    private SurfaceView surfaceview_video;
//    private MediaplayerController mController;
//    private SurfaceHolder.Callback mCallback;
//    private MediaPlayer.OnPreparedListener mPreparedListener;
//    private MediaplayerController.ControlOper mPlayerControl;
//    private RelativeLayout layout_video_play;
//    /**
//     * 视频播放连接
//     */
//    private String newsRadioUrl;
//    /**
//     * 返回按钮
//     */
//    private LinearLayout layout_video_back, layout_landscape_back,
//            layout_view_play;
//    private int position;
//    private final static String CURPOSITION = "curPosition";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ad_detail);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        if (savedInstanceState != null) {
//            position = savedInstanceState.getInt(CURPOSITION);
//        }
//        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
//        rl_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        tvContent = (TextView) findViewById(R.id.tvContent);
//        user_photo = (RoundImageView) findViewById(R.id.riv_userphoto);
//        tv_username = (TextView) findViewById(R.id.tv_username);
//        context = this;
//        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
//        msgApi.registerApp(Constants.APP_ID);
//        mTencent = Tencent.createInstance("1106382893", this.getApplicationContext());
//        listener = new BaseUiListener();
//        res = getResources();
//        bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_gf_default_photo);
//        thumbPath = convertIconToString(bitmap);
//        userInfo = new UserInfo();
//        userInfo.readData(context);
//        addViewList = new ArrayList();
//        addList = new ArrayList<>();
////        int ID=getIntent().getIntExtra("position", 0);
//        ll_down = (LinearLayout) findViewById(R.id.ll_down_timer);
//        down_time = (TextView) findViewById(R.id.down_timer);
//        redstate = (ImageView) findViewById(R.id.red_image);
//        timetext = (TextView) findViewById(R.id.time_text);
//        viewPager = (ViewPager) findViewById(R.id.view_pager);
//        rl_share = (LinearLayout) findViewById(R.id.rl_share);
//        rl_share.setOnClickListener(this);
//        btnlike = (ImageView) findViewById(R.id.iv_like);
//        btnlike.setOnClickListener(this);
//        layout_view_play = (LinearLayout) findViewById(R.id.layout_view_play);
//        surfaceview_video = (SurfaceView) findViewById(R.id.surfaceview_video);
//        initListeners();
//        AdInfo = (Convenience) getIntent().getSerializableExtra("adinfo");
//        if (AdInfo != null) {
//            tvContent.setText(AdInfo.getContent());
//            userphoto = AdInfo.getPhoto();
//            username = AdInfo.getName();
//            tv_username.setText(username);
//            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + userphoto, user_photo);
//            if (!AdInfo.getVideo().equals("")) {
//                newsRadioUrl = NetConstant.NET_DISPLAY_IMG + AdInfo.getVideo();
//                surfaceview_video.setVisibility(View.VISIBLE);
//                viewPager.setVisibility(View.GONE);
//                getLayoutVideoPlay();
//            } else {
//                surfaceview_video.setVisibility(View.GONE);
//                viewPager.setVisibility(View.VISIBLE);
//                if (AdInfo.getPic() != null && AdInfo.getPic().size() > 0) {
//                    imgurl = AdInfo.getPic().get(0).getPictureurl();
//                    imageView = new SmoothImageView(this);
//                    imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
//                    imageView.transformIn();
//                    imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
//                    imageView1 = new ImageView(context);
//                    imageView.buildDrawingCache();
//                    Bitmap bmap = imageView.getDrawingCache();
//                    imageView1.setImageBitmap(bmap);
//                    MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + imgurl, imageView1);
//                    imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    addViewList.add(imageView1);
//                    viewPageAdapter = new ViewPageAdapter(l, addViewList, context);
//                    viewPager.setAdapter(viewPageAdapter);
//                }
//                timer.start();
//            }
//
//        } else {
//            finish();
//        }
//
//    }
//
//    /**
//     * 视频播放部分
//     */
//    private void getLayoutVideoPlay() {
//        SurfaceHolder surfaceHolder = surfaceview_video.getHolder();
//        surfaceHolder.addCallback(mCallback);
//        mPlayer = new MediaPlayer();
//        mController = new MediaplayerController(context);
//        netTypeVideoPlayer();
//
//    }
//
//    /**
//     * 判断网络是移动网络还是 WIFI 还是 没网操作
//     */
//    private void netTypeVideoPlayer() {
//        try {
//            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mPlayer.setDataSource(this, Uri.parse(newsRadioUrl));
//            mPlayer.setOnPreparedListener(mPreparedListener);
//            mPlayer.prepareAsync();
//            timer.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private CountDownTimer timer = new CountDownTimer(10000, 1000) {
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            down_time.setText(millisUntilFinished / 1000 + "''");
//        }
//
//        @Override
//        public void onFinish() {
//            timetext.setVisibility(View.GONE);
//            down_time.setText("+0.1");
//            redstate.setImageResource(R.mipmap.ic_red_open);
//
//        }
//    };
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.back:
//                finish();
//                break;
//            case R.id.rl_share:
//                showPopFormBottom(view);
//                break;
//            case R.id.iv_like:
//                collection();
//        }
//    }
//
//    public void showPopFormBottom(View view) {
//        takePhotoPopWin = new SharePopupWindow(this, onClickListener);
//        //SharePopupWindow takePhotoPopWin = new SharePopupWindow(this, onClickListener);
//        takePhotoPopWin.showAtLocation(findViewById(R.id.ll_bottom), Gravity.BOTTOM, 0, 0);
//    }
//
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.haoyou:
//                    ToastUtils.showToast(AdDetailActivity.this, "微信好友");
//                    setting();
//                    break;
//                case R.id.dongtai:
//                    ToastUtils.showToast(AdDetailActivity.this, "微信朋友圈");
//                    history();
//                    break;
//                case R.id.qq:
//                    ToastUtils.showToast(AdDetailActivity.this, "QQ");
//                    shareToQQ();
//                    takePhotoPopWin.dismiss();
//                    break;
//                case R.id.kongjian:
//                    ToastUtils.showToast(AdDetailActivity.this, "QQ空间");
//                    shareToQzone();
//                    takePhotoPopWin.dismiss();
//                    break;
//                case R.id.zhifubao:
//                    ToastUtils.showToast(AdDetailActivity.this, "支付宝");
//                    toAlipay();
//                    takePhotoPopWin.dismiss();
//                    break;
//            }
//        }
//    };
//
//    /**
//     * 微信分享网页
//     */
//    private void shareWX() {
//        //创建一个WXWebPageObject对象，用于封装要发送的Url
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = NetConstant.SHARE_SHOP_H5 + userInfo.getUserId();
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = "我注册了52半径，发布了商品，来加入吧";
//        msg.description = "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
//        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
//        msg.setThumbImage(thumb);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = "weiyi";
//        req.message = msg;
//        req.scene = wxflag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//        msgApi.sendReq(req);
//    }
//
//    /**
//     * 分享到微信好友
//     */
//    private void setting() {
//        //ToastUtils.ToastShort(this, "分享到微信好友");
//        wxflag = 0;
//        shareWX();
//        takePhotoPopWin.dismiss();
//
//    }
//
//    /**
//     * 分享到微信朋友圈
//     */
//    private void history() {
//        // ToastUtils.ToastShort(this, "分享到微信朋友圈");
//        wxflag = 1;
//        shareWX();
//        takePhotoPopWin.dismiss();
//    }
//
//    //点击收藏商品
//    public void collection() {
//        String url = NetConstant.GOOD_COLLECTION;
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String status = jsonObject.getString("status");
//                    if (status.equals("success")) {
//                        flag = 1;
//                        btnlike.setImageResource(R.mipmap.yijingshoucang);
//                        Toast.makeText(context, "收藏成功", Toast.LENGTH_LONG).show();
//
//                    } else {
//                        flag = 2;
//                        Toast.makeText(context, "收藏失败", Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        request.putValue("userid", userInfo.getUserId());
//        request.putValue("goodsid", AdInfo.getMid());
//        request.putValue("type", "4");
////        request.putValue("title", shopGoodInfo.getGoodsname());
////        request.putValue("description", shopGoodInfo.getDescription());
//        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
//    }
//
//    //判断该商品是否被收藏
//    public void judgeIsCollection() {
//        String url = NetConstant.GOOD_IS_COLLECTION;
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    Log.d("==sou", s);
//                    String status = jsonObject.getString("status");
//                    if (status.equals("success")) {
//                        flag = 1;
//                        btnlike.setImageResource(R.mipmap.yijingshoucang);
//
//                    } else {
//                        flag = 2;
//                        btnlike.setImageResource(R.mipmap.shoucang);
//                    }
//                    btnlike.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (flag == 1) {
//                                cancleCollection();
//                            } else if (flag == 2) {
//                                collection();
//                            }
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        request.putValue("userid", userInfo.getUserId());
//        request.putValue("refid", AdInfo.getMid());
//        request.putValue("type", "4");
//        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
//    }
//
//    //获取收藏列表
//    public void collectionList() {
//        String url = NetConstant.HAS_COLLECTION;
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    String data = jsonObject.getString("data");
//                    Log.d("==用户data", data);
//                    if (data == null) {
//                        collection = null;
//                        Log.d("==用户是否收藏", "用户没有收藏");
//                    } else {
//                        Log.d("==用户是否收藏", "用户有收藏");
//                        Gson gson = new Gson();
//                        addList = gson.fromJson(data,
//                                new TypeToken<ArrayList<Collection>>() {
//                                }.getType());
//                        for (int i = 0; i < addList.size(); i++) {
//                            collection = addList.get(i);
//                        }
//                        judgeIsCollection();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        request.putValue("userid", userInfo.getUserId());
//        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
//
//    }
//
//    //取消收藏
//    public void cancleCollection() {
//        String url = NetConstant.GOOD_CANCLE_COLLECTION;
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    Log.d("==sou", s);
//                    String status = jsonObject.getString("status");
//                    if (status.equals("success")) {
//                        flag = 2;
//                        btnlike.setImageResource(R.mipmap.shoucang);
//                        Toast.makeText(context, "取消收藏", Toast.LENGTH_LONG).show();
//                        Log.d("===quxiao", "取消收藏成功");
//                    } else {
//                        flag = 1;
//                        btnlike.setImageResource(R.mipmap.yijingshoucang);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        request.putValue("userid", userInfo.getUserId());
//        request.putValue("goodsid", AdInfo.getMid());
//        request.putValue("type", "4");
//        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
//    }
//
//
//    private void toAlipay() {
//        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
//        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), "2016083001821606", false);
//        APWebPageObject webPageObject = new APWebPageObject();
//        webPageObject.webpageUrl = NetConstant.SHARE_SHOP_H5 + userInfo.getUserId();
//
//        //组装分享消息对象
//        APMediaMessage mediaMessage = new APMediaMessage();
//        mediaMessage.title = "我注册了52半径，发布了商品，来加入吧";
//        mediaMessage.description = "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台";
//        mediaMessage.mediaObject = webPageObject;
//        mediaMessage.setThumbImage(bitmap);
//        //将分享消息对象包装成请求对象
//        SendMessageToZFB.Req req = new SendMessageToZFB.Req();
//        req.message = mediaMessage;
//        req.transaction = "WebShare" + String.valueOf(System.currentTimeMillis());
//        //发送请求
//        api.sendReq(req);
//
//    }
//
//
//    private void shareToQQ() {
//        Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "我注册了52半径，发布了商品，来加入吧");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台");
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://banjing.xiaocool.net/uploads/ic_logo.png");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "52半径");
//        mTencent.shareToQQ(this, params, listener);
//    }
//
//    private void shareToQzone() {
//        Bundle params = new Bundle();
//        //分享类型
//        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
//        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "我注册了52半径，发布了商品，来加入吧");//必填
//        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "基于同城个人，商户服务 。商品购买。给个人，商户提供交流与服务平台");//选填
//        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_SHOP_H5 + userInfo.getUserId());//必填
//        ArrayList<String> images = new ArrayList<>();
//        images.add("http://banjing.xiaocool.net/uploads/ic_logo.png");
//        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
//        mTencent.shareToQzone(AdDetailActivity.this, params, listener);
//    }
//
//    public String convertIconToString(Bitmap bitmap) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] appicon = baos.toByteArray();// 转为byte数组
//        return Base64.encodeToString(appicon, Base64.DEFAULT);
//
//    }
//
//    private class BaseUiListener implements IUiListener {
//        @Override
//        public void onCancel() {
//            Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT)
//                    .show();
//        }
//
//        @Override
//        public void onError(UiError uiError) {
//            Toast.makeText(AdDetailActivity.this, uiError.errorMessage + "\n" + uiError.errorDetail,
//                    Toast.LENGTH_SHORT)
//                    .show();
//            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
//        }
//
//        @Override
//        public void onComplete(Object o) {
////            enableAction(enableActionShareQRCodeActivity.this.action);
//        }
//    }
//
//    private void initListeners() {
//        mCallback = new SurfaceHolder.Callback() {
//            public void surfaceCreated(SurfaceHolder holder) {
//                mPlayer.setDisplay(holder);
//            }
//
//            public void surfaceChanged(SurfaceHolder holder, int format,
//                                       int width, int height) {
//            }
//
//            public void surfaceDestroyed(SurfaceHolder holder) {
//            }
//        };
//        mPreparedListener = new MediaPlayer.OnPreparedListener() {
//            public void onPrepared(MediaPlayer mp) {
//
//                mController.setMediaPlayer(mPlayerControl);
//                mController
//                        .setAnchorView((FrameLayout) findViewById(R.id.video_surfacecontainer));
//                mPlayer.start();
//                if (position > 0) {
//                    mPlayer.seekTo(position);
//                }
//            }
//        };
//        mPlayerControl = new MediaplayerController.ControlOper() {
//            public void start() {
//                mPlayer.start();
//            }
//
//            public void pause() {
//                mPlayer.pause();
//            }
//
//            public int getDuration() {
//                return mPlayer.getDuration();
//            }
//
//            public int getCurPosition() {
//                return mPlayer.getCurrentPosition();
//            }
//
//            public void seekTo(int pos) {
//                mPlayer.seekTo(pos);
//            }
//
//            public boolean isPlaying() {
//                return mPlayer.isPlaying();
//            }
//
//            public int getBufPercent() {
//                return 0;
//            }
//
//            public boolean canPause() {
//                return true;
//            }
//
//            public boolean canSeekBackward() {
//                return true;
//            }
//
//            public boolean canSeekForward() {
//                return true;
//            }
//
//            public boolean isFullScreen() {
//                if (screenOrient() == 0) {
//                    return true;
//                }
//                return false;
//            }
//
//            /** 判断横竖屏 @return 1：竖 | 0：横 */
//            @SuppressWarnings("deprecation")
//            public int screenOrient() {
//                int orient = AdDetailActivity.this.getRequestedOrientation();
//                if (orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                        && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//                    WindowManager windowManager = AdDetailActivity.this
//                            .getWindowManager();
//                    Display display = windowManager.getDefaultDisplay();
//                    int screenWidth = display.getWidth();
//                    int screenHeight = display.getHeight();
//                    orient = screenWidth < screenHeight ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                            : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                }
//                return orient;
//            }
//
//            /** 使屏幕处于竖屏 */
//            public void verticalScreen() {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                setPortraitWidthHeight();
////                layout_video_back.setVisibility(View.VISIBLE);
////                layout_landscape_back.setVisibility(View.GONE);
////                scrollView_video.setVisibility(View.VISIBLE);
////                layout_video_bottom.setVisibility(View.VISIBLE);
////                lv_video.setVisibility(View.VISIBLE);
//            }
//
//            /** 使屏幕处于横屏 */
//            public void fullScreen() {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//
//setLandscapeWidthHeight();
////                layout_video_back.setVisibility(View.GONE);
////                layout_landscape_back.setVisibility(View.VISIBLE);
////                scrollView_video.setVisibility(View.GONE);
////                layout_video_bottom.setVisibility(View.GONE);
////                lv_video.setVisibility(View.GONE);
//            }
//        };
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        // TODO Auto-generated method stub
//        super.onSaveInstanceState(outState);
//        outState.putInt(CURPOSITION, mPlayer.getCurrentPosition());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onRestoreInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // 检测屏幕的方向：纵向或横向
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            System.out.println("当前屏幕=》》》》横屏");
//            // 当前为横屏， 在此处添加额外的处理代码
//            setLandscapeWidthHeight();
//            layout_video_back.setVisibility(View.GONE);
//            layout_landscape_back.setVisibility(View.VISIBLE);
////            scrollView_video.setVisibility(View.GONE);
////            layout_video_bottom.setVisibility(View.GONE);
////            lv_video.setVisibility(View.GONE);
//
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            System.out.println("当前屏幕=》》》》竖屏");
//            // 当前为竖屏， 在此处添加额外的处理代码
//            setPortraitWidthHeight();
//            layout_video_back.setVisibility(View.VISIBLE);
//            layout_landscape_back.setVisibility(View.GONE);
////            scrollView_video.setVisibility(View.VISIBLE);
////            layout_video_bottom.setVisibility(View.VISIBLE);
////            lv_video.setVisibility(View.VISIBLE);
//        }
//        // 检测实体键盘的状态：推出或者合上
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            // 实体键盘处于推出状态，在此处添加额外的处理代码
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//            // 实体键盘处于合上状态，在此处添加额外的处理代码
//        }
//    }
//
//    /**
//     * 当用户按下返回键的时候判断当前手机状态是横屏还是竖屏
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            if (mPlayerControl.screenOrient() == 0) {
//                mPlayerControl.fullScreen();
//                return false;
//            } else {
//                finish();
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /**
//     * 设置竖屏时候播放器的宽高
//     */
//    private void setPortraitWidthHeight() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int mSurfaceViewWidth = dm.widthPixels;
//        int mSurfaceViewHeight = dm.heightPixels;
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//        lp.width = mSurfaceViewWidth;
//        lp.height = mSurfaceViewHeight * 1 / 3;
//        layout_view_play.setLayoutParams(lp);
//    }
//
//    /**
//     * 设置横屏时候播放器的宽高
//     */
//    private void setLandscapeWidthHeight() {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int mSurfaceViewWidth = dm.widthPixels;
//        int mSurfaceViewHeight = dm.heightPixels;
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//        lp.width = mSurfaceViewWidth;
//        lp.height = mSurfaceViewHeight;
//        layout_view_play.setLayoutParams(lp);
//    }
//
//}
