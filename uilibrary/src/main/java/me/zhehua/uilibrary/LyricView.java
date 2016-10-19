package me.zhehua.uilibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.AbstractMap;

import me.zhehua.lyric.Lyric;

/**
 * Created by Zhehua on 2016/10/18.
 */

public class LyricView extends FrameLayout {

    private static final String TAG = "LyricView";

    protected ScrollView mScrollLyricView;
    protected LinearLayout mIndicator;
    protected LinearLayout mContentView;

    protected Lyric mLyric;
    private int halfHeight;

    private Scroller mScroller;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContentView = new LinearLayout(context);
        mContentView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(context);
        mScrollLyricView = new ScrollView(context) {


            @Override
            public void computeScroll() {
                super.computeScroll();
                Log.i(TAG, "computeScroll");
                if (mScroller.computeScrollOffset()) {
                    Log.i(TAG, "x " + mScroller.getCurrX() + " y " + mScroller.getCurrY());
                    this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                    invalidate();
                }
            }
        };
        mScrollLyricView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mScrollLyricView.addView(mContentView);
        mScrollLyricView.setFillViewport(true);
        mScrollLyricView.setVerticalScrollBarEnabled(false);
//        mScrollLyricView.setScrollbarFadingEnabled(false);
        addView(mScrollLyricView);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setLyric(Lyric lyric) {
        mLyric = lyric;
        mScrollLyricView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                halfHeight = mScrollLyricView.getHeight() / 2;
                mScrollLyricView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initViews();
            }
        });
    }

    private void initViews() {
        View blankHeader = new View(getContext());
        blankHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                halfHeight));
        mContentView.addView(blankHeader);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (AbstractMap.SimpleEntry<Long, String> sentence : mLyric) {
            TextView textView = new TextView(getContext());
            textView.setText(sentence.getValue());
            textView.setTextColor(0xffff0000);
            textView.setLayoutParams(textLayoutParams);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(20, 20, 15, 15);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mContentView.addView(textView);
        }
        View blankFooter = new View(getContext());
        blankFooter.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                halfHeight));
        mContentView.addView(blankFooter);
    }

    public void startScroll() {
        startScroll(0L);
    }

    public void startScroll(long startTime) {
        Log.i(TAG, "start to scroll");
        mScroller.startScroll(0, (int) startTime, 0, 100, 500);
        mScrollLyricView.invalidate();
        final int finalStartTime = (int) startTime + 100;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startScroll(finalStartTime);
            }
        }, 1000);
    }

    public void pauseScroll() {
//        removeCallbacks();
    }
}
