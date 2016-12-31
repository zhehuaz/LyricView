package me.zhehua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhehua.lyric.EnhancedLyric;
import me.zhehua.lyric.Lyric;

/**
 * Created by zhehua on 31/12/2016.
 */
public class EnhancedLrcParser implements LrcParser<EnhancedLyric> {
    SimpleLrcParser simpleParser = new SimpleLrcParser();
    @Override
    public void parseSource(EnhancedLyric lyric, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {
            simpleParser.constructOneLine(lyric, line);
            splitOneLineByProgress(lyric);
        }
    }

    void splitOneLineByProgress(EnhancedLyric lyric) {
        AbstractMap.SimpleEntry<Long, String> entry = lyric.remove();
        String line = entry.getValue();
        String splitReg = "<\\d{2}:\\d{2}\\.\\d{1,3}>";
        Pattern pattern = Pattern.compile(splitReg);
        Matcher matcher = pattern.matcher(line);
        String[] splitStrings = pattern.split(line);
        StringBuilder stringBuilder = new StringBuilder();
        int segStartIdx = 0;
        int i = 0;
        String timeString;
        ArrayList<AbstractMap.SimpleEntry<Long, Integer>> progressArray = new ArrayList<>();
        for (String seg : splitStrings) {
            timeString = matcher.group(i ++);
            progressArray.add(new AbstractMap.SimpleEntry<>(
                    TimestampUtils.string2Timestamp(timeString.substring(1, timeString.length() - 1)),
                    segStartIdx));
            stringBuilder.append(seg);
            segStartIdx += seg.length();
        }
        lyric.append(progressArray);
        lyric.append(entry.getKey(), stringBuilder.toString());
    }
}
