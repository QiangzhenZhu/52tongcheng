package cn.xcom.banjing.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.WXpay.Constants;
//import cn.xcom.banjing.activity.ConvenienceActivity;
import cn.xcom.banjing.activity.MyAdvertsingActivity;
import cn.xcom.banjing.activity.MyOrderActivity;
import cn.xcom.banjing.bean.OrderHelper;
import cn.xcom.banjing.utils.ToastUtils;

/**
 * Created by zhuchongkun.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String APP_ID = "wxc070e9d5f68c802e";
    private Context mContext;
    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("pay", "===enter");
        Log.e("pay", HelperApplication.getInstance().tradeNo);

//        if (HelperApplication.getInstance().payType.equals("3")) {
//
//            HelperApplication.getInstance().trendsBack = true;
//            ToastUtil.showShort(mContext, "3");
//            finish();
//        } else if (HelperApplication.getInstance().payType.equals("4")) {
//            ToastUtil.showShort(mContext, "4");
//            finish();
//            startActivity(new Intent(mContext, ConvenienceActivity.class));
//        } else if (HelperApplication.getInstance().payType.equals("5")) {
//            ToastUtil.showShort(mContext, "5");
//            HelperApplication.getInstance().advBack = true;
//            finish();
//        } else if (HelperApplication.getInstance().payType.equals("6")) {
//            ToastUtil.showShort(mContext, "6");
//            HelperApplication.getInstance().advBack = true;
////                HelperApplication.getInstance().type = "";
//            finish();
//            startActivity(new Intent(mContext, MyAdvertsingActivity.class));
//        } else {
            UpdateTradeState();
//        }

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            HelperApplication.getInstance().wxpay = "3";
            if ("0".equals(String.valueOf(resp.errCode))) {
                ToastUtils.showToast(this, "支付成功");
                Log.e("pay", "success");


            } else if ("-1".equals(String.valueOf(resp.errCode))) {
                Log.e("wxpay", String.valueOf(resp.errCode) );
                finish();
                ToastUtils.showToast(this, "支付失败了");

            } else if ("-2".equals(String.valueOf(resp.errCode))) {
                finish();
                ToastUtils.showToast(this, "取消支付");

            }
        }
    }

    /**
     * 更新任务状态
     */
    private void UpdateTradeState() {
//        String url = "";
//        if (HelperApplication.getInstance().payType.equals("1")) {
//            url = NetConstant.UPDATETASKPAY;
//        } else if (HelperApplication.getInstance().payType.equals("2")) {
//            url = NetConstant.UPDATESHOPPAY;
//        }
//        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.d("更新支付状态", s);
//                //跳转到我的订单页面
        if (HelperApplication.getInstance().payType.equals("2")) {
            finish();
            startActivity(new Intent(mContext, MyOrderActivity.class).
                    putExtra("order_type", OrderHelper.BuyerOrder));
        } else if (HelperApplication.getInstance().payType.equals("1")) {
//                    startActivity(new Intent(mContext, BillActivity.class));
            finish();
        } else if (HelperApplication.getInstance().payType.equals("3")) {

            HelperApplication.getInstance().trendsBack = true;

            finish();
        }
//        else if (HelperApplication.getInstance().payType.equals("4")) {
//
//            finish();
//            startActivity(new Intent(mContext, ConvenienceActivity.class));
//        }
        else if (HelperApplication.getInstance().payType.equals("5")) {

            HelperApplication.getInstance().advBack = true;
            finish();
        } else if (HelperApplication.getInstance().payType.equals("6")) {
            HelperApplication.getInstance().advBack = true;
//                HelperApplication.getInstance().type = "";
            finish();
            startActivity(new Intent(mContext, MyAdvertsingActivity.class));
        }
//                HelperApplication.getInstance().payType = "";
//                HelperApplication.getInstance().tradeNo = "";
        finish();
    }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(getApplication(), "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//        request.putValue("order_num", HelperApplication.getInstance().tradeNo);
//        request.putValue("paytype", "weixin");
//        SingleVolleyRequest.getInstance(getApplication()).addToRequestQueue(request);
//    }
}
