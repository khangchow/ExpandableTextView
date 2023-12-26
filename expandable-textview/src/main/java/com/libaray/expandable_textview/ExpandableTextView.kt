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
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var isSingleLine = false
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
            ellipsizeTextWidth = getTextWidth(ellipsizedText)
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
    private var contentHeight = 0
    var ellipsizedText = "...See more"
        set(value) {
            if (value == field || value.isBlank()) return
            field = value
            isNeedCalculated = true
            invalidate()
        }
    private var ellipsizeTextWidth = getTextWidth(ellipsizedText)
    private var isRemeasured = false
    private var isNeedCalculated = true
    private lateinit var expandedStaticLayout: StaticLayout
    private lateinit var collapsedStaticLayout: StaticLayout

    init {
        setOnClickListener {
            if (!isSingleLine) {
                isExpanding = !isExpanding
                isRemeasured = false
                invalidate()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, contentHeight)
    }

    private fun getStaticLayout(text: String, boundWidth: Int) =
        StaticLayout.Builder.obtain(text, 0, text.length, textPaint, boundWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .setIncludePad(true)
            .build()

    private fun getCollapsedStaticLayoutWidth(collapsedString: String, layoutWidth: Int): Int {
        var staticLayout: StaticLayout
        var additionalWidth = 0
        while (getStaticLayout(collapsedString, layoutWidth + additionalWidth).run {
                staticLayout = this
                this.lineCount > 1
            }) {
            additionalWidth++
        }
        return staticLayout.width + additionalWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isNeedCalculated) {
            expandedStaticLayout = getStaticLayout(text, width)
            if (expandedStaticLayout.lineCount <= 1) {
                isSingleLine = true
                isExpanding = true
            } else {
                val ellipsizedPosition = getTextPositionToEllipsize(width)
                val collapsedString = text.substring(0, ellipsizedPosition) + ellipsizedText
                collapsedStaticLayout = getStaticLayout(
                    collapsedString,
                    getCollapsedStaticLayoutWidth(collapsedString, width)
                )
                isSingleLine = false
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

    private fun getTextWidth(text: String): Int {
        val contentBound = Rect()
        textPaint.getTextBounds(text, 0, text.length, contentBound)
        return contentBound.width()
    }

    private fun getTextPositionToEllipsize(layoutWidth: Int): Int {
        if (text.isEmpty()) return 0
        var subString = ""
        var subStringEndPosition = 0
        while (getTextWidth(subString) + ellipsizeTextWidth < layoutWidth) {
            val endPosition = ++subStringEndPosition
            if (endPosition > text.length) break
            subString = text.substring(0, endPosition)
        }
        return subStringEndPosition - 2
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
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)

val Int.sp: Float
    get() = (this * Resources.getSystem().displayMetrics.scaledDensity)