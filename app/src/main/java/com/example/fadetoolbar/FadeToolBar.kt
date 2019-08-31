package com.example.fadetoolbar

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import androidx.core.view.children

class FadeToolBar : Toolbar, ListenableScrollView.OnScrollChangeListener {

    // region constructors

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.retrieveAttributes(attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.retrieveAttributes(attrs)
    }

    init {
        this.background.alpha = 0
    }

    // endregion

    interface OnAlphaChangeListener {
        fun onAlphaChanged(alpha: Int)
    }
    private var onAlphaChangeListener: OnAlphaChangeListener? = null

    private var scrollView: ListenableScrollView? = null

    private var titleTextColorStart: Int = DEFAULT_TINT_START
    private var titleTextColorEnd: Int = DEFAULT_TINT_END

    private var iconColorStart: Int = DEFAULT_TINT_START
    private var iconColorEnd: Int = DEFAULT_TINT_END

    companion object Constants {
        private const val DEFAULT_TINT_START: Int = Color.WHITE
        private const val DEFAULT_TINT_END: Int = Color.DKGRAY
    }

    private fun retrieveAttributes(attrs: AttributeSet) {
        val a = this.context.theme.obtainStyledAttributes(attrs, R.styleable.FadeToolBar, 0, 0)
        try {
            this.iconColorStart = a.getColor(R.styleable.FadeToolBar_iconColorStart, DEFAULT_TINT_START)
            this.iconColorEnd = a.getColor(R.styleable.FadeToolBar_iconColorEnd, DEFAULT_TINT_END)
            this.titleTextColorStart = a.getColor(R.styleable.FadeToolBar_titleTextColorStart, DEFAULT_TINT_START)
            this.titleTextColorEnd = a.getColor(R.styleable.FadeToolBar_titleTextColorEnd, DEFAULT_TINT_END)
        }
        finally {
            a.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.setIconColor(this.iconColorStart)
        this.setTitleTextColor(this.titleTextColorStart)
    }

    fun setupWithScrollView(scrollView: ListenableScrollView) {
        this.scrollView = scrollView
        this.scrollView?.setOnScrollChangeListener(this)
    }

    private fun setIconColor(color: Int) {
        this.children.filter { it is ImageButton }.map { it as ImageButton }.forEach {
            it.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    // region ListenableScrollView.OnScrollChangeListener

    override fun onScrollChanged(scrollY: Int, oldScrollY: Int) {

        val ratio = (scrollY.toFloat() / (this.height.toFloat() * 2)).coerceAtMost(1.0f)
        val alpha = (ratio * 255).toInt().coerceAtMost(255)

        this.background.alpha = alpha
        this.onAlphaChangeListener?.onAlphaChanged(alpha)

        val iconColor = ColorUtils.blendARGB(this.iconColorStart, this.iconColorEnd, ratio)
        val titleTextColor = ColorUtils.blendARGB(this.titleTextColorStart, this.titleTextColorEnd, ratio)

        this.setIconColor(iconColor)
        this.setTitleTextColor(titleTextColor)
    }

    // endregion

    fun setOnAlphaChangeListener(l: OnAlphaChangeListener) {
        this.onAlphaChangeListener = l
    }
}

class ListenableScrollView : ScrollView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var onScrollChangeListener: OnScrollChangeListener? = null

    interface OnScrollChangeListener {
        fun onScrollChanged(scrollY: Int, oldScrollY: Int)
    }

    fun setOnScrollChangeListener(l: OnScrollChangeListener?) {
        this.onScrollChangeListener = l
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        this.onScrollChangeListener?.onScrollChanged(t, oldt)
    }
}
