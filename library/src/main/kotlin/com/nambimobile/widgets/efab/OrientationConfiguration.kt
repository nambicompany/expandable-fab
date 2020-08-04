package com.nambimobile.widgets.efab

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

    /** The [FabOption]s (with optional labels) in this orientation. May be empty. **/
    @set:JvmSynthetic
    var fabOptions: MutableList<FabOption> = mutableListOf()
        internal set
}