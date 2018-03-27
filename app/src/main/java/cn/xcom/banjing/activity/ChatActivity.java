package cn.xcom.banjing.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.mylibrary.RecordAudioView.PlayRecordAudioView;
import com.example.mylibrary.RecordAudioView.PlayRecordListener;
import com.example.mylibrary.RecordAudioView.QRecordAudioView;
import com.example.mylibrary.RecordAudioView.RecordInterfaceListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lqr.emoji.EmotionLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.chat.ChatDataItemBean;
import cn.xcom.banjing.chat.ChatItemBean;
import cn.xcom.banjing.chat.ChatListAdapter;
import cn.xcom.banjing.chat.ExampleUtil;
import cn.xcom.banjing.chat.ImagePickerUtils;
import cn.xcom.banjing.chat.UploadImageUtils;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.fragment.FriendFragment;
import cn.xcom.banjing.kc.KBaseActivity;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.net.ResponseHelper;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.utils.ToastUtils;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@RuntimePermissions
public class ChatActivity extends KBaseActivity implements View.OnLayoutChangeListener {
    private final static int showingTextCorlor = Color.parseColor("#ff99cc00");
    private final static int noShowingTextCorlor = Color.parseColor("#ff33b5e5");
    private final static int noShowingBkResource = R.drawable.iv_trigger_emotion_keyboard;
    private final static int showingBkResource = R.drawable.iv_green_emotion_keyboard;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "newMessage";
    public static final String KEY_EXTRAS = "extras";
    public static final int REQUEST_IMAGE_PICKER = 1000;
    public final static int REQUEST_TAKE_PHOTO = 1001;
    @BindView(R.id.btn_exit)
    ImageView btnExit;
    @BindView(R.id.ivAlbum)
    ImageView ivAlbum;
    @BindView(R.id.ivShot)
    ImageView ivShot;
    @BindView(R.id.ivLocation)
    ImageView ivLocation;
    @BindView(R.id.rlLocation)
    AutoRelativeLayout rlLocation;
    @BindView(R.id.ivRedPack)
    ImageView ivRedPack;
    @BindView(R.id.rlRedPacket)
    AutoRelativeLayout rlRedPacket;
    private MessageReceiver mMessageReceiver;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.custom_btn)
    ImageView customBtn;
    @BindView(R.id.custom_text_btn)
    TextView customTextBtn;
    @BindView(R.id.common_app_bar)
    RelativeLayout commonAppBar;
    @BindView(R.id.recylerView)
    RecyclerView recylerView;
    @BindView(R.id.voice)
    TextView voice;
    @BindView(R.id.et_face_text_emotion)
    EditText etFaceTextEmotion;
    @BindView(R.id.recordVoiceView)
    QRecordAudioView recordVoiceView;
    @BindView(R.id.emotion)
    TextView emotion;
    @BindView(R.id.ivMore)
    ImageView ivMore;
    @BindView(R.id.btn_send_msg)
    Button btnSendMsg;
    @BindView(R.id.llMore)
    LinearLayout mLlMore;
    @BindView(R.id.rlAlbum)
    AutoRelativeLayout rlAlbum;
    @BindView(R.id.rlTakePhoto)
    AutoRelativeLayout rlTakePhoto;
    @BindView(R.id.elEmotion)
    EmotionLayout elEmotion;
    @BindView(R.id.playVoiceView)
    PlayRecordAudioView playVoiceView;
    @BindView(R.id.root_view)
    LinearLayout rootView;
    private ChatListAdapter adapter;
    private Context context;
    private ImagePickerUtils imagePickerUtils;//图片选择器
    private List<ChatDataItemBean> list;
    private List<ChatItemBean> chatItemBeanList;
    UserInfo userInfo;
    private MediaPlayer mediaPlayer;
    private String recordPath;
    private String myId, chat_Id;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private String chatName = "";
    private String receive_type = "";
    private String recordName = "";
    private int duration = 0;
    private ArrayList<String> listPicturePath = new ArrayList<>();

    @Override
    public int getContentViewId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

    }

    public void initView() {
        context = this;
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        imagePickerUtils = new ImagePickerUtils(1);
        userInfo = new UserInfo(context);
        chatItemBeanList = new ArrayList<>();
        elEmotion.attachEditText(etFaceTextEmotion);
        mediaPlayer = new MediaPlayer();
        chatName = getIntent().getStringExtra("name");
        receive_type = getIntent().getStringExtra("receive_type");
        title.setText(chatName);
        myId = userInfo.getUserId();
        chat_Id = getIntent().getStringExtra("id");
        if ("2".equals(receive_type)) {
            customTextBtn.setVisibility(View.VISIBLE);
            btnExit.setVisibility(View.VISIBLE);
        } else {
            customTextBtn.setVisibility(View.GONE);
            btnExit.setVisibility(View.GONE);

        }
        registerMessageReceiver();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ChatListAdapter(chatItemBeanList, context, R.layout.chat_item_layout, listPicturePath);
        recylerView.setAdapter(adapter);
        getChatList();
//        selectPictureView.setQPhotoListener(new QPhotoListener() {
//            @Override
//            public void onAlbumClicked() {
//                Log.i("看QPhotoListener", "去相册");
//            }
//
//            @Override
//            public void OnSendBtnClicked(int picCount, List<PictureDataBean> selectedPicList) {
//                Log.i("看QPhotoListener", "发送" + picCount + "张图片");
//            }
//
//            @Override
//            public void OnCropClicked(String picPath) {
//                Log.i("看QPhotoListener", "裁剪图片地址：" + picPath);
//            }
//        });
        customTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, GroupMemberActivity.class);
                intent.putExtra("groupId", chat_Id);
                startActivity(intent);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertView mAlertView = new AlertView("提示", "你确定要退出群聊吗？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, final int position) {
                        if (position == 0){
                            exitGroup();
                        }else {

                        }
                    }
                }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(Object o) {

                    }
                });
                mAlertView.show();
            }
        });

        etFaceTextEmotion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    if (recordVoiceView.isShown()) {
                        recordVoiceView.setVisibility(View.GONE);
                    }
                    if (mLlMore.isShown()) {
                        mLlMore.setVisibility(View.GONE);
                    }
                    if (elEmotion.isShown()) {
                        elEmotion.setVisibility(View.GONE);

                    }
                    if (playVoiceView.isShown()) {
                        playVoiceView.setVisibility(View.GONE);
                    }
                }
            }
        });


        etFaceTextEmotion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!etFaceTextEmotion.getText().toString().equals("")) {
                    btnSendMsg.setVisibility(View.VISIBLE);
                    ivMore.setVisibility(View.GONE);
                } else {
                    btnSendMsg.setVisibility(View.GONE);
                    ivMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        recordVoiceView.setRecordInterfaceListener(recordInterfaceListener);
        playVoiceView.setRecordInterfaceListener(playRecordListener);
    }


    @Override
    public void onBackPressed() {
        if (recordVoiceView.isShown()) {
            recordVoiceView.setVisibility(View.GONE);
            etFaceTextEmotion.requestFocus();
            return;
        }
        if (mLlMore.isShown()) {
            mLlMore.setVisibility(View.GONE);
            etFaceTextEmotion.requestFocus();
            return;
        }
        if (elEmotion.isShown()) {
            elEmotion.setVisibility(View.GONE);
            etFaceTextEmotion.requestFocus();
            return;
        }
        if (playVoiceView.isShown()) {
            playVoiceView.setVisibility(View.GONE);
            etFaceTextEmotion.setClickable(true);
        } //如果按下返回键返回，正在播放两人聊天的语音，则停止播放
        if (adapter.playerManager.isPlaying()) {
            adapter.playerManager.stop();
        }
        super.onBackPressed();
    }

    @OnClick({R.id.back, R.id.btn_send_msg, R.id.voice, R.id.ivMore, R.id.rlAlbum, R.id.emotion, R.id.rlTakePhoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (adapter.playerManager.isPlaying()) {
                    adapter.playerManager.stop();
                }
                finish();
                break;
            case R.id.btn_send_msg:
                sendMessage("0", etFaceTextEmotion.getText().toString(), "");
                break;
            case R.id.voice:
//                showVoice();
                ChatActivityPermissionsDispatcher.showVoiceWithCheck(this);
                break;
            case R.id.ivMore:
                showMore();
                break;
            case R.id.rlAlbum:
                imagePickerUtils.openSingleAblum(context, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        Log.i("看", " changdu " + resultList.size());
                        UploadImageUtils.pushImage(resultList.get(0).getPhotoPath(), NetConstant.gy_upload_image, new UploadImageUtils.OnUploadListener() {
                            @Override
                            public void onSuccess(String imgName) {
                                Log.i("看", " " + imgName);
                                sendMessage("1", "", imgName);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("看", " " + e.toString());
                            }
                        });
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });
                break;
            case R.id.emotion:
                showEmoj();
                break;
            case R.id.rlTakePhoto:
                imagePickerUtils.openSingleCamera(context, new GalleryFinal.OnHanlderResultCallback() {
                    @Override
                    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                        UploadImageUtils.pushImage(resultList.get(0).getPhotoPath(), NetConstant.gy_upload_image, new UploadImageUtils.OnUploadListener() {
                            @Override
                            public void onSuccess(String imgName) {
                                Log.i("看", " " + imgName);
                                sendMessage("1", "", imgName);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("看", " " + e.toString());
                            }
                        });
                    }

                    @Override
                    public void onHanlderFailure(int requestCode, String errorMsg) {

                    }
                });

        }
    }

    //显示语音控件
    @NeedsPermission({RECORD_AUDIO, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE})
    public void showVoice() {
        if (recordVoiceView.isShown()) {
            recordVoiceView.setVisibility(View.GONE);
            etFaceTextEmotion.requestFocus();
        } else {
            if (mLlMore.isShown()) {
                mLlMore.setVisibility(View.GONE);
                etFaceTextEmotion.requestFocus();
            }
            if (elEmotion.isShown()) {
                elEmotion.setVisibility(View.GONE);
                etFaceTextEmotion.requestFocus();
            }
            recordVoiceView.setVisibility(View.VISIBLE);
            etFaceTextEmotion.clearFocus();
            recylerView.scrollToPosition(recylerView.getAdapter().getItemCount() - 1);
        }

    }

    //显示更多
    private void showMore() {
        if (mLlMore.isShown()) {
            mLlMore.setVisibility(View.GONE);
            etFaceTextEmotion.requestFocus();
        } else {
            if (recordVoiceView.isShown()) {
                recordVoiceView.setVisibility(View.GONE);
                etFaceTextEmotion.requestFocus();
            }
            if (elEmotion.isShown()) {
                elEmotion.setVisibility(View.GONE);
                etFaceTextEmotion.requestFocus();
            }
            mLlMore.setVisibility(View.VISIBLE);
            etFaceTextEmotion.clearFocus();
            recylerView.scrollToPosition(recylerView.getAdapter().getItemCount() - 1);
        }

    }

    //显示表情
    private void showEmoj() {
        if (elEmotion.isShown()) {
            elEmotion.setVisibility(View.GONE);
            etFaceTextEmotion.requestFocus();
        } else {
            if (recordVoiceView.isShown()) {
                recordVoiceView.setVisibility(View.GONE);
                etFaceTextEmotion.requestFocus();
            }
            if (mLlMore.isShown()) {
                mLlMore.setVisibility(View.GONE);
                etFaceTextEmotion.requestFocus();
            }
            recylerView.scrollToPosition(recylerView.getAdapter().getItemCount() - 1);
            elEmotion.setVisibility(View.VISIBLE);
            etFaceTextEmotion.clearFocus();
        }

    }

    private void getChatList() {
        Log.d("getchatdatalist", NetConstant.GET_CHAT_DATA + "send_uid=" + myId + "receive_uid=" + chat_Id);
        String sendType = "";
        if ("2".equals(receive_type)){
            sendType = "2";
        }else {
            sendType = "1";
        }


        OkHttpUtils.post().url(NetConstant.GET_CHAT_DATA)
//                .addParams("send_uid", "2")
//                .addParams("receive_uid", "1")
                .addParams("send_uid", myId)
                .addParams("receive_uid", chat_Id)
                .addParams("type",sendType)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.d("刷新列表", response);
                            ResponseHelper rh = new ResponseHelper(response);
                            if (rh.isSuccess()) {
                                Log.d("hadchatlist", rh.getData().toString());
                                List<ChatItemBean> l = new Gson().fromJson(rh.getData(), new TypeToken<List<ChatItemBean>>() {
                                }.getType());
                                if (l.size() > 0) {
//                                    refashlist.addAll(list);
                                    listPicturePath.clear();
                                    listPicturePath = new ArrayList<>();
                                    for (int i = 0; i < l.size(); i++) {
                                        if (l.get(i).getContent_type().equals("1")) {
                                            listPicturePath.add(l.get(i).getPicInfo().getOriginal_url());
                                        }
                                    }
                                    adapter.setListPicturePath(listPicturePath);
                                }

                                chatItemBeanList.clear();
                                chatItemBeanList.addAll(l);
                                adapter.notifyDataSetChanged();
                                recylerView.scrollToPosition(recylerView.getAdapter().getItemCount() - 1);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private RecordInterfaceListener recordInterfaceListener = new RecordInterfaceListener() {
        @Override
        public void OnStartRecord(View ClickView) {

        }

        @Override
        public void OnDeleteRecordSeleted() {

        }

        //haha
        @Override
        public void OnPlayRecordSeleted(int Duration, String filePath) {
            recordPath = filePath;
            playVoiceView.setRecordTime(new Long(Duration));
            duration = Duration;
            Log.i("看", new Long(Duration) + "时间");
            recordVoiceView.setVisibility(View.GONE);
            etFaceTextEmotion.setClickable(false);
            playVoiceView.setVisibility(View.VISIBLE);
        }

        @Override
        public void OnRecordFinish(int Duration, String filePath, String recordName1) {
            Log.i("看", filePath + "松手");
            duration = Duration;
            recordName = recordName1;
            sendSound(filePath, recordName, Duration);
        }
    };

    //发送语音
    private void sendSound(final String filePath, final String recordName, final int duration) {
        File file = new File(filePath);
        OkHttpUtils.post()//
                .url(NetConstant.gy_upload_audio)
                .addFile("upfile", recordName, file)//
                .addParams("duration", duration + "")
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        try {
                            ToastUtils.showToast(context, "发送失败，请检查您的网络");
                        } catch (Exception e1) {
                            if (e1 instanceof ProtocolException) {
                                sendSound(filePath, recordName, duration);
                            }
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jo = new JSONObject(response);
                            String duration = jo.optString("duration");
                            String key_hash = jo.optString("key_hash");
                            String ctime = jo.optString("ctime");
                            String audio_id = jo.optString("audio_id");
                            sendMessage("2", "", audio_id);
                            Log.i("看", " " + response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private PlayRecordListener playRecordListener = new PlayRecordListener() {
        @Override
        public void OnSendRecordSeleted() {
            ToastUtils.showToast(context, "发送");
            String[] strings = recordPath.split("/");
            recordName = strings[strings.length-1];
            sendSound(recordPath,recordName,duration);
        }

        @Override
        public void OnCancleRecordSeleted() {
            ToastUtils.showToast(context, "取消");
            recordVoiceView.setVisibility(View.VISIBLE);
            playVoiceView.setVisibility(View.GONE);
        }

        @Override
        public void OnStartRecord() {
            ToastUtils.showToast(context, "播放");
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(recordPath);
                mediaPlayer.prepare();//准备播放
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                Log.i("看", e.toString());
            }
        }

        @Override
        public void OnStopRecord() {
            ToastUtils.showToast(context, "暂停");
        }
    };

    //                     public function gy_sendChatData(){
//                    $send_uid = I('send_uid');//605
//                    $receive_uid = I('receive_uid');
//                    $send_type=I('send_type');//1:单聊;2:群聊
//                    $content_type=Intval(I('content_type'));//0：文本；1:图片;2语音
//                    $realcontent=I('content');
//                    $extra_info_id=I('extra_info_id');
    /*
    * 发送消息
    * */
    private void sendMessage(String type, String content, String extra_info_id) {
//        send_uid,receive_uid,content,contenttype(0:文本,1:图片,2:语音),usertype(接收者的用户类型0:用户;2:群聊)
        String sendType = "";
        if ("2".equals(receive_type)){
            sendType = "2";
        }else {
            sendType = "1";
        }



        OkHttpUtils.post().url(NetConstant.SendChatData)
                .addParams("send_uid", userInfo.getUserId())
                .addParams("receive_uid", chat_Id)
                .addParams("content", content)
                .addParams("content_type", type)
                .addParams("send_type", sendType)
                .addParams("extra_info_id", extra_info_id)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(context, "发送失败，请检查您的网络");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            ResponseHelper rh = new ResponseHelper(response);
                            if (rh.isSuccess()) {
                                getChatList();
                                etFaceTextEmotion.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    //    @Override
//    protected void onStop() {
//        if (!isAppOnForeground()) {
////            Debug.i("dwy", "enter background");
//            FriendFragment.isForeground = false;
//        } else {
////            Debug.i("dwy", "foreground");
//            isForeground = true;
//        }
//        super.onStop();
//    }
    @Override
    protected void onResume() {
        //添加layout大小发生改变监听器
        rootView.addOnLayoutChangeListener(this);
        FriendFragment.isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        FriendFragment.isForeground = false;
        super.onPause();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_IMAGE_PICKER:
//                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
//                    if (data != null) {
//                        //是否发送原图
//                        boolean isOrig = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
//                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                        Log.e("CSDN_LQR", isOrig ? "发原图" : "不发原图");//若不发原图的话，需要在自己在项目中做好压缩图片算法
//                        for (ImageItem imageItem : images) {
//                            File imageFileThumb;
//                            File imageFileSource;
//                            if (isOrig) {
//                                imageFileSource = new File(imageItem.path);
//                                imageFileThumb = ImageUtils.genThumbImgFile(imageItem.path);
//                            } else {
//                                //压缩图片
//                                imageFileSource = ImageUtils.genThumbImgFile(imageItem.path);
//                                imageFileThumb = ImageUtils.genThumbImgFile(imageFileSource.getAbsolutePath());
//                            }
////                            if (imageFileSource != null && imageFileSource != null)
////                                mPresenter.sendImgMsg(imageFileThumb, imageFileSource);
//                        }
//                    }
//                }
//            case REQUEST_TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    String path = data.getStringExtra("path");
//                    if (data.getBooleanExtra("take_photo", true)) {
//                        //照片
////                        mPresenter.sendImgMsg(ImageUtils.genThumbImgFile(path), new File(path));
//                    } else {
//                        //小视频
////                        mPresenter.sendFileMsg(new File(path));
//                    }
//                }
//                break;
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);

        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            recylerView.scrollToPosition(recylerView.getAdapter().getItemCount() - 1);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            recylerView.scrollToPosition(recylerView.getAdapter().getItemCount() - 1);
        }
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    Log.i("kan", showMsg.toString());
                    //Log.i("kanextras",extras);
                    Log.i("kanextras在聊天界面", messge);
                    Log.i("kanextras在聊天界面", extras);
                   /* ShuaXinLisht(messge,1);*/
                    JSONObject jsonObject = new JSONObject(extras);
                    if (chat_Id.equals(jsonObject.getString("v"))) {
                        getChatList();
                        Log.i("kanextras在聊天界面", "刷新列表");
                    }
                }
            } catch (Exception e) {
            }
        }
    }


    public void exitGroup(){
        final RequestParams requestParams = new RequestParams();
        requestParams.put("chatid", chat_Id);
        requestParams.put("userid",userInfo.getUserId());

        HelperAsyncHttpClient.get(NetConstant.EXIT_GROUP, requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                LogUtils.e("TGB","---?"+response.toString());
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    finish();
                                }else {
                                    Toast.makeText(context,response.getString("data"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(context,"网络记载错误，请稍后重试",Toast.LENGTH_SHORT).show();
                        }

                    }


                });

    }
}