package me.zhehua.scrollinglyric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import me.zhehua.uilibrary.DoubleLyricAdapter;
import me.zhehua.uilibrary.LyricAdapter;
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
            LyricAdapter lyricAdapter = new DoubleLyricAdapter(lrcAStream, lrcBStream);
            lrcAStream.close();
            lrcBStream.close();
            mLyricView.setLyricAdapter(lyricAdapter);
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
