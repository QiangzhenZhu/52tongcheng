package cn.xcom.banjing.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.banjing.R;
import cn.xcom.banjing.activity.DisplayAdvertisingActivity;
import cn.xcom.banjing.bean.ADInfo;
import cn.xcom.banjing.bean.Convenience;
import cn.xcom.banjing.bean.UserInfo;
import cn.xcom.banjing.constant.NetConstant;
import cn.xcom.banjing.utils.SingleVolleyRequest;
import cn.xcom.banjing.utils.StringPostRequest;


/**
 * Created by 10835 on 2017/12/12.
 */

public class CommentPopupWindow extends PopupWindow {
    private Context mContext;
    private View view;
    private EditText commentEdit;
    private Button submit;
    private UserInfo userInfo;
    private Convenience adInfo;

    public CommentPopupWindow(Context context, View.OnClickListener itemOnclick, TextWatcher watcher,UserInfo userInfo, Convenience adInfo) {
        super(context);
        this.mContext = context;
        this.view= LayoutInflater.from(context).inflate(R.layout.popup_ad_comment,null);
        this.userInfo=userInfo;
        this.adInfo=adInfo;
        commentEdit = (EditText) view.findViewById(R.id.write_comment);
        submit = (Button) view.findViewById(R.id.bt_ad_submit);
        submit.setOnClickListener(itemOnclick);
        commentEdit.addTextChangedListener(watcher);

        ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        this.setOutsideTouchable(true);
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.popup_comment).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 实例化一个ColorDrawable颜色为不透明
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        //this.setAnimationStyle(R.style.take_photo_anim);
    }

}
