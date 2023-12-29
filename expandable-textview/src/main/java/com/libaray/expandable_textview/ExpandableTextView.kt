package com.libaray.expandable_textview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.*
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
            invalidate()
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
            invalidate()
        }
    var textColor = Color.BLACK
        set(value) {
            if (value == field) return
            field = value
            textPaint.color = value
            isNeedCalculated = true
            invalidate()
        }
    var ellipsizedTextColor = Color.GRAY
        set(value) {
            if (value == field) return
            field = value
            isNeedCalculated = true
            invalidate()
        }
    private var contentHeight = 0
    var ellipsizedText = "...See more"
        set(value) {
            if (value == field || value.isBlank()) return
            field = value
            isNeedCalculated = true
            invalidate()
        }
    private var isRemeasured = false
    private var isNeedCalculated = true
    private lateinit var expandedStaticLayout: StaticLayout
    private lateinit var collapsedStaticLayout: StaticLayout
    var lineToEllipsize = 1
        set(value) {
            if (value <= 0 || value == field) return
            field = value
            isNeedCalculated = true
            invalidate()
        }

    init {
        setOnClickListener {
            if (isCollapsable) {
                isExpanding = !isExpanding
                isRemeasured = false
                invalidate()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, contentHeight)
    }

    private fun getStaticLayout(
        string: String? = null,
        spannableString: SpannableString? = null,
        boundWidth: Int
    ): StaticLayout {
        val text = string ?: spannableString
        return StaticLayout.Builder.obtain(text ?: "", 0, text?.length ?: 0, textPaint, boundWidth)
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
        if (isNeedCalculated) {
            expandedStaticLayout = getStaticLayout(text, boundWidth = width)
            if (expandedStaticLayout.lineCount <= lineToEllipsize) {
                isCollapsable = false
                isExpanding = true
            } else {
                val ellipsizedPosition = getTextPositionToEllipsize(width)
                val collapsedString = text.substring(0, ellipsizedPosition) + ellipsizedText
                val collapsedStringWithSpan = getCollapsedStringWithSpan(collapsedString)
                collapsedStaticLayout = getStaticLayout(
                    spannableString = collapsedStringWithSpan,
                    boundWidth = width
                )
                isCollapsable = true
                isExpanding = false
            }
            isNeedCalculated = false
        }
        if (isExpanding) {
            contentHeight = expandedStaticLayout.height
            expandedStaticLayout.draw(canvas)
        } else {
            contentHeight = collapsedStaticLayout.height
            collapsedStaticLayout.draw(canvas)
        }
        isRemeasured = if (isRemeasured) {
            false
        } else {
            requestLayout()
            true
        }
    }

    private fun getTextPositionToEllipsize(layoutWidth: Int): Int {
        if (text.isEmpty()) return 0
        var subStringEndPosition = 1
        while (getStaticLayout(
                text.substring(0, subStringEndPosition) + ellipsizedText,
                boundWidth = layoutWidth
            ).lineCount <= lineToEllipsize
        ) {
            subStringEndPosition++
        }
        return subStringEndPosition - 1
    }

    override fun setBackground(background: Drawable?) {

    }

    override fun setBackgroundResource(resid: Int) {

    }

    override fun setBackgroundColor(color: Int) {

    }

    override fun setBackgroundTintBlendMode(blendMode: BlendMode?) {

    }

    override fun setBackgroundTintList(tint: ColorStateList?) {

    }

    override fun setBackgroundTintMode(tintMode: PorterDuff.Mode?) {

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