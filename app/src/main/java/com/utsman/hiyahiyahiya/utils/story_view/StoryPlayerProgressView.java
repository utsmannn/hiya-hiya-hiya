package com.utsman.hiyahiyahiya.utils.story_view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.utsman.hiyahiyahiya.R;

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
public class StoryPlayerProgressView extends View {
    public static final int PROGRESS_BAR_HEIGHT = 2;
    public static final int GAP_BETWEEN_PROGRESS_BARS = 2;
    public static final int SINGLE_STORY_DISPLAY_TIME = 1000;
    public static final String PROGRESS_PRIMARY_COLOR = "#009988";
    public static final String PROGRESS_SECONDARY_COLOR = "#EEEEEE";
    private int mScreenWidth;
    private int mProgressHeight;
    private int mGapBetweenProgressBars;
    private Resources resources;
    private Paint mProgressPaint;
    private int singleProgressBarWidth;
    private int progressBarsCount;
    private int[] progressBarRightEdge;
    private RectF progressBarRectF;
    private int top;
    private int bottom;
    private int progressBarPrimaryColor;
    private int progressBarSecondaryColor;
    private int currentProgressIndex;
    private long singleStoryDisplayTime;
    private ValueAnimator progressAnimator;
    private boolean isCancelled;

    public StoryPlayerProgressView(Context context) {
        super(context);
        setDefaults();
        init(context);
        prepareValues();
    }

    public StoryPlayerProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StoryPlayerProgressView, 0, 0);
        try {
            mProgressHeight = getPxFromDp((int) ta.getDimension(R.styleable.StoryPlayerProgressView_progressBarHeight, PROGRESS_BAR_HEIGHT));
            mGapBetweenProgressBars = getPxFromDp((int) ta.getDimension(R.styleable.StoryPlayerProgressView_gapBetweenProgressBar, GAP_BETWEEN_PROGRESS_BARS));
            progressBarPrimaryColor = ta.getColor(R.styleable.StoryPlayerProgressView_progressBarPrimaryColor, Color.parseColor(PROGRESS_PRIMARY_COLOR));
            progressBarSecondaryColor = ta.getColor(R.styleable.StoryPlayerProgressView_progressBarSecondaryColor, Color.parseColor(PROGRESS_SECONDARY_COLOR));
            singleStoryDisplayTime = ta.getInt(R.styleable.StoryPlayerProgressView_singleStoryDisplayTime, SINGLE_STORY_DISPLAY_TIME);
        } finally {
            ta.recycle();
        }
        prepareValues();
    }

    private void init(Context context) {
        resources = context.getResources();
        mScreenWidth = resources.getDisplayMetrics().widthPixels;
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
    }

    private void setDefaults() {
        mProgressHeight = getPxFromDp(PROGRESS_BAR_HEIGHT);
        mGapBetweenProgressBars = getPxFromDp(GAP_BETWEEN_PROGRESS_BARS);
        progressBarPrimaryColor = Color.parseColor(PROGRESS_PRIMARY_COLOR);
        progressBarSecondaryColor = Color.parseColor(PROGRESS_SECONDARY_COLOR);
        singleStoryDisplayTime = SINGLE_STORY_DISPLAY_TIME;
    }

    private void prepareValues() {
        top = mGapBetweenProgressBars;
        bottom = mGapBetweenProgressBars + mProgressHeight;
        mProgressPaint.setColor(progressBarSecondaryColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressBarRectF = new RectF(0, top, 0, bottom);
    }

    public void setProgressBarsCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count cannot be less than 1");
        }
        this.progressBarsCount = count;
        progressBarRightEdge = new int[progressBarsCount];
        calculateWidthOfEachProgressBar(progressBarsCount);
        invalidate();
        startAnimating(0);
    }

    public void setProgressBarHeight(int dpValue) {
        mProgressHeight = getPxFromDp(dpValue);
        invalidate();
    }

    public void setGapBetweenProgressBars(int dpValue) {
        mGapBetweenProgressBars = getPxFromDp(dpValue);
        invalidate();
    }

    public void setProgressPrimaryColor(int color) {
        progressBarPrimaryColor = color;
        invalidate();
    }

    public void setProgressSecondaryColor(int color) {
        progressBarSecondaryColor = color;
        invalidate();
    }

    public void setSingleStoryDisplayTime(int time) {
        singleStoryDisplayTime = time;
        invalidate();
    }

    public void pauseProgress() {
        if (progressAnimator != null) {
            if (progressAnimator.isRunning()) {
                progressAnimator.pause();
            }
        }
    }

    public void resumeProgress() {
        if (progressAnimator != null) {
            if (progressAnimator.isPaused()) {
                progressAnimator.resume();
            }
        }
    }

    public void cancelAnimation() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
            isCancelled = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < progressBarsCount; i++) {
            int left = (mGapBetweenProgressBars + singleProgressBarWidth) * i + mGapBetweenProgressBars;
            int right = (i + 1) * (mGapBetweenProgressBars + singleProgressBarWidth);
            mProgressPaint.setColor(progressBarSecondaryColor);
            progressBarRectF.set(left, top, right, bottom);
            canvas.drawRoundRect(progressBarRectF, mProgressHeight, mProgressHeight, mProgressPaint);
            right = progressBarRightEdge[i];
            if (right > 0) {
                mProgressPaint.setColor(progressBarPrimaryColor);
                progressBarRectF.set(left, top, right, bottom);
                canvas.drawRoundRect(progressBarRectF, mProgressHeight, mProgressHeight, mProgressPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mScreenWidth - getPaddingStart() + getPaddingEnd();
        int height = getPaddingTop() + getPaddingBottom() + (2 * mGapBetweenProgressBars) + mProgressHeight;
        int w = resolveSizeAndState(width, widthMeasureSpec, 0);
        int h = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    private void startAnimating(final int index) {
        if (index >= progressBarsCount) {
            if (storyPlayerListener != null) {
                storyPlayerListener.onFinishedPlaying();
                return;
            }
        }
        progressAnimator = ValueAnimator.ofInt(0, singleProgressBarWidth);
        progressAnimator.setDuration(singleStoryDisplayTime);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                progressBarRightEdge[index] = (index + 1) * mGapBetweenProgressBars + index * singleProgressBarWidth + value;
                invalidate();
            }
        });
        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isCancelled) startAnimating(index + 1);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isCancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        progressAnimator.start();
        if (storyPlayerListener != null) {
            storyPlayerListener.onStartedPlaying(index);
        }
    }

    private int getPxFromDp(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }

    private void calculateWidthOfEachProgressBar(int progressBarsCount) {
        this.singleProgressBarWidth = (mScreenWidth - (progressBarsCount + 1) * mGapBetweenProgressBars) / progressBarsCount;
    }

    private StoryPlayerListener storyPlayerListener;

    public void setStoryPlayerListener(StoryPlayerListener storyPlayerListener) {
        this.storyPlayerListener = storyPlayerListener;
    }

    public interface StoryPlayerListener {
        void onStartedPlaying(int index);

        void onFinishedPlaying();
    }
}