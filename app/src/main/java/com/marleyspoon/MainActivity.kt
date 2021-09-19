package com.marleyspoon

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.marleyspoon.listing.ui.detail.HasToolbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), HasToolbar {

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val appBar by lazy { findViewById<View>(R.id.appbar) as AppBarLayout }
    private var originalColor: Int = 0

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        originalColor = window.statusBarColor
    }

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun restore() {
        appBar.setExpanded(true, true)
    }

    override fun setColor(color: Int) {
        toolbar.setBackgroundColor(color)
    }

    override fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
    }

    override fun restoreStatusBarColor() {
        window.statusBarColor = originalColor
    }
}