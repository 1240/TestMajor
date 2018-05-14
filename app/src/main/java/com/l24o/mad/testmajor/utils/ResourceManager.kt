package com.l24o.mad.testmajor.utils

import android.support.annotation.StringRes
import com.l24o.mad.testmajor.TheApp

object ResourceManager {
    fun getString(@StringRes stringResId: Int): String {
        return TheApp.context.resources.getString(stringResId)
    }
}