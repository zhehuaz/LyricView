package me.zhehua.uilibrary.adapter;

/**
 * Attention : The index of lyric sentences starts from 1.
 * Created by Zhehua on 2016/10/21.
 */

public abstract class LyricAdapter {
    protected DataSetObserver mObserver;

    public abstract int getLineIndex(long milliTime);

    public abstract String getLine(long milliTime);

    public abstract String getLine(int index);

    /**
     * Implement this when the length of lyric sentence doesn't cover the whole line.
     * @param index line num
     * @return the actual length of lyric sentence
     */
    public int getLineLength(int index) {
        return -1;
    }

    public abstract long getMilliTime(int index);

    public abstract int size();

    public void setDataSetObserver(DataSetObserver observer) {
        this.mObserver = observer;
    }

    public void notifyDataSetChanged() {
        if (mObserver != null) {
            mObserver.onDataSetChanged(this);
        }
    }

    public interface DataSetObserver {
        void onDataSetChanged(LyricAdapter lyricAdapter);
    }
}
