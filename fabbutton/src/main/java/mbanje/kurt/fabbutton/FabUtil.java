package mbanje.kurt.fabbutton;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by kurt on 21 02 2015 .
 */
public class FabUtil {
    public static final float INDETERMINANT_MIN_SWEEP = 15f;
    public static final int ANIMATION_STEPS = 4;

    /**
     * the animation callback interface that should be used by classes that want to listen for events from the library animations
     */
    public static interface OnFabValueCallback{
        public void onIndeterminateValuesChanged(float indeterminateSweep,float indeterminateRotateOffset,float startAngle,float progress);
    }

    /**
     * Creates the starting angle animator for the circleview
     * @param view the view that the animator is to be attached too
     * @param from animate from value
     * @param to   animate to value
     * @param callback the callback interface for animations
     * @return ValueAnimator instance
     */
    public static ValueAnimator createStartAngleAnimator(final View view,float from,float to,final OnFabValueCallback callback){
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(5000);
        animator.setInterpolator(new DecelerateInterpolator(2));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float startAngle = (Float) animation.getAnimatedValue();
                callback.onIndeterminateValuesChanged(-1,-1,startAngle,-1);
                view.invalidate();
            }
        });
        return animator;
    }


    /**
     * Creates a progress animator
     * @param view the view that the animator is to be attached too
     * @param from animate from value
     * @param to   animate to value
     * @param callback the callback interface for animations
     * @return ValueAnimator instance
     */
    public static ValueAnimator createProgressAnimator(final View view,float from,float to,final OnFabValueCallback callback){
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float actualProgress = (Float) animation.getAnimatedValue();
                callback.onIndeterminateValuesChanged(-1,-1,-1,actualProgress);
                view.invalidate();
            }
        });
        return animator;
    }


    /**
     * Creates a progress animator
     * @param view the view that the animator is to be attached too
     * @param step animation steps of the prgress animation
     * @param animDuration   duration of the animation i.e. 1 cycle
     * @param callback the callback interface for animations
     * @return AnimatorSet instance
     */
    public static AnimatorSet createIndeterminateAnimator(final View view,float step,int animDuration, final OnFabValueCallback callback){
        final float maxSweep = 360f*(ANIMATION_STEPS-1)/ANIMATION_STEPS + INDETERMINANT_MIN_SWEEP;
        final float start = -90f + step*(maxSweep-INDETERMINANT_MIN_SWEEP);
        // Extending the front of the arc
        ValueAnimator frontEndExtend = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP, maxSweep);
        frontEndExtend.setDuration(animDuration/ANIMATION_STEPS/2);
        frontEndExtend.setInterpolator(new DecelerateInterpolator(1));
        frontEndExtend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float indeterminateSweep = (Float) animation.getAnimatedValue();
                callback.onIndeterminateValuesChanged(indeterminateSweep,-1,-1,-1);
                view.invalidate();
            }
        });

        // Overall rotation
        ValueAnimator rotateAnimator1 = ValueAnimator.ofFloat(step*720f/ANIMATION_STEPS, (step+.5f)*720f/ANIMATION_STEPS);
        rotateAnimator1.setDuration(animDuration/ANIMATION_STEPS/2);
        rotateAnimator1.setInterpolator(new LinearInterpolator());
        rotateAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float indeterminateRotateOffset = (Float) animation.getAnimatedValue();
                callback.onIndeterminateValuesChanged(-1,indeterminateRotateOffset,-1,-1);
            }
        });

        // Retracting the back end of the arc
        ValueAnimator backEndRetract = ValueAnimator.ofFloat(start, start+maxSweep-INDETERMINANT_MIN_SWEEP);
        backEndRetract.setDuration(animDuration/ANIMATION_STEPS/2);
        backEndRetract.setInterpolator(new DecelerateInterpolator(1));
        backEndRetract.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float startAngle = (Float) animation.getAnimatedValue();
                float indeterminateSweep = maxSweep - startAngle + start;
                callback.onIndeterminateValuesChanged(indeterminateSweep,-1,startAngle,-1);
                view.invalidate();
            }
        });

        // More overall rotation
        ValueAnimator rotateAnimator2 = ValueAnimator.ofFloat((step+.5f)*720f/ANIMATION_STEPS, (step+1)*720f/ANIMATION_STEPS);
        rotateAnimator2.setDuration(animDuration/ANIMATION_STEPS/2);
        rotateAnimator2.setInterpolator(new LinearInterpolator());
        rotateAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float indeterminateRotateOffset = (Float) animation.getAnimatedValue();
                callback.onIndeterminateValuesChanged(-1,indeterminateRotateOffset,-1,-1);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(frontEndExtend).with(rotateAnimator1);
        set.play(backEndRetract).with(rotateAnimator2).after(rotateAnimator1);
        return set;
    }
}
