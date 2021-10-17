package com.example.code.extensions

import android.view.View

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show() : View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.hide() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun View.setVisible(visibility: Boolean) {
    if(visibility)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}

fun View.setVisibleOrInvisible(isVisible: Boolean) {
    if(isVisible)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.INVISIBLE
}
