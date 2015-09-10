/**
 * Copyright 2015 Bartosz Lipinski
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.learn.mobile.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class ControllableAppBarLayout extends AppBarLayout implements AppBarLayout.OnOffsetChangedListener {
    private Behavior mBehavior;
    private WeakReference<CoordinatorLayout> mParent;
    private ToolbarChange mQueuedChange = ToolbarChange.NONE;
    private boolean mAfterFirstDraw = false;
    private State state;
    private List<WeakReference<OnStateChangeListener>> mListeners;

    public ControllableAppBarLayout(Context context) {
        super(context);
    }

    public ControllableAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mListeners = new ArrayList<>();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!(getLayoutParams() instanceof CoordinatorLayout.LayoutParams) || !(getParent() instanceof CoordinatorLayout)) {
            throw new IllegalStateException("ControllableAppBarLayout must be a direct child of CoordinatorLayout.");
        } else {
            mParent = new WeakReference<>((CoordinatorLayout) getParent());
        }
        addOnOffsetChangedListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mBehavior == null) {
            mBehavior = (Behavior) ((CoordinatorLayout.LayoutParams) getLayoutParams()).getBehavior();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (r - l > 0 && b - t > 0 && mAfterFirstDraw && mQueuedChange != ToolbarChange.NONE) {
            analyzeQueuedChange();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mAfterFirstDraw) {
            mAfterFirstDraw = true;
            if (mQueuedChange != ToolbarChange.NONE) {
                analyzeQueuedChange();
            }
        }
    }

    private synchronized void analyzeQueuedChange() {
        switch (mQueuedChange) {
            case COLLAPSE:
                performCollapsingWithoutAnimation();
                break;
            case COLLAPSE_WITH_ANIMATION:
                performCollapsingWithAnimation();
                break;
            case EXPAND:
                performExpandingWithoutAnimation();
                break;
            case EXPAND_WITH_ANIMATION:
                performExpandingWithAnimation();
                break;
        }

        mQueuedChange = ToolbarChange.NONE;
    }

    public void collapseToolbar() {
        collapseToolbar(false);
    }

    public void collapseToolbar(boolean withAnimation) {
        mQueuedChange = withAnimation ? ToolbarChange.COLLAPSE_WITH_ANIMATION : ToolbarChange.COLLAPSE;
        requestLayout();
    }

    public void expandToolbar() {
        expandToolbar(false);
    }

    public void expandToolbar(boolean withAnimation) {
        mQueuedChange = withAnimation ? ToolbarChange.EXPAND_WITH_ANIMATION : ToolbarChange.EXPAND;
        requestLayout();
    }

    private void performCollapsingWithoutAnimation() {
        if (mParent.get() != null) {
            mBehavior.onNestedPreScroll(mParent.get(), this, null, 0, getHeight(), new int[]{0, 0});
        }
    }

    private void performCollapsingWithAnimation() {
        if (mParent.get() != null) {
            mBehavior.onNestedFling(mParent.get(), this, null, 0, getHeight(), true);
        }
    }

    private void performExpandingWithoutAnimation() {
        if (mParent.get() != null) {
            mBehavior.setTopAndBottomOffset(0);
        }
    }

    private void performExpandingWithAnimation() {
        if (mParent.get() != null) {
            mBehavior.onNestedFling(mParent.get(), this, null, 0, -getHeight() * 5, false);
        }
    }

    private void dispatchStateChange(ControllableAppBarLayout layout) {
        List listeners = layout.mListeners;
        int i = 0;

        for (int z = listeners.size(); i < z; ++i) {
            WeakReference ref = (WeakReference) listeners.get(i);
            OnStateChangeListener listener = ref != null ? (OnStateChangeListener) ref.get() : null;
            if (listener != null) {
                listener.onAppBarStateChange(layout, state);
            }
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (state != State.EXPANDED) {
                state = State.EXPANDED;
                dispatchStateChange(this);
            }
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.COLLAPSED) {
                state = State.COLLAPSED;
                dispatchStateChange(this);
            }
        } else {
            if (state != State.IDLE) {
                state = State.IDLE;
                dispatchStateChange(this);
            }
        }
    }

    public void addOnStateChangeListener(OnStateChangeListener listener) {
        int i = 0;

        for (int z = this.mListeners.size(); i < z; ++i) {
            WeakReference ref = (WeakReference) this.mListeners.get(i);
            if (ref != null && ref.get() == listener) {
                return;
            }
        }

        this.mListeners.add(new WeakReference<>(listener));
    }

    public void removeOnStateChangeListener(OnStateChangeListener listener) {
        Iterator i = this.mListeners.iterator();

        while (true) {
            OnStateChangeListener item;
            do {
                if (!i.hasNext()) {
                    return;
                }

                WeakReference ref = (WeakReference) i.next();
                item = (OnStateChangeListener) ref.get();
            } while (item != listener && item != null);

            i.remove();
        }
    }

    public State getState() {
        return state;
    }

    private enum ToolbarChange {
        COLLAPSE,
        COLLAPSE_WITH_ANIMATION,
        EXPAND,
        EXPAND_WITH_ANIMATION,
        NONE
    }

    public interface OnStateChangeListener {
        void onAppBarStateChange(ControllableAppBarLayout layout, State state);
    }

    public enum State {
        COLLAPSED,
        EXPANDED,
        IDLE
    }
}