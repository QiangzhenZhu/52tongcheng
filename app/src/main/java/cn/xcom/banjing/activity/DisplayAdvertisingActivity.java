package cn.xcom.banjing.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.WXpay.Constants;
import cn.xcom.banjing.adapter.AdCommentListAdapter;
import cn.xcom.banjing.adapter.CommentsListAdapter;
import cn.xcom.banjing.adapter.ViewPageAdapter;
import cn.xcom.banjing.bean.ADInfo;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.bean.CommentInfo;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.share.ShareHelper;
import cn.xcom.banjing.utils.MyImageLoader;
import cn.xcom.banjing.utils.RoundImageView;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.SmoothImageView;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.utils.ToastUtils;
import cn.xcom.banjing.view.CommentPopupWindow;
import cn.xcom.banjing.view.SharePopupWindow;
//import me.zhanghai.android.materialprogressbar.Interpolators;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

/**
 * Created by mac on 2017/10/12.
 */

public class DisplayAdvertisingActivity extends BaseActivity implements View.OnClickListener {
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
    private Context context;
    private List addViewList;//添加图片的list
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private RelativeLayout rl_back;
    private LinearLayout ll_down;
    private TextView down_time,tvContent,timetext,tv_username,tvRedpacket ,tvLikeText,tvPriase ;
    private Convenience AdInfo;
    private LinearLayout rl_share;
    private RecyclerView chatAdRecycleView;
    private LinearLayout rl_comment;
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
    private PopupWindow commentPopupWindow;
    private AdCommentListAdapter adapter;
    private List<CommentInfo> commentInfos;
    //图片轮播的Url
    private ArrayList<String> imageUrls;
    private List<ImageView> imageViews;
    private int packetTime;
    private LinearLayout layoutViewPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_image_detail);
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
        tvRedpacket= (TextView) findViewById(R.id.tv_packet_count);
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
        tvLikeText= (TextView) findViewById(R.id.trend_item_tv_like);
        tvPriase= (TextView) findViewById(R.id.trend_item_tv_praise);
        rl_share = (LinearLayout) findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);
        rl_comment= (LinearLayout) findViewById(R.id.rl_chat);
        rl_comment.setOnClickListener(this);
        btnlike=(ImageView)findViewById(R.id.iv_like);
        btnlike.setOnClickListener(this);
        AdInfo= (Convenience) getIntent().getSerializableExtra("adinfo");
        chatAdRecycleView= (RecyclerView) findViewById(R.id.rv_ad_chat);
        if(AdInfo!=null){
            if (AdInfo.getComment()!=null&&AdInfo.getComment().size()>0){
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                chatAdRecycleView.setLayoutManager(linearLayoutManager);
                commentInfos=  AdInfo.getComment();
                adapter=new AdCommentListAdapter(commentInfos);
                chatAdRecycleView.setAdapter(adapter);
            }
            tvContent.setText(AdInfo.getContent());
            userphoto=AdInfo.getPhoto();
            username=AdInfo.getName();
            if ("".equals(AdInfo.getTouchcount())) {
                tvPriase.setText("0");
            }else {
                tvPriase.setText(AdInfo.getTouchcount());
            }
            tv_username.setText(username);
            tvRedpacket.setText(AdInfo.getRedpacket());
            tvLikeText.setText(AdInfo.getLike().size()+"");
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + userphoto, user_photo);
            imageUrls=new ArrayList<>();
            imageViews=new ArrayList<>();
            for (int i = 0; i <AdInfo.getPic().size() ; i++) {
                String imageUrl=AdInfo.getPic().get(i).getPictureurl();
                imageUrls.add(NetConstant.NET_DISPLAY_IMG + imageUrl);
                ImageView imageView=new ImageView(context);
                Bitmap bmap = imageView.getDrawingCache();
                imageView.setImageBitmap(bmap);
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + imageUrl, imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageViews.add(imageView);
            };

            viewPageAdapter=new ViewPageAdapter(imageUrls,imageViews,context);
            viewPager.setAdapter(viewPageAdapter);
            timer.start();
        }else{

            finish();
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_share:
                showPopFormBottom(view);
                break;
            case R.id.iv_like:
                setHadLikePost(AdInfo,AdInfo.getComment().size());
                break;
            case R.id.rl_chat:
                showPopComment();
                break;

        }
    }
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
                    ToastUtils.showToast(DisplayAdvertisingActivity.this, "微信好友");
                    setting();
                    break;
                case R.id.dongtai:
                    ToastUtils.showToast(DisplayAdvertisingActivity.this, "微信朋友圈");
                    history();
                    break;
                case R.id.qq:
                    ToastUtils.showToast(DisplayAdvertisingActivity.this, "QQ");
                    shareToQQ();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.kongjian:
                    ToastUtils.showToast(DisplayAdvertisingActivity.this, "QQ空间");
                    shareToQzone();
                    takePhotoPopWin.dismiss();
                    break;
                case R.id.zhifubao:
                    ToastUtils.showToast(DisplayAdvertisingActivity.this, "支付宝");
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
            Toast.makeText(DisplayAdvertisingActivity.this,"输入内容不能为空，请重新输入",Toast.LENGTH_SHORT)
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
                        Toast.makeText(DisplayAdvertisingActivity.this, "评论成功", Toast.LENGTH_LONG).show();
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
                        if (adapter.getItemCount() == 0){
                            adapter.notifyDataSetChanged();
                        }

                        adapter.notifyItemInserted(commentInfos.size());


                    } else {

                        Toast.makeText(DisplayAdvertisingActivity.this, "评论失败，请稍后重试", Toast.LENGTH_LONG).show();
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
        SingleVolleyRequest.getInstance(DisplayAdvertisingActivity.this).addToRequestQueue(request);
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
                Toast.makeText(DisplayAdvertisingActivity.this,"发生未知错误，请重试",Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("packetid",AdInfo.getPacketId());
        SingleVolleyRequest.getInstance(DisplayAdvertisingActivity.this).addToRequestQueue(request);
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
    /**
     * 微信分享网页
     */
    private void shareWX() {
        //创建一个WXWebPageObject对象，用于封装要发送的Url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = NetConstant.SHARE_SHOP_H5 + userInfo.getUserId();
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
        webPageObject.webpageUrl = NetConstant.SHARE_SHOP_H5 + userInfo.getUserId();

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
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, NetConstant.SHARE_AD + AdInfo.getMid());
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
        mTencent.shareToQzone(DisplayAdvertisingActivity.this, params, listener);
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
            Toast.makeText(DisplayAdvertisingActivity.this, uiError.errorMessage + "\n" + uiError.errorDetail,
                    Toast.LENGTH_SHORT)
                    .show();
            Log.d("QQshare", uiError.errorMessage + "\n" + uiError.errorDetail);
        }

        @Override
        public void onComplete(Object o) {
//            enableAction(enableActionShareQRCodeActivity.this.action);
        }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
