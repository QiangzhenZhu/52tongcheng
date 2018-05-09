package cn.xcom.banjing.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.TextOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import cn.xcom.banjing.R;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.net.OkHttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReportActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rl_msg_back)
    RelativeLayout rlMsgBack;
    @BindView(R.id.et_add_report_reason)
    EditText etAddReportReason;
    @BindView(R.id.btn_report_commit)
    TextView btnReportCommit;
    private Context mContext;
    private UserInfo userInfo;
    private String refid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        mContext = this;
        refid = getIntent().getStringExtra("refid");
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        rlMsgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnReportCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etAddReportReason.getText().toString().trim())){
                    report();
                }else {
                    Toast.makeText(mContext,"请填写举报理由",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void report(){
        Uri uri = Uri.parse(NetConstant.REPORT)
                .buildUpon()
                .appendQueryParameter("userid",userInfo.getUserId())
                .appendQueryParameter("refid",refid)
                .appendQueryParameter("type","5")
                .appendQueryParameter("content",etAddReportReason.getText().toString().trim())
                .build();
        OkHttpUtils.sendOkHttp(uri.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext,"网络加载错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    final JSONObject object = new JSONObject(result);
                    if ("success".equals(object.getString("status"))){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext,"举报成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String error = object.getString("data");
                                    Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
