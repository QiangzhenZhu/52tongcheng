package cn.xcom.banjing.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.List;

import cn.finalteam.toolsfinal.io.stream.ByteArrayOutputStream;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.AuthenticationActivity;
import cn.xcom.banjing.activity.ChatActivity;
import cn.xcom.banjing.activity.CityPickerActivity;
import cn.xcom.banjing.activity.DetailAuthenticatinActivity;
import cn.xcom.banjing.activity.HelpMeActivity;
import cn.xcom.banjing.activity.HomeActivity;
import cn.xcom.banjing.activity.MyCitySelectActivity;
import cn.xcom.banjing.activity.SaleActivity;
import cn.xcom.banjing.activity.TaskActivity;
import cn.xcom.banjing.bean.AuthenticationList;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.view.MapBottomPopWindow;
import cn.xcom.banjing.view.NiceDialog;
import cz.msebera.android.httpclient.Header;

import static cn.finalteam.toolsfinal.io.IOUtils.copy;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——地图
 */
public class MapFragment extends Fragment implements View.OnClickListener, OnGetGeoCoderResultListener {
    private static final int CITY_RESULT = 0X110;
    private String TAG = "MapFragment";
    private Context mContext;
    private RelativeLayout rl_location, rl_authentication_list;
    private TextView tv_I_help, tv_help_me, tv_city_interaction, locate_district;
    private List<AuthenticationList> lists;
    private List<Marker> markers;
    // 定位相关
    LocationClient mLocClient;
    public BDLocationListener myListener = new MyLocationListener();
    MapView mMapView;
    BaiduMap mBaiduMap;
    MyLocationData locData;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private KProgressHUD hud;
    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    private Marker marker;
    private boolean isResult;
    private double mLatitude, mLongtitude;
    private boolean isFirstIn = true;
    private String status;

    private NiceDialog mDialog;

    private HomeActivity homeActivity;
    private UserInfo userInfo;
    String markerId;
    List<AuthenticationList> skill;
    List<AuthenticationList> skills;
    MapBottomPopWindow mapBottomPopWindow;
    String markerName = "";
    String photoUrl = "";
    String address = "";
    String count = "";
    LinearLayout mapLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        lists = new ArrayList<>();
        markers = new ArrayList<>();
        skill = new ArrayList<>();
        skills = new ArrayList<>();
        userInfo = new UserInfo(mContext);
        mLocClient = new LocationClient(mContext);     //声明LocationClient类
        mLocClient.registerLocationListener(myListener);    //注册监听函数
        // 地图初始化
        mMapView = (MapView) getView().findViewById(R.id.mapView_fragment_map);
        mapLayout = (LinearLayout) getView().findViewById(R.id.map_layout);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        initLocation();
        initListener();
        ImageView button = (ImageView) getView().findViewById(R.id.btn_location);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ll = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18.0f);
                mBaiduMap.animateMapStatus(msu);
            }
        });
        initView();
