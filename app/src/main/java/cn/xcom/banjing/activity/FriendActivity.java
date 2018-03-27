package cn.xcom.banjing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.FriendListAdapter;
import cn.xcom.banjing.bean.FriendListInfo;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 2017/10/6.
 */

public class FriendActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    //    private RelativeLayout rl_back;
    private List<FriendListInfo> friendLists;
    private UserInfo userInfo;
    private ListView listView;
    private XRecyclerView xRecyclerView;
    private KProgressHUD hud;
    //    private TextView et_anno;
    LinearLayout circlePhoneFriend,nearbyFriend;
    //列表

    //    private ContactRecyclerAdapter mAdapter;
    private FriendListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend);
        ButterKnife.bind(this);
        mContext = this;
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        initView();
    }

    private void initView() {
        friendLists = new ArrayList<>();
        friendLists.clear();
        mContext = this;
        userInfo = new UserInfo(mContext);
//        et_anno=(TextView)getView().findViewById(R.id.anno);
//        et_anno.setVisibility(View.VISIBLE);
        circlePhoneFriend = (LinearLayout) findViewById(R.id.circle_phone_friend);
        circlePhoneFriend.setOnClickListener(this);
        nearbyFriend = (LinearLayout) findViewById(R.id.circle_nearby);
        nearbyFriend.setOnClickListener(this);

        xRecyclerView =(XRecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getFriendList();
            }
            @Override
            public void onLoadMore() {
                getFriendList();
            }
        });
        mAdapter = new FriendListAdapter(friendLists, mContext, new FriendListAdapter.OnFriendDeleteClick() {
            @Override
            public void onClick(int position) {
                getFriendList();
            }
        });
        xRecyclerView.setAdapter(mAdapter);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circle_phone_friend:
                startActivity(new Intent(mContext, SearchFriendActivity.class));
                break;
            case R.id.circle_nearby:
                startActivity(new Intent(mContext, NearbyActivity.class));
                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getFriendList();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
    private void getFriendList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        requestParams.put("status", "1");
        HelperAsyncHttpClient.get(NetConstant.GET_FRIENDS_LIST, requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    List<FriendListInfo> infos = new Gson().fromJson(data,
                                            new TypeToken<List<FriendListInfo>>() {
                                            }.getType());
                                    friendLists.clear();
                                    friendLists.addAll(infos);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                        xRecyclerView.refreshComplete();
                        xRecyclerView.loadMoreComplete();
                    }


                });

    }

}