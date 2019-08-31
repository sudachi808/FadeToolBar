package com.example.fadetoolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setSupportActionBar(this.toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.toolbar.setupWithScrollView(this.scrollView)
    }
}
