package com.nambimobile.widgets.efab

import android.content.Context
import android.os.Build
import android.util.TypedValue
import androidx.core.content.ContextCompat

/**
 * Attempts to get the colorAccent (or colorSecondary for MaterialComponents based themes, which
 * colorAccent is mapped to) defined in the app's current theme.
 *
 * If such a resource does not exist (or we somehow fail to get it), we default to either the
 * colorAccent defined in the app's colors.xml, or the library's locally defined colorAccent
 * (which is just black, #000000) if one is not defined at the app level.
 * */
@JvmSynthetic
internal fun getThemeColorAccent(context: Context): Int {
    val themeColorAccent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // colorAccent resourceId for SDK >= 21
            android.R.attr.colorAccent
        } else {
            // colorAccent resourceId for SDK < 21
            context.resources.getIdentifier("colorAccent", "attr", context.packageName)
        }

    val themeColorAccentValue = TypedValue()
    return if(context.theme.resolveAttribute(themeColorAccent, themeColorAccentValue, true)){
        // A colorAccent was defined in the theme, so we use its color value
        themeColorAccentValue.data
    } else {
        // No colorAccent defined in theme, use app's or library's colorAccent instead
        ContextCompat.getColor(context, R.color.colorAccent)
    }
}