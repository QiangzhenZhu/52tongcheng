package com.example.mylibrary.Chat;

/**
 * Created by 赵自强 on 2017/8/24 15:17.
 * 这个类的用处
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.Spannable;
import android.util.AttributeSet;

import com.example.mylibrary.Chat.BubbleDrawable.ArrowLocation;
import com.example.mylibrary.Chat.BubbleDrawable.BubbleType;
import com.example.mylibrary.Chat.BubbleDrawable.Builder;
import com.example.mylibrary.R;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class BubbleTextView extends EmojiconTextView {
    private BubbleDrawable bubbleDrawable;
    private float mArrowWidth;
    private float mAngle;
    private float mArrowHeight;
    private float mArrowPosition;
    private int bubbleColor;
    private ArrowLocation mArrowLocation;
    private boolean mArrowCenter;

    public BubbleTextView(Context context) {
        super(context);
        this.initView((AttributeSet)null);
    }
    public void setBubbleColor(int bubbleColor) {
        this.bubbleColor = bubbleColor;
        this.initView((AttributeSet)null);
    }

    public BubbleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(attrs);
    }

    public BubbleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initView(attrs);
    }

    private void emotifySpannable(Spannable spanned) {
    }

    private void initView(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray array = this.getContext().obtainStyledAttributes(attrs, R.styleable.BubbleView);
            this.mArrowWidth = array.getDimension(R.styleable.BubbleView_arrowWidth, Builder.DEFAULT_ARROW_WITH);
            this.mArrowHeight = array.getDimension(R.styleable.BubbleView_arrowHeight, Builder.DEFAULT_ARROW_HEIGHT);
            this.mAngle = array.getDimension(R.styleable.BubbleView_angle, Builder.DEFAULT_ANGLE);
            this.mArrowPosition = array.getDimension(R.styleable.BubbleView_arrowPosition, Builder.DEFAULT_ARROW_POSITION);
            this.bubbleColor = array.getColor(R.styleable.BubbleView_bubbleColor, Builder.DEFAULT_BUBBLE_COLOR);
            int location = array.getInt(R.styleable.BubbleView_arrowLocation, 0);
            this.mArrowLocation = ArrowLocation.mapIntToValue(location);
            this.mArrowCenter = array.getBoolean(R.styleable.BubbleView_arrowCenter, false);
            array.recycle();
        }

        this.setUpPadding();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w > 0 && h > 0) {
            this.setUp(w, h);
        }

    }

    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        this.setUp();
    }

    protected void onDraw(Canvas canvas) {
        if(this.bubbleDrawable != null) {
            this.bubbleDrawable.draw(canvas);
        }

        super.onDraw(canvas);
    }

    private void setUp(int width, int height) {
        this.setUp(0, width, 0, height);
    }

    private void setUp() {
        this.setUp(this.getWidth(), this.getHeight());
    }

    private void setUp(int left, int right, int top, int bottom) {
        RectF rectF = new RectF((float)left, (float)top, (float)right, (float)bottom);
        this.bubbleDrawable = (new Builder()).rect(rectF).arrowLocation(this.mArrowLocation).bubbleType(BubbleType.COLOR).angle(this.mAngle).arrowHeight(this.mArrowHeight).arrowWidth(this.mArrowWidth).bubbleColor(this.bubbleColor).arrowPosition(this.mArrowPosition).arrowCenter(this.mArrowCenter).build();
    }

    private void setUpPadding() {
        int left = this.getPaddingLeft();
        int right = this.getPaddingRight();
        int top = this.getPaddingTop();
        int bottom = this.getPaddingBottom();
        switch (mArrowLocation) {
            case LEFT:
                left += mArrowWidth;
                break;
            case RIGHT:
                right += mArrowWidth;
                break;
            case TOP:
                top += mArrowHeight;
                break;
            case BOTTOM:
                bottom += mArrowHeight;
                break;
        }

        this.setPadding(left, top, right, bottom);
    }

}
