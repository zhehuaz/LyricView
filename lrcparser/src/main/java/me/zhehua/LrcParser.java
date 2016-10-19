package me.zhehua;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.lyric.Lyric;

public interface LrcParser {

    long MILLIS_IN_A_MINUTE = 60 * 1000;
    long MILLIS_IN_A_SECOND = 1000;

    void parseSource(Lyric lyric, InputStream inputStream) throws IOException;
}
