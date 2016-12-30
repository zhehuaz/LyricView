# LyricView
![Demo](https://github.com/LangleyChang/LyricView/blob/master/demo_karaok.gif)

A netease-music-like scrolling lyric view.

## Features

- Parsing .lrc file
- Auto scrolling
- Text highlight
- Manual progress select
- Multi-language display
- Karaoke effect

## Usage
  
Copy module `lrcparser` and `uilibrary` to your project.

Declare it like this in your XML:
```xml
  <me.zhehua.uilibrary.LyricView
        android:id="@+id/lv_main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#212121"/>
```
And call this to initialize the view in `OnCreate()`:
```Java
  mLyricView = (LyricView) findViewById(R.id.lv_main);
  InputStream lrcStream = getResources().openRawResource(R.raw.sample);
  try {
      LyricAdapter lyric = new SimpleLyricAdapter(lrcStream);
      lrcStream.close();
      mLyricView.setLyricAdapter(lyric);
  } catch (IOException e) {
      e.printStackTrace();
  }
```
Or you can use `DoubleLyricAdatper` initialized with two streams to show a two-way display lyric view.

If no adpter fits your need, use a custom adatper extending `LyricAdapter`.

When `setLyricAdapter()` is called, the lyric view starts to build.

Then call
```Java
  mLyricView.startScroll();
```
to start the scroll.

Be careful that you have to call this after the view in activity is shown.

It's safe to call this in `Activity#onWindowFocusChanged(boolean)`.

If you're not sure about this, get the proper time by calling:
```Java
    mLyricView.setReadyListener(new LyricView.OnLyricReadyListener() {
            @Override
            public void onReady(View view) {
                do something...
            }
        });
```

## Contribute
  
  Thanks a lot for contribute to this repo by coding or issues.
  New PRs on `develop` branch please~


## License


    Copyright 2016 Zhehua Zhang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
  
