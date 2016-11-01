/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Kurt Mbanje
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mbanje.kurt.fabbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.DefaultBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import java.util.List;


/**
 * Created by kurt on 21 02 2015 .
 */
@DefaultBehavior(FabButton.Behavior.class)
public class FabButton extends FrameLayout implements CircleImageView.OnFabViewListener {

    private CircleImageView circle;
    private ProgressRingView ring;
    private float ringWidthRatio = 0.14f; //of a possible 1f;
    private boolean indeterminate;
    private boolean autostartanim;
    private int endBitmapResource;
    private boolean showEndBitmap;
    private boolean hideProgressOnComplete;

    public FabButton(Context context) {
        super(context);
        init(context,null, 0);
    }

    public FabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public FabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    protected void init(Context context,AttributeSet attrs, int defStyle) {
        View v = View.inflate(context, R.layout.widget_fab_button,this);
        setClipChildren(false);
        circle = (CircleImageView) v.findViewById(R.id.fabbutton_circle);
        ring = (ProgressRingView)v.findViewById(R.id.fabbutton_ring);
        circle.setFabViewListener(this);
        ring.setFabViewListener(this);
        int color = Color.BLACK;
        int progressColor = Color.BLACK;
        int animDuration = 4000;
        int icon = -1;
        float maxProgress = 0;
        float progress =0;
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            color = a.getColor(R.styleable.CircleImageView_android_color, Color.BLACK);
            progressColor = a.getColor(R.styleable.CircleImageView_fbb_progressColor, Color.BLACK);
            progress = a.getFloat(R.styleable.CircleImageView_android_progress, 0f);
            maxProgress = a.getFloat(R.styleable.CircleImageView_android_max, 100f);
            indeterminate = a.getBoolean(R.styleable.CircleImageView_android_indeterminate, false);
            autostartanim = a.getBoolean(R.styleable.CircleImageView_fbb_autoStart, true);
            animDuration = a.getInteger(R.styleable.CircleImageView_android_indeterminateDuration, animDuration);
            icon = a.getResourceId(R.styleable.CircleImageView_android_src, icon);
            ringWidthRatio = a.getFloat(R.styleable.CircleImageView_fbb_progressWidthRatio, ringWidthRatio);
            endBitmapResource = a.getResourceId(R.styleable.CircleImageView_fbb_endBitmap, R.drawable.ic_fab_complete);
            showEndBitmap = a.getBoolean(R.styleable.CircleImageView_fbb_showEndBitmap, false);
            hideProgressOnComplete = a.getBoolean(R.styleable.CircleImageView_fbb_hideProgressOnComplete, false);
            circle.setShowShadow(a.getBoolean(R.styleable.CircleImageView_fbb_showShadow, true));
            a.recycle();
        }

