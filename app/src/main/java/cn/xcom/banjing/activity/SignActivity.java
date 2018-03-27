package cn.xcom.banjing.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.xcom.banjing.R;
import cn.xcom.banjing.adapter.SignCouponAdapter;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.utils.LogUtils;
import cz.msebera.android.httpclient.Header;
/**
 * Created by zhuchongkun on 16/6/12.
 * 签到页
 */
public class SignActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="SignActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_integral;
    private Button bt_sign;
    private SignCouponAdapter mAdapter;
    private UserInfo userInfo;
    private String todayTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign);
        mContext=this;
        initView();
    }
    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_sign_back);
        rl_back.setOnClickListener(this);
        tv_integral= (TextView) findViewById(R.id.tv_sign_integral);
        bt_sign= (Button) findViewById(R.id.bt_sign_sign);
        bt_sign.setOnClickListener(this);
        userInfo=new UserInfo(mContext);
        getNowTimeStemp();
        isSign();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_sign_back:
                finish();
                break;
            case R.id.bt_sign_sign:
                toSign();
                break;
        }

    }

    private void isSign() {
        RequestParams requestParams=new RequestParams();
        requestParams.put("userid",userInfo.getUserId());
        requestParams.put("day", todayTime);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_SIGN_STATE,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            bt_sign.setClickable(false);
                            bt_sign.setText(R.string.bt_sign_sign_ok);
                        }if(state.equals("error")){
                            bt_sign.setClickable(true);
                            bt_sign.setText(R.string.bt_sign_sign);
                            String data=response.getString("data");
                            Toast.makeText(mContext,data,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void toSign() {
        RequestParams requestParams=new RequestParams();
        requestParams.put("userid",userInfo.getUserId());
        requestParams.put("day", todayTime);
        LogUtils.e(TAG,"--requestParams->"+requestParams.toString());
        HelperAsyncHttpClient.get(NetConstant.NET_TO_SIGN,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                    try {
                        String state=response.getString("status");
                        if (state.equals("success")){
                            bt_sign.setClickable(false);
                            bt_sign.setText(R.string.bt_sign_sign_ok);
                        }if(state.equals("error")){
                            bt_sign.setClickable(true);
                            bt_sign.setText(R.string.bt_sign_sign);
                            String data=response.getString("data");
                            Toast.makeText(mContext,data,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getNowTimeStemp(){
        Date date = new Date();
        long time = date.getTime();
        String s = String.valueOf(time);
        todayTime =s.substring(0, 10);
    }

}
