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
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import mbanje.kurt.fabbutton.R;


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
    private BitmapDrawable drawables[] = new BitmapDrawable[2];
    private TransitionDrawable crossfader;

    private int ringWidth;
    private ObjectAnimator ringAnimator;


    float radiusY = 3.5f;
    float radiusX = 0f;
    float shadowRadius = 10f;
    int shadowTransparency = 100;



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

    private void init(Context context, AttributeSet attrs) {
        this.setFocusable(true);
        this.setScaleType(ScaleType.CENTER_INSIDE);
        setClickable(true);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setShadowLayer(shadowRadius, radiusX, radiusY, Color.argb(shadowTransparency, 0, 0, 0));

        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        int color = Color.BLACK;
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
            color = a.getColor(R.styleable.CircleImageView_android_color, Color.BLACK);
            ringWidthRatio = a.getFloat(R.styleable.CircleImageView_progressWidthRatio, ringWidthRatio);
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

    /**
     * sets the icon that will be shown on the fab icon
     * @param resource the resource id of the icon
     */
    public void setIcon(int resource){
        Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(),resource);
        Bitmap endBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_fab_complete);
        drawables[0] = new BitmapDrawable(getResources(),srcBitmap);
        drawables[1] = new BitmapDrawable(getResources(),endBitmap);
        crossfader = new TransitionDrawable(drawables);
        crossfader.setCrossFadeEnabled(true);
        setImageDrawable(crossfader);
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
     */
    public void showCompleted(boolean show){
        if(show){
            crossfader.startTransition(500);
        }else{
            crossfader.reverseTransition(500);
        }
    }
}
