##FabProgress
 Android Circular floating action button with intergrated progress indicator ring
[As per material design docs](http://www.google.com/design/spec/components/progress-activity.html#progress-activity-types-of-indicators)

##Demo:

![FabButton](example.gif)
![365 Body Workout](365.gif)

 [![alt text][2]][1]
 
   [1]: https://play.google.com/store/apps/details?id=com.peirr.workout.play
   [2]: https://developer.android.com/images/brand/en_app_rgb_wo_45.png (365 Body workout)
   
##HOW TO ADD TO YOUR PROJECT

 Gradle:
 
 ```groovy
 dependencies {
         compile 'mbanje.kurt:fabbutton:1.0.0'
 }
 ```

##Usage

-	Use FabButton: (check the demo app included)
```xml
<view
        android:layout_width="@dimen/button_size"
        android:layout_height="@dimen/button_size"
        class="mbanje.kurt.fabbutton.FabButton"
        android:id="@+id/indeterminate"
        android:layout_gravity="center"
        android:color="#aa66cc"
        android:src="@drawable/ic_fab_play"
        android:visibility="visible"
        android:indeterminate="true"
        android:max="100"
        app:autoStart="true"
        app:progressColor="#0099cc"
        app:progressWidthRatio="0.2"
        android:layout_centerHorizontal="true"
        android:layout_above="@+id/determinate"
        android:layout_marginBottom="48dp"
        />
```


