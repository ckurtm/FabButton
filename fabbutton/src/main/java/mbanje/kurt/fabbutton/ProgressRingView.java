/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Rahat Ahmed
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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class ProgressRingView extends View implements FabUtil.OnFabValueCallback{
    String TAG = ProgressRingView.class.getSimpleName();
    private Paint progressPaint;
    private int size = 0;
    private RectF bounds;
    private float boundsPadding = 0.14f;
    private int viewRadius;
    private float ringWidthRatio = 0.14f; //of a possible 1f;
    private boolean indeterminate,autostartanim;
    private float progress, maxProgress, indeterminateSweep, indeterminateRotateOffset;
    private int ringWidth,midRingWidth,animDuration;
    private int progressColor = Color.BLACK;


    // Animation related stuff
    private float startAngle;
    private float actualProgress;
    private ValueAnimator startAngleRotate;
    private ValueAnimator progressAnimator;
    private AnimatorSet indeterminateAnimator;

    private CircleImageView.OnFabViewListener fabViewListener;

    public ProgressRingView(Context context) {
        super(context);
        init(null, 0);
    }

    public ProgressRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ProgressRingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
        progress = a.getFloat(R.styleable.CircleImageView_android_progress, 0f);
        progressColor = a.getColor(R.styleable.CircleImageView_fbb_progressColor,progressColor);
        maxProgress = a.getFloat(R.styleable.CircleImageView_android_max, 100f);
        indeterminate = a.getBoolean(R.styleable.CircleImageView_android_indeterminate, false);
        autostartanim = a.getBoolean(R.styleable.CircleImageView_fbb_autoStart, true);
        animDuration = a.getInteger(R.styleable.CircleImageView_android_indeterminateDuration, 4000);
        ringWidthRatio = a.getFloat(R.styleable.CircleImageView_fbb_progressWidthRatio, ringWidthRatio);
        a.recycle();
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.BUTT);
        if(autostartanim) {
            startAnimation();
        }
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size = Math.min(w,h);
        viewRadius = size / 2;
        setRingWidth(-1,true);
    }

    public void setRingWidthRatio(float ringWidthRatio) {
        this.ringWidthRatio = ringWidthRatio;
    }

    public void setAutostartanim(boolean autostartanim) {
        this.autostartanim = autostartanim;
    }

    public void setFabViewListener(CircleImageView.OnFabViewListener fabViewListener) {
        this.fabViewListener = fabViewListener;
    }

    public void setRingWidth(int width,boolean original){
        if(original){
            ringWidth = Math.round((float) viewRadius * ringWidthRatio);
        }else{
            ringWidth = width;
        }
        midRingWidth = ringWidth/2;
        progressPaint.setStrokeWidth(ringWidth);
        updateBounds();
    }

    private void updateBounds(){
        bounds = new RectF(midRingWidth,midRingWidth, size - midRingWidth, size- midRingWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the arc
        float sweepAngle = isInEditMode() ? (progress/maxProgress*360) : (actualProgress/maxProgress*360);
        if(!indeterminate) {
            canvas.drawArc(bounds, startAngle, sweepAngle, false, progressPaint);
        }else {
            canvas.drawArc(bounds, startAngle + indeterminateRotateOffset, indeterminateSweep, false, progressPaint);
        }
    }


    /**
     * Sets the progress of the progress bar.
     * @param currentProgress the current progress you want to set
     */
    public void setProgress(final float currentProgress) {
        this.progress = currentProgress;
        // Reset the determinate animation to approach the new progress
        if(!indeterminate){
            if(progressAnimator != null && progressAnimator.isRunning()) {
                progressAnimator.cancel();
            }
            progressAnimator = FabUtil.createProgressAnimator(this,actualProgress,currentProgress,this);
            progressAnimator.start();
        }
        invalidate();

    }


    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }


    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public void setAnimDuration(int animDuration) {
        this.animDuration = animDuration;
    }


    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
    }



    /**
     * Starts the progress bar animation.
     * (This is an alias of resetAnimation() so it does the same thing.)
     */
    public void startAnimation() {
        resetAnimation();
    }


    public void stopAnimation(boolean hideProgress){
        if(startAngleRotate != null && startAngleRotate.isRunning()) {
            startAngleRotate.cancel();
        }
        if(progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
        }
        if(indeterminateAnimator != null && indeterminateAnimator.isRunning()) {
            indeterminateAnimator.cancel();
        }
        if(hideProgress){
            setRingWidth(0, false);
        }else{
            setRingWidth(0,true);
        }
        invalidate();
    }
    /**
     * Resets the animation.
     */
    public void resetAnimation() {
        stopAnimation(false);
        // Determinate animation
        if(!indeterminate){
            // The cool 360 swoop animation at the start of the animation
            startAngle = -90f;
            startAngleRotate = FabUtil.createStartAngleAnimator(this,-90f,270f,this);
            startAngleRotate.start();
            // The linear animation shown when progress is updated
            actualProgress = 0f;
            progressAnimator = FabUtil.createProgressAnimator(this, actualProgress, progress, this);
            progressAnimator.start();
        }else  { // Indeterminate animation
            startAngle = -90f;
            indeterminateSweep = FabUtil.INDETERMINANT_MIN_SWEEP;
            // Build the whole AnimatorSet
            indeterminateAnimator = new AnimatorSet();
            AnimatorSet prevSet = null, nextSet;
            for(int k=0;k<FabUtil.ANIMATION_STEPS;k++){
                nextSet = FabUtil.createIndeterminateAnimator(this,k,animDuration,this);
                AnimatorSet.Builder builder = indeterminateAnimator.play(nextSet);
                if(prevSet != null) {
                    builder.after(prevSet);
                }
                prevSet = nextSet;
            }

            // Listen to end of animation so we can infinitely loop
            indeterminateAnimator.addListener(new AnimatorListenerAdapter() {
                boolean wasCancelled = false;
                @Override
                public void onAnimationCancel(Animator animation) {
                    wasCancelled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(!wasCancelled) {
                        resetAnimation();
                    }
                }
            });
            indeterminateAnimator.start();
        }
    }

    @Override
    public void onIndeterminateValuesChanged(float indeterminateSweep, float indeterminateRotateOffset, float startAngle,float progress) {
        if(indeterminateSweep != -1){
            this.indeterminateSweep = indeterminateSweep;
        }
        if(indeterminateRotateOffset != -1){
            this.indeterminateRotateOffset = indeterminateRotateOffset;
        }
        if(startAngle != -1){
            this.startAngle = startAngle;
        }
        if(progress != -1){
            this.actualProgress = progress;
            if(Math.round(actualProgress) == 100 && fabViewListener != null){
                fabViewListener.onProgressCompleted();
            }
        }
    }
}