//        hud = KProgressHUD.create(mContext)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setCancellable(true);

        getCityId();
        if (hud != null) {
            hud.dismiss();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        this.homeActivity = (HomeActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("activity_result", "yes");
        switch (requestCode) {
            case CITY_RESULT:
                isResult = true;
                status = HelperApplication.getInstance().status;
                Log.e("statu", status);
                if (status.equals("0")) {
                    showDialog();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 对地图事件的消息响应
     */
    private void initListener() {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                currentPt = mBaiduMap.getMapStatus().target;
                if (currentPt.longitude != 0 && currentPt.latitude != 0) {
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(currentPt));
                }
                Log.e("中心点", currentPt.latitude + "");
                if (marker != null) {
                    marker.remove();
                }
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                hud.show();

                markerId = lists.get(Integer.parseInt(marker.getTitle())).getId();
                if (markerId != null) {
                    skill.clear();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getSpecificAuthentication(markerId);
                        }
                    }).start();

                }

                Log.d("----markerid", markerId);
                Log.d("----markerid", "null");
//                    } else {
//                        if (hud != null) {
//                            hud.dismiss();
//                        }
//                    }
//                }


                return true;
            }
        });
    }

    /**
     * 构建坐标点
     *
     * @param mPt
     * @param name
     */
    public void createMarker(final LatLng mPt, String name) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_dingwei_shou);
        OverlayOptions options = new MarkerOptions()
                .position(mPt)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));
        //创建InfoWindow展示的view
        Button button = new Button(mContext);
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HelpMeActivity.class);
                intent.putExtra("lat", mPt.latitude);
                intent.putExtra("lon", mPt.longitude);
                startActivity(intent);
            }
        });
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, mPt, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
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

    private void initView() {
        rl_location = (RelativeLayout) getView().findViewById(R.id.rl_fragment_map_location);
        rl_location.setOnClickListener(this);
        rl_authentication_list = (RelativeLayout) getView().findViewById(R.id.rl_fragment_map_authentication_list);
        rl_authentication_list.setOnClickListener(this);
        tv_I_help = (TextView) getView().findViewById(R.id.tv_fragment_map_I_help);
        tv_I_help.setOnClickListener(this);
        tv_help_me = (TextView) getView().findViewById(R.id.tv_fragment_map_help_me);
        tv_help_me.setOnClickListener(this);
        tv_city_interaction = (TextView) getView().findViewById(R.id.tv_fragment_map_city_interaction);
        tv_city_interaction.setOnClickListener(this);
        locate_district = (TextView) getView().findViewById(R.id.locate_district);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("onhidedenChanged", "yes");
        if (HelperApplication.getInstance().mCurrentLocLon != 0) {
            currentPt = new LatLng(HelperApplication.getInstance().mCurrentLocLat, HelperApplication.getInstance().mCurrentLocLon);
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(currentPt, 18.0f);
            mBaiduMap.animateMapStatus(msu);
            /*mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(currentPt));
            MapStatus mMapStatus = new MapStatus.Builder().target(currentPt).zoom(18.0f)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            mBaiduMap.setMapStatus(mMapStatusUpdate);*/

            if (!"".equals(HelperApplication.getInstance().mDistrict)) {
                getWorkingState();
                getAuthentication();
                homegetSpecificAuthentication();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("resume", "yes");
        Log.e("当前定位坐标", HelperApplication.getInstance().mCurrentLocLat + "," + HelperApplication.getInstance().mCurrentLocLon);

        if (HelperApplication.getInstance().mCurrentLocLon != 0) {
            currentPt = new LatLng(HelperApplication.getInstance().mCurrentLocLat, HelperApplication.getInstance().mCurrentLocLon);
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(currentPt, 18.0f);
            mBaiduMap.animateMapStatus(msu);
            locate_district.setText(HelperApplication.getInstance().mDistrict);
            getWorkingState();
            getAuthentication();
            homegetSpecificAuthentication();
        }
        if (!"".equals(HelperApplication.getInstance().mDistrict)) {
            getWorkingState();
            getAuthentication();
            homegetSpecificAuthentication();
        }
        getCityId();
        mMapView.onResume();
        //第一次进入应用 上传位置
//        if (HelperApplication.getInstance().needUploadLocation) {
//            HelperApplication.getInstance().needUploadLocation = false;
//            getWorkingState();
//        }
    }

    /**
     * 获取认证圈服务者
     */

    private void getAuthentication() {
        String url = NetConstant.GET_HOME_MAP_AUTHENTICATION_USER;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        getAuthenticationData(jsonObject);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                createPersonTag();
                            }
                        }).start();
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
        request.putValue("cityname", HelperApplication.getInstance().mDistrict);
        request.putValue("latitude", "");
        request.putValue("longitude", "");
        request.putValue("beginid", "-1");

        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }

    /**
     * 获取当前用户开工收工状态
     */
    private void getWorkingState() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_WORKING_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String state = response.optString("data");
                if (state.equals("1")) {
                    uploadLocation();
                }else{
                    goUploadLocation();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, responseString);
                goUploadLocation();
            }
        });
    }

    private void uploadLocation() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("address", HelperApplication.getInstance().mLocAddress);
        params.put("longitude", HelperApplication.getInstance().mLocLon);
        params.put("latitude", HelperApplication.getInstance().mLocLat);
        params.put("isworking", "1");
        HelperAsyncHttpClient.get(NetConstant.CHANGE_WORKING_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, String.valueOf(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, responseString);
            }
        });
    }
    private void goUploadLocation() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("address", HelperApplication.getInstance().mLocAddress);
        params.put("longitude", HelperApplication.getInstance().mLocLon);
        params.put("latitude", HelperApplication.getInstance().mLocLat);
        HelperAsyncHttpClient.get(NetConstant.GO_UPLOAD_LOCATION, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, String.valueOf(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, responseString);
            }
        });
    }

    /**
     * 在地图中显示小红人图标
     */
    private void createPersonTag() {
        if (markers.size() > 0) {
            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).remove();
            }
        }
        markers.clear();
        for (int i = 0; i < lists.size(); i++) {
            LatLng latLng = new LatLng(Double.parseDouble(lists.get(i).getLatitude()), Double.parseDouble(lists.get(i).getLongitude()));
            //构建Marker图标
//            BitmapDescriptor bitmap = BitmapDescriptorFactory
//                    .fromResource(R.drawable.service_person);
            Bitmap bitmaps = null;

            try {
                if (!TextUtils.isEmpty(lists.get(i).getPhoto())) {
                    bitmaps = makeRoundCorner(GetLocalOrNetBitmap(NetConstant.NET_DISPLAY_IMG + lists.get(i).getPhoto()));
                }else {
                    bitmaps = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_logo);
                }
            } catch (IOException e) {
                e.printStackTrace();
                ToastUtil.showShort(mContext, "111");
            }
            Bitmap sbitmap = zoomImage(bitmaps, 90, 90);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(sbitmap);

            OverlayOptions options = new MarkerOptions()
                    .position(latLng)//设置marker的位置
                    .icon(bitmap)  //设置marker图标
                    .zIndex(9) //设置marker所在层级
                    .title(i + "")
                    .draggable(true);  //设置手势拖拽
            //将marker添加到地图上
            Marker m = (Marker) (mBaiduMap.addOverlay(options));
            markers.add(m);
        }

    }

    /**
     * 解析认证帮信息
     *
     * @param jsonObject
     */
    private void getAuthenticationData(JSONObject jsonObject) {
        lists.clear();
        try {
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                if (object.optString("isworking").equals("1")) {
                    AuthenticationList authenticationList = new AuthenticationList();
                    authenticationList.setId(object.optString("id"));
                    authenticationList.setName(object.optString("name"));
                    authenticationList.setPhoto(object.optString("photo"));
                    authenticationList.setPhone(object.optString("phone"));
                    authenticationList.setAddress(object.optString("address"));
                    authenticationList.setStatus(object.optString("status"));
                    authenticationList.setUsertype(object.optString("usertype"));
                    authenticationList.setIsworking(object.optString("isworking"));
                    authenticationList.setServiceCount(object.optString("serviceCount"));
                    authenticationList.setLongitude(object.optString("longitude"));
                    authenticationList.setLatitude(object.optString("latitude"));
//                    authenticationList.setDistance(Long.parseLong(object.optString("distance")));
//                    JSONArray skillArray = object.optJSONArray("skilllist");
//                    List<AuthenticationList.SkilllistBean> skilllistBeans = new ArrayList<>();
//                    for (int j = 0; j < skillArray.length(); j++) {
//                        AuthenticationList.SkilllistBean skilllistBean = new AuthenticationList.SkilllistBean();
//                        skilllistBean.setType(skillArray.optJSONObject(j).optString("type"));
//                        skilllistBean.setTypename(skillArray.optJSONObject(j).optString("typename"));
//                        skilllistBean.setParent_typeid(skillArray.optJSONObject(j).optString("parent_typeid"));
//                        skilllistBeans.add(skilllistBean);
//                    }
//                    authenticationList.setSkilllist(skilllistBeans);
                    lists.add(authenticationList);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    /**
     * 弹出城市是否开通信息对话框
     */
    private void showDialog() {
        mDialog = new NiceDialog(mContext);
        mDialog.setOKButton("返回城市选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, CityPickerActivity.class), CITY_RESULT);
                mDialog.dismiss();
            }
        });
        mDialog.show();
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        WindowManager.LayoutParams layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = width - 200;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(layoutParams);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_fragment_map_location:
                startActivityForResult(new Intent(mContext, MyCitySelectActivity.class), CITY_RESULT);
                break;
            case R.id.rl_fragment_map_authentication_list:
                startActivity(new Intent(mContext, AuthenticationActivity.class));
                break;
            case R.id.tv_fragment_map_I_help:
//                homeActivity.checkToSecond(1);
                startActivity(new Intent(mContext, TaskActivity.class));
                break;
            case R.id.tv_fragment_map_help_me:
                Intent intent = new Intent(mContext, HelpMeActivity.class);
                if (currentPt != null) {
                    intent.putExtra("lat", currentPt.latitude);
                    intent.putExtra("lon", currentPt.longitude);
                }
                startActivity(intent);
                break;
            case R.id.tv_fragment_map_city_interaction:
                startActivity(new Intent(mContext, SaleActivity.class));
//                homeActivity.checkToSecond(2);
                break;
        }

    }
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        /*MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(geoCodeResult.getLocation()).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));*/
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null) {
            return;
        }
        try {
            if (reverseGeoCodeResult.getPoiList() != null) {
                createMarker(reverseGeoCodeResult.getLocation(), reverseGeoCodeResult.getPoiList().get(0).name);
            } else {
                createMarker(reverseGeoCodeResult.getLocation(), reverseGeoCodeResult.getAddressDetail().city + reverseGeoCodeResult.getAddressDetail().district + reverseGeoCodeResult.getAddressDetail().street);
            }
        } catch (NullPointerException e) {

        }
        isResult = false;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();
            //currentLocPt = new LatLng(location.getLatitude(),location.getLongitude());
            HelperApplication.getInstance().mLocLat = location.getLatitude();
            HelperApplication.getInstance().mLocLon = location.getLongitude();
            HelperApplication.getInstance().mLocAddress = location.getCity() + location.getDistrict() + location.getPoiList().get(0).getName();

            if (isFirstIn) {
                LatLng ll = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18.0f);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
                HelperApplication.getInstance().mCurrentLocLat = location.getLatitude();
                HelperApplication.getInstance().mCurrentLocLon = location.getLongitude();
                HelperApplication.getInstance().mCurrentAddress = location.getCity() + location.getDistrict() + location.getPoiList().get(0).getName();
                HelperApplication.getInstance().mDistrict = location.getDistrict();
                locate_district.setText(location.getDistrict());
                getAuthentication();
                homegetSpecificAuthentication();
            }
