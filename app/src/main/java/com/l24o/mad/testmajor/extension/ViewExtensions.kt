package com.l24o.mad.testmajor.extension

import android.view.View

fun View.makeVisibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}