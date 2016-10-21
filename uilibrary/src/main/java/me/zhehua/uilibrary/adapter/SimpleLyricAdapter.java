package me.zhehua.uilibrary.adapter;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.lyric.ArrayLyric;
import me.zhehua.lyric.Lyric;

/**
 * Created by Zhehua on 2016/10/21.
 */

public class SimpleLyricAdapter extends LyricAdapter {

    private Lyric mLyric;

    public SimpleLyricAdapter(InputStream inputStream) throws IOException {
        mLyric = new ArrayLyric(inputStream);
    }

    public SimpleLyricAdapter(Lyric lyric) {
        this.mLyric = lyric;
    }

    public void setInputStream(InputStream inputStream) throws IOException {
        mLyric = new ArrayLyric(inputStream);
        notifyDataSetChanged();
    }

    @Override
    public int getLineIndex(long milliTime) {
        return mLyric.getLineIndex(milliTime);
    }

    @Override
    public String getLine(long milliTime) {
        return mLyric.getLine(milliTime);
    }

    @Override
    public String getLine(int index) {
        return mLyric.getLine(index);
    }

    @Override
    public long getMilliTime(int index) {
        return mLyric.getMilliTime(index);
    }

    @Override
    public int size() {
        return mLyric.size();
    }
}
