package cn.xcom.banjing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LLSInterface;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.HelperConstant;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.HelperAsyncHttpClient;
import cn.xcom.banjing.utils.LogUtils;
import cn.xcom.banjing.utils.SPUtils;
import cz.msebera.android.httpclient.Header;


public class AuthenticateMoneyActivity extends AppCompatActivity implements View.OnClickListener {
    private String status;
    private ImageView statusView;
    private TextView TitleText;
    private TextView contentText;
    private Button nowPay;
    private Button notPay;
    private LinearLayout llRealName;
    private UserInfo userInfo;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate_money);
        //获取保证金认证状态
        status=(String) SPUtils.get(this,HelperConstant.IS_INSURANCE,"");
        ////type=1,2分别表示为从实名认证启动，还是保证金启动
        type=getIntent().getStringExtra("type");
        statusView= (ImageView) findViewById(R.id.iv_realname_status);
        TitleText= (TextView) findViewById(R.id.tv_realname_title);
        contentText= (TextView) findViewById(R.id.tv_realname_content);
        nowPay = (Button) findViewById(R.id.now_pay);
        notPay = (Button) findViewById(R.id.not_pay);
        nowPay.setOnClickListener(this);
        notPay.setOnClickListener(this);
        userInfo=new UserInfo(this);
        llRealName= (LinearLayout) findViewById(R.id.ll_realName);
        status=(String) SPUtils.get(this, HelperConstant.IS_INSURANCE,"");
        //先判断Type在判断保證金已經交了，审核通过了，或者还在审核中
        if ("1".equals(type)) {
            //保证金交了或者在审核中
            if ("1".equals(status) || "-1".equals(status)) {
                TitleText.setText("实名认证已完成");
                contentText.setText("");
                llRealName.setVisibility(View.GONE);
            }
            //没交保证金；
            if ("0".equals(status)){
                statusView.setImageResource(R.mipmap.ic_autn_ing);
                TitleText.setText("实名认证已完成");
            }
        }
        //是在保证金接口启动的
        if ("2".equals(type)) {
            if ("0".equals(status)) {
                //保证金还没交
                statusView.setImageResource(R.mipmap.ic_autn_ing);
                TitleText.setText("保证金还未缴纳");
                contentText.setText("缴纳保证金才可以接单奥~");
            }
            if ("1".equals(status)){
                TitleText.setText("保证金已缴纳");
                contentText.setText("赶快拿起手机接单吧");
                llRealName.setVisibility(View.GONE);
            }
            if ("-1".equals(status)){
                statusView.setImageResource(R.mipmap.ic_autn_ing);
                TitleText.setText("保证金正在审核中");
                contentText.setText("");
                llRealName.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.now_pay:
                getMarginPrice();
                break;
            case R.id.not_pay:
                finish();
                break;
        }
    }


    /**
     * 获取保证金价格
     */

    public void getMarginPrice(){
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("city","0");
        HelperAsyncHttpClient.get(NetConstant.GET_MARGIN_PRIC, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e("获取保证金价格", response.toString());
                if (response.optString("status").equals("success")) {
                    String price= response.optString("data");
                    getOrderNum(price);

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }

    /**
     * 获取订单号
     */
    public void getOrderNum(final String price){

            RequestParams params = new RequestParams();
            params.put("userid", userInfo.getUserId());
            params.put("money","0.1");
            HelperAsyncHttpClient.get(NetConstant.GET_MARGIN_ORDER_NUM, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    LogUtils.e("获取订单号", response.toString());
                    if (response.optString("status").equals("success")) {
                        Intent intent=new Intent(AuthenticateMoneyActivity.this,PaymentActivity.class);
                        intent.putExtra("price",price);
                        intent.putExtra("tradeNo",response.optString("data"));
                        intent.putExtra("body","保证金");
                        intent.putExtra("type","2");
                        startActivity(intent);

                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e("认证", responseString);
                }
            });

    }

}
