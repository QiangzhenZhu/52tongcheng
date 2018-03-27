package cn.xcom.banjing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.AuthenticationList;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.utils.ToastUtil;
import cn.xcom.banjing.view.ClearEditText;
import cz.msebera.android.httpclient.Header;

//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by mac on 2017/9/23.
 */

public class SearchFriendActivity extends CBaseActivity {
    @BindView(R.id.filter_edit)
    ClearEditText filterEdit;
    @BindView(R.id.search_friend_xrv)
    XRecyclerView searchFriendXrv;
    private Context context;
    AuthenticationList friendListBean;
    String type="";
    private UserInfo userInfo;
    private Context mContext;
    private KProgressHUD hud;
    @Override
    public int getContentViewId() {
        mContext = this;
        return R.layout.activity_search_friend;
    }


    @Override
    public void initView() {

        context = this;
//        setTitleBar("添加好友", true);
        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");
        userInfo = new UserInfo();
        userInfo.readData(mContext);

        initSearch();

    }

    private void initSearch() {
        filterEdit.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchFriendActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    if (filterEdit.getText().toString().equals("")) {
//                        ToastUtils.showShort(context, "请输入控糖id");
                    } else {
                        search(filterEdit.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    private void search(String keyword) {
        hud = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        hud.show();
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        requestParams.put("keyword", keyword);
        HelperAsyncHttpClient.get(NetConstant.NET_SEARCH_FRIEND, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (hud != null) {
                            hud.dismiss();
                        }
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    Gson gson = new Gson();
                                    friendListBean = gson.fromJson(data, AuthenticationList.class);
                                    Intent intent = new Intent(context, CircleFriendDetialActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("friend", friendListBean);
                                    intent.putExtras(bundle);
                                    intent.putExtra("type", type);
                                    context.startActivity(intent);
//                                    friendLists.clear();
//                                    friendLists.addAll(infos);
//                                    friendListAdapter.notifyDataSetChanged();
                                } else {
                                    ToastUtil.Toast(mContext, "没有搜到用户");
                                }
                            } catch (JSONException e) {
                                ToastUtil.Toast(mContext, "没有搜到用户");
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.Toast(mContext, "没有搜到用户,请检查您的网络");
                        }
                    }
                });

    }
//        showProgressDialog(true);
//        OkHttpUtils.post().url(NetConstant.NET_SEARCH_FRIEND)
//                .addParams("userid", userInfo.getUserId())
//                .addParams("keyword", keyword)
//                .build()
//                .execute(new StringCallback() {
////                    @Override
////                    public void onError(Call call, Exception e, int id) {
//////                        ToastUtils.showShort(context, "搜索失败，请检查您的网络");
////                    }
//
//                    @Override
//                    public void onError(okhttp3.Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
////                        closeProgressDialog();
//                        try {
//                            ResponseHelper rh = new ResponseHelper(response);
//                            if (rh.isSuccess()) {
//                                Gson gson = new Gson();
//                                friendListBean = gson.fromJson(rh.getData(), FriendListInfo.class);
////                                Intent intent = new Intent(context, CircleFriendDetialActivity.class);
////                                Bundle bundle = new Bundle();
////                                bundle.putSerializable("friend", friendListBean);
////                                intent.putExtras(bundle);
////                                intent.putExtra("type",type);
////                                context.startActivity(intent);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}