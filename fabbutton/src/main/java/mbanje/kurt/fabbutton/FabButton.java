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

    private CircleImageView view;
    private ProgressRingView ring;
    private float ringWidthRatio = 0.14f; //of a possible 1f;
    private boolean indeterminate;
    private boolean autostartanim;

    public FabButton(Context context) {
        super(context);
        init(null, 0);
    }

    public FabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    protected void init(AttributeSet attrs, int defStyle) {
        View v = View.inflate(getContext(), R.layout.widget_fab_button,this);
        view = (CircleImageView) v.findViewById(R.id.fab);
        ring = (ProgressRingView)v.findViewById(R.id.ring);
        view.setFabViewListener(this);
        ring.setFabViewListener(this);
        int color = Color.BLACK;
        int progressColor = Color.BLACK;
        int animDuration = 4000;
        int icon = -1;
        float maxProgress = 0;
        float progress =0;
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleImageView);
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

        view.setColor(color);
        ring.setProgressColor(progressColor);
        ring.setProgress(progress);
        ring.setMaxProgress(maxProgress);
        ring.setAutostartanim(autostartanim);
        ring.setAnimDuration(animDuration);
        view.setRingWidthRatio(ringWidthRatio);
        ring.setRingWidthRatio(ringWidthRatio);
        ring.setIndeterminate(indeterminate);
        if(icon != -1){
            view.setIcon(icon);
        }
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
        view.setOnClickListener(listener);
    }
    
    /**
     * shows the animation ring
     * @param show shows animation ring when set to true
     */
    public void showProgress(boolean show){
        if(show){
            view.showRing(true);
        }else{
            view.showRing(false);
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
        view.showCompleted(true);
    }
}
