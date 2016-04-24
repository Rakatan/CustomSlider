package com.apexis.viewplayground;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rakatan on 23.04.2016.
 */
public class ScoringSlider extends View {

    private int currentValue;
    private int minValue;
    private int maxValue;

    private String labelLeft;
    private int labelLeftColor;
    private int textSize;

    private int barColor;
    private int barHeight;

    private int barFillColor;
    private int barFillHeight;

    private int thumbRadius;
    private int thumbColor;

    private int thumbTextColor;

    private int intervalLeftColor;

    private int intervalRightColor;

    private float circlePositionX;

    Paint leftLabelPaint;
    Paint barPaint;
    Paint barFillPaint;
    Paint thumbPaint;
    Paint thumbTextPaint;
    Paint intervalLeftPaint;
    Paint intervalRightPaint;

    public ScoringSlider(Context context) {
        super(context);
    }

    public ScoringSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScoringSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScoringSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled()){
            return false;
        }
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch(actionMasked){
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                if(downX >= getBarStart() && downX <= getBarEnd()){
                    float distFromStart = downX - getBarStart();
                    setCurrentValue((int)(distFromStart / getBarUnitWidth()));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                if(moveX >= getBarStart() && moveX <= getBarEnd()){
                    circlePositionX = moveX;
                    updateCurrentValue();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                if(upX <= getBarStart()){
                    setCurrentValue(minValue);
                }
                else if(upX >= getBarEnd()){
                    setCurrentValue(maxValue);
                }else {
                    float distFromStart = upX - getBarStart();
                    setCurrentValue((int) (distFromStart / getBarUnitWidth()));
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLeftLabel(canvas);
        drawBar(canvas);
    }

    private void drawBar(Canvas canvas) {
        // How much was used up by the label
        int usedWidth = getPaddingLeft() + getPaddingRight();
        Rect textMeasure = new Rect();
        leftLabelPaint.getTextBounds(labelLeft, 0, labelLeft.length(), textMeasure);
        usedWidth += textMeasure.width();
        // Width we have available to draw the bar and two text fields
        float availableSpace = getBarWidth();
        float spaceUnit = getBarUnitWidth();
        float leftLimit = getBarStart();

        // Draw the first bar component
        float barTop = getViewCenter() - barHeight;
        float barBottom = getViewCenter() + barHeight;
        float firstBarRight = leftLimit + spaceUnit;

        RectF firstBarComponent = new RectF(leftLimit, barTop, firstBarRight, barBottom);
        canvas.drawRect(firstBarComponent, barPaint);

        // Draw the second bar component
        float secondBarLeft = leftLimit + 2 * spaceUnit;
        float secondBarRight = secondBarLeft + 8 * spaceUnit;

        RectF secondBarComponent = new RectF(secondBarLeft, barTop, secondBarRight, barBottom);
        canvas.drawRect(secondBarComponent, barPaint);

        // Draw the left interval number
        float textX = leftLimit + spaceUnit + spaceUnit / 2;
        textMeasure = new Rect();
        String intervalLeft = String.valueOf(minValue + 1);
        intervalLeftPaint.getTextBounds(intervalLeft, 0, intervalLeft.length(), textMeasure);
        float textY = getViewCenter() + textMeasure.height() / 2;

        canvas.drawText(intervalLeft, textX, textY, intervalLeftPaint);

        // Draw the right interval number
        textX = leftLimit + 10 * spaceUnit + spaceUnit / 2;
        textMeasure = new Rect();
        String intervalRight = String.valueOf(maxValue);
        intervalRightPaint.getTextBounds(intervalRight, 0, intervalRight.length(), textMeasure);
        textY = getViewCenter() + textMeasure.height() / 2 ;

        canvas.drawText(intervalRight, textX, textY, intervalRightPaint);

        // Draw circle
        canvas.drawCircle(circlePositionX, getViewCenter(), thumbRadius, thumbPaint);

        // Draw circle text
        textMeasure = new Rect();
        String currentValueString = String.valueOf(currentValue);
        thumbTextPaint.getTextBounds(currentValueString, 0, currentValueString.length(), textMeasure);
        textY = getViewCenter() + textMeasure.height() / 2;

        canvas.drawText(currentValueString, circlePositionX, textY, thumbTextPaint);
    }

    private void drawLeftLabel(Canvas canvas) {
        float textX = getPaddingLeft();
        Rect textMeasure = new Rect();
        leftLabelPaint.getTextBounds(labelLeft, 0, labelLeft.length(), textMeasure);
        float textY = getViewCenter() + textMeasure.height() / 2;
//        RectF testRect = new RectF(0, 0, textX+100, getHeight());
//        canvas.drawRect(testRect, leftLabelPaint);

        canvas.drawText(labelLeft, textX, textY, leftLabelPaint);
    }

    private float getBarStart(){
        // Get label width
        Rect textMeasure = new Rect();
        leftLabelPaint.getTextBounds(labelLeft, 0, labelLeft.length(), textMeasure);

        // Get where the bar should start
        return getPaddingLeft() + textMeasure.width() + textMeasure.width() * 0.5f;
    }

    private float getBarEnd(){
       return getBarStart() + getBarWidth();
    }

    private float getBarWidth(){
        int usedWidth = getPaddingLeft() + getPaddingRight();
        Rect textMeasure = new Rect();
        leftLabelPaint.getTextBounds(labelLeft, 0, labelLeft.length(), textMeasure);
        usedWidth += textMeasure.width();
        // Width we have available to draw the bar and two text fields
       return getWidth() - usedWidth;
    }

    private float getBarUnitWidth(){
        return getBarWidth() / (maxValue - minValue + 1);
    }
    private float getViewCenter() {
        return (getHeight() - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        updateCircleX();
    }

    private int measureHeight(int measureSpec) {
        int size = getPaddingTop() + getPaddingBottom();
        // Get left label textsize
        float maxTextHeight = Math.max(leftLabelPaint.getFontSpacing(),
                Math.max(intervalLeftPaint.getFontSpacing(),
                        Math.max(intervalRightPaint.getFontSpacing(),
                                thumbTextPaint.getFontSpacing()
                        )
                )
        );

        size += Math.max(barHeight,
                Math.max(thumbRadius * 2,
                        Math.max(barFillHeight,
                                maxTextHeight)
                )
        );

        return resolveSizeAndState(size, measureSpec, 0);
    }

    private int measureWidth(int measureSpec) {
        int size = getPaddingLeft() + getPaddingRight();
        Rect textMeasure = new Rect();

        leftLabelPaint.getTextBounds(labelLeft, 0, labelLeft.length(), textMeasure);
        size += textMeasure.width();

        textMeasure = new Rect();
        String minValueString = String.valueOf(minValue);
        intervalLeftPaint.getTextBounds(minValueString, 0, minValueString.length(), textMeasure);
        size += textMeasure.width();

        textMeasure = new Rect();
        String maxValueString = String.valueOf(maxValue);
        intervalRightPaint.getTextBounds(maxValueString, 0, maxValueString.length(), textMeasure);
        size += textMeasure.width();

        return resolveSizeAndState(size, measureSpec, 0);
    }

    public void setMaxValue(int newMaxValue) {
        maxValue = newMaxValue;
        invalidate();
        requestLayout();
    }

    public void setMinValue(int newMinValue) {
        minValue = newMinValue;
        invalidate();
        requestLayout();
    }

    public void setCurrentValue(int currentValue) {
        if (currentValue > maxValue)
            this.currentValue = maxValue;
        else if (currentValue < minValue)
            this.currentValue = minValue;
        else
            this.currentValue = currentValue;

        updateCircleX();

        invalidate();
    }

    public void updateCircleX(){
        circlePositionX = getBarStart() + getBarUnitWidth() * this.currentValue + getBarUnitWidth() / 2;
    }
    public void updateCurrentValue(){
        currentValue = (int)((circlePositionX - getBarStart()) / getBarUnitWidth());
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScoringSlider, 0, 0);

        currentValue = ta.getInteger(R.styleable.ScoringSlider_currentValue, 0);
        minValue = ta.getInteger(R.styleable.ScoringSlider_minValue, 0);
        maxValue = ta.getInteger(R.styleable.ScoringSlider_maxValue, 0);

        labelLeft = ta.getString(R.styleable.ScoringSlider_labelLeft);
        labelLeftColor = ta.getColor(R.styleable.ScoringSlider_labelLeftColor, Color.BLACK);
        textSize = ta.getDimensionPixelSize(R.styleable.ScoringSlider_textSize, 0);

        barColor = ta.getColor(R.styleable.ScoringSlider_barColor, Color.BLACK);
        barHeight = ta.getDimensionPixelSize(R.styleable.ScoringSlider_barHeight, 0);

        barFillColor = ta.getColor(R.styleable.ScoringSlider_barFillColor, Color.BLACK);
        barFillHeight = ta.getDimensionPixelSize(R.styleable.ScoringSlider_barFillHeight, 0);

//        thumbRadius = ta.getDimensionPixelSize(R.styleable.ScoringSlider_thumbRadius, 0);
        thumbRadius = textSize *3 / 4;
        thumbColor = ta.getColor(R.styleable.ScoringSlider_thumbColor, Color.BLACK);

        thumbTextColor = ta.getColor(R.styleable.ScoringSlider_thumbTextColor, Color.BLACK);

        intervalLeftColor = ta.getColor(R.styleable.ScoringSlider_intervalLeftColor, Color.BLACK);

        intervalRightColor = ta.getColor(R.styleable.ScoringSlider_intervalRightColor, Color.BLACK);

        ta.recycle();

        // Setup the paints
        leftLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        leftLabelPaint.setTextSize(textSize);
        leftLabelPaint.setColor(labelLeftColor);
        leftLabelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        leftLabelPaint.setTextAlign(Paint.Align.LEFT);

        intervalLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        intervalLeftPaint.setTextSize(textSize);
        intervalLeftPaint.setColor(intervalLeftColor);
        intervalLeftPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        intervalLeftPaint.setTextAlign(Paint.Align.CENTER);

        intervalRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        intervalRightPaint.setTextSize(textSize);
        intervalRightPaint.setColor(intervalRightColor);
        intervalRightPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        intervalRightPaint.setTextAlign(Paint.Align.CENTER);

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setColor(barColor);

        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barFillPaint.setColor(barFillColor);

        thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbPaint.setColor(thumbColor);

        thumbTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        thumbTextPaint.setTextSize(textSize);
        thumbTextPaint.setColor(thumbTextColor);
        thumbTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        thumbTextPaint.setTextAlign(Paint.Align.CENTER);
    }

}
