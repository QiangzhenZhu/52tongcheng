package com.example.mylibrary.KeyBoardHeightTool;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵自强 on 2017/8/22 ${Time}.
 * 这个类的用处
 */

public class KeyBoardAndViewManage {
    private static final String TAG = "看";
    private Activity mActivity;
    private final static long DURATION_SWITCH_EMOTION_KEYBOARD = 150L;
    private View mContentView;

    private OnViewChangeShowStateListener listener;
    private EditText mEditText;
    private  int lastPosition = -1;
    private InputMethodManager mInputMethodManager;
    private List<Bean> list;
    private static int showPosition = -1;
    private int ShownPosition = showPosition;
    private boolean isViewShowing = false;
    private int lastChange = -1;
    public KeyBoardAndViewManage(Activity mActivity, View mContentView, EditText mEditText) {
        this.mActivity = mActivity;
        this.mContentView = mContentView;
        this.mEditText = mEditText;
        list = new ArrayList<>();

        initFieldsWithDefaultValue();
        initEditView();
    }

    private void initEditView() {
        mEditText.requestFocus();
        mEditText.setOnTouchListener(new ThrottleTouchListener() {
            @Override
            public void onThrottleTouch() {
                if (ShownPosition != showPosition) {
                    hideEmotionKeyboardByLockContentViewHeight(list.get(ShownPosition).getShownView());
                    isViewShowing = false;
                    ChangeBackground();
                }
            }
        });
    }

    public static class Bean{
        private View TouchView;
        private View ShownView;

        public Bean(View touchView, View shownView) {
            TouchView = touchView;
            ShownView = shownView;
        }

        public View getTouchView() {
            return TouchView;
        }

        public void setTouchView(View touchView) {
            TouchView = touchView;
        }

        public View getShownView() {
            return ShownView;
        }

        public void setShownView(View shownView) {
            ShownView = shownView;
        }
    }
    public void addBean(Bean bean){list.add(bean);}
    public void setViewList(List<Bean> list){
        this.list.clear();
        this.list = list;
        commit();
    }
    public void commit(){
        for (int i = 0 ; i < list.size() ; i++){
           final int  finalI = i;
            list.get(i).getTouchView().setOnTouchListener(new ThrottleTouchListener() {
                @Override
                public void onThrottleTouch() {
                    if (ShownPosition != showPosition) { // "颜文字键盘"显示
                        if (finalI == ShownPosition){
                            hideEmotionKeyboardByLockContentViewHeight(list.get(finalI).getShownView());
                        }else {
                            hideEmotionKeyboardByLockContentViewHeight1(list.get(finalI).getShownView(),finalI);
                        }
                    } else { // "颜文字键盘"隐藏
                        if (SupportSoftKeyboardUtil.isSoftKeyboardShown(mActivity)) { // "软键盘"显示
                            showEmotionKeyboardByLockContentViewHeight(list.get(finalI).getShownView(),finalI);
                        } else { // "软键盘"隐藏
                            showEmotionKeyboardWithoutLockContentViewHeight(list.get(finalI).getShownView(),finalI);
                        }
                    }
                    lastPosition = finalI;
                    Log.i("看","上次点击的位置："+lastPosition);
                    ChangeBackground();
                }
            });
        }
    }

    private void ChangeBackground() {
        if (listener!= null){
//                    listener.OnViewChangeShowState(lastPosition,true);
//                    listener.OnViewChangeShowState(lastChange,false);

            if (isViewShowing) {
                Log.i(TAG, "ChangeBackground: isViewShowing" + isViewShowing);
                    if (lastChange != -1){
                        Log.i(TAG, "ChangeBackground: 都该" );
                        listener.OnViewChangeShowState(lastChange, false);
                        listener.OnViewChangeShowState(lastPosition, true);
                    }else {
                        Log.i(TAG, "ChangeBackground: 改一个" );
                        listener.OnViewChangeShowState(lastPosition, true);
                    }


            } else {
                Log.i(TAG, "ChangeBackground: isViewShowing" + isViewShowing);
                listener.OnViewChangeShowState(lastPosition, false);
            }
            lastChange = lastPosition;

        }

    }

    public void setOnViewChangeShowStateListener(OnViewChangeShowStateListener listener) {
        this.listener = listener;
    }

