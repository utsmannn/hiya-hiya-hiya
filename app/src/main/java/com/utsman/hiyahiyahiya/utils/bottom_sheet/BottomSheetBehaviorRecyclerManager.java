package com.utsman.hiyahiyahiya.utils.bottom_sheet;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedor on 21.03.2017.
 */

public class  BottomSheetBehaviorRecyclerManager <T extends View>{

    private List<View> mViews;
    private View.OnTouchListener mTouchEventListener;

    private CoordinatorLayout mParent;
    private ICustomBottomSheetBehavior<T> mBehavior;
    private T mBottomSheetView;

    public BottomSheetBehaviorRecyclerManager(CoordinatorLayout mParent, ICustomBottomSheetBehavior<T> mBehavior, T mBottomSheetView) {
        mViews = new ArrayList<>();
        this.mParent = mParent;
        this.mBehavior = mBehavior;
        this.mBottomSheetView = mBottomSheetView;
        initTouchCallback();
    }


    public void addControl(View recyclerView) {
        if (mViews == null) {
            mViews = new ArrayList<>();
        }
        mViews.add(recyclerView);
        mBehavior.setNestedScrollingChildRefList(mViews);
    }

    public void create() {
        if (mViews == null) {
            return;
        }
        if (mParent == null) {
            return;
        }
        if (mBehavior == null) {
            return;
        }
        if (mBottomSheetView == null) {
            return;
        }
        for (int i = 0; i < mViews.size(); i++) {
            mViews.get(i).setOnTouchListener(mTouchEventListener);
        }
    }


    private void initTouchCallback() {
        mTouchEventListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onTouchScroll(view, motionEvent);
                return false;
            }
        };
    }

    public void onTouchScroll(View view, MotionEvent motionEvent) {
        mBehavior.onLayoutChild(mParent, mBottomSheetView, ViewCompat.LAYOUT_DIRECTION_LTR);
    }
}