package me.zhehua.uilibrary.adapter;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.lyric.ArrayLyric;
import me.zhehua.lyric.Lyric;

/**
 * Created by Zhehua on 2016/10/21.
 */

public class DoubleLyricAdapter extends LyricAdapter {

    private Lyric mLyricA;
    private Lyric mLyricB;

    public DoubleLyricAdapter(InputStream inputStreamA, InputStream inputStreamB) throws IOException {
        mLyricA = new ArrayLyric(inputStreamA);
        mLyricB = new ArrayLyric(inputStreamB);
    }

    public DoubleLyricAdapter(Lyric lyricA, Lyric lyricB) {
        this.mLyricA = lyricA;
        this.mLyricB = lyricB;
    }

    public void setInputStreams(InputStream inputStreamA, InputStream inputStreamB) throws IOException {
        mLyricA = new ArrayLyric(inputStreamA);
        mLyricB = new ArrayLyric(inputStreamB);
        notifyDataSetChanged();
    }

    @Override
    public int getLineIndex(long milliTime) {
        return mLyricA.getLineIndex(milliTime);
    }

    @Override
    public String getLine(long milliTime) {
        return getLine(getLineIndex(milliTime));
    }

    @Override
    public String getLine(int index) {
        String lineB = mLyricB.getLine(index);
        if (lineB != null) {
            return mLyricA.getLine(index) + "\n" + mLyricB.getLine(index);
        }
        return mLyricA.getLine(index);
    }

    @Override
    public long getMilliTime(int index) {
        return mLyricA.getMilliTime(index);
    }

    @Override
    public int size() {
        return mLyricA.size();
    }

    @Override
    public int getLineLength(int index) {
        return mLyricA.getLine(index).length();
    }
}