    public interface OnViewChangeShowStateListener{
        void OnViewChangeShowState(int position,boolean isShowing);
    }
    /**
     * 显示“颜文字键盘”，隐藏软键盘(锁定“ContentView”的高度)
     */
    private void showEmotionKeyboardByLockContentViewHeight(View view, int showingPosition) {
        view.setVisibility(View.VISIBLE);
        ShownPosition = showingPosition;
        view.getLayoutParams().height =
                SupportSoftKeyboardUtil.getSupportSoftKeyboardHeight(mActivity);

        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0F, 1.0F);
        showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockContentViewHeight();
                hideSoftKeyboard();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                unlockContentViewHeight();
            }
        });
        showAnimator.start();
        isViewShowing =true;
    }

    /**
     * 隐藏“颜文字键盘”，显示“软键盘”（锁定“ContentView”的高度）
     */
    private void hideEmotionKeyboardByLockContentViewHeight(final View view) {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0F, 0.0F);
        hideAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        hideAnimator.setInterpolator(new AccelerateInterpolator());
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockContentViewHeight();
               // showSoftKeyboard();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                ShownPosition = showPosition;
                unlockContentViewHeight();
            }
        });
        hideAnimator.start();
        isViewShowing =false;
    }
    private void hideEmotionKeyboardByLockContentViewHeight1(final View view, final int showingPosition) {
        list.get(ShownPosition).getShownView().setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        ShownPosition = showingPosition;
        view.getLayoutParams().height =
                SupportSoftKeyboardUtil.getSupportSoftKeyboardHeight(mActivity);

        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0F, 1.0F);
        showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lockContentViewHeight();
                hideSoftKeyboard();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                unlockContentViewHeight();
            }
        });
        showAnimator.start();
        isViewShowing =true;
    }
    /**
     * 显示“颜文字键盘”，隐藏“软键盘”(不锁定“ContentView”的高度)
     */
    private void showEmotionKeyboardWithoutLockContentViewHeight(View view, int showingPosition) {
        view.setVisibility(View.VISIBLE);
        ShownPosition = showingPosition;
        view.getLayoutParams().height =
                SupportSoftKeyboardUtil.getSupportSoftKeyboardHeight(mActivity);

        ObjectAnimator showAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.0F, 1.0F);
        showAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        showAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        showAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                hideSoftKeyboard();
            }
        });
        showAnimator.start();
        isViewShowing =true;
    }

    /**
     * 隐藏"颜文字键盘"，不显示“软键盘”
     */
    private void hideEmotionKeyboardWithoutSoftKeyboard(final View view) {
        ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(view, "alpha", 1.0F, 0.0F);
        hideAnimator.setDuration(DURATION_SWITCH_EMOTION_KEYBOARD);
        hideAnimator.setInterpolator(new AccelerateInterpolator());
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        hideAnimator.start();
        ShownPosition = showPosition;
        lastChange = showPosition;
        isViewShowing =false;
        ChangeBackground();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     */
    private void showSoftKeyboard() {
        mEditText.requestFocus();
        mInputMethodManager.showSoftInput(mEditText, 0);
    }

    /**
     * 锁定 ContentView 的高度
     */
    private void lockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        layoutParams.height = mContentView.getHeight();
        layoutParams.weight = 0.0F;
    }
    public boolean interceptBackPressed() {
        // 如果颜文字键盘还在显示，中断"back"操作
        if (isViewShowing) {
            hideEmotionKeyboardWithoutSoftKeyboard(list.get(ShownPosition).getShownView());
            return true;
        }
        return false;
    }
    /**
     * 解锁 ContentView 的高度
     */
    private void unlockContentViewHeight() {
        ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
    }
    private void initFieldsWithDefaultValue() {
        this.mInputMethodManager =
                (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mActivity.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private abstract class ThrottleTouchListener implements View.OnTouchListener {

        private long mLastTouchTime = 0L;

        private final static long TOUCH_SKIP_DURATION = 300L;

        @Override public boolean onTouch(View v, MotionEvent event) {

            long currentTime = System.currentTimeMillis();

            long touchTimeInterval = currentTime - mLastTouchTime;

            if (event.getAction() == MotionEvent.ACTION_UP && touchTimeInterval > TOUCH_SKIP_DURATION) {
                onThrottleTouch();
                mLastTouchTime = currentTime;
            }

            return false;
        }

        /**
         * 过滤了快速点击的方法回调
         */
        public abstract void onThrottleTouch();
    }
}
