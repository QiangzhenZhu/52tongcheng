package cn.xcom.banjing.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.ButterKnife;
import cn.xcom.banjing.R;

/**
 * Created by hzh on 16/11/22.
 * 此层封装头布局
 */


public abstract class CBaseActivity extends AppCompatActivity {
    private final String TAG = "BaseActivity";
    private TextView titleTv, customTxtBtn;
    private View appBar;
    private ImageView backBtn, customBtn;
    private KProgressHUD hud;//Loading加载框

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);//子类使用ButterKnife时不用在调用此方法
        ActivityCollector.getInstance().addActivity(this);
        Log.d(TAG, getClass().getSimpleName());
        initView();

    }

    public abstract int getContentViewId();

    public abstract void initView();

    /**
     * 通用titlebar 使用时在子类调用此方法，布局文件中include toolbar
     *
     * @param title
     * @param showBackBtn 是否显示返回按钮
     */
    public void setTitleBar(String title, boolean showBackBtn) {
        appBar = findViewById(R.id.common_app_bar);
        titleTv = (TextView) appBar.findViewById(R.id.title);
        backBtn = (ImageView) appBar.findViewById(R.id.back);
        titleTv.setText(title);
        if (showBackBtn) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } else {
            backBtn.setVisibility(View.GONE);
        }

    }


    /**
     * 右侧带有按钮的titlebar
     *
     * @param title
     * @param showBackBtn
     * @param imageId         右侧按钮的图片资源
     * @param onClickListener
     */
    public void setTitleBar(String title, boolean showBackBtn, int imageId,
                            View.OnClickListener onClickListener) {
        this.setTitleBar(title, showBackBtn);
        customBtn = (ImageView) appBar.findViewById(R.id.custom_btn);
        customBtn.setImageResource(imageId);
        customBtn.setOnClickListener(onClickListener);
    }

    /**
     * 右侧带文字的titlebar
     *
     * @param title
     * @param showBackBtn
     * @param customTxt
     * @param onClickListener
     */
    public void setTitleBar(String title, boolean showBackBtn, String customTxt,
                            View.OnClickListener onClickListener) {
        this.setTitleBar(title, showBackBtn);
        customTxtBtn = (TextView) appBar.findViewById(R.id.custom_text_btn);
        customTxtBtn.setText(customTxt);
        customTxtBtn.setOnClickListener(onClickListener);
    }

    /**
     * @param cancellable 返回键是否可取消
     *                    Loading加载框
     */
    public void showProgressDialog(boolean cancellable) {
        if (hud == null) {
            hud = KProgressHUD.create(this).setCancellable(cancellable).show();
        }
    }

    /**
     * @param cancellable
     * @param label       //提示信息
     */
    public void showProgressDialog(boolean cancellable, String label) {
        hud = KProgressHUD.create(this).setCancellable(cancellable).setLabel(label).show();
    }

    public void closeProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        ActivityCollector.getInstance().removeActivity(this);

//        RefWatcher refWatcher = HappyNineApplication.getRefWatcher(this);
//        refWatcher.watch(this);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}