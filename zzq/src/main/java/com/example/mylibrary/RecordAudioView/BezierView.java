package com.example.mylibrary.RecordAudioView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.mylibrary.R;

/**
 * Created by 赵自强 on 2017/8/24 1:17.
 * 这个类的用处
 */

public class BezierView extends View {
    private Point assistPoint; //控制点
    private Paint mPaint; //画笔
    private Path mPath; //路径
    private Point startPoint; //起点
    private Point endPoint;  //终点
    private int paintWidth = 2;
    private int paintColor = Color.RED;
    private float proportion = 1f;
    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.BezierView));
        init();
    }
    private void parseAttributes(TypedArray a) {
        paintWidth = (int) a.getDimension(R.styleable.BezierView_paint_width,
                paintWidth);
        paintColor = a.getColor(R.styleable.BezierView_paint_color, paintColor);
        proportion = a.getFraction(R.styleable.BezierView_height_proportion,1,1,proportion);
        // 使用TypedArray获得控件属性时必须要注意：使用结束后必须回收TypedArray的对象
        a.recycle();
    }
    public BezierView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();

        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("kan","画图");
        mPaint.setColor(paintColor); //画笔颜色
        mPaint.setStrokeWidth(paintWidth); //画笔宽度
        mPaint.setStyle(Paint.Style.STROKE);
        Log.i("看zuo",getLeft()+"");
        Log.i("看shang ",getTop()+"");
        Log.i("看you ",getRight()+"");
        startPoint = new Point(getLeft(), getTop());
        endPoint = new Point(getRight(), getTop());
        assistPoint = new Point((getRight() +getLeft())/2,getBottom());
        mPath.reset();
        //起点
        mPath.moveTo(startPoint.x, startPoint.y);
        //mPath
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        //画path
        canvas.drawPath(mPath, mPaint);
        //画控制点
        canvas.drawPoint(assistPoint.x, assistPoint.y, mPaint);

        //画线
//        canvas.drawLine(startPoint.x, startPoint.y, assistPoint.x, assistPoint.y, mPaint);
//        canvas.drawLine(endPoint.x, endPoint.y, assistPoint.x, assistPoint.y, mPaint);
    }

}
