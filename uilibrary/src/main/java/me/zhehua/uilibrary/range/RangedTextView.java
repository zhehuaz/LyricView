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
        spannable.setSpan(mRangeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        super.setText(spannable);
    }

    public RangedTextView setRange(float percentage) {
        mRangeSpan.setRange(percentage);
        return this;
    }

    public RangedTextView setNormalColor(int color) {
        super.setTextColor(color);
        return this;
    }

    public RangedTextView setRangedColor(@ColorInt int highlightColor) {
        mRangeSpan.setHighlightColor(highlightColor);
        return this;
    }

    public RangedTextView setRangedBackColor(@ColorInt int normalColor) {
        mRangeSpan.setTextColor(normalColor);
        return this;
    }

    public void show() {
        invalidate();
    }
}
