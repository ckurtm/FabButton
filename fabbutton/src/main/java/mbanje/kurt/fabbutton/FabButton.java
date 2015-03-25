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
    private int endBitmapResource;
    private boolean showEndBitmap;

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
            progressColor = a.getColor(R.styleable.CircleImageView_fbb_progressColor, Color.BLACK);
            progress = a.getFloat(R.styleable.CircleImageView_android_progress, 0f);
            maxProgress = a.getFloat(R.styleable.CircleImageView_android_max, 100f);
            indeterminate = a.getBoolean(R.styleable.CircleImageView_android_indeterminate, false);
            autostartanim = a.getBoolean(R.styleable.CircleImageView_fbb_autoStart, true);
            animDuration = a.getInteger(R.styleable.CircleImageView_android_indeterminateDuration, animDuration);
            icon = a.getResourceId(R.styleable.CircleImageView_android_src,icon);
            ringWidthRatio = a.getFloat(R.styleable.CircleImageView_fbb_progressWidthRatio, ringWidthRatio);
            endBitmapResource = a.getResourceId(R.styleable.CircleImageView_fbb_endBitmap, R.drawable.ic_fab_complete);
            showEndBitmap = a.getBoolean(R.styleable.CircleImageView_fbb_showEndBitmap,false);

            a.recycle();
        }

        circle.setColor(color);
        circle.setShowEndBitmap(showEndBitmap);
        ring.setProgressColor(progressColor);
        ring.setProgress(progress);
        ring.setMaxProgress(maxProgress);
        ring.setAutostartanim(autostartanim);
        ring.setAnimDuration(animDuration);
        circle.setRingWidthRatio(ringWidthRatio);
        ring.setRingWidthRatio(ringWidthRatio);
        ring.setIndeterminate(indeterminate);
        if(icon != -1){
            circle.setIcon(icon,endBitmapResource);
        }
    }

    public void setIcon(int resource,int endIconResource){
        circle.setIcon(resource,endIconResource);
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
        circle.showCompleted(showEndBitmap);
    }
}
