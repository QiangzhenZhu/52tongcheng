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
import android.widget.RelativeLayout;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.FriendListAdapter;
import cn.xcom.banjing.adapter.FriendSelectListAdapter;
import cn.xcom.banjing.bean.FriendBean;
import cn.xcom.banjing.bean.FriendListInfo;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 2017/10/6.
 */

public class SelectGroupMemberActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    //    private RelativeLayout rl_back;
    private ArrayList<FriendBean> friendLists=new ArrayList<FriendBean>();
    private ArrayList<FriendBean> selectfriendLists=new ArrayList<FriendBean>();

    private UserInfo userInfo;
    private ListView listView;
    private XRecyclerView xRecyclerView;
    private KProgressHUD hud;
    //列表

    //    private ContactRecyclerAdapter mAdapter;
    private FriendSelectListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout rl_back, rl_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_group_member);
        ButterKnife.bind(this);
        mContext = this;
        selectfriendLists= (ArrayList<FriendBean>) getIntent().getSerializableExtra("select");
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
        rl_back = (RelativeLayout) findViewById(R.id.rl_social_publish_back);
        rl_back.setOnClickListener(this);
        rl_submit = (RelativeLayout) findViewById(R.id.rl_social_publish_submit);
        rl_submit.setOnClickListener(this);
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
        mAdapter = new FriendSelectListAdapter(friendLists, mContext);
        xRecyclerView.setAdapter(mAdapter);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_social_publish_back:
                finish();
                break;
            case R.id.rl_social_publish_submit:
                toSelectfriend();
                Intent s=new Intent(SelectGroupMemberActivity.this,GroupActivty.class);
                s.putExtra("select",selectfriendLists);
                setResult(1,s);
                finish();
//                LogUtils.e("TGB","---?"+friendLists.toString());
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
                                LogUtils.e("TGB","---?"+response.toString());
                                String state = response.getString("status");
                                friendLists.clear();
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    JSONArray jsonArray=response.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json =jsonArray.getJSONObject(i);
                                        FriendBean friendBean=new FriendBean();
                                        friendBean.setId(json
                                                .getString("friend_userid"));
                                         friendBean.setName(json
                                                .getString("friend_name"));
                                         friendBean.setPhoto(json
                                                .getString("friend_photo"));
                                        friendBean.setSelectStatus("0");
                                        friendLists.add(friendBean);
                                    }
                                    selectfriend();
                                    mAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                        xRecyclerView.refreshComplete();
                    }


                });

    }

    private void selectfriend() {
        for (int i = 0; i < selectfriendLists.size(); i++) {
           FriendBean friendBean=selectfriendLists.get(i);
            for (int f = 0; i < friendLists.size(); i++) {
                FriendBean friend=friendLists.get(f);
                if (friendBean.getId().equals(friend.getId())){
                    friendLists.get(f).setSelectStatus("1");
                    return;
                }

            }
        }
    }
    private void toSelectfriend() {
        selectfriendLists.clear();
        for (int i = 0; i < friendLists.size(); i++) {
           if (friendLists.get(i).getSelectStatus().equals("1")){
               selectfriendLists.add(friendLists.get(i));
           }

        }
    }

}