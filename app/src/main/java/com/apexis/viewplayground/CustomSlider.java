//package com.apexis.viewplayground;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.graphics.Typeface;
//import android.os.Build;
//import android.util.AttributeSet;
//import android.view.View;
//
///**
// * Created by rakatan on 23.04.2016.
// */
//public class CustomSlider extends View {
//    private int maxValue;
//    private int currentValue = 0;
//
//    private int barHeight = 1;
//    private int circleRadius = 4;
//    private int spaceAfterBar = 10;
//    private int circleTextSize = 4;
//    private int maxValueTextSize = 10;
//    private int labelTextSize = 4;
//    private int labelTextColor = Color.BLACK;
//    private int currentValueTextColor = Color.BLACK;
//    private int circleTextColor = Color.WHITE;
//    private int baseColor = Color.BLACK;
//    private int fillColor = Color.RED;
//    private String labelText = "Label Text";
//
//    Paint labelPaint;
//    Paint maxValuePaint;
//    Paint barBasePaint;
//    Paint barFillPaint;
//    Paint circlePaint;
//    Paint currentValuePaint;
//
//    public CustomSlider(Context context) {
//        super(context);
//        init(context, null);
//    }
//
//    // This is the tutorial one
//    public CustomSlider(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        if (!isInEditMode())
//            init(context, attrs);
//    }
//
//    public CustomSlider(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        if (!isInEditMode())
//            init(context, attrs);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public CustomSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        if (!isInEditMode())
//            init(context, attrs);
//    }
//
//    private void init(Context context, AttributeSet attrs) {
//        // TODO Implement this
//        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomSlider, 0, 0);
//
//        // Get info from the XML
//        currentValue = ta.getDimensionPixelSize(R.styleable.CustomSlider_currentValue, 5);
//        barHeight = ta.getDimensionPixelSize(R.styleable.CustomSlider_barHeight, 0);
//        circleRadius = ta.getDimensionPixelSize(R.styleable.CustomSlider_circleRadius, 0);
//        spaceAfterBar = ta.getDimensionPixelSize(R.styleable.CustomSlider_spaceAfterBar, 0);
//        circleTextSize = ta.getDimensionPixelSize(R.styleable.CustomSlider_circleTextSize, 0);
//        maxValueTextSize = ta.getDimensionPixelSize(R.styleable.CustomSlider_maxValueTextSize, 0);
//        labelTextSize = ta.getDimensionPixelSize(R.styleable.CustomSlider_labelTextSize, 0);
//        labelTextColor = ta.getColor(R.styleable.CustomSlider_labelTextColor, Color.BLACK);
//        currentValueTextColor = ta.getColor(R.styleable.CustomSlider_maxValueTextColor, Color.BLACK);
//        circleTextColor = ta.getColor(R.styleable.CustomSlider_circleTextColor, Color.BLACK);
//        baseColor = ta.getColor(R.styleable.CustomSlider_baseColor, Color.BLACK);
//        fillColor = ta.getColor(R.styleable.CustomSlider_fillColor, Color.BLACK);
//        labelText = ta.getString(R.styleable.CustomSlider_labelText);
//
//        ta.recycle();
//
//        // Setup the things that we use to draw with
//        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        labelPaint.setTextSize(labelTextSize);
//        labelPaint.setColor(labelTextColor);
//        labelPaint.setTextAlign(Paint.Align.LEFT);
//        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//
//        maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        maxValuePaint.setTextSize(maxValueTextSize);
//        maxValuePaint.setColor(currentValueTextColor);
//        maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        maxValuePaint.setTextAlign(Paint.Align.RIGHT);
//
//        barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        barBasePaint.setColor(baseColor);
//
//        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        barFillPaint.setColor(fillColor);
//
//        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        circlePaint.setColor(fillColor);
//
//        currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        currentValuePaint.setTextSize(circleTextSize);
//        currentValuePaint.setColor(circleTextColor);
//        currentValuePaint.setTextAlign(Paint.Align.CENTER);
//    }
//
//    public void setMaxValue(int maxValue) {
//        this.maxValue = maxValue;
//        invalidate();
//        requestLayout();
//    }
//
//    public void setValue(int newValue) {
//        if (newValue > maxValue)
//            currentValue = maxValue;
//        else if (newValue < 0)
//            currentValue = 0;
//        else currentValue = newValue;
//
//        invalidate();
//    }
//
//    private int measureHeight(int measureSpec) {
//        int size = getPaddingTop() + getPaddingBottom();
//        size += labelPaint.getFontSpacing();
//        float maxValueTextSpacing = maxValuePaint.getFontSpacing();
//        size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));
//
//        return resolveSizeAndState(size, measureSpec, 0);
//    }
//
//    private int measureWidth(int measureSpec) {
//        int size = getPaddingLeft() + getPaddingRight();
//        Rect bounds = new Rect();
//        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
//        size += bounds.width();
//
//        bounds = new Rect();
//        String maxValueText = String.valueOf(maxValue);
//        maxValuePaint.getTextBounds(maxValueText, 0, maxValueText.length(), bounds);
//        size += bounds.width();
//
//        return resolveSizeAndState(size, measureSpec, 0);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
//    }
//
//    private void drawLabel(Canvas canvas) {
//        float x = getPaddingLeft();
//        Rect bounds = new Rect();
//        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
//        float y = getPaddingTop() + bounds.height();
//
//        canvas.drawText(labelText, x, y, labelPaint);
//    }
//
//    private float getBarCenter() {
//        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
//        barCenter += getPaddingTop() + 0.1f * getHeight();
//
//        return barCenter;
//    }
//
//    private void drawMaxValue(Canvas canvas) {
//        String maxValue = String.valueOf(this.maxValue);
//        Rect maxValueRect = new Rect();
//        maxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), maxValueRect);
//
//        float xPos = getWidth() - getPaddingRight();
//        float yPos = getBarCenter() + maxValueRect.height() / 2;
//
//        canvas.drawText(maxValue, xPos, yPos, maxValuePaint);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        drawLabel(canvas);
//        drawBar(canvas);
//        drawMaxValue(canvas);
//    }
//
//    private void drawBar(Canvas canvas) {
//        String maxValueString = String.valueOf(maxValue);
//        Rect maxValueRect = new Rect();
//        maxValuePaint.getTextBounds(maxValueString, 0, maxValueString.length(), maxValueRect);
//        // Trebuie 2x aici cred;
//        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius - maxValueRect.width() - spaceAfterBar;
//
//        float barCenter = getBarCenter();
//
//        float halfBarHeight = barHeight / 2;
//        float top = barCenter - halfBarHeight;
//        float bottom = barCenter + halfBarHeight;
//        float left = getPaddingLeft();
//        float right = getPaddingLeft() + barLength;
//        RectF rect = new RectF(left, top, right, bottom);
//        canvas.drawRoundRect(rect, halfBarHeight, halfBarHeight, barBasePaint);
//
//        float percentFilled = (float) currentValue / (float) maxValue;
//        float fillLength = barLength * percentFilled;
//        float fillPosition = left + fillLength;
//        RectF fillRect = new RectF(left, top, fillPosition, bottom);
//        canvas.drawRoundRect(fillRect, halfBarHeight, halfBarHeight, barFillPaint);
//
//        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);
//
//        Rect bounds = new Rect();
//        String valueStirng = String.valueOf(Math.round(currentValue));
//        currentValuePaint.getTextBounds(maxValueString, 0, maxValueString.length(), bounds);
//        float y = barCenter + bounds.height() / 2;
//        canvas.drawText(maxValueString, fillPosition, y, currentValuePaint);
//    }
//}
