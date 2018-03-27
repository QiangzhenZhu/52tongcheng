package cn.xcom.banjing.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.widget.tab.PagerSlidingTabStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.CollectionAdapter;
import cn.xcom.banjing.bean.Collection;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.fragment.collection.AdCollectionFragment;
import cn.xcom.banjing.fragment.collection.MarketFragment;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;

/**
 * Created by zhuchongkun on 16/6/12.
 * 收藏页
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="";
    private Context mContext;
    private ListView listView;
    private RelativeLayout rl_back;
    private List<Collection> addList;
    private UserInfo userInfo;
    private CollectionAdapter collectionAdapter;
    private PagerSlidingTabStrip pagerTitles;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);
        mContext=this;
        initView();
        MarketFragment fragment1 = new MarketFragment();
        AdCollectionFragment fragment2 = new AdCollectionFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        pagerTitles = (PagerSlidingTabStrip) findViewById(R.id.order_title);
        viewPager = (ViewPager) findViewById(R.id.vp_my_post);
        PostedOrderAdapter orderAdapter = new PostedOrderAdapter(getSupportFragmentManager());
        viewPager.setAdapter(orderAdapter);
        pagerTitles.setViewPager(viewPager);

        //collectionList();
    }

    private void initView(){
        userInfo=new UserInfo();
        userInfo.readData(mContext);
        rl_back= (RelativeLayout) findViewById(R.id.rl_collection_back);
        rl_back.setOnClickListener(this);
        /*listView= (ListView) findViewById(R.id.collection_listView);*/
        /*swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swif);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorTheme);
        //swip.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorTextWhite));*/
        /*addList = new ArrayList<>();*/
        /*collectionAdapter=new CollectionAdapter(addList,mContext);*/
        /*listView.setAdapter(collectionAdapter);*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_collection_back:
                finish();
                break;
        }

    }

    private class PostedOrderAdapter extends FragmentPagerAdapter {
        private String titles[] = {"集市","广告",};

        public PostedOrderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void changePager(){
        viewPager.setCurrentItem(3);
    }
    /*public void collectionList(){
        String url= NetConstant.HAS_COLLECTION;
        StringPostRequest request=new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    String data=jsonObject.getString("data");
                    if (data!=null){
                        Gson gson=new Gson();
                        List<Collection> fronts =gson.fromJson(data,
                                new TypeToken<ArrayList<Collection>>(){}.getType());
                        addList.clear();
                        addList.addAll(fronts);
                        collectionAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }*/
}
