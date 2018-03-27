package com.example.mylibrary.photoListView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 赵自强 on 2017/8/26 0:39.
 * 这个类的用处
 */

public class IamgeViewAdjustHeight extends ImageView {
    public IamgeViewAdjustHeight(Context context) {
        super(context);
    }

    public IamgeViewAdjustHeight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IamgeViewAdjustHeight(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IamgeViewAdjustHeight(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if(d!=null){
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            //高度根据使得图片的宽度充满屏幕计算而得
            int resultWidth = (int) Math.ceil((float) height * (float) d.getIntrinsicWidth() / (float) d.getIntrinsicHeight());
            int minWidth =getMinimumWidth();
            if (resultWidth < minWidth){
                setMeasuredDimension(minWidth,height);
            }else if (resultWidth >= minWidth && resultWidth <= width ){
                setMeasuredDimension(resultWidth,height);
            }else if (resultWidth > width){
                setMeasuredDimension(width,height);
            }

        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