//
//
//            HelperApplication.getInstance().mLocaddresscity = location.getCity();
//            HelperApplication.getInstance().mDistrict = location.getDistrict();
            getCityId();
//            HelperApplication.getInstance().mLocaddressprovince = location.getProvince();
//            Log.d("====mfmLocaddresscity", HelperApplication.getInstance().mLocaddresscity);
        }
    }

    @Nullable
    public static Bitmap GetLocalOrNetBitmap(String url) throws IOException {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 10 * 10);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 10 * 10);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {

            assert in != null;
            in.close();
            assert out != null;
            out.close();

        }
    }

    /*
    * bitmap设置圆角
    * */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = Color.RED;

        final Paint paint = new Paint();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        final float roundPx = 200;

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    /*
    * bitmap缩放
    * */
    public static Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
// 获取这个图片的宽和高

        int width = bgimage.getWidth();
        int height = bgimage.getHeight();

// 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

// 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

// 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
                matrix, true);
        return bitmap;

    }

    /*
    * bitmap设置为圆形
    * */
    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }


        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 根据用户编号获取具体认证帮服务者
     */
    private void getSpecificAuthentication(final String userId) {

        String url = NetConstant.GET_SKILLS_BY_USERID;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {

                        String data = "[" + jsonObject.getString("data") + "]";
                        Gson gson = new Gson();
                        skill.clear();
                        List<AuthenticationList> lis = gson.fromJson(data, new TypeToken<List<AuthenticationList>>() {
                        }.getType());
                        skill.add(lis.get(0));
//                        Intent intent = new Intent(getActivity(), DetailAuthenticatinActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("authentication", skill.get(0));
//                        intent.putExtras(bundle);
//                        getActivity().startActivity(intent);
                        markerName = skill.get(0).getName();
                        photoUrl = skill.get(0).getPhoto();
                        address = skill.get(0).getAddress();
                        if (count != null) {
                            count = skill.get(0).getRanking();
                        } else {
                            count = "0";
                        }
                        mapBottomPopWindow = new MapBottomPopWindow(getActivity(), markerName, photoUrl, address, count, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.bottom_pop_ly:

                                        Intent intent = new Intent(getActivity(), DetailAuthenticatinActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("authentication", skill.get(0));
                                        intent.putExtras(bundle);
                                        getActivity().startActivity(intent);
                                        break;
                                    case R.id.btn_chat:
                                        if (skill.get(0).getId() == userInfo.getUserId()) {
                                            ToastUtil.showShort(mContext, "请不要和自己说话");
                                        } else {
                                            Intent chatintent = new Intent(mContext, ChatActivity.class);
                                            chatintent.putExtra("id", skill.get(0).getId());
                                            chatintent.putExtra("name", skill.get(0).getName());
                                            mContext.startActivity(chatintent);
                                        }
                                        break;
                                }
                            }
                        });
                        //设置弹出位置
                        mapBottomPopWindow.showAtLocation(mapLayout, Gravity.BOTTOM, 20, 100);

                        if (hud != null) {
                            hud.dismiss();
                        }
                    }else{
                        if (hud != null) {
                            hud.dismiss();
                        }
                        ToastUtil.showShort(mContext, "附近没有人");
                    }


                } catch (JSONException e) {
                    if (hud != null) {
                        hud.dismiss();
                    }
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
        request.putValue("userid", userId);
        SingleVolleyRequest.getInstance(getActivity()).addToRequestQueue(request);


    }

    /**
     * 认证帮服务者
     */
    private void homegetSpecificAuthentication() {

        String url = NetConstant.GET_AUTHENTICATION_LIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        skills.clear();
                        List<AuthenticationList> lis = gson.fromJson(data, new TypeToken<List<AuthenticationList>>() {
                        }.getType());

                        for (int i = 0; i < lis.size(); i++) {
                            lis.get(i).setTitiId(i + "");
                            skills.add(lis.get(i));
                        }

                    }


                } catch (JSONException e) {
                    if (hud != null) {
                        hud.dismiss();
                    }
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
        request.putValue("cityname", HelperApplication.getInstance().mDistrict);
        SingleVolleyRequest.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void getCityId() {
        String url = NetConstant.CHECK_CITY;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    String state = jsonObject.getString("status");
                    if (state.equals("success")) {
                        String jsonObject1 = jsonObject.getString("data");
                        HelperApplication.getInstance().mLocaddresscityid = jsonObject1;
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                ToastUtils.showToast(getActivity(), "网络连接错误，请检查您的网络");

            }
        });
        request.putValue("city", HelperApplication.getInstance().mDistrict);

        SingleVolleyRequest.getInstance(getContext()).addToRequestQueue(request);
    }
}
