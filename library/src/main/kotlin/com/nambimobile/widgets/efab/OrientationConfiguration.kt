package com.nambimobile.widgets.efab

import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Holder for all the views of an ExpandableFab widget in a specific [Orientation].
 * Not meant to be instantiated by clients, as an instance can be retrieved via
 * [ExpandableFabLayout.portraitConfiguration] or [ExpandableFabLayout.landscapeConfiguration].
 * Values for the views will only be populated after they are added through calls to
 * ExpandableFabLayout's addView methods (or after they're defined via XML).
 *
 * Implementation Notes:
 * 1) Since the Kotlin 'internal' modifier translates to 'public' in Java, the [JvmSynthetic]
 * annotation is used on those functions and properties to hide them from the published API for
 * Java clients. A proper solution to this issue would be a package-private visibility modifier,
 * but Kotlin has yet to implement it (https://youtrack.jetbrains.com/issue/KT-29227). Until
 * then, the JvmSynthetic annotations should remain in order to present the proper published API
 * to both Java & Kotlin clients.
 *
 * @since 1.0.0
 * */
class OrientationConfiguration {
    /** The [Overlay] in this orientation. May be null. **/
    @set:JvmSynthetic
    var overlay: Overlay? = null
        internal set

    /** The [ExpandableFab] in this orientation. May be null. **/
    @set:JvmSynthetic
    var efab: ExpandableFab? = null
        internal set

    /**
     * The [FabOption]s (with optional labels) in this orientation. May be empty.
     *
     * To remove a FabOption from this orientation, simply call remove(FabOption) or remove(int)
     * (the latter is named removeAt(int) in Kotlin).
     * */
    @set:JvmSynthetic
    var fabOptions: MutableList<FabOption> = object : ArrayList<FabOption>() {
        override fun removeAt(index: Int): FabOption {
            return super.removeAt(index).also { setFabOptionAnchor(this[index], index) }
        }

        override fun remove(element: FabOption): Boolean {
            this.forEachIndexed { index, fabOption ->
                if(element == fabOption){
                    removeAt(index)
                    return true
                }
            }

            return false
        }
    }
        internal set

    /**
     * Sets the anchor of the given FabOption, allowing the list of FabOptions in this orientation
     * to stack correctly.
     * */
    @JvmSynthetic
    internal fun setFabOptionAnchor(fabOption: FabOption, index: Int){
        if(fabOptions.size > index){
            (fabOption.layoutParams as CoordinatorLayout.LayoutParams).let { fabParams ->
                when(index){
                    0 -> efab?.let { fabParams.anchorId = it.id }
                    else -> fabParams.anchorId = fabOptions[index - 1].id // previous fabOption
                }

                efab?.let { fabParams.anchorGravity = it.fabOptionPosition.value }
                fabOption.layoutParams = fabParams
            }
        }
    }
}