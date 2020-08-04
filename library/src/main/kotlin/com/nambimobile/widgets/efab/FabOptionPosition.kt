package com.nambimobile.widgets.efab

import android.view.Gravity

/**
 * The legal positions of FabOptions, in relation to the previous FabOption in an orientation.
 * By default, FabOptions are positioned [ABOVE] the previous FabOption in an orientation.
 *
 * @since 1.0.0
 * */
enum class FabOptionPosition(val value: Int) {
    ABOVE(Gravity.TOP.or(Gravity.CENTER_HORIZONTAL)),
    BELOW(Gravity.BOTTOM.or(Gravity.CENTER_HORIZONTAL))
}