package com.tc5u.vehiclemanger.customerstyle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.tc5u.vehiclemanger.R;
import com.tc5u.vehiclemanger.utils.TextDrawUtils;

import java.util.ArrayList;
import java.util.List;


public class SideBarView extends View {
    private int mBackgroundColor;
    private int mStrokeColor;
    private int mTextColor;
    private int mTextSize;
    private int mSelectTextColor;
    private int mSelectTextSize;
    private int mHintTextSize;
    private int mContentPadding;
    private int mBarPadding;
    private int mBarWidth;

    private List<String> mLetters = new ArrayList<>();
    private RectF mSlideBarRect;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private int mSelect;
    private int mPreSelect;
    private int mNewSelect;
    private ValueAnimator mRatioAnimator;
    private float mAnimationRatio;
    private OnLetterChangeListener mListener;
    private float mTextHeight;
    private float contentTop;
    private int hitWeight;
    private int hintBackgroundColor;
    private int mTouchY;

    public SideBarView(Context context) {
        this(context, null);
    }

    public SideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(attrs, defStyleAttr);
        initData();
    }

    private void initAttribute(AttributeSet attrs, int defStyleAttr) {
        TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.SideBarView, defStyleAttr, 0);

        // sidebar颜色
        mBackgroundColor = typeArray.getColor(R.styleable.SideBarView_backgroundColor, Color.parseColor("#F9F9F9")); // 灰色 有效内容背景
        mStrokeColor = typeArray.getColor(R.styleable.SideBarView_strokeColor, Color.parseColor("#F9F9F9"));

        // sidebarIndex 颜色
        mTextColor = typeArray.getColor(R.styleable.SideBarView_textColor, Color.parseColor("#000000"));// 黑色
        mTextSize = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                        getResources().getDisplayMetrics()));

        // 选中颜色
        mSelectTextColor = typeArray.getColor(R.styleable.SideBarView_selectTextColor, Color.parseColor("#09F797")); // 绿色
        mSelectTextSize = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_selectTextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                        getResources().getDisplayMetrics()));

        mContentPadding = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_contentPadding,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                        getResources().getDisplayMetrics()));
        mBarPadding = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_barPadding,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                        getResources().getDisplayMetrics()));
        mBarWidth = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_barWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                        getResources().getDisplayMetrics()));
        if (mBarWidth == 0) {
            mBarWidth = 2 * mTextSize;
        }

        mTextHeight = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_textHeight,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));


        mHintTextSize = typeArray.getDimensionPixelOffset(R.styleable.AZWaveSideBarView_hintTextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                        getResources().getDisplayMetrics()));

        hitWeight = typeArray.getDimensionPixelOffset(R.styleable.SideBarView_hintHeight,
                 (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60,
                        getResources().getDisplayMetrics()));


        hintBackgroundColor = typeArray.getColor(R.styleable.SideBarView_hintBackGround, Color.parseColor("#808080"));

        typeArray.recycle();
    }

    private void initData() {
        mTextPaint = new TextPaint();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mSelect = -1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSlideBarRect == null || mSlideBarRect.bottom - mSlideBarRect.top < 1) {
            mSlideBarRect = new RectF();
        }
        int gheight = getMeasuredHeight();
        float height = mLetters.size() * mTextHeight;
        float contentLeft = getMeasuredWidth() - mBarWidth - mBarPadding;
        float contentRight = getMeasuredWidth() - mBarPadding;
        contentTop = (gheight - height) / 2 + mBarPadding;
        float contentBottom = contentTop + height + mBarPadding;
        mSlideBarRect.set(contentLeft, contentTop, contentRight, contentBottom);
        //绘制slide bar 上字母列表
        drawLetters(canvas);
        //绘制选中时的提示信息(圆＋文字)
        drawHint(canvas);
        //绘制选中的slide bar上的那个文字
        drawSelect(canvas);
    }

    /**
     * 绘制slide bar 上字母列表
     */
    private void drawLetters(Canvas canvas) {
////        绘制圆角矩形
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(mBackgroundColor);
//        canvas.drawRoundRect(mSlideBarRect, mBarWidth / 2.0f, mBarWidth / 2.0f, mPaint);
////        绘制描边
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(mStrokeColor);
//        canvas.drawRoundRect(mSlideBarRect, mBarWidth / 2.0f, mBarWidth / 2.0f, mPaint);
        //顺序绘制文字
        for (int index = 0; index < mLetters.size(); index++) {
            float baseLine = TextDrawUtils.getTextBaseLineByCenter(
                    mSlideBarRect.top + mContentPadding + mTextHeight * index + mTextHeight / 2, mTextPaint, mTextSize);
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            float pointX = mSlideBarRect.left + (mSlideBarRect.right - mSlideBarRect.left) / 2.0f;
            canvas.drawText(mLetters.get(index), pointX, baseLine, mTextPaint);
        }
    }

    /**
     * 绘制选中时的提示信息(圆＋文字)
     */
    private void drawSelect(Canvas canvas) {
        if (mSelect != -1) {
            mTextPaint.setColor(mSelectTextColor);
            mTextPaint.setTextSize(mSelectTextSize);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            float baseLine = TextDrawUtils.getTextBaseLineByCenter(
                    mSlideBarRect.top + mContentPadding + mTextHeight * mSelect + mTextHeight / 2, mTextPaint, mTextSize);
            float pointX = mSlideBarRect.left + (mSlideBarRect.right - mSlideBarRect.left) / 2.0f;
            canvas.drawText(mLetters.get(mSelect), pointX, baseLine, mTextPaint);
        }
    }

    /**
     * 绘制选中的slide bar上的那个文字
     */
    private void drawHint(Canvas canvas) {
        if (mSelect != -1) {
            Rect rect = new Rect((getMeasuredWidth() - hitWeight) / 2, (getMeasuredHeight() - hitWeight) / 2, (getMeasuredWidth() + hitWeight) / 2, (getMeasuredHeight() + hitWeight) / 2);//画一个矩形
            mPaint.setColor(hintBackgroundColor);
            mPaint.setStyle(Paint.Style.FILL);

            canvas.drawRect(rect, mPaint);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(mHintTextSize);
            textPaint.setFakeBoldText(true);
            textPaint.setStrokeWidth(5);//设置画笔宽度
            textPaint.setStyle(Paint.Style.FILL);
            //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
            textPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
            canvas.drawText(mLetters.get(mSelect), rect.centerX(), baseLineY, textPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY() - contentTop;
        final float x = event.getX();
        mPreSelect = mSelect;
        mNewSelect = (int) (y / (mSlideBarRect.bottom - mSlideBarRect.top) * mLetters.size());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < mSlideBarRect.left || y < 0 || y > mSlideBarRect.bottom) {
                    return false;
                }
                mTouchY = (int) y;
                if (mPreSelect != mNewSelect) {
                    if (mNewSelect >= 0 && mNewSelect < mLetters.size()) {
                        mSelect = mNewSelect;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(mNewSelect));
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mTouchY = (int) y;
                if (mPreSelect != mNewSelect) {
                    if (mNewSelect >= 0 && mNewSelect < mLetters.size()) {
                        mSelect = mNewSelect;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(mNewSelect));
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startAnimator(1f);
                mSelect = -1;
                break;
            default:
                break;
        }
        return true;
    }

    private void startAnimator(float value) {
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(value);
        mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                mAnimationRatio = (float) value.getAnimatedValue();
                //球弹到位的时候，并且点击的位置变了，即点击的时候显示当前选择位置
                if (mAnimationRatio == 1f && mPreSelect != mNewSelect) {
                    if (mNewSelect >= 0 && mNewSelect < mLetters.size()) {
                        mSelect = mNewSelect;
                        if (mListener != null) {
                            mListener.onLetterChange(mLetters.get(mNewSelect));
                        }
                    }
                }
                invalidate();
            }
        });
        mRatioAnimator.start();
    }

    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setMLetters(List<String> letters) {
        mLetters = letters;
        invalidate();
    }

    public interface OnLetterChangeListener {

        void onLetterChange(String letter);
    }
}
