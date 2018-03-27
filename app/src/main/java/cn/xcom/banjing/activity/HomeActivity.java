package cn.xcom.banjing.activity;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.HelperConstant;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.fragment.AdFragment;
import cn.xcom.banjing.fragment.BuyFragment;
import cn.xcom.banjing.fragment.FriendFragment;
import cn.xcom.banjing.fragment.MapFragment;
import cn.xcom.banjing.fragment.MeFragment;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.service.OrderService;
import cn.xcom.banjing.utils.SPUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面
 */
public class HomeActivity extends AppCompatActivity {

    private Context mContext;
    private Button[] mTabs;
    private TextView unReadMap, unReadBuy, unReadSale, unReadMe;
    private MapFragment mapFragment;
//    private BuyFragment buyFragment;
    private FriendFragment friendFragment;
    private AdFragment adFragment;
    private MeFragment meFragment;
//    private ConvenienceActivity
    private Fragment[] fragments;
    private UserInfo userInfo;
    private int a =0;
    private int index;
    private int currentTanIndex;
    private String from = "";
    private String state;
    private int flag=0;
    private boolean resumeFlag = false;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext = this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        initView();
        initFragment();
        state = SPUtils.get(mContext,HelperConstant.IS_HAD_AUTHENTICATION,"").toString();
//        if(state.equals("0")){
//            mTabs[1].setText("抢单");
//        }else if(state.equals("1")){
//            mTabs[1].setText("抢单");
//        }
//        Intent intent = new Intent(this, OrderService.class);
//        startService(intent);
//        getOrder(userInfo.getUserId());
}
    public static Intent newIntent(Context context,int i){
        Intent intent = new Intent(context,HomeActivity.class);
        intent.putExtra("selectFragment",i);
        return intent;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initView() {
        unReadMap = (TextView) findViewById(R.id.tv_home_map_red);
        unReadBuy = (TextView) findViewById(R.id.tv_home_buy_red);
        unReadSale = (TextView) findViewById(R.id.tv_home_sale_red);
        unReadMe = (TextView) findViewById(R.id.tv_home_me_red);
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.bt_home_map);
        mTabs[1] = (Button) findViewById(R.id.bt_home_buy);
        mTabs[2] = (Button) findViewById(R.id.bt_home_sale);
        mTabs[3] = (Button) findViewById(R.id.bt_home_me);
        mTabs[a].setSelected(true);
    }
    private void initFragment() {
        mapFragment = new MapFragment();
        friendFragment = new FriendFragment();
        adFragment = new AdFragment();
        meFragment = new MeFragment();
        fragments = new Fragment[]{mapFragment, friendFragment, adFragment, meFragment};
        switch (a) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, mapFragment)
                        .add(R.id.rl_home_fragment_container, friendFragment)
                        .hide(friendFragment)
                        .show(mapFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, mapFragment)
                        .add(R.id.rl_home_fragment_container, friendFragment)
                        .add(R.id.rl_home_fragment_container, adFragment)
                        .hide(mapFragment)
                        .show(friendFragment)
                        .hide(adFragment).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, friendFragment)
                        .add(R.id.rl_home_fragment_container, adFragment)
                        .add(R.id.rl_home_fragment_container, meFragment)
                        .hide(friendFragment)
                        .show(adFragment)
                        .hide(meFragment).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, adFragment)
                        .add(R.id.rl_home_fragment_container, meFragment)
                        .hide(adFragment)
                        .show(meFragment).commit();
                break;

        }

    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_home_map:
                index = 0;
                break;
            case R.id.bt_home_buy:
                index = 1;
                break;
            case R.id.bt_home_sale:
                index = 2;
                break;
            case R.id.bt_home_me:
                index = 3;
                break;
        }
        if (currentTanIndex != index) {
            /*if(index == 1 && state != SPUtils.get(mContext,HelperConstant.IS_HAD_AUTHENTICATION,"").toString()&&flag==0){
                flag = 1;
                fragments[1] = new BuyFragment();
                mTabs[1].setText("抢单");
            }*/
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTanIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.rl_home_fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTanIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTanIndex = index;
    }

    public void checkToSecond(int selectid) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTanIndex]);
        if (!fragments[selectid].isAdded()) {
            trx.add(R.id.rl_home_fragment_container, fragments[selectid]);
        }
        trx.show(fragments[selectid]).commit();
        mTabs[currentTanIndex].setSelected(false);
        mTabs[selectid].setSelected(true);
        currentTanIndex = selectid;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInsurance();

    }



    /**
     * 获取保险认证
     */
    private void getInsurance() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.Check_Insurance, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证", response.toString());
                if (response.optString("status").equals("success")) {
                    SPUtils.put(mContext, HelperConstant.IS_INSURANCE, response.optString("data"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }

    @Override
    protected void onResumeFragments() {//在onResumeFragment里做fragment transaction操作
        super.onResumeFragments();
        from = getIntent().getStringExtra("from");
        int type = getIntent().getIntExtra("selectFragment",0);
        if (type == 2){
            if (!resumeFlag){
                Log.d("HomeActivity", "selectFragment 2 ");
                onTabClicked(findViewById(R.id.bt_home_sale));
                resumeFlag = true;
            }

        }

        Log.d("HomeActivity","out");
        if("push".equals(from)){
            Log.d("HomeActivity","in");
            onTabClicked(findViewById(R.id.bt_home_buy));
        }

    }

    @Override
    protected void onDestroy() {
        Intent stopintent = new Intent(this, OrderService.class);
        stopService(stopintent);
        super.onDestroy();
    }
//    private void getOrder(final String userId) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("userid", userId);
//
//        HelperAsyncHttpClient.get(NetConstant.GET_MY_NOAPPLY_HIRE_TAST_COUNT, requestParams,
//                new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        if (response != null) {
//                            Log.d("===response", response.toString() + userId);
//                            try {
//                                String state = response.getString("status");
//                                String data = response.getString("data");
//                                if (state.equals("success") && (!data.equals("0"))) {
//
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                                    builder.setMessage("您有雇佣订单未处理，请前往处理");
//                                    builder.setTitle("提示");
//                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            startActivity(new Intent(mContext, OrderTakingActivity.class));
//                                            dialog.dismiss();
//
//                                        }
//                                    });
//
//                                    builder.show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//                    }
//                });
//    }
}
