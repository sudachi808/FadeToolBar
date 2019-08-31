package com.example.fadetoolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View
import androidx.core.graphics.ColorUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setSupportActionBar(this.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.window.statusBarColor = ColorUtils.setAlphaComponent(this.window.statusBarColor, 0)

        this.toolbar.setupWithScrollView(this.scrollView)
        this.toolbar.setOnAlphaChangeListener(object : FadeToolBar.OnAlphaChangeListener {
            override fun onAlphaChanged(alpha: Int) {
                (this@MainActivity.window).apply {
                    statusBarColor = ColorUtils.setAlphaComponent(statusBarColor, alpha)
                }
            }
        })

        //
        // ステータスバー部分もアプリで使用
        //
        findViewById<View>(android.R.id.content).apply {
            systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}
