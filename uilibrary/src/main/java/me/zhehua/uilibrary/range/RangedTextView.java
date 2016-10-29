package me.zhehua.uilibrary.range;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Zhehua on 2016/10/27.
 */

public class RangedTextView extends TextView {
    RangeSpan mRangeSpan;

    public RangedTextView(Context context) {
        this(context, null);
    }

    public RangedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRangeSpan = new RangeSpan();
    }

    public void setRangedText(String text, int start, int end) {
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(mRangeSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        super.setText(spannable);
    }

    public void setRange(float percentage) {
        mRangeSpan.setRange(percentage);
        postInvalidate();
    }

    public void setTextColor(int color) {
        mRangeSpan.setTextColor(color);
        super.setTextColor(color);
//        postInvalidate();
    }

    public void setRangedColor(@ColorInt int highlightColor) {
        mRangeSpan.setHighlightColor(highlightColor);
        postInvalidate();
    }
}
