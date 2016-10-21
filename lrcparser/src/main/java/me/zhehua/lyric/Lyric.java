package me.zhehua.lyric;

import java.util.AbstractMap;

/**
 * Attention: The first sentence of lyric should be null.
 *            Or to say, the index of lyric starts with 1.
 * Created by Zhehua on 2016/10/18.
 */

public interface Lyric extends Iterable<AbstractMap.SimpleEntry<Long, String>> {
    int getLineIndex(long milliTime);

    String getLine(long milliTime);

    String getLine(int index);

    long getMilliTime(int index);

    void append(long milliTime, String oneLine);

    public int size();
}
