package me.zhehua.lyric;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Created by Zhehua on 2016/10/18.
 */

public abstract class Lyric {
    protected ArrayList<AbstractMap.SimpleEntry<Long, String>> mLyric;

    public Lyric() {
        this.mLyric = new ArrayList<>();
    }

    public abstract String getLine(long milliTime);

    public abstract String getLine(int index);

    public long getMilliTime(int index) {
        if (index < mLyric.size()) {
            return mLyric.get(index).getKey();
        }
        return -1L;
    }
    public abstract void append(long milliTime, String oneLine);
}
