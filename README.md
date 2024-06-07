# ExpandableTextView
This is an custom view using Canva to make the text expandable with ellipsized text.

To begin please follow below instructions:

1/ Add into your settings.gradle

```
maven { url 'https://jitpack.io' }
```

2/ Add into your build.gradle

```
implementation 'com.github.khangchow:ExpandableTextView:1.1.3'
```

3/ How to use 

- Add ExpandableTextView into your xml layout

```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    android:padding="20dp"
    tools:context=".MainActivity">

    <com.libaray.expandable_textview.ExpandableTextView
        android:id="@+id/tv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</LinearLayout>
```

- Customize properties programmatically

```
binding.tv.apply {
            text = "1234567891011121314151617181920212223242526272829303132333435363738394041424344454647484950"
            textSize = 25
            textColor = Color.RED
            ellipsizedTextColor = Color.BLUE
            ellipsizedText = "...Whatsup!"
            lineToEllipsize = 3
            setPaddings(all = 20) or setPaddings(top = 20, bottom = 10)
        }
```

That is it! Enjoy!
