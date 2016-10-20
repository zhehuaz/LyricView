package me.zhehua.uilibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Zhehua on 2016/10/20.
 */

public class LyricIndicator extends LinearLayout {
    Paint linePaint;
    ImageView playBtn;
    public LyricIndicator(Context context) {
        this(context, null);
    }

    public LyricIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);

        playBtn = new ImageView(context);
        playBtn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        playBtn.setImageResource(R.drawable.ic_play_arrow_white_24dp);

        addView(playBtn);
        linePaint = new Paint();
        linePaint.setColor(0x30ffffff);
        linePaint.setStrokeWidth(2f);
        setWillNotDraw(false);
    }


    public LyricIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(getPaddingLeft() + getChildAt(0).getWidth(), getMeasuredHeight() / 2,
                getMeasuredWidth() - getPaddingRight(),
                getMeasuredHeight() / 2,
                linePaint);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        playBtn.setOnClickListener(l);
    }

    public void enable() {
        setVisibility(VISIBLE);
        playBtn.setClickable(true);
    }

    public void disable() {
        setVisibility(INVISIBLE);
        playBtn.setClickable(false);
    }
}
