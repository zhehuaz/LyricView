package me.zhehua.scrollinglyric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.lyric.ArrayLyric;
import me.zhehua.lyric.Lyric;
import me.zhehua.uilibrary.LyricView;

public class MainActivity extends AppCompatActivity {
    LyricView mLyricView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLyricView = (LyricView) findViewById(R.id.lv_main);
        InputStream lrcStream = getResources().openRawResource(R.raw.sample);
        try {
            Lyric lyric = new ArrayLyric(lrcStream);
            lrcStream.close();
            mLyricView.setLyric(lyric);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean started = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && !started) {
            mLyricView.startScroll();
            started = true;
        }
    }
}
