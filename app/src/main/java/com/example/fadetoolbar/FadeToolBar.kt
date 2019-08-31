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

    private var scrollView: ListenableScrollView? = null

    private var tintColorStart: Int = Color.WHITE
    private var tintColorEnd: Int = Color.DKGRAY

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

    private fun retrieveAttributes(attrs: AttributeSet) {
        val a = this.context.theme.obtainStyledAttributes(attrs, R.styleable.FadeToolBar, 0, 0)
        try {
            this.tintColorStart = a.getColor(R.styleable.FadeToolBar_tintColorStart, Color.WHITE)
            this.tintColorEnd = a.getColor(R.styleable.FadeToolBar_tintColorEnd, Color.DKGRAY)
        }
        finally {
            a.recycle()
        }
    }

    // endregion

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        this.setTintColor(this.tintColorStart)
    }

    fun setupWithScrollView(scrollView: ListenableScrollView) {
        this.scrollView = scrollView
        this.scrollView?.setOnScrollChangeListener(this)
    }

    fun setTintColor(color: Int) {
        this.setTitleTextColor(color)
        this.setIconColor(color)
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

        val color = ColorUtils.blendARGB(this.tintColorStart, this.tintColorEnd, ratio)
        this.setTintColor(color)
    }

    // endregion
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
