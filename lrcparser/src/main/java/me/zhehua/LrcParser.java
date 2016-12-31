package me.zhehua;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.lyric.Lyric;

public interface LrcParser<T extends Lyric> {

    void parseSource(T lyric, InputStream inputStream) throws IOException;
}
