package com.libaray.expandable_textview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var isCollapsable = false
    private var isExpanding = false
    var text = ""
        set(value) {
            if (value == field) return
            field = value
            isNeedCalculated = true
            requestLayout()
        }
    private val textPaint = TextPaint().apply {
        textSize = 20.sp
        color = Color.BLACK
    }
    var textSize = 0
        set(value) {
            if (value == field || value < 0) return
            field = value
            textPaint.textSize = value.sp
            isNeedCalculated = true
            requestLayout()
        }
    var textColor = Color.BLACK
        set(value) {
            if (value == field) return
            field = value
            textPaint.color = value
            isNeedCalculated = true
            requestLayout()
        }
    var ellipsizedTextColor = Color.GRAY
        set(value) {
            if (value == field) return
            field = value
            isNeedCalculated = true
            requestLayout()
        }
    private var contentHeight = 1
    var ellipsizedText = "...See more"
        set(value) {
            if (value == field || value.isBlank()) return
            field = value
            isNeedCalculated = true
            requestLayout()
        }
    private var isNeedCalculated = true
    private lateinit var expandedStaticLayout: StaticLayout
    private lateinit var collapsedStaticLayout: StaticLayout
    private var hasViewWidth = false
    private var paddingTop = 0
    private var paddingBottom = 0
    private var paddingStart = 0
    private var paddingEnd = 0
    var lineToEllipsize = 1
        set(value) {
            if (value <= 0 || value == field) return
            field = value
            isNeedCalculated = true
            requestLayout()
        }

    init {
        setOnClickListener {
            if (isCollapsable) {
                isExpanding = !isExpanding
                requestLayout()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isNeedCalculated && hasViewWidth) {
            expandedStaticLayout = getStaticLayout(text)
            if (expandedStaticLayout.lineCount <= lineToEllipsize) {
                isCollapsable = false
                isExpanding = true
            } else {
                val ellipsizedPosition = getTextPositionToEllipsize()
                val collapsedString = text.substring(0, ellipsizedPosition) + ellipsizedText
                val collapsedStringWithSpan = getCollapsedStringWithSpan(collapsedString)
                collapsedStaticLayout = getStaticLayout(
                    spannableString = collapsedStringWithSpan,
                )
                isCollapsable = true
                isExpanding = false
            }
            isNeedCalculated = false
        }
        contentHeight = if (isExpanding) {
            if (::expandedStaticLayout.isInitialized) expandedStaticLayout.height else 1
        } else {
            if (::collapsedStaticLayout.isInitialized) collapsedStaticLayout.height else 1
        }
        setMeasuredDimension(
            widthMeasureSpec,
            contentHeight + paddingTop.dp.toInt() + paddingBottom.dp.toInt()
        )
    }

    fun setPaddings(all: Int) {
        setPaddings(all, all, all, all)
    }

    fun setPaddings(top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0) {
        paddingTop = top
        paddingBottom = bottom
        paddingStart = start
        paddingEnd = end
    }

    private fun getStaticLayout(
        string: String? = null,
        spannableString: SpannableString? = null,
    ): StaticLayout {
        val availableWidth = width - (paddingStart.dp + paddingEnd.dp).toInt()
        val str = string ?: spannableString
        return StaticLayout.Builder.obtain(
            str ?: "",
            0,
            str?.length ?: 0,
            textPaint,
            availableWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(true)
            .build()
    }

    private fun getCollapsedStringWithSpan(collapsedString: String) =
        SpannableString(collapsedString).apply {
            setSpan(
                ForegroundColorSpan(ellipsizedTextColor),
                collapsedString.length - ellipsizedText.length,
                collapsedString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!hasViewWidth || isNeedCalculated) {
            hasViewWidth = true
            requestLayout()
            return
        }
        val staticLayout = if (isExpanding) {
            expandedStaticLayout
        } else {
            collapsedStaticLayout
        }
        if (paddingStart != 0 || paddingTop != 0) {
            canvas.save()
            canvas.translate(paddingStart.dp, paddingTop.dp)
            staticLayout.draw(canvas)
            canvas.restore()
        } else {
            staticLayout.draw(canvas)
        }
    }

    private fun getTextPositionToEllipsize(): Int {
        if (text.isEmpty()) return 0
        var subStringEndPosition = 1
        while (getStaticLayout(
                text.substring(0, subStringEndPosition) + ellipsizedText,
            ).lineCount <= lineToEllipsize
        ) {
            subStringEndPosition++
        }
        return subStringEndPosition - 1
    }
}

val Int.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Int.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )