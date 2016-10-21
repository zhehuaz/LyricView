package me.zhehua.uilibrary;

/**
 * Attention : The index of lyric sentences starts from 1.
 * Created by Zhehua on 2016/10/21.
 */

public interface LyricAdapter {
    int getLineIndex(long milliTime);

    String getLine(long milliTime);

    String getLine(int index);

    long getMilliTime(int index);

    public int size();
}
