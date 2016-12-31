package me.zhehua;

/**
 * Created by zhehua on 31/12/2016.
 */

public class TimestampUtils {
    static long MILLIS_IN_A_MINUTE = 60 * 1000;
    static long MILLIS_IN_A_SECOND = 1000;
    public static long string2Timestamp(String timeString) {
        long time = 0;
        String[] times = timeString.split(":|\\.");
        if (times.length == 3) {
            time += Integer.parseInt(times[0]) * MILLIS_IN_A_MINUTE;
            time += Integer.parseInt(times[1]) * MILLIS_IN_A_SECOND;
            while (times[2].length() < 3) {
                times[2] += "0";
            }
            time += Integer.parseInt(times[2]);
        }
        return time;
    }
}
