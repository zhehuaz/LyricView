package me.zhehua.uilibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import me.zhehua.lyric.Lyric;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Zhehua on 2016/10/18.
 */

public class LyricView extends FrameLayout implements View.OnTouchListener{

    private static final String TAG = "LyricView";

    protected ScrollView mScrollLyricView;
    protected LinearLayout mIndicator;
    protected LinearLayout mContentView;

    protected Lyric mLyric;
    private List<Integer> mLineYPositions;
    private int halfHeight;

    private Scroller mScroller;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContentView = new LinearLayout(context) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                Log.i(TAG, "linear layout");
                super.onLayout(changed, l, t, r, b);
                if (getChildCount() == mLyric.size() + 2) {
                    markTextY();
                }
            }
//
//            public void markTextY() {
//                int position = 0;
//                for (int i = 1; i < mContentView.getChildCount() - 1; i ++) {
//                    mLineYPositions.add(position);
//                    position += mContentView.getChildAt(i).getHeight();
//                }
//            }
        };
        mContentView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(context);
        mScrollLyricView = new ScrollView(context) {
            @Override
            public void computeScroll() {
                super.computeScroll();
                if (mScroller.computeScrollOffset()) {
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
        mScrollLyricView.setOnTouchListener(this);
        addView(mScrollLyricView);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void markTextY() {
        int position = 0;
        for (int i = 1; i < mContentView.getChildCount() - 1; i ++) {
            mLineYPositions.add(position);
            position += mContentView.getChildAt(i).getHeight();
        }
    }

    public void setLyric(Lyric lyric) {
        mLyric = lyric;
        mLineYPositions = new ArrayList<>(lyric.size());
        initViews();
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                halfHeight = mScrollLyricView.getHeight() / 2;
                Log.i(TAG, "onLayout");
                mScrollLyricView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams headerLayoutParams = mContentView.getChildAt(0).getLayoutParams();
                headerLayoutParams.height = halfHeight;
                mContentView.getChildAt(0).setLayoutParams(headerLayoutParams);

                ViewGroup.LayoutParams footerLayoutParams = mContentView.getChildAt(mContentView.getChildCount() - 1)
                        .getLayoutParams();
                footerLayoutParams.height = halfHeight;
                mContentView.getChildAt(0).setLayoutParams(footerLayoutParams);
                markTextY();
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
        final View blankFooter = new View(getContext());
        blankFooter.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                halfHeight));
        mContentView.addView(blankFooter);
    }

    /**
     * Start to scroll, make sure to call this after {@link android.app.Activity#onWindowFocusChanged(boolean)}.
     *
     * @return if successfully start to scroll.If not, you probably call this too early.
     */
    public boolean startScroll() {
        return startScroll(0L);
    }

    boolean isSkipScroll = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG, "touch event:" + event.getAction());
        switch (event.getAction()) {
            case ACTION_DOWN:
                isSkipScroll = true;
                break;
            case ACTION_UP:
                isSkipScroll = false;
                break;
        }
        return false;
    }

    public class ScrollRunnable implements Runnable {
        long nextTime;

        @Override
        public void run() {
            startScroll(nextTime);
        }
    }

    ScrollRunnable mScrollRunnable = new ScrollRunnable();

    public boolean startScroll(long startTime) {
        if (mLineYPositions == null || mLyric == null) {
            throw new IllegalStateException("No lyric content found. Call setLyric() first.");
        }
        if (mLineYPositions.isEmpty())
            return false;
        int lineIndex = mLyric.getLineIndex(startTime);
        if (lineIndex < mLyric.size() - 1) { // prepare for the next scroll
            mScrollRunnable.nextTime = mLyric.getMilliTime(lineIndex + 1);
            postDelayed(mScrollRunnable, mScrollRunnable.nextTime - startTime);
        }

        if (!isSkipScroll) { // execute this scroll
            int curY = mScrollLyricView.getScrollY();
            mScroller.startScroll(0, curY, 0, mLineYPositions.get(lineIndex) - curY, 1000);
            mScrollLyricView.invalidate();
        }
        return true;
    }

    public void pauseScroll() {
        removeCallbacks(mScrollRunnable);
    }
}
