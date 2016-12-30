package me.zhehua.uilibrary.range;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.text.style.ReplacementSpan;

/**
 * Created by Zhehua on 2016/10/27.
 */

public class RangeSpan extends ReplacementSpan {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mPercent;
    private Rect bounds = new Rect();
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    private int mHighlightColor = 0xffFF0000;
    private int mTextColor = 0xFF565656;

    public RangeSpan() {
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setRange(float percentage) {
        mPercent = Math.max(0, Math.min(1, percentage));
    }

    public void setTextColor(@ColorInt int textColor) {
        mTextColor = textColor;
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        mHighlightColor = highlightColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint.getTextBounds(text.toString(), start, end, bounds);
        if (fm != null) {

            fm.ascent = bounds.top;
            fm.descent = 6;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }
        return bounds.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        int sc = canvas.saveLayer(0, 0, 720, 1080, null, Canvas.ALL_SAVE_FLAG);

        // roughly measure text width to be drawn
        float textWidth = mPaint.measureText(text.toString());
        // dst
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(paint.getTextSize());
        canvas.drawText(text.toString().substring(start, end), x, y, mPaint);
        mPaint.setXfermode(xfermode);
        // src
        mPaint.setColor(mHighlightColor);
        canvas.drawRect(bounds.left, 0, textWidth * mPercent, 720, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

}
