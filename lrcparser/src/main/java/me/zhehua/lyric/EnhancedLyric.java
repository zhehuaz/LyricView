package me.zhehua.lyric;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Created by zhehua on 31/12/2016.
 */

public class EnhancedLyric extends ArrayLyric {
    protected ArrayList<ArrayList<AbstractMap.SimpleEntry<Long, Integer>>> mLyricProgress;


    public EnhancedLyric() {
        mLyricProgress = new ArrayList<>();
        // null head to make available items start with 1
        mLyricProgress.add(new ArrayList<AbstractMap.SimpleEntry<Long, Integer>>());
    }

    public void append(ArrayList<AbstractMap.SimpleEntry<Long, Integer>> progress) {
        mLyricProgress.add(progress);
    }
}
