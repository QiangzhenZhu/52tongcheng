package cn.xcom.banjing.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.GridViewAdapter;
import cn.xcom.banjing.bean.ADDetial;
import cn.xcom.banjing.bean.ADInfo;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.SkillTagInfo;
import cn.xcom.banjing.bean.TaskType;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.HelperConstant;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.record.AudioPlayer;
import cn.xcom.banjing.utils.CommonAdapter;
import cn.xcom.banjing.utils.DateUtil;
import cn.xcom.banjing.utils.GalleryFinalUtil;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.PicturePickerDialog;
import cn.xcom.banjing.utils.PushImage;
import cn.xcom.banjing.utils.PushImageUtil;
import cn.xcom.banjing.utils.PushVideo;
import cn.xcom.banjing.utils.PushVideoUtil;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringJoint;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.utils.ToastUtils;
import cn.xcom.banjing.utils.ViewFactory;
import cn.xcom.banjing.view.CycleViewPager;
import cn.xcom.banjing.view.NoScrollGridView;
import cz.msebera.android.httpclient.Header;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * 发布收费广告
 */
public class ReleaseConvenienceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ReleaseConvenienceActiv";
    public static final int REQUEST_CODE_RECORD_FINISH = 0;
    private static final int SOUND_CODE = 111;
    private static final int REQ_CODE = 10001;
    private static final int REQUEST_VIDEO_CODE = 2;
    private static final int CAMERA_RQ = 8099;
    private static final int CHECK_PERMISSION = 8001;
    private static final int VIDEO = 5;
    private static  int MIN_PACKET_COUNT = 0;
    private static  double MIN_PACKET_MONEY  = 0.0;

    private Context context;
    private List<PhotoInfo> addImageList;
    private RelativeLayout back;
    private List<String> nameList;//添加相册选取完返回的的list
    private TextView et_content,cnnvenience_release, convenience_money,convenience_count;
    private String description;
//    private
    private GridView gridview;
    private LinearLayout image_linearLayout;
    private ImageView tupian, video;
    ;
    //    private ImageView slideImg;
    private View view_line, view_lines;
    private JCVideoPlayerStandard videoView;
    //    private VideoView videoView;
    private View inflate;
    private Button chooseVideo, playVideo, cancel;
    private Dialog dialog;
    private GalleryFinalUtil galleryFinalUtil;
    private GridViewAdapter gridViewAdapter;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private UserInfo userInfo;//得到用户的userid
    private KProgressHUD hud;
    private String descriptionString;
    private List<ADDetial> advertisingImgs;
    private String advertisingurl;
    private String payId;
    private String mid;
    private String mVideoPath;
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    private CycleViewPager cycleViewPager;
    private SkillTagInfo selectTag;
    private ArrayList<SkillTagInfo> skillTagInfos;
    private NoScrollGridView gv_skill;
    private HelpMeSkillAdapter mHelpMeSkillAdapter;
    private List<TaskType> selectList;
    private CommonAdapter skilladp;
    private String typeId;
    private String postions;
    private String skill;
    private String[] imageUrls = {"http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg"
    };
    private List<MediaItem> mMediaSelectedList;
    private MediaController mediaController;
    private Double AdPrice;
    private String type;
    private int  mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_convenience);
        initView();
        configImageLoader();
        MIN_PACKET_MONEY = getIntent().getIntExtra("min_packet_money",0);
        MIN_PACKET_COUNT = getIntent().getIntExtra("min_packet_count",0);