        circle.setColor(color);
        circle.setShowEndBitmap(showEndBitmap);
        circle.setRingWidthRatio(ringWidthRatio);
        ring.setProgressColor(progressColor);
        ring.setProgress(progress);
        ring.setMaxProgress(maxProgress);
        ring.setAutostartanim(autostartanim);
        ring.setAnimDuration(animDuration);
        ring.setRingWidthRatio(ringWidthRatio);
        ring.setIndeterminate(indeterminate);
        if(icon != -1){
            circle.setIcon(icon,endBitmapResource);
        }
    }

    public void setShadow(boolean showShadow) {
        circle.setShowShadow(showShadow);
    }

    public void setIcon(int resource,int endIconResource){
        circle.setIcon(resource,endIconResource);
    }

    public void showShadow(boolean show){
        circle.setShowShadow(show);
        invalidate();
    }

    public void setColor(int color) {
        circle.setColor(color);
    }

    public void setIcon(Drawable icon, Drawable endIcon) {
        circle.setIcon(icon, endIcon);
    }

    public void resetIcon(){
        circle.resetIcon();
    }
    /**
     * sets the progress to indeterminate or not
     * @param indeterminate the flag
     */
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        ring.setIndeterminate(indeterminate);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(listener);
        ring.setOnClickListener(listener);
        circle.setOnClickListener(listener);
    }

    /**
     * shows the animation ring
     * @param show shows animation ring when set to true
     */
    public void showProgress(boolean show){
        circle.showRing(show);
    }

    public void hideProgressOnComplete(boolean hide) {
        hideProgressOnComplete = hide;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        circle.setEnabled(enabled);
        ring.setEnabled(enabled);
    }

    /**
     * sets current progress
     * @param progress the current progress to set value too
     */
    public void setProgress(float progress){
        ring.setProgress(progress);
    }

    @Override
    public void onProgressVisibilityChanged(boolean visible) {
        if(visible){
            ring.setVisibility(View.VISIBLE);
            ring.startAnimation();
        }else{
            ring.stopAnimation(true);
            ring.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProgressCompleted() {
        circle.showCompleted(showEndBitmap, hideProgressOnComplete);
        if (hideProgressOnComplete) {
            ring.setVisibility(View.GONE);
        }
    }




    public static class Behavior extends CoordinatorLayout.Behavior<FabButton> {
        // We only support the FAB <> Snackbar shift movement on Honeycomb and above. This is
        // because we can use view translation properties which greatly simplifies the code.
        private static final boolean SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;

        private Rect mTmpRect;
        private boolean mIsAnimatingOut;
        private float mTranslationY;

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, FabButton child,
                                       View dependency) {
            // We're dependent on all SnackbarLayouts (if enabled)
            return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, FabButton child, View dependency) {
            if (dependency instanceof Snackbar.SnackbarLayout) {
                updateFabTranslationForSnackbar(parent, child, dependency);
            } else if (dependency instanceof AppBarLayout) {
                final AppBarLayout appBarLayout = (AppBarLayout) dependency;
                if (mTmpRect == null) {
                    mTmpRect = new Rect();
                }

                // First, let's get the visible rect of the dependency
                final Rect rect = mTmpRect;
                ViewGroupUtils.getDescendantRect(parent, dependency, rect);

                if (rect.bottom <= getMinimumHeightForVisibleOverlappingContent(appBarLayout)) {
                    // If the anchor's bottom is below the seam, we'll animate our FAB out
                    if (!mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
                        animateOut(child);
                    }
                } else {
                    // Else, we'll animate our FAB back in
                    if (child.getVisibility() != View.VISIBLE) {
                        animateIn(child);
                    }
                }
            }


            return false;
        }

        final int getMinimumHeightForVisibleOverlappingContent(AppBarLayout bar) {
            int topInset = 0;
            int minHeight = ViewCompat.getMinimumHeight(bar);
            if(minHeight != 0) {
                return minHeight * 2 + topInset;
            } else {
                int childCount = bar.getChildCount();
                return childCount >= 1?ViewCompat.getMinimumHeight(bar.getChildAt(childCount - 1)) * 2 + topInset:0;
            }
        }


        private void updateFabTranslationForSnackbar(CoordinatorLayout parent,
                                                     FabButton fab, View snackbar) {
            final float translationY = getFabTranslationYForSnackbar(parent, fab);
            if (translationY != mTranslationY) {
                // First, cancel any current animation
                ViewCompat.animate(fab).cancel();

                if (Math.abs(translationY - mTranslationY) == snackbar.getHeight()) {
                    // If we're travelling by the height of the Snackbar then we probably need to
                    // animate to the value
                    ViewCompat.animate(fab)
                            .translationY(translationY)
                            .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                            .setListener(null);
                } else {
                    // Else we'll set use setTranslationY
                    ViewCompat.setTranslationY(fab, translationY);
                }
                mTranslationY = translationY;
            }
        }

        private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                    FabButton fab) {
            float minOffset = 0;
            final List<View> dependencies = parent.getDependencies(fab);
            for (int i = 0, z = dependencies.size(); i < z; i++) {
                final View view = dependencies.get(i);
                if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                    minOffset = Math.min(minOffset,
                            ViewCompat.getTranslationY(view) - view.getHeight());
                }
            }

            return minOffset;
        }

        private void animateIn(FabButton button) {
            button.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= 14) {
                ViewCompat.animate(button)
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                        .withLayer()
                        .setListener(null)
                        .start();
            } else {
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(
                        button.getContext(), R.anim.design_fab_in);
                anim.setDuration(200);
                anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                button.startAnimation(anim);
            }
        }

        private void animateOut(final FabButton button) {
            if (Build.VERSION.SDK_INT >= 14) {
                ViewCompat.animate(button)
                        .scaleX(0f)
                        .scaleY(0f)
                        .alpha(0f)
                        .setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                        .withLayer()
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {
                                mIsAnimatingOut = true;
                            }

                            @Override
                            public void onAnimationCancel(View view) {
                                mIsAnimatingOut = false;
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                mIsAnimatingOut = false;
                                view.setVisibility(View.GONE);
                            }
                        }).start();
            } else {
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(
                        button.getContext(), R.anim.design_fab_out);
                anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                anim.setDuration(200);
                anim.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mIsAnimatingOut = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mIsAnimatingOut = false;
                        button.setVisibility(View.GONE);
                    }
                });
                button.startAnimation(anim);
            }
        }
    }
}
