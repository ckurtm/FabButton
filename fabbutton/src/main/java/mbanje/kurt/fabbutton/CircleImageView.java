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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;


public class CircleImageView extends ImageView {

    public interface OnFabViewListener {
        public void onProgressVisibilityChanged(boolean visible);
        public void onProgressCompleted();
    }

    private static final int animationDuration = 200;
    private int centerY;
    private int centerX;
    private int viewRadius;
    private boolean progressVisible;
    private int circleRadius;
    private Paint circlePaint;
    private OnFabViewListener fabViewListener;
    private final int ringAlpha = 75;
    private int ringRadius;
    private Paint ringPaint;
    private float currentRingWidth;
    private float ringWidthRatio = 0.14f; //of a possible 1f;
    private Drawable drawables[] = new Drawable[2];
    private TransitionDrawable crossfader;

    private int ringWidth;
    private ObjectAnimator ringAnimator;

    float shadowDy = 3.5f;
    float shadowDx = 0f;
    float shadowRadius = 10f;
    int shadowTransparency = 100;
    private boolean showEndBitmap;
    private boolean showShadow = true;
    public CircleImageView(Context context) {
        super(context);
        init(context, null);
    }



    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setFabViewListener(OnFabViewListener fabViewListener) {
        this.fabViewListener = fabViewListener;
    }

    public void setShowShadow(boolean showShadow) {
        if(showShadow) {
            circlePaint.setShadowLayer(shadowRadius, shadowDx, shadowDy, Color.argb(shadowTransparency, 0, 0, 0));
        } else {
            circlePaint.clearShadowLayer();
        }
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        this.setFocusable(false);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        setClickable(true);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if(displayMetrics.densityDpi <= 240) {
            shadowRadius = 1.0f;
        } else if(displayMetrics.densityDpi <= 320) {
            shadowRadius = 3.0f;
        } else {
            shadowRadius = 10.0f;
        }
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        int color = Color.BLACK;
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            color = a.getColor(R.styleable.CircleImageView_android_color, Color.BLACK);
            ringWidthRatio = a.getFloat(R.styleable.CircleImageView_fbb_progressWidthRatio, ringWidthRatio);
            shadowRadius = a.getFloat(R.styleable.CircleImageView_android_shadowRadius,shadowRadius);
            shadowDy = a.getFloat(R.styleable.CircleImageView_android_shadowDy, shadowDy);
            shadowDx = a.getFloat(R.styleable.CircleImageView_android_shadowDx, shadowDx);
            setShowShadow(a.getBoolean(R.styleable.CircleImageView_fbb_showShadow, true));
            a.recycle();
        }
        setColor(color);
        final int pressedAnimationTime = animationDuration;
        ringAnimator = ObjectAnimator.ofFloat(this, "currentRingWidth", 0f, 0f);
        ringAnimator.setDuration(pressedAnimationTime);
        ringAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (fabViewListener != null) {
                    fabViewListener.onProgressVisibilityChanged(progressVisible);
                }
            }
        });
    }

    public void setShowEndBitmap(boolean showEndBitmap) {
        this.showEndBitmap = showEndBitmap;
    }

    /**
     * sets the icon that will be shown on the fab icon
     * @param resource the resource id of the icon
     */
    public void setIcon(int resource, int endBitmapResource){
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(),resource);
        if(showEndBitmap){
            Bitmap endBitmap = BitmapFactory.decodeResource(getResources(),endBitmapResource);
            setIconAnimation(new BitmapDrawable(getResources(),srcBitmap), new BitmapDrawable(getResources(),endBitmap));
        }else{
            setImageBitmap(srcBitmap);
        }
    }

    /**
     * sets the icon that will be shown on the fab icon
     * @param icon the initial icon
     * @param endIcon the icon to be displayed when the progress is finished
     */
    public void setIcon(Drawable icon, Drawable endIcon){
        if(showEndBitmap){
            setIconAnimation(icon, endIcon);
        }else{
            setImageDrawable(icon);
        }
    }

    private void setIconAnimation(Drawable icon, Drawable endIcon){
        drawables[0] = icon;
        drawables[1] = endIcon;
        crossfader = new TransitionDrawable(drawables);
        crossfader.setCrossFadeEnabled(true);
        setImageDrawable(crossfader);
    }

    public void resetIcon(){
        crossfader.resetTransition();
    }

    /**
     * this sets the thickness of the ring as a fraction of the radius of the circle.
     * @param ringWidthRatio the ratio 0-1
     */
    public void setRingWidthRatio(float ringWidthRatio) {
        this.ringWidthRatio = ringWidthRatio;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float ringR = ringRadius + currentRingWidth;
        canvas.drawCircle(centerX, centerY, ringR, ringPaint); // the outer ring
        canvas.drawCircle(centerX, centerY, circleRadius, circlePaint); //the actual circle
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        viewRadius = Math.min(w,h) / 2;
        ringWidth = Math.round((float) viewRadius * ringWidthRatio);
        circleRadius = viewRadius - ringWidth;
        ringPaint.setStrokeWidth(ringWidth);
        ringPaint.setAlpha(ringAlpha);
        ringRadius = circleRadius - ringWidth/2;
    }

    public float getCurrentRingWidth() {
        return currentRingWidth;
    }

    public void setCurrentRingWidth(float currentRingWidth) {
        this.currentRingWidth = currentRingWidth;
        this.invalidate();
    }

    /**
     * sets the color of the circle
     * @param color the actual color to set to
     */
    public void setColor(int color) {
        circlePaint.setColor(color);
        ringPaint.setColor(color);
        ringPaint.setAlpha(ringAlpha);
        this.invalidate();
    }

    /**
     * whether to show the ring or not
     * @param show set flag
     */
    public void showRing(boolean show){
        progressVisible = show;
        if(show){
            ringAnimator.setFloatValues(currentRingWidth, ringWidth);
        }else{
            ringAnimator.setFloatValues(ringWidth, 0f);
        }
        ringAnimator.start();
    }

    /**
     * this animates between the icon set in the imageview and the completed icon. does as crossfade animation
     * @param show set flag
     * @param hideOnComplete if true animate outside ring out after progress complete
     */
    public void showCompleted(boolean show, boolean hideOnComplete){
        if(show){
            crossfader.startTransition(500);
        }
        if (hideOnComplete) {
            ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(this, "currentRingWidth", 0f, 0f);
            hideAnimator.setFloatValues(1);
            hideAnimator.setDuration(animationDuration);
            hideAnimator.start();

        }
    }
}
