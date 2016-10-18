package me.zhehua;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zhehua.lyric.Lyric;

public class LrcParser {

    private static final long MILLIS_IN_A_MINUTE = 60 * 1000;
    private static final long MILLIS_IN_A_SECOND = 1000;

    public static void parseSource(Lyric lyric, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = reader.readLine()) != null && line.length() > 0) {
            constructOneLine(lyric, line);
        }
    }
    private static final Pattern timePattern = Pattern.compile("^\\[\\d{2}:\\d{2}\\.\\d{2}\\]");
    private static final Pattern contentPattern = Pattern.compile("(?<=\\[\\d{2}:\\d{2}\\.\\d{2}\\]).*$");


    private static void constructOneLine(Lyric lyric, String line) {
        long time = 0;
        String content = null;
        Matcher timeMatcher = timePattern.matcher(line);
        String timeString;
        if (timeMatcher.find()) { // in case malformed
            timeString = timeMatcher.group(0);
            time += Integer.parseInt(timeString.substring(1, 3)) * MILLIS_IN_A_MINUTE;
            time += Integer.parseInt(timeString.substring(4, 6)) * MILLIS_IN_A_SECOND;
            time += Integer.parseInt(timeString.substring(7, 9));
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
