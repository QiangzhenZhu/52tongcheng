package cn.xcom.banjing.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.xcom.banjing.HelperApplication;
import cn.xcom.banjing.R;
import cn.xcom.banjing.WXpay.Constants;
import cn.xcom.banjing.activity.DisplayAdViewPagerActivivty;
import cn.xcom.banjing.adapter.CityAdapter;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.bean.locationBase;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;
import cn.xcom.banjing.utils.ToastUtil;


/**
 * Created by zhuchongkun
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String APP_ID="wxc070e9d5f68c802e";
    private IWXAPI iwxapi;
    private Context mContext;
    private KProgressHUD hud;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        iwxapi= WXAPIFactory.createWXAPI(this, Constants.APP_ID,false);
        iwxapi.registerApp(APP_ID);
        iwxapi.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                hud = KProgressHUD.create(mContext)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setDetailsLabel("正在加载...")
                        .setCancellable(true);
                userInfo = new UserInfo(mContext);
                if(hud!= null) {
                    hud.show();
                }
                StringPostRequest request = new StringPostRequest(NetConstant.SHARE_SUCCESS, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                HelperApplication.shareType = 0;
                                HelperApplication.refid = 0;
                                String data = jsonObject.getString("data");
                                finish();


                            }
                            else {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (hud != null) {
                            hud.dismiss();
                        }
                        finish();


                    }
                });
                request.putValue("userid",userInfo.getUserId());
                request.putValue("type", String.valueOf(HelperApplication.shareType));
                request.putValue("ref_id",String.valueOf(HelperApplication.refid));
                SingleVolleyRequest.getInstance(mContext).addToRequestQueue(request);
                break;

            default:
                break;
        }
    }
}
