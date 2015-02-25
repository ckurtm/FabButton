package mbanje.kurt.fabbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by kurt on 21 02 2015 .
 */
public class FabButton extends FrameLayout implements CircleImageView.OnFabViewListener {

    private CircleImageView circle;
    private ProgressRingView ring;
    private float ringWidthRatio = 0.14f; //of a possible 1f;
    private boolean indeterminate;
    private boolean autostartanim;

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
            progressColor = a.getColor(R.styleable.CircleImageView_progressColor, Color.BLACK);
            progress = a.getFloat(R.styleable.CircleImageView_android_progress, 0f);
            maxProgress = a.getFloat(R.styleable.CircleImageView_android_max, 100f);
            indeterminate = a.getBoolean(R.styleable.CircleImageView_android_indeterminate, false);
            autostartanim = a.getBoolean(R.styleable.CircleImageView_autoStart, true);
            animDuration = a.getInteger(R.styleable.CircleImageView_android_indeterminateDuration, animDuration);
            icon = a.getResourceId(R.styleable.CircleImageView_android_src,icon);
            ringWidthRatio = a.getFloat(R.styleable.CircleImageView_progressWidthRatio, ringWidthRatio);
            a.recycle();
        }

        circle.setColor(color);
        ring.setProgressColor(progressColor);
        ring.setProgress(progress);
        ring.setMaxProgress(maxProgress);
        ring.setAutostartanim(autostartanim);
        ring.setAnimDuration(animDuration);
        circle.setRingWidthRatio(ringWidthRatio);
        ring.setRingWidthRatio(ringWidthRatio);
        ring.setIndeterminate(indeterminate);
        if(icon != -1){
            circle.setIcon(icon);
        }
    }

    public void setIcon(int resource){
        circle.setIcon(resource);
    }

    /**
     * sets the progress to indeterminate or not
     * @param indeterminate the flag
     */
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        ring.setIndeterminate(indeterminate);
    }

    public void setOnClickListener(OnClickListener listener){
        ring.setOnClickListener(listener);
        circle.setOnClickListener(listener);
    }

    /**
     * shows the animation ring
     * @param show shows animation ring when set to true
     */
    public void showProgress(boolean show){
        if(show){
            circle.showRing(true);
        }else{
            circle.showRing(false);
        }
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
        circle.showCompleted(true);
    }
}
