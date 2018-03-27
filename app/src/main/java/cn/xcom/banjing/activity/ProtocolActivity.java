package cn.xcom.banjing.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.banjing.R;
import cn.xcom.banjing.constant.NetConstant;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class ProtocolActivity extends Activity {
    @BindView(R.id.rl_i_protocol_back)
    RelativeLayout rlIProtocolBack;
    @BindView(R.id.wv_userr_protocol)
    WebView wvUserrProtocol;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);
        context = this;
        wvUserrProtocol.getSettings().setJavaScriptEnabled(true);
        wvUserrProtocol.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvUserrProtocol.loadUrl(NetConstant.USER_PROTOCOL);
        wvUserrProtocol.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.rl_i_protocol_back)
    public void onViewClicked() {
        finish();
    }

}
