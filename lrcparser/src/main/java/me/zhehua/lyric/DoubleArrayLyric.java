package me.zhehua.lyric;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Created by Zhehua on 2016/10/21.
 */

public class DoubleArrayLyric extends ArrayLyric {
    protected ArrayList<AbstractMap.SimpleEntry<Long, String>> mSubLyric;

    protected InputStream mSubLyricInputStream;

    public DoubleArrayLyric(InputStream inputStream, InputStream subInputStream) throws IOException {
        super(inputStream);
        mSubLyric = new ArrayList<>();
        mSubLyric.add(new AbstractMap.SimpleEntry<>(-1L, "head"));
        this.setSubInputStream(subInputStream);
    }


    public void setSubInputStream(InputStream subLyricStream) {
        mSubLyricInputStream = subLyricStream;
        if (subLyricStream != null) {
        }
    }

    public void setInputStream(InputStream materLyricStream, InputStream subLyricStream)
            throws IOException {
        setInputStream(materLyricStream);
        mSubLyricInputStream = subLyricStream;
        if (subLyricStream != null) {
            mLrcParser.parseSource(this, mSubLyricInputStream);
        }
    }

    @Override
    public String getLine(long milliTime) {
        return this.getLine(getLineIndex(milliTime));
    }

    @Override
    public String getLine(int index) {
        return super.getLine(index) + "\n" + mSubLyric.get(index);
    }

    @Override
    public void append(long milliTime, String oneLine) {
        super.append(milliTime, oneLine);
        if (mSubLyric.get(mSubLyric.size() - 1).getKey() < milliTime) {
            mSubLyric.add(new AbstractMap.SimpleEntry<>(milliTime, oneLine));
        }
    }
}
