package com.nambimobile.widgets.efab

import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * The legal sizes of ExpandableFabs and FabOptions.
 *
 * [FabSize.MINI], [FabSize.NORMAL] and [FabSize.AUTO] map to [FloatingActionButton.SIZE_MINI],
 * [FloatingActionButton.SIZE_NORMAL] and [FloatingActionButton.SIZE_AUTO], respectively.
 * [FabSize.CUSTOM] should be used when setting the size of the ExpandableFab or FabOption's
 * layout_width and layout_height to a custom value manually, and thus maps to a random value
 * that will never be used.
 *
 * Developer Notes:
 * 1) When using [CUSTOM], the Android Framework also requires you to set app:fabCustomSize
 * equal to your custom layout_width/layout_height size and app:maxImageSize equal to your icon
 * size in order to ensure your icon is centered and sized properly.
 *
 * Example:
 *
   <com.nambimobile.widgets.efab.ExpandableFab
    android:id="@+id/expandable_fab"
    android:layout_width="175dp"
    android:layout_height="175dp"
    app:efab_size="custom"
    app:efab_fabOptionSize="custom"
    app:efab_icon="@drawable/ic_random11_white_84dp"
    app:fabCustomSize="175dp"   <!-- Required by Android Framework, equal to layout_width/layout_height  -->
    app:maxImageSize="84dp"     <!-- Required by Android Framework, equal to efab_icon/fab_icon-->
    />
 *
 * @since 1.0.0
 * */
enum class FabSize(val value: Int) {
    MINI(FloatingActionButton.SIZE_MINI),
    NORMAL(FloatingActionButton.SIZE_NORMAL),
    AUTO(FloatingActionButton.SIZE_AUTO),
    CUSTOM(-1234)
}