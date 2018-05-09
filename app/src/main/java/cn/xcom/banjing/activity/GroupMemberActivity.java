package cn.xcom.banjing.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.GroupMemberListAdapter;
import cn.xcom.banjing.bean.FriendBean;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.temp.RecyclerViewOnItemLongClickListener;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 2017/10/6.
 */

public class GroupMemberActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ArrayList<FriendBean> friendLists=new ArrayList<FriendBean>();

    private UserInfo userInfo;
    private ListView listView;
    private XRecyclerView xRecyclerView;
    private TextView mExitChatGroup;
    private KProgressHUD hud;
    //列表
    private GroupMemberListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout rl_back, rl_submit;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group_member);
        ButterKnife.bind(this);
        mContext = this;
        groupId = getIntent().getStringExtra("groupId");
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
        mExitChatGroup = (TextView) findViewById(R.id.btn_exit_group);
        mExitChatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams requestParams = new RequestParams();
                requestParams.put("chatid", groupId);
                requestParams.put("userid",userInfo.getUserId());

                HelperAsyncHttpClient.get(NetConstant.GET_GROUP_FRIENDS_LIST, requestParams,
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
                                            Intent intent = new Intent();
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
        });
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
        mAdapter = new GroupMemberListAdapter(friendLists, mContext, new GroupMemberListAdapter.onGroupMemberChanger() {
            @Override
            public void onDeleteSuccess(int location) {
                mAdapter.notifyDataSetChanged();
            }
        });
        xRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerViewOnItemClickListener(new RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, Object object) {
                FriendBean friendBean= (FriendBean) object;
                delMemberData(friendBean);
                return false;
            }
        });


    }
    private void delMemberData(final FriendBean friendBean) {
        AlertDialog dlg = new AlertDialog.Builder(mContext,
                AlertDialog.THEME_HOLO_LIGHT).setItems(
                R.array.OperationDelHelperLongClick,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                toDelete(friendBean);
                                break;
                        }
                    }
                }).create();
        dlg.show();
    }
    private void toDelete(FriendBean friendBean) {
        String url;

        url = NetConstant.NET_DELETE_GROUP_MEMBER;//groupid=66&userid=124&inviterid=123
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String state = object.getString("status");
                        LogUtils.e("TGB","-toInviteGroup  ---object--?"+object.toString());
                        if (state.equals("success")) {
                            String data = object.getString("data");
                            HelperApplication.getInstance().getTaskTypes().clear();

//                            homeActivity.checkToSecond(2);
//                            Intent intent = new Intent(mContext, HomeActivity.class);
//                            startActivity(intent);
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            getFriendList();
//                            Intent intent = new Intent(context, NearbyActivity.class);
//
//                            startActivity(intent);
                        } else {
                            String error = object.getString("data");
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
//                            HelperApplication.getInstance().getTaskTypes().clear();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HelperApplication.getInstance().getTaskTypes().clear();
                Toast.makeText(mContext, "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("groupid", groupId);
        request.putValue("userid",friendBean.getId());
        request.putValue("inviterid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_social_publish_back:
                finish();
                break;
            case R.id.rl_social_publish_submit:
                Intent s=new Intent(GroupMemberActivity.this,GroupActivty.class);
                s.putExtra("select",friendLists);
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
        requestParams.put("groupid", groupId);
        HelperAsyncHttpClient.get(NetConstant.GET_GROUP_FRIENDS_LIST, requestParams,
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
                                                .getString("userid"));
                                         friendBean.setName(json
                                                .getString("send_nickname"));
                                         friendBean.setPhoto(json
                                                .getString("send_face"));
                                        friendBean.setSelectStatus("0");
                                        friendLists.add(friendBean);
                                    }
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

}