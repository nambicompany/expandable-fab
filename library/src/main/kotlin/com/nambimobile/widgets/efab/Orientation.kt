package com.nambimobile.widgets.efab

/**
 * The screen orientations that the views of this widget can be in. If you only set a single
 * ExpandableFab widget, it will automatically be used for both portrait and landscape orientations.
 *
 * For example: defining an Overlay, ExpandableFab and 3 FabOptions all to be in
 * Orientation.PORTRAIT, while also *NOT* defining anything else to be in Orientation.LANDSCAPE,
 * will ensure that the previously defined Overlay, ExpandableFab and 3 FabOptions will actually
 * show in both portrait AND landscape configurations. See [ExpandableFabLayout] for more details.
 *
 * @since 1.0.0
 * */
enum class Orientation {
    PORTRAIT,
    LANDSCAPE
}