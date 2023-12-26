# ExpandableTextView
This is an custom view using Canva to make the text expandable with ellipsized text.

To begin please follow below instructions:
1/ Add into your settings.gradle

```
maven { url 'https://jitpack.io' }
```

2/ Add into your build.gradle

```
implementation 'com.github.khangchow:ExpandableTextView:1.1.1'
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
            text = "Hello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello World"
            textSize = 25
            textColor = Color.RED
            ellipsizedText = "...Ellipsized text"
        }
```

That is it! Enjoy!
