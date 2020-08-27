package com.utsman.hiyahiyahiya.utils.story_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.utsman.hiyahiyahiya.R;
import com.utsman.hiyahiyahiya.utils.CropCircleTransformation;

import java.util.ArrayList;

/**
 * MIT License
 *
 * Copyright (c) 2018 bxute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * */
public class StoryView extends View {
    public static final int STORY_IMAGE_RADIUS_IN_DP = 36;
    public static final int STORY_INDICATOR_WIDTH_IN_DP = 4;
    public static final int SPACE_BETWEEN_IMAGE_AND_INDICATOR = 4;
    public static final int START_ANGLE = 270;
    public static final String PENDING_INDICATOR_COLOR = "#009988";
    public static final String VISITED_INDICATOR_COLOR = "#33009988";
    public static int ANGEL_OF_GAP = 15;
    StoryPreference storyPreference;
    private int mStoryImageRadiusInPx;
    private int mStoryIndicatorWidthInPx;
    private int mSpaceBetweenImageAndIndicator;
    private int mPendingIndicatorColor;
    private int mVisistedIndicatorColor;
    private int mViewWidth;
    private int mViewHeight;
    private int mIndicatoryOffset;
    private Resources resources;
    private ArrayList<StoryModel> storyImageUris;
    private Paint mIndicatorPaint;
    private int indicatorCount;
    private int indicatorSweepAngle;
    private Bitmap mIndicatorImageBitmap;
    private Rect mIndicatorImageRect;

    public StoryView(Context context) {
        super(context);
        init(context);
        setDefaults();
    }

    private void init(Context context) {
        storyPreference = new StoryPreference(context);
        resources = context.getResources();
        storyImageUris = new ArrayList<>();
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void setDefaults() {
        mStoryImageRadiusInPx = getPxFromDp(STORY_IMAGE_RADIUS_IN_DP);
        mStoryIndicatorWidthInPx = getPxFromDp(STORY_INDICATOR_WIDTH_IN_DP);
        mSpaceBetweenImageAndIndicator = getPxFromDp(SPACE_BETWEEN_IMAGE_AND_INDICATOR);
        mPendingIndicatorColor = Color.parseColor(PENDING_INDICATOR_COLOR);
        mVisistedIndicatorColor = Color.parseColor(VISITED_INDICATOR_COLOR);
        prepareValues();
    }

    private int getPxFromDp(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }

    private void prepareValues() {
        mViewHeight = 2 * (mStoryIndicatorWidthInPx + mSpaceBetweenImageAndIndicator + mStoryImageRadiusInPx);
        mViewWidth = mViewHeight;
        mIndicatoryOffset = mStoryIndicatorWidthInPx / 2;
        int mIndicatorImageOffset = mStoryIndicatorWidthInPx + mSpaceBetweenImageAndIndicator;
        mIndicatorImageRect = new Rect(mIndicatorImageOffset, mIndicatorImageOffset, mViewWidth - mIndicatorImageOffset, mViewHeight - mIndicatorImageOffset);
    }

    public StoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StoryView, 0, 0);
        try {
            mStoryImageRadiusInPx = getPxFromDp((int) ta.getDimension(R.styleable.StoryView_storyImageRadius, STORY_IMAGE_RADIUS_IN_DP));
            mStoryIndicatorWidthInPx = getPxFromDp((int) ta.getDimension(R.styleable.StoryView_storyItemIndicatorWidth, STORY_INDICATOR_WIDTH_IN_DP));
            mSpaceBetweenImageAndIndicator = getPxFromDp((int) ta.getDimension(R.styleable.StoryView_spaceBetweenImageAndIndicator, SPACE_BETWEEN_IMAGE_AND_INDICATOR));
            mPendingIndicatorColor = ta.getColor(R.styleable.StoryView_pendingIndicatorColor, Color.parseColor(PENDING_INDICATOR_COLOR));
            mVisistedIndicatorColor = ta.getColor(R.styleable.StoryView_visitedIndicatorColor, Color.parseColor(VISITED_INDICATOR_COLOR));
        }
        finally {
            ta.recycle();
        }
        prepareValues();
    }

    public void resetStoryVisits() {
        storyPreference.clearStoryPreferences();
    }

    public void setImageUris(ArrayList<StoryModel> imageUris) {
        this.storyImageUris = imageUris;
        this.indicatorCount = imageUris.size();
        calculateSweepAngle(indicatorCount);
        invalidate();
        loadFirstImageBitamp();
    }

    private void calculateSweepAngle(int itemCounts) {
        if (itemCounts == 1) {
            ANGEL_OF_GAP = 0;
        }
        this.indicatorSweepAngle = (360 / itemCounts) - ANGEL_OF_GAP / 2;
    }

    private void loadFirstImageBitamp() {
        Picasso.get()
                .load(storyImageUris.get(0).imageUri)
                .transform(new CropCircleTransformation())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mIndicatorImageBitmap = bitmap;
                        invalidate();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.i("HIYA HIYAA HIYA", "onBitmapFailed: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    public void navigateToStoryPlayerPage(Context context) {
        if (context == null) {
            throw new RuntimeException("Activity Context MUST not be null. You need to call StoryView.setActivityContext(activity)");
        } else {
            Intent intent = new Intent(context, StoryPlayerActivity.class);
            intent.putParcelableArrayListExtra(StoryPlayerActivity.STORY_IMAGE_KEY, storyImageUris);
            context.startActivity(intent);
        }

    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mIndicatorPaint.setColor(mPendingIndicatorColor);
        mIndicatorPaint.setStrokeWidth(mStoryIndicatorWidthInPx);
        int startAngle = START_ANGLE + ANGEL_OF_GAP / 2;
        for (int i = 0; i < indicatorCount; i++) {
            mIndicatorPaint.setColor(getIndicatorColor(i));
            canvas.drawArc(mIndicatoryOffset, mIndicatoryOffset, mViewWidth - mIndicatoryOffset, mViewHeight - mIndicatoryOffset, startAngle, indicatorSweepAngle - ANGEL_OF_GAP / 2, false, mIndicatorPaint);
            startAngle += indicatorSweepAngle + ANGEL_OF_GAP / 2;
        }
        if (mIndicatorImageBitmap != null) {
            canvas.drawBitmap(mIndicatorImageBitmap, null, mIndicatorImageRect, null);
        }
    }

    private int getIndicatorColor(int index) {
        return storyPreference.isStoryVisited(storyImageUris.get(index).imageUri) ? mVisistedIndicatorColor : mPendingIndicatorColor;
    }

    @Override
    @SuppressLint("NewApi")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingStart() + getPaddingEnd() + mViewWidth;
        int height = getPaddingTop() + getPaddingBottom() + mViewHeight;
        int w = resolveSizeAndState(width, widthMeasureSpec, 0);
        int h = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }
}