package me.zhehua.uilibrary;

import android.animation.ValueAnimator;
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

import java.util.ArrayList;
import java.util.List;

import me.zhehua.uilibrary.adapter.LyricAdapter;
import me.zhehua.uilibrary.range.RangedTextView;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Zhehua on 2016/10/18.
 */

public class LyricView extends FrameLayout
        implements View.OnTouchListener, LyricAdapter.DataSetObserver, View.OnScrollChangeListener {

    private static final String TAG = "LyricView";

    protected ScrollView mScrollLyricView;
    protected LyricIndicator mIndicator;
    protected LinearLayout mContentView;

    protected LyricAdapter mAdapter;
    private List<Integer> mLineYPositions;
    private int halfHeight;

    private OnLyricProgressChangedListener mProgressChangedListener;
    private OnLyricReadyListener mReadyListener;

    private Scroller mScroller;

    private int mNormalTextColor = DEFAULT_NORMAL_COLOR;
    private int mSelectedTextColor = DEFAULT_SELECTED_COLOR;
    private int mHighlightTextColor = DEFAULT_HIGHLIGHT_COLOR;

    private static final int DEFAULT_NORMAL_COLOR = 0xff898989;
    private static final int DEFAULT_SELECTED_COLOR = 0xffA0A0A0;
    private static final int DEFAULT_HIGHLIGHT_COLOR = 0xffffffff;

    /**
     *   <-------------->  slicing
     *  ^
     *  |
     *  | scrolling
     *  |
     *  v
     */
    private boolean mIsSlicing = true;

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
                if (mScroller.computeScrollOffset()) {
                    this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                    invalidate();
                }
            }

            @Override
            public void fling(int velocityY) {
                super.fling(velocityY / 3);
            }
        };
        mScrollLyricView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mScrollLyricView.addView(mContentView);
        mScrollLyricView.setFillViewport(true);
        mScrollLyricView.setVerticalScrollBarEnabled(false);
        mScrollLyricView.setOnTouchListener(this);
        mScrollLyricView.setOnScrollChangeListener(this);
        mIndicator = new LyricIndicator(context);
        FrameLayout.LayoutParams indicatorLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
        mIndicator.setLayoutParams(indicatorLayoutParams);
        mIndicator.disable();
        addView(mScrollLyricView);
        addView(mIndicator);
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgressChangedListener(OnLyricProgressChangedListener mProgressChangedListener) {
        this.mProgressChangedListener = mProgressChangedListener;
    }

    public void setReadyListener(OnLyricReadyListener mReadyListener) {
        this.mReadyListener = mReadyListener;
    }

    private void markTextY() {
        int position = 0;
//        mLineYPositions.add(-1);// empty position
        for (int i = 1; i < mContentView.getChildCount(); i ++) {
            mLineYPositions.add(position);
            position += mContentView.getChildAt(i).getHeight();
        }
        mLineYPositions.add(position);
    }

    public void setLyricAdapter(LyricAdapter lyricAdapter) {
        mAdapter = lyricAdapter;
        initViews(lyricAdapter.size());
    }

    private void initViews(int n) {
        mLineYPositions = new ArrayList<>(n);

        RangedTextView blankHeader = new RangedTextView(getContext());
        blankHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                halfHeight));
        mContentView.addView(blankHeader);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        int size = mAdapter.size();
        for (int i = 1; i < size; i ++) {
            String sentence = mAdapter.getLine(i);
            RangedTextView textView = new RangedTextView(getContext());
            textView.setRangedText(sentence, 0, mAdapter.getLineLength(i) == -1 ? sentence.length() : mAdapter.getLineLength(i));
            textView.setRangedColor(mHighlightTextColor)
                    .setRangedBackColor(mNormalTextColor)
                    .setNormalColor(mNormalTextColor);
            if (!mIsSlicing) {
                textView.setRange(1);
            }
            textView.setLayoutParams(textLayoutParams);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setPadding(20, 20, 15, 15);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            mContentView.addView(textView);
        }
        final TextView blankFooter = new RangedTextView(getContext());
        blankFooter.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                halfHeight));
        mContentView.addView(blankFooter);

        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                halfHeight = mScrollLyricView.getHeight() / 2 + 5;
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
                if (mReadyListener != null) {
                    mReadyListener.onReady(LyricView.this);
                }

                // set click callback when view is ready
                mIndicator.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIndicator.disable();
                        pauseScroll();
                        long curProgress = mAdapter.getMilliTime(currSelectedLine);
                        startScroll(curProgress);
                        if (mProgressChangedListener != null) {
                            mProgressChangedListener.onProgressChanged(curProgress);
                        }
                    }
                });
            }
        });
    }

    /**
     * Start to scroll, make sure to call this after {@link android.app.Activity#onWindowFocusChanged(boolean)}.
     *
     * @return if successfully start to scroll.If not, you probably call this too early.
     */
    public boolean startScroll() {
        return startScroll(0L);
    }

    volatile boolean isSkipScroll = false;

    Runnable enableAutoScroll = new Runnable() {
        @Override
        public void run() {
            isSkipScroll = false;
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                removeCallbacks(enableAutoScroll);
                isSkipScroll = true;
                mIndicator.enable();
                break;
            case ACTION_UP:
                postDelayed(enableAutoScroll, 3000);
                break;
        }
        return false;
    }

    private int currSelectedLine = 0;
    private int curHighlightLine = 0;

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int lastSelected = currSelectedLine;
        if (scrollY > mLineYPositions.get(currSelectedLine)) {
            currSelectedLine = Math.min(mLineYPositions.size() - 1, currSelectedLine + 1);
        } else if (currSelectedLine > 0 && scrollY < mLineYPositions.get(currSelectedLine - 1)) {
            currSelectedLine = Math.max(0, currSelectedLine - 1);
        } else {
            return;
        }
        Log.d(TAG, "currSelectedLine: " + currSelectedLine + " scrollY: " + scrollY + " linePosition: " + mLineYPositions.get(currSelectedLine));
        if (lastSelected != curHighlightLine) {
            ((RangedTextView) mContentView.getChildAt(lastSelected))
                    .setRangedBackColor(mNormalTextColor)
                    .setRange(0.f)
                    .setNormalColor(mNormalTextColor);
        }
        if (currSelectedLine != curHighlightLine) {
            ((RangedTextView) mContentView.getChildAt(currSelectedLine))
                    .setRangedBackColor(mSelectedTextColor)
                    .setRange(0.f)
                    .setNormalColor(mSelectedTextColor);
        }
    }

    @Override
    public void onDataSetChanged(LyricAdapter lyricAdapter) {
        initViews(lyricAdapter.size());
    }

    public class ScrollRunnable implements Runnable {
        long nextTime;

        @Override
        public void run() {
            startScroll(nextTime);
        }
    }

    ValueAnimator slicingAnimator = ValueAnimator.ofFloat(0, 1);

    private void switchHighlight(int from, final int to) {

        slicingAnimator.cancel();
        ((RangedTextView) mContentView.getChildAt(to))
                .setRangedColor(mHighlightTextColor)
                .setRangedBackColor(mSelectedTextColor)
                .setRange(0)
                .setNormalColor(mHighlightTextColor);

        ((RangedTextView) mContentView.getChildAt(from))
                .setRangedBackColor(mNormalTextColor)
                .setRangedColor(mNormalTextColor)
                .setRange(0)
                .setNormalColor(mNormalTextColor);


        if (to < mAdapter.size()) {
            final RangedTextView rangedTextView = (RangedTextView) mContentView.getChildAt(to);
            long duration = mAdapter.getMilliTime(to + 1) - mAdapter.getMilliTime(to);
            slicingAnimator.removeAllUpdateListeners();
            slicingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    rangedTextView.setRange((Float) animation.getAnimatedValue()).show();
                }
            });
            slicingAnimator.setDuration(Math.max(0, duration - 300));
            slicingAnimator.start();
        }

    }

    private ScrollRunnable mScrollRunnable = new ScrollRunnable();

    public boolean startScroll(long startTime) {
        if (mLineYPositions == null || mAdapter == null) {
            throw new IllegalStateException("No lyric content found. Call setLyric() first.");
        }
        if (mLineYPositions.isEmpty())
            return false;
        int lastHighlightLine = curHighlightLine;
        curHighlightLine = mAdapter.getLineIndex(startTime);
        Log.d(TAG, "lastHighlight: " + lastHighlightLine + " curHighlight: " + curHighlightLine);
        switchHighlight(lastHighlightLine, curHighlightLine); // highlight one line
        if (curHighlightLine < mAdapter.size() - 1) { // prepare for the next scroll
            mScrollRunnable.nextTime = mAdapter.getMilliTime(curHighlightLine + 1);
            postDelayed(mScrollRunnable, mScrollRunnable.nextTime - startTime);
        }

        if (!isSkipScroll) { // execute this scroll
            mIndicator.disable();
            int curY = mScrollLyricView.getScrollY();
            // not accurate
            int curLinePos = mLineYPositions.get(curHighlightLine);
            int nextLinePos = mLineYPositions.get(curHighlightLine + 1);
            mScroller.startScroll(0, curY, 0,
                    (int) (curLinePos - (nextLinePos - curLinePos) / 2.f - curY), 1000);
            mScrollLyricView.invalidate();
        }
        return true;
    }

    public void pauseScroll() {
        removeCallbacks(mScrollRunnable);
    }

    public LyricAdapter getAdapter() {
        return mAdapter;
    }

    public interface OnLyricProgressChangedListener {
        /**
         *
         * @param progress playback progress in milliseconds
         */
        void onProgressChanged(long progress);
    }

    public interface OnLyricReadyListener {
        void onReady(View view);
    }
}
