package cn.xcom.banjing.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.fragment.AdFragment;
import cn.xcom.banjing.fragment.order.DisplayAdFragment;
import cn.xcom.banjing.utils.SingleConvenience;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;

public class DisplayAdViewPagerActivivty extends BaseActivity {
    private static final String TAG = "DisplayAdViewPagerActiv";
    private Context mContext;
    private ViewPager adViewPager;
    public static final String CONVENIENCE = "cn.xcom.banjing.activivty.DisplayAdViewPagerActivivty";
    private List<Convenience> addList;
    private int currentItem = 0;
    private KProgressHUD hud;
    private FragmentStatePagerAdapter adapter;
    float startX;
    float endX;
    private int type;
    private  Convenience convenience;
    private int position;
    private UserInfo userInfo;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ad_view_pager_activivty);
        Log.e("adTest", "onCreate: ");
        type = getIntent().getIntExtra("type",1);
        mContext = this;
        if (type == 1){
            addList = SingleConvenience.get().getAddlist();
             position=getIntent().getBundleExtra(CONVENIENCE).getInt("position");
          }else if (type == 2){
            convenience = (Convenience) getIntent().getSerializableExtra("adinfo");
            addList =new ArrayList<>();
            addList.add(convenience);
        }
        Log.e("adTest", "onCreate: "+ "   " +addList.size());
        userInfo = new UserInfo(mContext);
        adViewPager = (ViewPager) findViewById(R.id.ad_detail_viewPager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Convenience convenience = addList.get(position);
                if (!TextUtils.isEmpty(convenience.getVideo())) {
                    DisplayAdFragment displayAdFragment = DisplayAdFragment.newInstance(convenience, 1);
                    return displayAdFragment;
                } else {
                    DisplayAdFragment displayAdFragment = DisplayAdFragment.newInstance(convenience, 2);
                    return displayAdFragment;
                }
            }

            @Override
            public int getCount() {
                Log.e("adTest", "getCount: "+ " "+addList.size());
                return addList.size();
            }
        };
        adViewPager.setAdapter(adapter);

        adViewPager.setCurrentItem(position);
        adViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX =event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        WindowManager manager= (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                        Point point = new Point();
                        manager.getDefaultDisplay().getSize(point);
                        int width = point.x;
                        if (currentItem == (addList.size()-1)&&((startX -endX)) >= (width/4.0)) {
                            hud = KProgressHUD.create(DisplayAdViewPagerActivivty.this)
                                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                    .setDetailsLabel("正在加载...")
                                    .setCancellable(true);
                            hud.show();
                            getMoreDatas(HelperApplication.getInstance().mDistrict,"");

                        }
                        break;
                }
                return false;
            }
        });

    }
    private void getMoreDatas1(String district, String keyWord) {
        Log.e("zcq", "getmore");
        String url = NetConstant.CONVENIENCE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        String data = jsonObject.getString("data");
                        Log.e("获取广告", data);
                        Gson gson = new Gson();
                        List<Convenience> lists = gson.fromJson(data, new TypeToken<ArrayList<Convenience>>() {
                        }.getType());
                        addList.addAll(lists);
                        adapter.notifyDataSetChanged();
                        adViewPager.setCurrentItem(addList.size()/2);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hud.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(DisplayAdViewPagerActivivty.this, "网络错误，请检查");
                hud.dismiss();
            }

        });
        Convenience lastConV = addList.get(addList.size() - 1);
        request.putValue("beginid", lastConV.getMid());
        request.putValue("type", AdFragment.type);
        request.putValue("onlineType",AdFragment.onlineType);
        request.putValue("moneyType",AdFragment.moneyType);
        request.putValue("city", HelperApplication.getInstance().mDistrict);
        request.putValue("keyword", keyWord);
        Log.e("获取广告", HelperApplication.getInstance().mDistrict);
        Log.e("city", HelperApplication.getInstance().mDistrict);//HelperApplication.getInstance().mDistrict
        Log.e("beginid", lastConV.getMid());
        SingleVolleyRequest.getInstance(DisplayAdViewPagerActivivty.this).addToRequestQueue(request);

    }

    private void getMoreDatas(String district, String keyWord) {
        Log.e("zcq", "getmore");
        String url = NetConstant.GET_AD_LIST;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {

                        String data = jsonObject.getString("data");
                        Log.e("获取广告", data);
                        Gson gson = new Gson();
                        List<Convenience> lists = gson.fromJson(data, new TypeToken<ArrayList<Convenience>>() {
                        }.getType());
                        addList.addAll(lists);
                        adapter.notifyDataSetChanged();
                        adViewPager.setCurrentItem(addList.size()/2);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hud.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.Toast(DisplayAdViewPagerActivivty.this, "网络错误，请检查");
                hud.dismiss();
            }

        });
        request.putValue("userid", userInfo.getUserId());
        request.putValue("type", AdFragment.type);
        request.putValue("original_array_count",String.valueOf(addList.size()));
        request.putValue("province",AdFragment.provenience);
        request.putValue("city",AdFragment.city);
        request.putValue("address",district);
        request.putValue("orderby",AdFragment.orderby);
        Log.d(TAG, "getDatas: type"+type +"/original_array_count :"+addList.size()+"sort_by_create_time :"+AdFragment.onlineType
                +"sort_by_bounty :"+AdFragment.moneyType+"sort_by_share_count :"+AdFragment.shareCount);
        SingleVolleyRequest.getInstance(DisplayAdViewPagerActivivty.this).addToRequestQueue(request);

    }

    /**
     *
     * @param context
     * @param lists
     * @return
     */
    public static Intent newIntent(Context context, List<Convenience> lists,int position,int type){
        Intent intent=new Intent(context, DisplayAdViewPagerActivivty.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putSerializable(CONVENIENCE,(Serializable) lists);
        intent.putExtra(CONVENIENCE,bundle);
        intent.putExtra("type",type);
        return intent;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 一定要spuer，否则事件打住,不会在向下调用了
        super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            // 记录用户手指点击的位置
            case MotionEvent.ACTION_DOWN:
                startX= ev.getX();
                break;
        }
        return true;// return false,继续向下传递，return true;拦截,不向下传递
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("adTest", "onResume: ");
        if (adapter != null){
            adapter.notifyDataSetChanged();
            Log.e("adTest", "onResume: notifty"+"  "+ adapter.getCount() );
        }
    }


    @Override
    protected void onDestroy() {
        if (adapter != null){
            adapter.notifyDataSetChanged();
            Log.e("adTest", "onDestroy: " +adapter.getCount());
        }
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //DisplayAdFragment.mTencent.onActivityResult(requestCode, resultCode, data);
    }
}
