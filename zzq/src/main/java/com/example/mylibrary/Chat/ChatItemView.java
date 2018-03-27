package com.example.mylibrary.Chat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mylibrary.R;
import com.example.mylibrary.View.BubbleImageView;
import com.example.mylibrary.View.CircleImageView;
import com.lqr.emoji.MoonUtils;
//import com.lqr.emoji.MoonUtils;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
////import com.bumptech.glide.request.RequestOptions;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by 赵自强 on 2017/8/24 9:50.
 * 这个类的用处
 */

public class ChatItemView extends LinearLayout {
    private Context mContext;
    private CircleImageView photoLeft;
    private TextView nicknameLeft;
    private BubbleTextView chatTextLeft;
    private com.example.mylibrary.View.BubbleImageView chatPictureLeft;
    private RelativeLayout leftAudio;
    private RelativeLayout rightAudio;
    private ImageView playLeft;
    private TextView timeLeft;
    private RelativeLayout chatVoiceLeft;
    private TextView nicknameRight;
    private BubbleTextView chatTextRight;
    private com.example.mylibrary.View.BubbleImageView  chatPictureRight;
    private TextView timeRight;
    private ImageView playRight;
    private RelativeLayout chatVoiceRight;
    private CircleImageView photoRight;
    private LinearLayout chatLeft;
    private LinearLayout chatRight;
    private BubbleTextView voiceBKLeft;
    private BubbleTextView voiceBKRight;
    private TextView TimeL,TimeR;
    public ChatItemView(Context context) {
        super(context);
    }
    private int showNickname = ShowNickname;
    private NicknameConfigure nicknameConfigure;
    private int leftPhotoSize = 0;
    private int rightPhotoSize = 0;
    private int defaultLeftChatTextSize = DefaultLeftChatTextSize ;
    private int defaultRightChatTextSize = DefaultRightChatTextSize;
    private int defaultNicknameTextSizeLeft = DefaultNicknameTextSizeLeft;
    private int defaultNicknameTextSizeRight = DefaultNicknameTextSizeRight;
    private int defaultBackgroundRight = DefaultBackgroundRight;
    private int defaultBackgroundLeft = DefaultBackgroundLeft;
    private int defaultNicknameTextColorLeft = DefaultNicknameTextColorLeft;
    private int defaultNicknameTextColorRight = DefaultNicknameTextColorRight;
    private int defaultChatTextColorLeft = DefaultChatTextColorLeft ;
    private int defaultChatTextColorRight = DefaultChatTextColorRight;
    private static  final int ShowNickname = 0;
    private static  final float LeftPhotoSize = 50;
    private static  final float RightPhotoSize = 50;
    private static  final int DefaultLeftChatTextSize = 14;
    private static  final int DefaultRightChatTextSize = 14;
    private static  final int DefaultNicknameTextSizeLeft = 12;
    private static  final int DefaultNicknameTextSizeRight = 12;
    private static  final int DefaultBackgroundRight = Color.parseColor("#7EC0EE");
    private static  final int DefaultBackgroundLeft = Color.parseColor("#9BE5B4");
    private static final int DefaultNicknameTextColorLeft = Color.BLACK;
    private static final int DefaultNicknameTextColorRight =  Color.BLACK;
    private static final  int DefaultChatTextColorLeft =  Color.BLACK;
    private static final  int DefaultChatTextColorRight = Color.BLACK;
    private ChatType MessageType = ChatType.textLeft;
    private OnChatViewClickListener listener;
//    private ImageLoader imageLoader  ;
//    private DisplayImageOptions options = new DisplayImageOptions.Builder().
//            showImageOnLoading(R.drawable.wait).showImageOnFail(R.mipmap.ic_launcher)
//            .cacheInMemory(true).cacheOnDisc(true).build();
//    private RequestOptions option;
    public ChatItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext =context;
        LayoutInflater.from(context).inflate(R.layout.chat_item_view, this); //创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
//                .writeDebugLogs() //打印log信息
//                .build();
//        ImageLoader.getInstance().init(configuration);
//        imageLoader = ImageLoader.getInstance();
//        option = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.wait)
//                .priority(Priority.HIGH)
//                .diskCacheStrategy(DiskCacheStrategy.NONE);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ChatItemView));
        initView();
    }

    public void setOnChatViewClickListener(OnChatViewClickListener listener) {
        this.listener = listener;
        initData();
    }

    public void setLeftTextType(String nickname, String text , String path){
        MessageType = ChatType.textLeft;
        initData();
        MoonUtils.identifyFaceExpression(mContext, chatTextLeft, text, ImageSpan.ALIGN_BOTTOM);
        nicknameLeft.setText(nickname);
        LoadImage(path,photoLeft);
    }
    public void setRightTextType(String nickname,String text,String path){
        MessageType = ChatType.textRight;
        initData();
        nicknameRight.setText(nickname);
        MoonUtils.identifyFaceExpression(mContext, chatTextRight, text, ImageSpan.ALIGN_BOTTOM);
        LoadImage(path,photoRight);
    }
    public void setLeftPictureType(String nickname,String PicturePath,String photoPath){
        MessageType = ChatType.pictureLeft;
        initData();
        nicknameLeft.setText(nickname);
        LoadImage(photoPath,photoLeft);
        LoadImage(PicturePath,chatPictureLeft);
    }
    public void setRightPictureType(String nickname,String PicturePath,String photoPath){
        MessageType = ChatType.pictureRight;
        initData();
        nicknameRight.setText(nickname);
        LoadImage(photoPath,photoRight);
        LoadImage(PicturePath,chatPictureRight);
    }
    public void setLeftVoiceType(String nickname,String duration,String photoPath){
        MessageType = ChatType.voiceLeft;
        initData();
        nicknameLeft.setText(nickname);
        timeLeft.setText(showTime(Integer.valueOf(duration)));
        LoadImage(photoPath,photoLeft);
    }
    public void setRightVoiceType(String nickname ,String duration,String photoPath){
        MessageType = ChatType.voiceRight;
        initData();
        nicknameLeft.setText(nickname);
        timeRight.setText(showTime(Integer.valueOf(duration)));
        LoadImage(photoPath,photoRight);
    }
    private void LoadImage(String path, ImageView view){
        Glide.with(mContext)
                .load(path)
                .into(view);
//        imageLoader.displayImage(path, view, options);
    }
    private void parseAttributes(TypedArray array) {
        showNickname = array.getInt(R.styleable.ChatItemView_show_nickname,ShowNickname);
        leftPhotoSize  = array.getDimensionPixelSize(R.styleable.ChatItemView_LeftPhotoSize,new Float(LeftPhotoSize).intValue()) ;
        rightPhotoSize = array.getDimensionPixelSize(R.styleable.ChatItemView_RightPhotoSize,new Float(RightPhotoSize).intValue());
        defaultLeftChatTextSize = array.getDimensionPixelSize(R.styleable.ChatItemView_DefaultLeftChatTextSize,DefaultLeftChatTextSize );
        defaultRightChatTextSize = array.getDimensionPixelSize(R.styleable.ChatItemView_DefaultLeftChatTextSize,DefaultRightChatTextSize);
        defaultNicknameTextSizeLeft = array.getDimensionPixelSize(R.styleable.ChatItemView_DefaultNicknameTextSizeLeft,DefaultNicknameTextSizeLeft);
        defaultNicknameTextSizeRight = array.getDimensionPixelSize(R.styleable.ChatItemView_DefaultNicknameTextSizeRight,DefaultNicknameTextSizeRight);
        defaultBackgroundRight = array.getColor(R.styleable.ChatItemView_DefaultBackgroundRight ,DefaultBackgroundRight);
        defaultBackgroundLeft = array.getColor(R.styleable.ChatItemView_DefaultBackgroundLeft,DefaultBackgroundLeft);
        defaultNicknameTextColorLeft = array.getColor(R.styleable.ChatItemView_DefaultNicknameTextColorLeft,DefaultNicknameTextColorLeft);
        defaultNicknameTextColorRight = array.getColor(R.styleable.ChatItemView_DefaultNicknameTextColorRight,DefaultNicknameTextColorRight);
        defaultChatTextColorLeft = array.getColor(R.styleable.ChatItemView_DefaultChatTextColorLeft,DefaultChatTextColorLeft );
        defaultChatTextColorRight = array.getColor(R.styleable.ChatItemView_DefaultChatTextColorRight,DefaultChatTextColorRight);
//haha
        array.recycle();
    }
    public void setLeftTimeShow(String time){
        TimeL.setVisibility(VISIBLE);
        TimeL.setText(time);
    }
    public void setRightTimeShow(String time){
        TimeR.setVisibility(VISIBLE);
        TimeR.setText(time);
    }
    public void setTimeGone(){
        TimeR.setVisibility(GONE);
        TimeL.setVisibility(GONE);
    }
    private void initView() {
        leftAudio= (RelativeLayout) findViewById(R.id.chatVoiceLeft);
        rightAudio= (RelativeLayout) findViewById(R.id.chatVoiceRight);
        TimeL = (TextView) findViewById(R.id.timeL);
        TimeR = (TextView) findViewById(R.id.timeR);
        photoLeft = (CircleImageView) findViewById(R.id.photoLeft);
        nicknameLeft = (TextView) findViewById(R.id.nicknameLeft);
        chatTextLeft = (BubbleTextView) findViewById(R.id.chatTextLeft);
        chatPictureLeft = (com.example.mylibrary.View.BubbleImageView) findViewById(R.id.chatPictureLeft);
        playLeft = (ImageView) findViewById(R.id.playLeft);
        timeLeft = (TextView) findViewById(R.id.timeLeft);
        chatVoiceLeft = (RelativeLayout) findViewById(R.id.chatVoiceLeft);
        nicknameRight = (TextView) findViewById(R.id.nicknameRight);
        chatTextRight = (BubbleTextView) findViewById(R.id.chatTextRight);
        chatPictureRight = (BubbleImageView) findViewById(R.id.chatPictureRight);
        timeRight = (TextView) findViewById(R.id.timeRight);
        playRight = (ImageView) findViewById(R.id.playRight);
        chatVoiceRight = (RelativeLayout) findViewById(R.id.chatVoiceRight);
        photoRight = (CircleImageView) findViewById(R.id.photoRight);
        photoLeft = (CircleImageView) findViewById(R.id.photoLeft);
        photoLeft = (CircleImageView) findViewById(R.id.photoLeft);
        photoLeft = (CircleImageView) findViewById(R.id.photoLeft);
        photoLeft = (CircleImageView) findViewById(R.id.photoLeft);
        photoLeft = (CircleImageView) findViewById(R.id.photoLeft);
        chatLeft = (LinearLayout) findViewById(R.id.chatLeft);
        chatRight = (LinearLayout) findViewById(R.id.chatRight);
        voiceBKLeft = (BubbleTextView) findViewById(R.id.voiceBKLeft);
        voiceBKRight = (BubbleTextView) findViewById(R.id.voiceBKRight);
        initSetUp();

    }

    private void initSetUp() {
        /*左右头像大小*/
        LayoutParams leftLayoutParams = (LayoutParams) photoLeft.getLayoutParams();
        leftLayoutParams.height = dip2px(leftPhotoSize);
        leftLayoutParams.width = dip2px(leftPhotoSize);
        photoLeft.setLayoutParams(leftLayoutParams);
        LayoutParams rightLayoutParams = (LayoutParams) photoRight.getLayoutParams();
        rightLayoutParams.width = dip2px(rightPhotoSize);
        rightLayoutParams.height = dip2px(rightPhotoSize);
        photoRight.setLayoutParams(rightLayoutParams);
        /*左右文字框字体大小*/
        chatTextLeft.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,dip2px(defaultLeftChatTextSize));
        timeLeft.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,dip2px(defaultLeftChatTextSize));
        chatTextRight.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,dip2px(defaultRightChatTextSize));
        timeRight.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,dip2px(defaultRightChatTextSize));
        /*左右昵称框大小*/
        nicknameLeft.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,dip2px(defaultNicknameTextSizeLeft));
        nicknameRight.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,dip2px(defaultNicknameTextSizeRight));
        /*聊天bubble设置背景色*/
        chatTextLeft.setBubbleColor(defaultBackgroundLeft);
        chatTextRight.setBubbleColor(defaultBackgroundRight);
        voiceBKLeft.setBubbleColor(defaultBackgroundLeft);
        voiceBKRight.setBubbleColor(defaultBackgroundRight);
        /*
        /*设置字体颜色*/
        nicknameLeft.setTextColor(defaultNicknameTextColorLeft);
        nicknameRight.setTextColor(defaultNicknameTextColorRight);
        chatTextLeft.setTextColor(defaultChatTextColorLeft);
        chatTextRight.setTextColor(defaultChatTextColorRight);
        timeLeft.setTextColor(defaultChatTextColorLeft);
        timeRight.setTextColor(defaultChatTextColorRight);
        initData();
    }


    private void initData() {
        if (showNickname == 0){
            nicknameConfigure = NicknameConfigure.noShowEvery;
        }else  if (showNickname ==1){
            nicknameConfigure = NicknameConfigure.showLeft;
        }else if (showNickname == 2){
            nicknameConfigure = NicknameConfigure.showRight;
        }else {
            nicknameConfigure = NicknameConfigure.showEvery;
        }
        switch (MessageType){
            case textLeft:
                chatLeft.setVisibility(VISIBLE);
                chatRight.setVisibility(GONE);
                chatTextLeft.setVisibility(VISIBLE);
                chatPictureLeft.setVisibility(GONE);
                chatVoiceLeft.setVisibility(GONE);
                if (listener!= null){
                    chatTextLeft.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            listener.OnTextViewLongClicked(view);
                            return false;
                        }
                    });
                    photoLeft.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPhotoClicked(view, photoType.left);
                        }
                    });
                }
                break;
            case textRight:
                chatLeft.setVisibility(GONE);
                chatRight.setVisibility(VISIBLE);
                chatTextRight.setVisibility(VISIBLE);
                chatPictureRight.setVisibility(GONE);
                chatVoiceRight.setVisibility(GONE);
                if (listener!= null){
                    chatTextRight.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            listener.OnTextViewLongClicked(view);
                            return false;
                        }
                    });
                    photoRight.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPhotoClicked(view, photoType.right);
                        }
                    });
                }
                break;
            case pictureLeft:
                Log.i("看","左图");
                chatLeft.setVisibility(VISIBLE);
                chatRight.setVisibility(GONE);
                chatTextLeft.setVisibility(GONE);
                chatPictureLeft.setVisibility(VISIBLE);
                chatVoiceLeft.setVisibility(GONE);
                if (listener!= null){
                    Log.i("看","listener图片不为空");
                    chatPictureLeft.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                listener.OnPictureClickedListener(view);
                        }
                    });
                    chatPictureLeft.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            listener.OnPictureLongClickedListener(view);
                            return false;
                        }
                    });
                    photoLeft.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPhotoClicked(view, photoType.left);
                        }
                    });
                }
                break;
            case pictureRight:
                chatLeft.setVisibility(GONE);
                chatRight.setVisibility(VISIBLE);
                chatTextRight.setVisibility(GONE);
                chatPictureRight.setVisibility(VISIBLE);
                chatVoiceRight.setVisibility(GONE);
                if (listener!= null){
                    chatPictureRight.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPictureClickedListener(view);
                        }
                    });
                    chatPictureRight.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            listener.OnPictureLongClickedListener(view);
                            return false;
                        }
                    });
                    photoRight.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPhotoClicked(view, photoType.right);
                        }
                    });
                }
                break;
            case voiceLeft:
                chatLeft.setVisibility(VISIBLE);
                chatRight.setVisibility(GONE);
                chatTextLeft.setVisibility(GONE);
                chatPictureLeft.setVisibility(GONE);
                chatVoiceLeft.setVisibility(VISIBLE);
                if (listener!= null){
                    leftAudio.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.OnPlayButtonClickedListener(v,photoType.left);
                        }
                    });
                    photoLeft.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPhotoClicked(view, photoType.left);
                        }
                    });
                }
                break;
            case voiceRight:
                chatLeft.setVisibility(GONE);
                chatRight.setVisibility(VISIBLE);
                chatTextRight.setVisibility(GONE);
                chatPictureRight.setVisibility(GONE);
                chatVoiceRight.setVisibility(VISIBLE);
                if (listener!= null){
                    rightAudio.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.OnPlayButtonClickedListener(v,photoType.right);
                        }
                    });
                    photoRight.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.OnPhotoClicked(view, photoType.right);
                        }
                    });
                }
                break;
        }
        switch (nicknameConfigure){
            case noShowEvery:
                nicknameLeft.setVisibility(INVISIBLE);
                nicknameRight.setVisibility(VISIBLE);
                break;
            case showLeft:
                nicknameLeft.setVisibility(VISIBLE);
                nicknameRight.setVisibility(VISIBLE);
                break;
            case showRight:
                nicknameLeft.setVisibility(INVISIBLE);
                nicknameRight.setVisibility(VISIBLE);
                break;
            case showEvery:
                nicknameLeft.setVisibility(VISIBLE);
                nicknameRight.setVisibility(VISIBLE);
                break;
        }
        if (listener != null){

        }
    }

    public void setNicknameConfigure(NicknameConfigure nicknameConfigure) {
        this.nicknameConfigure = nicknameConfigure;
        initSetUp();
    }

    public void setLeftPhotoSize(int leftPhotoSize) {
        this.leftPhotoSize = leftPhotoSize;
        initSetUp();
    }

    public void setRightPhotoSize(int rightPhotoSize) {
        this.rightPhotoSize = rightPhotoSize;
        initSetUp();
    }

    public void setDefaultLeftChatTextSize(int defaultLeftChatTextSize) {
        this.defaultLeftChatTextSize = defaultLeftChatTextSize;
        initSetUp();
    }

    public void setDefaultRightChatTextSize(int defaultRightChatTextSize) {
        this.defaultRightChatTextSize = defaultRightChatTextSize;
        initSetUp();
    }

    public void setDefaultNicknameTextSizeLeft(int defaultNicknameTextSizeLeft) {
        this.defaultNicknameTextSizeLeft = defaultNicknameTextSizeLeft;
        initSetUp();
    }

    public void setDefaultNicknameTextSizeRight(int defaultNicknameTextSizeRight) {
        this.defaultNicknameTextSizeRight = defaultNicknameTextSizeRight;
        initSetUp();
    }

    public void setDefaultBackgroundRight(int defaultBackgroundRight) {
        this.defaultBackgroundRight = defaultBackgroundRight;
        initSetUp();
    }

    public void setDefaultBackgroundLeft(int defaultBackgroundLeft) {
        this.defaultBackgroundLeft = defaultBackgroundLeft;
        initSetUp();
    }

    public void setDfaultNicknameTextColorLeft(int dfaultNicknameTextColorLeft) {
        this.defaultNicknameTextColorLeft = dfaultNicknameTextColorLeft;
        initSetUp();
    }

    public void setDefaultNicknameTextColorRight(int defaultNicknameTextColorRight) {
        this.defaultNicknameTextColorRight = defaultNicknameTextColorRight;
        initSetUp();
    }

    public void setDefaultChatTextColorLeft(int defaultChatTextColorLeft) {
        this.defaultChatTextColorLeft = defaultChatTextColorLeft;
        initSetUp();
    }

    public void setDefaultChatTextColorRight(int defaultChatTextColorRight) {
        this.defaultChatTextColorRight = defaultChatTextColorRight;
        initSetUp();
    }



    enum  NicknameConfigure{
         noShowEvery ,
         showLeft ,
         showRight ,
         showEvery ;
    }
    private  int dip2px( float dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    private String showTime(int v) {
        int fen = v /60 == 0 ? 0 : v / 60 ;
        int miao = v %60 ==0 ? 0: v % 60;
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

}
