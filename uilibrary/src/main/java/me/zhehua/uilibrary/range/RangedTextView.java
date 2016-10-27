package me.zhehua.uilibrary.range;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.Spannable;
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

    public RangedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (text instanceof Spannable) {
            ((SpannableString) text).setSpan(mRangeSpan, 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
    }

    public void setRange(float percentage) {
        mRangeSpan.setRange(percentage);
        postInvalidate();
    }

    public void setNormalColor(@ColorInt int textColor) {
        mRangeSpan.setTextColor(textColor);
    }

    public void setHighlightColor(@ColorInt int highlightColor) {
        mRangeSpan.setHighlightColor(highlightColor);
    }
}
