package me.zhehua.lyric;

import java.util.AbstractMap;

/**
 * Lyric sequence start with "1" because it has an empty head.
 * Created by Zhehua on 2016/10/18.
 */
public class HeadedLyric extends Lyric {

    public HeadedLyric() {
        super();
        mLyric.add(new AbstractMap.SimpleEntry<>(-1L, "head"));
    }

    @Override
    public String getOneLine(long milliTime) {
        return getOneLine(searchOneLine(milliTime));
    }

    @Override
    public String getOneLine(int index) {
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

    protected int searchOneLine(long time) {
        int left = 1;
        int right = mLyric.size() - 1;
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
