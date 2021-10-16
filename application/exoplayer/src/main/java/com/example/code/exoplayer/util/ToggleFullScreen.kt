package com.example.code.exoplayer.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity

class ToggleFullScreen(
    val activity: FragmentActivity?,
    val view: View?
) {

    @SuppressLint("InlinedApi")
    fun toggleSystemUI(isFullScreen:Boolean=true) {
        activity?.let { it ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Tell the window that we want to handle/fit any system windows
                WindowCompat.setDecorFitsSystemWindows(it.window, false)

                val controller = view?.windowInsetsController

                // Hide the keyboard (IME)
                controller?.hide(WindowInsets.Type.ime())

                // Sticky Immersive is now ...
                controller?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                // When we want to hide the system bars
                controller?.hide(WindowInsets.Type.systemBars())

                /*val flag = WindowInsets.Type.statusBars()
                WindowInsets.Type.navigationBars()
                WindowInsets.Type.captionBar()
                window?.insetsController?.hide(flag)*/
            } else {
                //noinspection
                @Suppress("DEPRECATION")
                // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
                it.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
        }
    }

}