//        getImg();
    }
    private void initView() {
        context = this;
        userInfo = new UserInfo();
        userInfo.readData(context);
        AdPrice = 0.00;
        addImageList = new ArrayList();
        advertisingImgs = new ArrayList<>();
        nameList = new ArrayList<>();
        if (mVideoPath != null) {
            galleryFinalUtil = new GalleryFinalUtil(0);
        } else {
            galleryFinalUtil = new GalleryFinalUtil(9);
        }

        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        view_line = findViewById(R.id.view_line);
        view_lines = findViewById(R.id.view_lines);
        cnnvenience_release = (TextView) findViewById(R.id.cnnvenience_release);
        cnnvenience_release.setOnClickListener(this);
        convenience_money = (EditText) findViewById(R.id.et_ad_publsh_money);
        convenience_count = (EditText) findViewById(R.id.et_ad_pubish_count);
        et_content = (EditText) findViewById(R.id.et_ad_content);
        gridview = (GridView) findViewById(R.id.gridview);
        image_linearLayout = (LinearLayout) findViewById(R.id.image_linearLayout);
        tupian = (ImageView) findViewById(R.id.tupian);
        tupian.setOnClickListener(this);

        video = (ImageView) findViewById(R.id.video);
        video.setOnClickListener(this);
        videoView = (JCVideoPlayerStandard) findViewById(R.id.JCvideoView);
//        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setOnClickListener(this);
//        slideImg = (ImageView) findViewById(R.id.slide_pic);
//        slideImg.setOnClickListener(this);
        selectTag = new SkillTagInfo();
        skillTagInfos = new ArrayList<SkillTagInfo>();
        gv_skill = (NoScrollGridView) findViewById(R.id.gridView_help_me);
//        if (!HelperApplication.getInstance().help) {
            gv_skill.setSelector(new ColorDrawable(Color.TRANSPARENT));
            mHelpMeSkillAdapter = new HelpMeSkillAdapter(context, skillTagInfos);
            gv_skill.setAdapter(mHelpMeSkillAdapter);
            gv_skill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!TextUtils.isEmpty(type)){
                        skillTagInfos.get(mPosition).setChecked(false);
                    }
                    mPosition=position;
                     type = skillTagInfos.get(position).getSkill_id();
                    skillTagInfos.get(position).setChecked(true);
                    mHelpMeSkillAdapter.notifyDataSetChanged();
                }
            });
            getSkill();

//        } else {
//            getSkills();
//        }
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        //getPacketSettings();

//        if (!HelperApplication.getInstance().help) {
    /*        selectList = HelperApplication.getInstance().getTaskTypes();
            Log.e("count", String.valueOf(selectList.size()));
            for (int i = 0; i < skillTagInfos.size(); i++) {
                skillTagInfos.get(i).setChecked(check(skillTagInfos.get(i).getSkill_id()));
                if (skillTagInfos.get(i).isChecked()) {
                    description = skillTagInfos.get(i).getSkill_name();
                }
            }
            mHelpMeSkillAdapter.notifyDataSetChanged();
//        }*/

    }




    /**
     * 判断大分类是否选中
     *
     * @param id
     * @return
     */
    private boolean check(String id) {
        for (int i = 0; i < selectList.size(); i++) {
            if (selectList.get(i).getParent().equals(id)) {
                return true;
            }
        }

        return false;
    }

