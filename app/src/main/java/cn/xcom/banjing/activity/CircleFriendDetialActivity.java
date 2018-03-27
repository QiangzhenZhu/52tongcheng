package cn.xcom.banjing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.AuthenticationList;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.GlideUtils;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;

/**
 * Created by mac on 2017/9/27.
 */


public class CircleFriendDetialActivity extends CBaseActivity {
    @BindView(R.id.circle_detical_request)
    EditText circle_detical_request;
    @BindView(R.id.circle_detical_message)
    Button circleDeticalMessage;
    @BindView(R.id.circle_detical_av)
    ImageView circleDeticalAv;
    @BindView(R.id.circle_detical_name)
    TextView circleDeticalName;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    private Context context;
    AuthenticationList friendListBean;
    UserInfo userinfo;
    String type = "";
    private KProgressHUD hud, submit_hub;
    private HomeActivity homeActivity;

    @Override
    public int getContentViewId() {
        return R.layout.activity_circle_friend_detial;
    }

    @Override
    public void initView() {
        context = this;
        userinfo = new UserInfo(context);

        initdata();
    }

    private void initdata() {
        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");
        friendListBean = (AuthenticationList) getIntent().getSerializableExtra("friend");

        GlideUtils.loadRectangImageView(context, NetConstant.NET_DISPLAY_IMG + friendListBean.getPhoto(), circleDeticalAv);
        circleDeticalName.setText(friendListBean.getName());
//        setTitleBar(friendListBean.getName(), true);
//        String time = ((DateUtils.dateToUnixTimestamp() / 1000 - Integer.parseInt(friendListBean.getTime()))) / 86400 + "";
//        int age = (Integer.parseInt(DateUtils.getNowTime("yyyyMMdd")) - Integer.parseInt(DateUtils.getStrTime(friendListBean.getBirthday(), "yyyyMMdd")));
//        GlideUtils.loadRectangImageView(context, NetConstant.SHOW_IMAGE + friendListBean.getPhoto(), circleDeticalAv);
//        circleDeticalAge.setText("年龄：" + age / 10000 + "岁");
//        circleDeticalTreatment.setText("治疗天数：" + time + "天");
//        circleDeticalDisease.setText("病种：" + "服务器暂无此字段");
//        circleDeticalIdnum.setText("ID号：" + friendListBean.getId());
//        circleDeticalName.setText(friendListBean.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.circle_detical_message,R.id.rl_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.circle_detical_message:
                addFriend();
                break;

        }
    }
//    @Override
//    public void onAttach(Activity activity) {
//
//        super.onAttach(activity);
//    }
    private void addFriend() {
        submit_hub = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true);
        submit_hub.show();
        String url;

        url = NetConstant.NET_ADD_FRIEND;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                if (s != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String state = object.getString("status");
                        if (state.equals("success")) {
                            String data = object.getString("data");
                            HelperApplication.getInstance().getTaskTypes().clear();
                            Log.d("添加好友成功", data);
//                            homeActivity.checkToSecond(2);
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                             Toast.makeText(getApplication(), "好友申请已提交", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(context, NearbyActivity.class);
//
//                            startActivity(intent);
                            finish();
                        } else {
                            String error = object.getString("data");
                            Toast.makeText(getApplication(), error, Toast.LENGTH_SHORT).show();
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
                if (submit_hub != null) {
                    submit_hub.dismiss();
                }
                HelperApplication.getInstance().getTaskTypes().clear();
                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });
        request.putValue("userid", userinfo.getUserId());
        request.putValue("friendid", friendListBean.getId());
        request.putValue("remark", circle_detical_request.getText().toString());
        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
    }
}
