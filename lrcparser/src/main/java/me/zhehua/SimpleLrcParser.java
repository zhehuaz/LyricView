package me.zhehua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhehua.lyric.Lyric;

/**
 * Created by Zhehua on 2016/10/19.
 */

public class SimpleLrcParser implements LrcParser {
    @Override
    public void parseSource(Lyric lyric, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {
            constructOneLine(lyric, line);
        }
    }
    private static final Pattern timePattern = Pattern.compile("(?<=^\\[)\\d{2}:\\d{2}\\.\\d{2}(?=\\])");
    private static final Pattern contentPattern = Pattern.compile("(?<=\\[\\d{2}:\\d{2}\\.\\d{2}\\]).*$");


    protected void constructOneLine(Lyric lyric, String line) {
        long time = 0;
        String content = null;
        Matcher timeMatcher = timePattern.matcher(line);
        String timeString;
        if (timeMatcher.find()) { // in case malformed
            timeString = timeMatcher.group(0);
            String[] times = timeString.split(":|\\.");
            if (times.length == 3) {
                time += Integer.parseInt(times[0]) * MILLIS_IN_A_MINUTE;
                time += Integer.parseInt(times[1]) * MILLIS_IN_A_SECOND;
                while (times[2].length() < 3) {
                    times[2] += "0";
                }
                time += Integer.parseInt(times[2]);
            }
        } else {
            System.err.println("Time tag should format as [mm:ss:ll]");
            return;
        }
        Matcher contentMatcher = contentPattern.matcher(line);
        if (contentMatcher.find()) {
            content = contentMatcher.group(0);
        } else {
            System.err.println("No lyric content found in line > " + line);
            return;
        }

        if (content != null) {
            lyric.append(time, content);
        }
    }
}
