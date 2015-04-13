##FabProgress
 Android Circular floating action button with intergrated progress indicator ring
[As per material design docs](http://www.google.com/design/spec/components/progress-activity.html#progress-activity-types-of-indicators)

##Demo:

[Demo apk](demo.apk)

![FabButton](example.gif)
![365 Body Workout](365.gif)

##HOW TO ADD TO YOUR PROJECT
 [ ![Download](https://api.bintray.com/packages/ckurtm/maven/FabButton/images/download.svg) ](https://bintray.com/ckurtm/maven/FabButton/_latestVersion)

 Gradle:
 ```groovy
 dependencies {
         compile 'mbanje.kurt:fabbutton:1.0.9'
 }
 ```

##Usage

-	Use FabButton: (check the demo app included)
```xml
    <view
        android:layout_width="@dimen/button_size"
        android:layout_height="@dimen/button_size"
        class="mbanje.kurt.fabbutton.FabButton"
        android:id="@+id/determinate"
        android:layout_gravity="center"
        android:color="#ff6e9cff"
        android:src="@drawable/ic_fab_play"
        android:visibility="visible"
        android:layout_centerInParent="true"
        android:indeterminate="false"
        android:max="100"
        app:fbb_autoStart="true"
        app:fbb_progressColor="#ff170aff"
        app:fbb_progressWidthRatio="0.1"
        app:fbb_endBitmap="@drawable/ic_fab_complete"
        app:fbb_showEndBitmap="true"
        />
```
##Apps using library

 [![alt text][2]][1]
 
   [1]: https://play.google.com/store/apps/details?id=com.peirr.workout.play
   [2]: https://developer.android.com/images/brand/en_app_rgb_wo_45.png (365 Body workout)
   
##License

The MIT License (MIT)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
