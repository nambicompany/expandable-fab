package com.nambimobile.widgets.efab

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams

/**
 * The container and controller for all children views of the ExpandableFab widget (Overlay,
 * ExpandableFab, FabOption, Label). The ExpandableFabLayout handles the bulk of the functionality
 * for the ExpandableFab widget as a whole (from coordinating opening and closing animations to
 * screen orientation changes, etc). See below for an example on how to easily set up the
 * ExpandableFab widget using the ExpandableFabLayout as the containing ViewGroup.
 *
 * Protip: If you only set a single ExpandableFab widget, it will automatically be used for both
 * portrait and landscape orientations. No need to set duplicate views if you would like the same
 * widget in both orientations. If you would like two different widgets for portrait and
 * landscape however, you can do so by explicitly defining different orientations for the
 * different sets of widget views. In the example below we don't set orientation for any of the
 * views, so they default to 'portrait'. However, since we didn't explicitly set landscape views,
 * the widget will actually be used for both portrait *and* landscape.
 *
 * ExpandableFab widget Example via XML:

    <com.nambimobile.widgets.efab.ExpandableFabLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.nambimobile.widgets.efab.Overlay
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

        <com.nambimobile.widgets.efab.ExpandableFab
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|end"
            android:layout_marginBottom="@dimen/ui_margin_medium"
            android:layout_marginEnd="@dimen/ui_margin_medium"
            android:layout_marginRight="@dimen/ui_margin_medium"/>

        <com.nambimobile.widgets.efab.FabOption
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>

    </com.nambimobile.widgets.efab.ExpandableFabLayout>
 *
 * Developer Notes:
 * 1) The ExpandableFabLayout should be given a layout_width and layout_height of match_parent,
 * and it should be a child of a ViewGroup that has access to draw over the full screen as well.
 * This is necessary as some views of the widget (like the [Overlay]) may need the ability to draw
 * over the full screen. Setting the dimensions as such will not impede the viewability,
 * clickability or focusability of any other views in your layout.
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
class ExpandableFabLayout : CoordinatorLayout {
    /**
     * A holder for all the views of the ExpandableFab widget declared in the portrait screen
     * orientation. Values for the views will only be populated after they are added through calls
     * to ExpandableFabLayout's addView methods (or after they're defined via XML).
     * */
    var portraitConfiguration = OrientationConfiguration()
        private set

    /**
     * A holder for all the views of the ExpandableFab widget declared in the landscape screen
     * orientation. Values for the views will only be populated after they are added through calls
     * to ExpandableFabLayout's addView methods (or after they're defined via XML).
     * */
    var landscapeConfiguration = OrientationConfiguration()
        private set

    @get:JvmSynthetic
    @set:JvmSynthetic
    internal var efabAnimationsFinished = true
        get // Redundant declarations, but must be defined for JvmSynthetic to hide from Java clients
        set
    private var groupAnimationsFinished = true
    private var open = false
    private var closeWhenAble = false
    private var fabOptionAlreadyClicked = false

    /**
     * Used to create an ExpandableFabLayout programmatically (do not use the other constructor
     * ExpandableFabLayout(context, attributeSet) - it is for use by the Android framework when
     * inflating an ExpandableFabLayout via XML).
     * */
    constructor(context: Context): super(context)

    /**
     * Called by the system when creating an ExpandableFabLayout via XML (don't call this directly).
     * To create an ExpandableFabLayout programmatically, use the ExpandableFabLayout(context)
     * constructor.
     * */
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    init {
        if (id == View.NO_ID){
            id = ViewCompat.generateViewId()
        }
    }


    // ******************* Public methods available through the published API ******************* \\
    /**
     * Adds a child view with the specified layout parameters to the ExpandableFabLayout.
     *
     * In general, only Overlay, ExpandableFab, FabOption, and specific Material Design views
     * (like Snackbar and BottomAppBar) should be added as children of the ExpandableFabLayout.
     *
     * While this library won't stop you from adding in other types as direct children to the
     * ExpandableFabLayout, please know adding other View types may cause visual issues.
     * */
    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        when(child){
            is Overlay -> addOverlay(child, index, params)
            is ExpandableFab -> addExpandableFab(child, index, params)
            is FabOption -> addFabOption(child, index, params)
            else -> super.addView(child, index, params)
        }
    }

    /**
     * Convenience method for adding multiple [children] views to the ExpandableFabLayout at once,
     * programmatically. Ensure your children views are of type Overlay, ExpandableFab or FabOption
     * only.
     * */
    fun addViews(vararg children: View?){
        for (child in children){
            addView(child)
        }
    }

    /**
     * Removes all child views in both portrait and landscape orientation from the
     * ExpandableFabLayout.
     *
     * This is the only correct way to remove *ALL* views from your ExpandableFab widget should you
     * choose to reuse the same ExpandableFabLayout instead of instantiating a new one. Using any
     * other removeView variation will provide no guarantees of proper internal state control,
     * and thus could potentially lead to Exceptions during runtime.
     *
     * If you only want to remove certain FabOptions, get the current OrientationConfiguration
     * ([portraitConfiguration] or [landscapeConfiguration] or [getCurrentConfiguration]), then
     * call remove(FabOption) or remove(int) (the latter is named removeAt(int) in Kotlin) on its
     * list of FabOptions.
     * */
    override fun removeAllViews() {
        super.removeAllViews()

        portraitConfiguration = OrientationConfiguration()
        landscapeConfiguration = OrientationConfiguration()
        efabAnimationsFinished = true
        groupAnimationsFinished = true
        open = false
        closeWhenAble = false
        fabOptionAlreadyClicked = false
    }

    /**
     * Attempts to close the ExpandableFab, playing the appropriate animations in the process. If
     * the ExpandableFab is not in a position to close (it's still playing its opening
     * animations), it will remind itself to close once it is able.
     *
     * Safe to call even when no ExpandableFab is open (will do nothing).
     * */
    fun close(){
        if(!animationsFinished()){
            closeWhenAble = true
            return
        } else if(!open){
            return
        }

        groupAnimationsFinished = false

        getClosingAnimations(getCurrentConfiguration()).apply { start() }
    }

    /**
     * Returns true if the ExpandableFab is currently open (any attached Overlay, FabOption and
     * Labels are visible and all animations are done).
     * */
    fun isOpen(): Boolean {
        return open
    }

    /**
     * Returns the OrientationConfiguration showing for the current screen orientation.
     * An OrientationConfiguration is just a holder for all the views of an ExpandableFab widget
     * in a specific [Orientation].
     *
     * Should only be called after you have added the views to an ExpandableFabLayout
     * programmatically via the addView methods, or declared them within an ExpandableFabLayout
     * via XML. Otherwise, the views contained may be null for references or empty for lists.
     *
     * Although the [portraitConfiguration] and [landscapeConfiguration] properties are both exposed
     * publicly for clients to obtain directly, this method allows you to retrieve the correct
     * set of views without first knowing what screen orientation the device is currently in.
     *
     * Note: This method will automatically take into account orientations with no configuration
     * set. That is, if you only set a portrait configuration, then this method will pass back
     * that configuration even if the device is currently in landscape. You don't have to worry
     * about being given an OrientationConfiguration for an orientation that you did not set.
     * */
    fun getCurrentConfiguration(): OrientationConfiguration {
        return when(resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT ->
                if(portraitConfiguration.efab != null) portraitConfiguration else landscapeConfiguration
            else -> if(landscapeConfiguration.efab != null) landscapeConfiguration else portraitConfiguration
        }
    }


    // ****************** Private / Internal methods not available to clients ******************* \\
    private fun addOverlay(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?
    ){
        super.addView(child, index, params)
        val overlay = (child as Overlay).also { it.defaultOnClickBehavior = ::defaultOverlayOnClickBehavior }

        when(overlay.orientation){
            Orientation.PORTRAIT -> portraitConfiguration.overlay = overlay
            Orientation.LANDSCAPE -> landscapeConfiguration.overlay = overlay
        }
    }

    private fun addExpandableFab(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?
    ){
        super.addView(child, index, params)
        val efab = (child as ExpandableFab).also {
            it.defaultOnClickBehavior = ::defaultExpandableFabOnClickBehavior
            it.onAnimationStart = { efabAnimationsFinished = false }
        }

        // anchoring the label to its ExpandableFab and adding it to the ExpandableFabLayout
        efab.label.let { label ->
            addView(label)

            label.updateLayoutParams<LayoutParams> {
                anchorId = efab.id
            }

            label.showLabel()
        }

        when(efab.orientation){
            Orientation.PORTRAIT -> {
                if(portraitConfiguration.efab != null){
                    illegalState(resources.getString(R.string.efab_layout_multiple_efabs, efab.orientation))
                }

                portraitConfiguration.efab = efab
                efab.show()

                // This portrait efab will show if we're in portrait orientation OR if we're in
                // landscape, but no landscape efab has been set.
                if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                    landscapeConfiguration.efab?.hide()
                } else if(landscapeConfiguration.efab != null) {
                    efab.hide()
                }
            }

            Orientation.LANDSCAPE -> {
                if(landscapeConfiguration.efab != null){
                    illegalState(resources.getString(R.string.efab_layout_multiple_efabs, efab.orientation))
                }

                landscapeConfiguration.efab = efab
                efab.show()

                // This landscape efab will show if we're in landscape orientation OR if we're in
                // portrait, but no portrait efab has been set.
                if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                    portraitConfiguration.efab?.hide()
                } else if(portraitConfiguration.efab != null) {
                    efab.hide()
                }
            }
        }
    }

    private fun addFabOption(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?
    ){
        super.addView(child, index, params)
        val fabOption = (child as FabOption).also { it.defaultOnClickBehavior = ::defaultFabOptionOnClickBehavior }
        val configuration = when(fabOption.orientation){
            Orientation.PORTRAIT -> portraitConfiguration
            Orientation.LANDSCAPE -> landscapeConfiguration
        }

        // anchoring the label to its fabOption and adding it to the ExpandableFabLayout
        fabOption.label.let { label ->
            addView(label)

            label.updateLayoutParams<LayoutParams> {
                anchorId = fabOption.id
            }
        }

        configuration.fabOptions.add(fabOption)
        configuration.setFabOptionAnchor(fabOption, configuration.fabOptions.lastIndex)
    }

    private fun defaultOverlayOnClickBehavior() {
        close()
    }

    private fun defaultExpandableFabOnClickBehavior(){
        if(open || !animationsFinished()){
            close()
        } else {
            open()
        }
    }

    /**
     * Returns whether the FabOption should run its custom onClick listener (only when no other
     * FabOption has been clicked during the currently playing opening/closing animations).
     * */
    private fun defaultFabOptionOnClickBehavior(): Boolean {
        return if(fabOptionAlreadyClicked){
            false
        } else {
            fabOptionAlreadyClicked = true
            close()

            true
        }
    }

    private fun open(){
        if(open){
            return
        }

        groupAnimationsFinished = false

        getOpeningAnimations(getCurrentConfiguration()).apply { start() }
    }

    private fun getOpeningAnimations(configuration: OrientationConfiguration): Animator {
        val efab = configuration.efab as ExpandableFab
        val optionAndLabelAnimationPairs = configuration.fabOptions.mapIndexed { index, fabOption ->
            fabOption.openingAnimations(
                index,
                efab.fabOptionSize,
                efab.fabOptionPosition,
                efab.firstFabOptionMarginPx,
                efab.successiveFabOptionMarginPx
            )
        }

        return AnimatorSet().apply {
            playTogether(
                configuration.overlay?.openingAnimations() ?: AnimatorSet(),
                efab.openingAnimations {
                    efabAnimationsFinished = true
                    setState(true)
                },
                AnimatorSet().apply { playSequentially(optionAndLabelAnimationPairs) }
            )

            addListener(onEnd = {
                groupAnimationsFinished = true
                setState(true)
            })
        }
    }

    private fun getClosingAnimations(configuration: OrientationConfiguration): Animator {
        val efab = configuration.efab as ExpandableFab
        val optionAndLabelAnimationPairs = configuration.fabOptions.map {
            it.closingAnimations()
        }

        return AnimatorSet().apply {
            playTogether(
                configuration.overlay?.closingAnimations() ?: AnimatorSet(),
                efab.closingAnimations {
                    efabAnimationsFinished = true
                    setState(false)
                },
                AnimatorSet().apply { playSequentially(optionAndLabelAnimationPairs.reversed()) }
            )

            addListener(onEnd = {
                groupAnimationsFinished = true
                setState(false)
            })
        }
    }

    /**
     * Tells us whether all animations are finished. [ExpandableFab] is unique in that it
     * performs its animations manually, without use of an Animator (unlike the other views
     * within this widget). So the ExpandableFabLayout essentially has to keep track of two
     * separate sets of animations: 1) manual animations on the currently shown ExpandableFab,
     * and 2) Animator animations of the Overlay, FabOption and all Labels, grouped together.
     *
     * This will only return true if BOTH sets of animations are finished running.
     * */
    private fun animationsFinished(): Boolean {
        return efabAnimationsFinished && groupAnimationsFinished
    }

    /**
     * Sets the state necessary to show that the ExpandableFab is now fully opened (opening
     * animations are finished playing and all views of the widget are visible) or fully closed
     * (closing animations are finished playing and all views of the widget are hidden).
     *
     * As stated in [animationsFinished], we have to track two separate sets of animations
     * because the animations performed on the [ExpandableFab] are unique and don't use an Animator
     * like the other widget views.
     *
     * The two sets of animations are:
     * 1) Manual animations on the currently shown ExpandableFab, and
     * 2) [Animator] animations of the Overlay, FabOption and all Labels, grouped together).
     *
     * Both sets of animations will call this method when their respective animations have
     * finished playing. However, this method will only actually set the state of the widget as
     * opened or closed when it can determine both sets of animations have finished. In other
     * words, only the call from the longer (duration wise) of the two animations will actually
     * trigger the state to change. So seeing this called twice per call to open/close is expected.
     * */
    private fun setState(opened: Boolean){
        if(animationsFinished()){
            if(opened){
                open = true

                if(closeWhenAble){
                    close()
                }
            } else {
                open = false
                closeWhenAble = false
                fabOptionAlreadyClicked = false
            }
        }
    }
}