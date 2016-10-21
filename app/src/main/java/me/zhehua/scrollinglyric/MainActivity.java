package me.zhehua.scrollinglyric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.lyric.ArrayLyric;
import me.zhehua.lyric.Lyric;
import me.zhehua.uilibrary.LyricView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    LyricView mLyricView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLyricView = (LyricView) findViewById(R.id.lv_main);
        InputStream lrcAStream = getResources().openRawResource(R.raw.sample);
        InputStream lrcBStream = getResources().openRawResource(R.raw.sample_cn);
        try {
            Lyric lyricA = new ArrayLyric(lrcAStream);
//            Lyric lyricB = new ArrayLyric(lrcBStream);
            lrcAStream.close();
//            lrcBStream.close();
            mLyricView.setLyric(lyricA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean started = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !started) {
            Log.i(TAG, "window focused");
            mLyricView.startScroll();
            started = true;
        }
    }
}
