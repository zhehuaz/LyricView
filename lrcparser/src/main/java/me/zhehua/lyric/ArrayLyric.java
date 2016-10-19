package me.zhehua.lyric;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;

import me.zhehua.LrcParser;
import me.zhehua.SimpleLrcParser;

/**
 * Lyric sequence start with "1" because it has an empty head.
 *
 * Created by Zhehua on 2016/10/18.
 */
public class ArrayLyric extends Lyric {
    InputStream mLyricInputStream;
    LrcParser mLrcParser;

    public ArrayLyric() {
        super();
        mLrcParser = new SimpleLrcParser();
        mLyric.add(new AbstractMap.SimpleEntry<>(-1L, "head"));
    }

    public ArrayLyric(InputStream inputStream) throws IOException {
        this();
        setInputStream(inputStream);
    }

    /**
     * Only one input stream is supported.
     * @param inputStream
     * @throws IOException
     */
    public void setInputStream(InputStream inputStream) throws IOException {
        if (mLyricInputStream == null) {
            mLrcParser.parseSource(this, inputStream);
        }
    }

    public void setParser(LrcParser lrcParser) {
        mLrcParser = lrcParser;
    }

    @Override
    public String getLine(long milliTime) {
        return getLine(searchOneLine(milliTime));
    }

    @Override
    public String getLine(int index) {
        if (index < mLyric.size() && index > 0)
            return mLyric.get(index).getValue();
        return null;
    }

    @Override
    public void append(long milliTime, String oneLine) {
        if (mLyric.get(mLyric.size() - 1).getKey() < milliTime) {
            mLyric.add(new AbstractMap.SimpleEntry<>(milliTime, oneLine));
        }
    }

    /**
     * Find one lyric line shown at the given time.
     * Searching by binary search algorithm.
     * @param time at what time to show the lyric line
     * @return the index of line
     */
    protected int searchOneLine(long time) {
        int left = 0;
        int right = mLyric.size();
        int middle = -1;

        while (left < right - 1) {
            middle = (left + right) / 2;
            if (mLyric.get(middle).getKey() > time) {
                right = middle;
            } else {
                left = middle;
            }
        }
        if (middle == -1)
            return -1;
        return left;
    }
}
