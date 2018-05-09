package cn.xcom.banjing.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.IInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.mylibrary.RecordAudioView.QRecordAudioView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.List;

import bolts.CancellationTokenSource;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.BindAccountAuthorizedActivity;
import cn.xcom.banjing.activity.ReleaseConvenienceActivity;
import cn.xcom.banjing.adapter.CityAdapter;
import cn.xcom.banjing.adapter.ConvenienceAdapter;
import cn.xcom.banjing.bean.AdSetting;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.SkillTagInfo;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.bean.locationBase;
import cn.xcom.banjing.constant.HelperConstant;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.net.OkHttpUtils;
import cn.xcom.banjing.record.AudioPlayer;
import cn.xcom.banjing.utils.CommonAdapter;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.PermissionUtil;
import cn.xcom.banjing.utils.SPUtils;
import cn.xcom.banjing.utils.ScreenUtils;
import cn.xcom.banjing.utils.SingleConvenience;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.utils.ViewHolder;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by mac on 2017/8/26.
 */

public class AdFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AdFragment";
    private RelativeLayout cnnvenience_release, rl_money, rl_type, rl_publishtime,rl_location,rl_share;
    private TextView tvType, tvMoney,tv_Publishtime;
    private KProgressHUD kud;
    private List<Convenience> addlist;
    private ConvenienceAdapter convenienceAdapter;
    private XRecyclerView xRecyclerView;
    private KProgressHUD hud;
    private Context mContext;
    private List<locationBase> locationBaseList = new ArrayList<>();
    private TextView mSelectText;

    String msgCount;
    UserInfo user;
    String keyWord = "";
    // 定位相关
    LocationClient mLocClient;
    private boolean isFirstIn = true;
    public BDLocationListener myListener = new MyLocationListener();
    public static String type;//技能
    public static String moneyType ="0";//金钱排序
    public static String onlineType ="0";//时间排序
    public static String shareCount = "0";//分享排序
    public static String provenience = "";//省份
    public static String city = "";//市
    public static String district = "";//区
    public static boolean isRefresh = true;
    public static String orderby = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        Log.e("state", SPUtils.get(getActivity(), HelperConstant.IS_HAD_AUTHENTICATION, "").toString());

        view = inflater.inflate(R.layout.activity_convenience, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mLocClient = new LocationClient(mContext);     //声明LocationClient类
        mLocClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        initView();
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
//        hud.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (HelperApplication.getInstance().trendsBack) {
          //  getNewDatas(HelperApplication.getInstance().mDistrict, "");
        //}
        //          getDatas();
        //initView();

    }

    @Override
    public void onPause() {
        super.onPause();
        HelperApplication.getInstance().trendsBack = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HelperApplication.getInstance().trendsBack = false;
        if (AudioPlayer.isPlaying) {
            AudioPlayer.getInstance().stopPlay();
        }
    }

    private void initView() {
        type="0";
        user = new UserInfo();
        user.readData(mContext);
        addlist = SingleConvenience.get().getAddlist();
        addlist.clear();
        mSelectText = (TextView) getView().findViewById(R.id.ad_type_select);
        mSelectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
        cnnvenience_release = (RelativeLayout) getView().findViewById(R.id.cnnvenience_release);
        rl_share = (RelativeLayout) getView().findViewById(R.id.rl_share_count);
        rl_share.setOnClickListener(this);
        rl_money = (RelativeLayout) getView().findViewById(R.id.rl_money);
        rl_money.setOnClickListener(this);
        rl_location = (RelativeLayout) getView().findViewById(R.id.rl_location);
        rl_location.setOnClickListener(this);
        rl_publishtime = (RelativeLayout)getView().findViewById(R.id.rl_publishtime);
        rl_publishtime.setOnClickListener(this);
        rl_type = (RelativeLayout) getView().findViewById(R.id.rl_type);
        rl_type.setOnClickListener(this);
        tvType = (TextView) getView().findViewById(R.id.tv_type);
        tvMoney = (TextView) getView().findViewById(R.id.tv_money);
        tv_Publishtime=(TextView)getView().findViewById(R.id.tv_publishtime) ;
        cnnvenience_release.setOnClickListener(this);
        xRecyclerView = (XRecyclerView) getView().findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //getNewDatas(HelperApplication.getInstance().mDistrict, keyWord);
                // TODO: 2018/4/19  刷新
                //getDatas("0","0","0","0","0");
                isRefresh = true;
                Log.d(TAG, "onRefresh: ");
                getDatas();
            }

            @Override
            public void onLoadMore() {
                //getMoreDatas(HelperApplication.getInstance().mDistrict, keyWord);
                //getDatas("1","0","0","0","0");
                //todo 加载更多
                Log.d(TAG, "onLoadMore: ");
                isRefresh = false;
                getDatas();
            }
        });
        convenienceAdapter = new ConvenienceAdapter(addlist, mContext,0);
        xRecyclerView.setAdapter(convenienceAdapter);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.e("zcq", "getmap1");
            if (location != null) {
                district = location.getDistrict();
                provenience = location.getProvince();
                city = location.getCity();
                //district = location.getDistrict();
                mLocClient.stop();
                if (isFirstIn) {
                    Log.e("zcq", "getmap2");
                    isFirstIn = false;
                    //getNewDatas(district, "");
                   /* getDatas("0","0","0","0","0");*/
                   //todo 第一次刷新，第一次加载；
                    getDatas();
                }
            }
        }
    }

    /**
     * 开始定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //发布便民消息
            case R.id.cnnvenience_release:
                /*if (SPUtils.get(context, HelperConstant.IS_HAD_AUTHENTICATION, "").equals("1")) {
                    goPublish();
                } else {
                    goAuthorized();
                }*/
                if (!"".equals(HelperApplication.getInstance().mDistrict) && PermissionUtil.gPSIsOPen(mContext)) {
                    getPacketSetting();

                } else {
                    ToastUtil.showShort(mContext, "请开启定位");
                }
                break;
            case R.id.rl_money:
                showPopupWindow2();
                break;
            case R.id.rl_type:
                showPopupMenu();
                break;
            case R.id.rl_publishtime:
                showPopupWindowTime();
                break;
            case R.id.rl_location:
                showLocationPopupWindow();
                break;
            case R.id.rl_share_count:
                showPopupWindowShare();
                break;

        }
    }

    /**
     * 广告分类弹出框
     */
    private void showPopupMenu() {
        final ArrayList<SkillTagInfo> skillTagInfos = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("id", 0);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_ADTYPELIST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONArray data = response.getJSONArray("data");
                            skillTagInfos.clear();
                            SkillTagInfo tagInfo = new SkillTagInfo();
                            tagInfo.setSkill_name("全部");
                            tagInfo.setSkill_id("0");
                            skillTagInfos.add(tagInfo);
                            for (int i = 0; i < data.length(); i++) {
                                SkillTagInfo info = new SkillTagInfo();
                                JSONObject jsonObject = data.getJSONObject(i);
                                info.setSkill_id(jsonObject.getString("id"));
                                info.setSkill_name(jsonObject.getString("name"));
                                skillTagInfos.add(info);
                            }
                            //自定义布局
                            View layout = LayoutInflater.from(mContext).inflate(R.layout.select_type_list, null);
                            //初始化popwindow
                            final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                            popupWindow.setFocusable(true);
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setBackgroundDrawable(new BitmapDrawable());
                            //设置弹出位置
                            int[] location = new int[2];
                            rl_type.getLocationOnScreen(location);
                            popupWindow.showAsDropDown(rl_type);
                            ListView listView = (ListView) layout.findViewById(R.id.type_list);
                            listView.setAdapter(new CommonAdapter<SkillTagInfo>(mContext, skillTagInfos, R.layout.item_skill) {
                                @Override
                                public void convert(ViewHolder holder, SkillTagInfo skillTagInfo) {
                                    holder.setText(R.id.tv_type_name, skillTagInfo.getSkill_name());
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    tvType.setText(skillTagInfos.get(position).getSkill_name());
                                     type = skillTagInfos.get(position).getSkill_id();
                                     //todo 技能加载
                                    isRefresh = true;
                                    getDatas();
                                    popupWindow.dismiss();
                                }
                            });
                            // 设置背景颜色变暗
                            final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                            lp.alpha = 0.7f;
                            getActivity().getWindow().setAttributes(lp);
                            //监听popwindow消失事件，取消遮盖层
                            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    lp.alpha = 1.0f;
                                    getActivity().getWindow().setAttributes(lp);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    /**
     * 更多排序
     */
    private void showPopupWindowTime() {
        //自定义布局
        View layout = LayoutInflater.from(mContext).inflate(R.layout.select_time_mode, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rl_money.getLocationOnScreen(location);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        popupWindow.showAsDropDown(rl_publishtime);
//        popupWindow.showAtLocation(rl_money, Gravity.TOP,100,100);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        final TextView tv3 = (TextView) layout.findViewById(R.id.tv3);
        final TextView tv4 = (TextView) layout.findViewById(R.id.tv4);
        final TextView tv5 = (TextView) layout.findViewById(R.id.tv5);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                tv_Publishtime.setText("智能排序");
                orderby="";
                getDatas();
                // TODO: 2018/4/19
                popupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                tv_Publishtime.setText("时间最近");
                orderby = "10";
                getDatas();
                // TODO: 2018/4/19
                popupWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                tv_Publishtime.setText("赏金最高");
                orderby = "20";
                getDatas();
                popupWindow.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                tv_Publishtime.setText("分享最多");
                orderby = "30";
                getDatas();
                popupWindow.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                tv_Publishtime.setText("剩余赏金最多");
                orderby = "40";
                getDatas();
                popupWindow.dismiss();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 分享数量由大到小，由小到大；
     */
    private void showPopupWindowShare() {
        //自定义布局
        View layout = LayoutInflater.from(mContext).inflate(R.layout.select_time_mode, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rl_money.getLocationOnScreen(location);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        popupWindow.showAsDropDown(rl_publishtime);
//        popupWindow.showAtLocation(rl_money, Gravity.TOP,100,100);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        tv1.setText("由少到多");
        tv2.setText("由多到少");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCount = "0";
                getDatas();
                // TODO: 2018/4/19
                popupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCount = "1";
                getDatas();
                // TODO: 2018/4/19
                popupWindow.dismiss();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }


    /**
     * 赏金高低
     */
    private void showPopupWindow2() {
        //自定义布局
        View layout = LayoutInflater.from(mContext).inflate(R.layout.select_type_mode2, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        int[] location = new int[2];
        rl_money.getLocationOnScreen(location);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        popupWindow.showAsDropDown(rl_money);
//        popupWindow.showAtLocation(rl_money, Gravity.TOP,100,100);
        final TextView tv1 = (TextView) layout.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) layout.findViewById(R.id.tv2);
        tv1.setText("由高至低");
        tv2.setText("由低至高");
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMoney.setText(tv1.getText());
                moneyType = "0";
                getDatas();
                // TODO: 2018/4/19 赏金排序
                popupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMoney.setText(tv2.getText());
                moneyType = "1";
                getDatas();
                // TODO: 2018/4/19 赏金排序
                popupWindow.dismiss();
            }
        });
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }
    /**
     * 地区选择PopupWindow
     */
    private void showLocationPopupWindow() {
        //自定义布局
        final View layout = LayoutInflater.from(mContext).inflate(R.layout.location_select, null);
        //初始化popwindow
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, 660);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹出位置
        final int[] location = new int[2];
        rl_money.getLocationOnScreen(location);
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        popupWindow.showAsDropDown(rl_location);
//        popupWindow.showAtLocation(rl_money, Gravity.TOP,100,100);
        StringPostRequest request = new StringPostRequest(NetConstant.LOCATION, new Response.Listener<String>() {
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
                        List<locationBase> locationBases = new Gson().fromJson(data,new TypeToken<List<locationBase>>(){}.getType());

                        locationBase base = new locationBase();
                        final List<locationBase.ChildlistBean> childlistBeans = new ArrayList<>();
                        locationBase.ChildlistBean bean = new locationBase.ChildlistBean();
                        bean.setName("全国各地");
                        childlistBeans.add(bean);
                        base.setName("全国");
                        base.setChildlist(childlistBeans);
                        locationBaseList.clear();
                        locationBaseList.add(base);
                        locationBaseList.addAll(locationBases);
                        ListView lvLocation = (ListView) layout.findViewById(R.id.lv_location);
                        final RecyclerView rvlocation = (RecyclerView) layout.findViewById(R.id.rv_location);
                        final List<String> proveniences = new ArrayList<>();
                        for (int i = 0; i <locationBaseList.size(); i++) {
                            proveniences.add(locationBaseList.get(i).getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,proveniences);
                        lvLocation.setAdapter(adapter);
                        lvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                provenience = proveniences.get(position);
                                GridLayoutManager manager = new GridLayoutManager(mContext,3);
                                rvlocation.setLayoutManager(manager);
                                final List<locationBase.ChildlistBean> childlistBeans = (List<locationBase.ChildlistBean>) locationBaseList.get(position).getChildlist();
                                final CityAdapter cityAdapter = new CityAdapter(locationBaseList.get(position).getChildlist(), new CityAdapter.ItemCliclk() {
                                    @Override
                                    public void onClick(int position) {
                                        popupWindow.dismiss();
                                        //todo 地区选择
                                        city = childlistBeans.get(position).getName();
                                        isRefresh = true;
                                        getDatas();
                                    }
                                });
                                rvlocation.setAdapter(cityAdapter);
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                ToastUtil.Toast(mContext, "网络错误，请检查");


            }
        });

        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    private void goPublish(int money,int count) {
        Intent intent = new Intent(mContext, ReleaseConvenienceActivity.class);
        intent.putExtra("min_packet_money",money);
        intent.putExtra("min_packet_count",count);
        startActivity(intent);


    }

    private void goAuthorized() {
        Intent intent = new Intent(mContext, BindAccountAuthorizedActivity.class);
        startActivity(intent);
    }

    private void getDatas() {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("正在加载...")
                .setCancellable(true);
        hud.show();
        String size = "0";
        String url = NetConstant.GET_AD_LIST;
        Log.d("---url", url);
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
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
                        Gson gson = new Gson();
                        List<Convenience> lists = gson.fromJson(data, new TypeToken<ArrayList<Convenience>>() {
                        }.getType());
                        if (isRefresh){
                            addlist.clear();
                            isRefresh = false;
                        }
                        if (null!=lists&&lists.size()>0) {
                            addlist.addAll(lists);
                        }
                        convenienceAdapter.notifyDataSetChanged();

                    }
                    else {
                        String data = jsonObject.getString("data");
                        Toast.makeText(mContext,data,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                ToastUtil.Toast(mContext, "网络错误，请检查");
                xRecyclerView.refreshComplete();
                xRecyclerView.loadMoreComplete();

            }
        });
        request.putValue("userid",user.getUserId());
        request.putValue("type", type);
        if (isRefresh){
            size = "0";
        }else {
            size = addlist.size()+"";
        }
        request.putValue("original_array_count",size);
        request.putValue("province",provenience);
        request.putValue("city",city);
        request.putValue("address",district);
        request.putValue("orderby",orderby);
        /*request.putValue("sort_by_create_time",onlineType);
        request.putValue("sort_by_bounty",moneyType);
        request.putValue("sort_by_share_count",shareCount);*/
        Log.d(TAG, "getDatas: type"+type +"/original_array_count :"+size+"sort_by_create_time :"+onlineType
        +"sort_by_bounty :"+moneyType+"sort_by_share_count :"+shareCount);
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }

    /**
     * 获取红包设置
     */
    public void getPacketSetting(){
        StringPostRequest request = new StringPostRequest(NetConstant.GET_PUBLISH_AD_SETTING, new Response.Listener<String>() {
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
                        AdSetting adSetting = new Gson().fromJson(data,AdSetting.class);
                        if (adSetting.getCan_publish().isStatus()){
                            // TODO: 2018/4/19

                            goPublish(adSetting.getPacket_setting().getMoney(),adSetting.getPacket_setting().getCount());
                        }else {
                            Toast.makeText(mContext,adSetting.getCan_publish().getReason(),Toast.LENGTH_SHORT).show();
                        }



                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                xRecyclerView.refreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (hud != null) {
                    hud.dismiss();
                }
                ToastUtil.Toast(mContext, "网络错误，请检查");


            }
        });
        request.putValue("userid",user.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }




}

