package cn.xcom.banjing.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.FriendActivity;
import cn.xcom.banjing.activity.GroupActivty;
import cn.xcom.banjing.adapter.ChatListAdapter;
import cn.xcom.banjing.bean.ChatListInfo;
import cn.xcom.banjing.bean.FriendListInfo;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.chat.ExampleUtil;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 2017/8/30.
 * 朋友圈
 */

public class FriendFragment extends Fragment implements View.OnClickListener{
    private String TAG = "FriendFragment";
    private Context mContext;
//    private RelativeLayout rl_back;
    private List<FriendListInfo> friendLists;
    private UserInfo userInfo;
    private ListView listView;
    private XRecyclerView xRecyclerView;
    private KProgressHUD hud;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "newMessage";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    LinearLayout myFriend,myGroup;//circlePhoneFriend,nearbyFriend,
    private MessageReceiver mMessageReceiver;
    //列表

//    private ContactRecyclerAdapter mAdapter;
//    private FriendListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatListInfo> chatLists;
    private ChatListAdapter chatListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        registerMessageReceiver();
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
//        hud.show();
    }


    private void initView() {
        friendLists = new ArrayList<>();
        friendLists.clear();
        chatLists = new ArrayList<>();
        chatLists.clear();
        mContext = getActivity();
        userInfo = new UserInfo(mContext);
//        et_anno=(TextView)getView().findViewById(R.id.anno);
//        et_anno.setVisibility(View.VISIBLE);
//        circlePhoneFriend = (LinearLayout) getView().findViewById(R.id.circle_phone_friend);
//        circlePhoneFriend.setOnClickListener(this);
//        nearbyFriend = (LinearLayout) getView().findViewById(R.id.circle_nearby);
//        nearbyFriend.setOnClickListener(this);
        myFriend =(LinearLayout)getView().findViewById(R.id.circle_friend);
        myGroup= (LinearLayout) getView().findViewById(R.id.group_friend);
        myFriend.setOnClickListener(this);
        myGroup.setOnClickListener(this);
        xRecyclerView =(XRecyclerView) getView().findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLayoutManager(linearLayoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getChatList();
            }
            @Override
            public void onLoadMore() {
                getChatList();
            }
        });
//        mAdapter = new FriendListAdapter(friendLists, mContext);
        chatListAdapter=new ChatListAdapter(chatLists,mContext);
        xRecyclerView.setAdapter(chatListAdapter);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.circle_phone_friend:
//                startActivity(new Intent(mContext, SearchFriendActivity.class));
//                break;
//            case R.id.circle_nearby:
//                startActivity(new Intent(mContext, NearbyActivity.class));
//            break;
            case R.id.group_friend:
                startActivity(new Intent(mContext, GroupActivty.class));
                break;
            case R.id.circle_friend:
                startActivity(new Intent(mContext, FriendActivity.class));
                break;

        }
    }
    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
        getChatList();

    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
//    private void getFriendList() {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("userid", userInfo.getUserId());
//        requestParams.put("status", "1");
//        HelperAsyncHttpClient.get(NetConstant.GET_FRIENDS_LIST, requestParams,
//                new JsonHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        if (hud != null) {
//                            hud.dismiss();
//                        }
//                        super.onSuccess(statusCode, headers, response);
//                        if (response != null) {
//                            try {
//                                String state = response.getString("status");
//                                if (state.equals("success")) {
//                                    String data = response.getString("data");
//                                    List<FriendListInfo> infos = new Gson().fromJson(data,
//                                            new TypeToken<List<FriendListInfo>>() {
//                                            }.getType());
//                                    friendLists.clear();
//                                    friendLists.addAll(infos);
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }else{
//
//                        }
//                        xRecyclerView.refreshComplete();
//                    }
//
//
//                });
//
//    }
    private void getChatList() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_CHAT_LIST, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    LogUtils.e("TGB","-infos--?"+data.toString());
                                    List<ChatListInfo> infos = new Gson().fromJson(data,
                                            new TypeToken<List<ChatListInfo>>() {
                                            }.getType());
                                    LogUtils.e("TGB","-infos--?"+infos.toString());
                                    chatLists.clear();
                                    chatLists.addAll(infos);
                                    chatListAdapter.notifyDataSetChanged();
                                    xRecyclerView.refreshComplete();
                                    xRecyclerView.loadMoreComplete();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver, filter);
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
                    Log.i("kan在聊天列表", showMsg.toString());
                    Log.i("kanextras在聊天列表", messge);
                    JSONObject jsonObject = new JSONObject(extras);
//                    if (!SelectQunId.equals(jsonObject.getString("txt"))) {
                        getChatList();
//                        Log.i("kanextras在聊天列表", "刷新列表");
//                    }

                }
            } catch (Exception e) {
            }
        }


    }
}