//    private void getSkills() {
////        Log.d("testid", authenticationList.getId());
//        String url = NetConstant.GET_SKILLS_BY_USERID;
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(s);
//                    String status = jsonObject.getString("status");
//                    if (status.equals("success")) {
//                        if (hud != null) {
//                            hud.dismiss();
//                        }
//                        String data = jsonObject.getString("data");
//                        JSONObject jo = new JSONObject(data);
//                        String skillStr = jo.getString("skilllist");
//                        Gson gson = new Gson();
//                        final List<AuthenticationList.SkilllistBean> skills = gson.fromJson(skillStr,
//                                new TypeToken<ArrayList<AuthenticationList.SkilllistBean>>() {
//                                }.getType());
//                        gv_skill.setSelector(new ColorDrawable(Color.TRANSPARENT));
//                        skilladp = new CommonAdapter<AuthenticationList.SkilllistBean>(context, skills, R.layout.item_skill_tag) {
//                            @Override
//                            public void convert(ViewHolder holder, AuthenticationList.SkilllistBean skilllistBean) {
//                                holder.setText(R.id.tv_item_help_me_skill_tag, skilllistBean.getTypename());
//                                if (postions != null) {
//                                    skilladp.canselect = true;
//                                    skilladp.selectIndex = Integer.parseInt(postions);
//                                    skill = skills.get(Integer.parseInt(postions)).getTypename();
//                                    typeId = skills.get(Integer.parseInt(postions)).getParent_typeid();
//                                    postions = null;
//                                }
//                            }
//                        };
//                        gv_skill.setAdapter(skilladp);
//                        gv_skill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                TextView view1 = (TextView) view.findViewById(R.id.tv_item_help_me_skill_tag);
//
//                                if (view.isClickable()) {
//                                    view1.setTextColor(getResources().getColor(R.color.colorTextWhite));
//                                } else {
//                                    view1.setTextColor(getResources().getColor(R.color.colorTheme));
//                                }
//                                skilladp.canselect = true;
//                                skill = skills.get(position).getTypename();
//                                typeId = skills.get(position).getParent_typeid();
//                                skilladp.selectIndex = position;
//                                skilladp.notifyDataSetChanged();
//
//                            }
//                        });
//
//
//                        String commentStr = jo.getString("commentlist");
//                        List<AuthenticationList.EvaluatelistBean> comments = gson.fromJson(commentStr,
//                                new TypeToken<ArrayList<AuthenticationList.EvaluatelistBean>>() {
//                                }.getType());
//
//
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
////        request.putValue("userid", authenticationList.getId());
////        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
//
//    }

    /**
     * 技能列表
     */
    private void getSkill() {
        RequestParams params = new RequestParams();
        params.put("id", 0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_ADTYPELIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
//                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (hud != null) {
                    hud.dismiss();
                }
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            skillTagInfos.clear();
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            mHelpMeSkillAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + responseString);
                if (hud != null) {
                    hud.dismiss();
                }
            }
        });
    }

    /*
    * 获取广告图片
    * */
    private void getImg() {
        String url = NetConstant.GET_SLIDE_LIST_NEW;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("=====显示111", "" + s);
                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        Gson gson = new Gson();
                        advertisingImgs.clear();
                        List<ADDetial> imgs = gson.fromJson(jsonObject1,
                                new TypeToken<ArrayList<ADDetial>>() {
                                }.getType());
                        advertisingImgs.addAll(imgs);
                        if (imgs.size() > 0) {
                            for (int i = 0; i < imgs.size(); i++) {
//                                infos.get(i).setUrl(NetConstant.NET_DISPLAY_IMG + imgs.get(i).getSlide_pic());
//                                infos.get(i).setContent("http://" + imgs.get(i).getSlide_url());
                                ADInfo info = new ADInfo();
                                info.setUrl(NetConstant.NET_DISPLAY_IMG + advertisingImgs.get(i).getSlide_pic());
                                info.setContent(advertisingImgs.get(i).getSlide_url());
                                infos.add(info);
                            }
                            initialize();
                        }
                    } else {
                        advertisingImgs.clear();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.showToast(ReleaseConvenienceActivity.this, "网络连接错误，请检查您的网络");

            }
        });
        request.putValue("typeid", "1");
        request.putValue("cityid", HelperApplication.getInstance().mLocaddresscityid);
        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
    }

    private void submit() throws IOException {
        descriptionString = et_content.getText().toString().trim();
        String format = String.format(getString(R.string.packet_setting),MIN_PACKET_MONEY,MIN_PACKET_COUNT);
        if (TextUtils.isEmpty(convenience_money.getText().toString().trim())) {
            Toast.makeText(this, "请填写红包金额", Toast.LENGTH_SHORT).show();
        }
        else if (Double.parseDouble(convenience_money.getText().toString().trim())<MIN_PACKET_MONEY) {
            Toast.makeText(this, format, Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(convenience_count.getText().toString().trim())<MIN_PACKET_COUNT) {
            Toast.makeText(this, format, Toast.LENGTH_SHORT).show();
        }else if ((Double.parseDouble(convenience_money.getText().toString())/Double.parseDouble(convenience_count.getText().toString())) < 0.01){
            Toast.makeText(this, "单个红包最小金额不能小于0.01元", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descriptionString)) {
            Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show();

        } else {
            if (addImageList.size() < 1) {
                if (TextUtils.isEmpty(mVideoPath)) {
                        //uploadConvenience();
                    Toast.makeText(getApplication(), "请添加一张图片或视频", Toast.LENGTH_SHORT).show();

                } else {
                    uploadVideo();
                }
            } else {
                uploadImgs();
            }
        }

    }


    /**
     * 上传图片
     */
    private void uploadImgs() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //先上传图片再发布
        new PushImageUtil().setPushIamge(getApplication(), addImageList, nameList, new PushImage() {
            @Override
            public void success(boolean state) throws IOException {
                if (!TextUtils.isEmpty(mVideoPath)) {
                    uploadVideo();
                } else {
                    uploadConvenience();
                }
            }

            @Override
            public void error() {
                Toast.makeText(getApplication(), "上传图片失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    * 上传视频
    * */
    private void uploadVideo() throws IOException {
        if (!TextUtils.isEmpty(mVideoPath)) {
            if (!hud.isShowing()) {
                hud.show();
            }
            new PushVideoUtil().pushVideo(getApplication(), mVideoPath, new PushVideo() {
                @Override
                public void success(String videoName, String imageName) {

                        uploadConvenience();
                }

                @Override
                public void error() {
                    hud.dismiss();
                    Toast.makeText(getApplication(), "上传视频失败", Toast.LENGTH_SHORT).show();
                    mVideoPath = null;
                    JCVideoPlayer.releaseAllVideos();
                }
            });
        } else {
            uploadConvenience();
        }
    }

    /**
     * 最后上传便民圈
     */
    private void uploadConvenience() {
        if (!hud.isShowing()) {
            hud.show();
        }
        //Toast.makeText(getApplication(), "图片上传成功", Toast.LENGTH_SHORT).show();
        //发布广告
        String url = NetConstant.CONVENIENCE_RELEASE;

        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (hud != null) {
                    hud.dismiss();
                }
                LogUtils.e("DEF---release", "-->"+s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {
                        JSONObject publishresult = object.getJSONObject("data");
                        LogUtils.e("DEF---release", "-->"+publishresult.toString());
                        if (publishresult.optString("status").equals("1")) {
                            mid = publishresult.optString("id");
                            dialog();
                        } else {
                            Toast.makeText(ReleaseConvenienceActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            HelperApplication.getInstance().trendsBack = true;
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplication(), object.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "解析错误", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        request.putValue("money", convenience_money.getText().toString());
        request.putValue("type", type);
        request.putValue("moneycount", convenience_count.getText().toString());
        request.putValue("content", descriptionString);
        if (addImageList.size() > 0 && mVideoPath != null) {
            String s = StringJoint.arrayJointchar(nameList, ",");
            request.putValue("picurl", s + "," + PushVideoUtil.getPicName());
        } else if (mVideoPath != null && addImageList.size() < 1) {
            request.putValue("picurl", PushVideoUtil.getPicName());
        } else if (mVideoPath == null && addImageList.size() > 0) {
            String s = StringJoint.arrayJointchar(nameList, ",");
            request.putValue("picurl", s);
        } else {
            request.putValue("picurl", "");
        }

        if (!TextUtils.isEmpty(PushVideoUtil.getVideoName())) {
            request.putValue("video", PushVideoUtil.getVideoName());
        } else {
            request.putValue("video", "");
        }
        request.putValue("latitude", String.valueOf(HelperApplication.getInstance().mLocLat));
        request.putValue("longitude", String.valueOf(HelperApplication.getInstance().mLocLon));
        //request.putValue("address", HelperApplication.getInstance().mDistrict);
        request.putValue("province",String.valueOf(HelperApplication.getInstance().mProvince));
        request.putValue("city",String.valueOf(HelperApplication.getInstance().city));
        request.putValue("address",HelperApplication.getInstance().mArea);
        Log.e("发布广告", String.valueOf(HelperApplication.getInstance().mLocLat) + HelperApplication.getInstance().mLocAddress);
        SingleVolleyRequest.getInstance(ReleaseConvenienceActivity.this).addToRequestQueue(request);
    }


    public void showPicturePicker(View view) {
        PicturePickerDialog picturePickerDialog = new PicturePickerDialog(this);
        picturePickerDialog.show(new PicturePickerDialog.PicturePickerCallBack() {
            @Override
            public void onPhotoClick() {

                Toast.makeText(context, "拍 照", Toast.LENGTH_SHORT).show();
                //获取拍照权限
                if (galleryFinalUtil.openCamera(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                    return;
                } else {
                    String[] perms = {"android.permission.CAMERA"};
                    int permsRequestCode = 200;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(perms, permsRequestCode);
                    }
                }
            }

            @Override
            public void onAlbumClick() {
                galleryFinalUtil.openAblum(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                Toast.makeText(context, "相册选择", Toast.LENGTH_SHORT).show();

            }

        });
    }

    /**
     * 选择图片后 返回的图片数据放在list里面
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                addImageList.clear();
                addImageList.addAll(resultList);
                Log.d("======haha", addImageList.size() + "");
                gridViewAdapter = new GridViewAdapter(ReleaseConvenienceActivity.this, (ArrayList<PhotoInfo>) addImageList);
                gridview.setAdapter(gridViewAdapter);
                view_line.setVisibility(View.VISIBLE);

            } else {
                view_line.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(ReleaseConvenienceActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tupian:
                showPicturePicker(v);
                break;
            case R.id.cnnvenience_release:
                try {
                    submit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.video:
                show();

                break;

//            case R.id.btn_play_video:
//                if (ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                        && ContextCompat.checkSelfPermission(ReleaseConvenienceActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    ActivityCompat.requestPermissions(ReleaseConvenienceActivity.this, new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.RECORD_AUDIO}, CHECK_PERMISSION);
//                }

//                break;

            case R.id.btn_chose_video:
                MediaOptions.Builder builder = new MediaOptions.Builder();
                MediaOptions options = null;
                options = builder.selectVideo().setMaxVideoDuration(180* 1000)
                        .setShowWarningBeforeRecordVideo(true).build();

                if (options != null) {

                    MediaPickerActivity.open(this, REQUEST_VIDEO_CODE, options);
                }
                dialog.dismiss();
                break;
            case R.id.video_btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.video_dialog, null);
        chooseVideo = (Button) inflate.findViewById(R.id.btn_chose_video);
        cancel = (Button) inflate.findViewById(R.id.video_btn_cancel);
        chooseVideo.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_RQ:
                    try {
                        String filePath = data.getStringExtra("videoUrl");
                        Log.e("lzf_video", filePath);
                        if (filePath != null && !filePath.equals("")) {
                            if (filePath.startsWith("file://")) {
                                filePath = data.getStringExtra("videoUrl").substring(7, filePath.length());
                                et_content.setText("视频保存在：" + filePath);
                            }
                        }
                    } catch (Exception ex) {

                    }
                    break;
                case REQUEST_VIDEO_CODE:
                    dialog.dismiss();


                    mMediaSelectedList = MediaPickerActivity
                            .getMediaItemSelected(data);
                    if (mMediaSelectedList != null) {
                        for (MediaItem mediaItem : mMediaSelectedList) {
                            mVideoPath = null;
                            mVideoPath = mediaItem.getPathOrigin(context);
                            if (mVideoPath != null) {
//                                int length = mVideoPath.length();
//                                String str = mVideoPath.substring(length-4, length);
//                                if (str.equals(".mp4")){
                                videoView.setVisibility(View.VISIBLE);
                                videoView.setUp(mVideoPath, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
//                                }else {
//                                    ToastUtil.showShort(context,"请选择MP4格式视频");
//                                }
                            }

//                            videoView.setVideoURI(mediaItem.getUriOrigin());
//                            mediaController = new MediaController(this);
//                            mediaController.setAnchorView(videoView);
//                            mediaController.setKeepScreenOn(true);
//
//                            videoView.setMediaController(mediaController);
                        }
                    } else {
                        Log.e("===", "Error to get media, NULL");
                    }
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission Granted
//                startActivityForResult(new Intent(this, RecordActivity.class), SOUND_CODE);
//            } else {
//                // Permission Denied
//            }
        }
        if (requestCode == CHECK_PERMISSION
                && grantResults.length == 4
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

        }
    }

    /*
    *点击输入框外隐藏小键盘
    * */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (DateUtil.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseConvenienceActivity.this);
        builder.setMessage("广告已发布，请支付红包费用");
        builder.setTitle("系统提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getMessagePrice();
                finish();

            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(ReleaseConvenienceActivity.this, MyMessageActivity.class));
            }
        });
        builder.show();
    }
    private void getMessagePrice() {
        String url = NetConstant.ADD_PACKET;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                LogUtils.e("DEF---price", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {
                        String payId = object.optString("data");
                        Intent payIntent = new Intent(ReleaseConvenienceActivity.this, PaymentActivity.class);
                        payIntent.putExtra("paytype", "ConveniencePay");
                        payIntent.putExtra("tradeNo",payId);
                        payIntent.putExtra("body", "便民圈");
                        payIntent.putExtra("price",convenience_money.getText().toString() );
                        payIntent.putExtra("type", "3");
                        startActivity(payIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        request.putValue("mid",mid);
        request.putValue("money",convenience_money.getText().toString());
        request.putValue("count",convenience_count.getText().toString());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }

    private void play(final String path) {

        if (!TextUtils.isEmpty(mVideoPath)) {
            videoView.setVisibility(View.VISIBLE);
        }
//
//        boolean setUp = videoView.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
//        if (setUp) {
//            Glide.with(ReleaseConvenienceActivity.this).load(NetConstant.NET_DISPLAY_IMG + PushVideoUtil.getPicName()).into(videoView.thumbImageView);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }
//
//    @Override
//    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    protected void onPause() {
//        mVideoPath= null;
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
        super.onPause();
    }

    @SuppressLint("NewApi")
    private void initialize() {

        cycleViewPager = (CycleViewPager) getFragmentManager()
                .findFragmentById(R.id.fragment_cycle_viewpager_content);

//        for (int i = 0; i < imageUrls.length; i++) {
//            ADInfo info = new ADInfo();
////            info.setUrl(NetConstant.NET_HOST +advertisingImgs.get(i).getSlide_pic());
////            info.setContent("http://"+advertisingImgs.get(i).getSlide_url() );
//            info.setUrl(imageUrls[i]);
//            info.setContent("图片-->" + i);
//            infos.add(info);
//        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(this, infos.get(i).getUrl()));

        }
        if (infos.size() < 3) {
            for (int j = 0; j < 3 - infos.size(); j++) {
                Log.d("==infosize", infos.size() + "");
                views.add(ViewFactory.getImageViewforid(this, R.drawable.adv));
            }
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(this, infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        //设置轮播
        cycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;

                Intent intent = new Intent(context, AdvImgDetialActivity.class);
                intent.putExtra("path", info.getUrl());
                intent.putExtra("url", info.getContent());
                startActivity(intent);
//                Toast.makeText(ReleaseConvenienceActivity.this,
//                        "position-->" + info.getContent()+info.getUrl(), Toast.LENGTH_SHORT)
//                        .show();

            }

        }

    };

    /**
     * 任务分类适配器
     */
    public class HelpMeSkillAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<SkillTagInfo> mSkillTagInfos;

        public HelpMeSkillAdapter(Context context, ArrayList<SkillTagInfo> skillTagInfos) {
            this.mContext = context;
            if (skillTagInfos == null)
                skillTagInfos = new ArrayList<SkillTagInfo>();
            this.mSkillTagInfos = skillTagInfos;
        }

        @Override
        public int getCount() {
            return mSkillTagInfos.size();
        }

        @Override
        public SkillTagInfo getItem(int position) {
            return mSkillTagInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HelpMeSkillAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new HelpMeSkillAdapter.ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_help_me_skill_tag, null);
                viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.tv_item_help_me_skill_tags);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (HelpMeSkillAdapter.ViewHolder) convertView.getTag();
            }
            viewHolder.tv_tag.setText(mSkillTagInfos.get(position).getSkill_name());
            if (mSkillTagInfos.get(position).isChecked()) {
                typeId= mSkillTagInfos.get(position).getSkill_id();
                //skill = HelperApplication.getInstance().getTaskTypes().get(0).getName();
                //ToastUtil.showShort(mContext,HelperApplication.getInstance().getTaskTypes().get(0).getName());
                skill = mSkillTagInfos.get(position).getSkill_name();
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag_select);
            } else {
                viewHolder.tv_tag.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                viewHolder.tv_tag.setBackgroundResource(R.drawable.tv_tag);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView tv_tag;
        }
    }
    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }


}
