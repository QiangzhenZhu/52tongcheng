package com.example.mylibrary.Chat;

import android.view.View;

/**
 * Created by 赵自强 on 2017/8/25 20:13.
 * 这个类的用处
 */

public interface OnChatViewClickListener {
    /*头像的点事件监听*/
    void OnPhotoClicked(View view,photoType photoType);
    /*文本长按事件监听*/
    void OnTextViewLongClicked(View view);
    /*播放按钮点击事件监听*/
    void OnPlayButtonClickedListener(View view,photoType photoType);
    /*图片点击事件监听*/
    void OnPictureClickedListener(View view);
    /*图片长按事件监听*/
    void OnPictureLongClickedListener(View view);
}
