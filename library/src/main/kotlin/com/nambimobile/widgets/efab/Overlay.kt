package com.nambimobile.widgets.efab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

/**
 * An overlay that will show when an [ExpandableFab] is clicked in order to partially or fully
 * obscure the screen's content, allowing the [ExpandableFab] and [FabOption]s to become more
 * apparent.
 *
 * Developer Notes:
 * 1) If you would like the Overlay to cover the entire screen, layout_width and layout_height must
 * both be set to match_parent (assuming the parent ExpandableFabLayout has no size restrictions).
 * 2) Overlays must be defined within an [ExpandableFabLayout] to function properly.
 * 3) To ensure that Overlays cover screen content but NOT the ExpandableFab widget itself,
 * ensure the Overlay is the first child of ExpandableFabLayout via XML, or the first view passed
 * to ExpandableFabLayout's addView/addViews methods via code.
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
class Overlay : FrameLayout {

    /** The [Orientation] this Overlay is viewable in. Default value is [Orientation.PORTRAIT]. **/
    var orientation = Orientation.PORTRAIT
        private set

    /**
     * The color of the overlay. Default value is white.
     *
     * Usage of this property is preferred over the inherited setBackgroundColor method.
     * */
    var overlayColor = ContextCompat.getColor(context, android.R.color.white)
        set(value) {
            this.setBackgroundColor(value)
            field = value
        }

    /**
     * The opacity of the overlay as a positive float, where 0 is completely transparent and 1 is
     * completely opaque. Default value is 0.78431f.
     *
     * Usage of this property is preferred over the inherited set/getAlpha methods.
     * */
    var overlayAlpha = 0.78431f
        set(value) {
            this.alpha = value
            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the animations that will be played
     * when this Overlay is being shown from a hidden state (when the ExpandableFab is opening).
     * Set to 0L if you don't want opening animations played. Default value is 300L.
     * */
    var openingAnimationDurationMs = 300L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_overlay_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the animations that will be played
     * when this Overlay is being hidden from a visible state (when the ExpandableFab is closing).
     * Set to 0L if you don't want closing animations played. Default value is 300L.
     * */
    var closingAnimationDurationMs = 300L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_overlay_illegal_optional_properties))
            }

            field = value
        }

    /** Default onClick functionality. Set by the parent layout. Not to be used by clients. **/
    @get:JvmSynthetic
    @set:JvmSynthetic
    internal var defaultOnClickBehavior: (() -> Unit)? = null
        get() {
            if(field == null){
                illegalState(resources.getString(R.string.efab_layout_must_be_child_of_expandablefab_layout))
            }

            return field
        }
        set // Redundant declaration, but must be defined for JvmSynthetic to hide from Java clients

    // Declared as a property so we don't create a new one each animation... slight waste reduction?
    private val hideOnAnimationEnd = object : AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            this@Overlay.visibility = View.GONE
        }
    }


    /**
     * Used to create an Overlay programmatically (do not use the other constructor Overlay(context,
     * attributeSet) - it is for use by the Android framework when inflating an Overlay via XML).
     * This constructor keeps all optional properties of an Overlay at their default values,
     * though you can always change these values after instantiation by using the appropriate
     * setter methods (with the exception of [orientation], which cannot be changed after
     * instantiation).
     *
     * See documentation for an exhaustive list of all optional properties and their default values.
     *
     * Please review the Notes documented at the top of the class for guidelines and limitations
     * when using Overlay.
     * */
    constructor(context: Context, orientation: Orientation? = Orientation.PORTRAIT): super(context){
        setOptionalProperties(orientation = orientation ?: this.orientation)
    }

    /**
     * Called by the system when creating an Overlay via XML (don't call this directly).
     * To create an Overlay programmatically, use the Overlay(context, orientation) constructor.
     * */
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.Overlay, 0, 0).apply {
            try {
                val orientationIndex = getInt(R.styleable.Overlay_overlay_orientation, Orientation.PORTRAIT.ordinal)
                val openingDuration = getString(R.styleable.Overlay_overlay_openingAnimationDurationMs)?.toLong()
                    ?: openingAnimationDurationMs
                val closingDuration = getString(R.styleable.Overlay_overlay_closingAnimationDurationMs)?.toLong()
                    ?: closingAnimationDurationMs

                setOptionalProperties(
                    orientation = Orientation.values()[orientationIndex],
                    overlayColor = getColor(R.styleable.Overlay_overlay_color, overlayColor),
                    overlayAlpha = getFloat(R.styleable.Overlay_overlay_alpha, overlayAlpha),
                    openingAnimationDurationMs = openingDuration,
                    closingAnimationDurationMs = closingDuration
                )
            } catch (e: Exception) {
                illegalArg(resources.getString(R.string.efab_overlay_illegal_optional_properties), e)
            } finally {
                recycle()
            }
        }
    }

    init {
        if (id == View.NO_ID){
            id = ViewCompat.generateViewId()
        }

        visibility = View.GONE
    }


    // ******************* Public methods available through the published API ******************* \\
    /**
     * Registers a callback to be invoked when this Overlay is clicked. The default behavior the
     * Overlay will be executed before this custom callback.
     * */
    override fun setOnClickListener(onClickListener: OnClickListener?) {
        super.setOnClickListener {
            defaultOnClickBehavior?.invoke()
            onClickListener?.onClick(it)
        }
    }


    // ****************** Private / Internal methods not available to clients ******************* \\
    /**
     * Sets the optional properties of this Overlay.
     *
     * Kotlin does not use the declared custom setter of properties when setting their default
     * values. This is unfortunate, as our custom setters have logic that need to be executed
     * when the property is set. So we must assign each property manually to ensure the custom
     * setters are executed, even if we're just setting it with the already set default value.
     * */
    private fun setOptionalProperties(
        orientation: Orientation,
        overlayColor: Int = this.overlayColor,
        overlayAlpha: Float = this.overlayAlpha,
        openingAnimationDurationMs: Long = this.openingAnimationDurationMs,
        closingAnimationDurationMs: Long = this.closingAnimationDurationMs
    ){
        this.orientation = orientation
        this.overlayAlpha = overlayAlpha
        this.overlayColor = overlayColor
        this.openingAnimationDurationMs = openingAnimationDurationMs
        this.closingAnimationDurationMs = closingAnimationDurationMs

        if(!hasOnClickListeners()){
            // setOnClickListener has some logic we need to set for default functionality, but we
            // only want to call it when this view doesn't have an existing onClickListener
            // (meaning when the onClick attribute was not set via XML, which is the only way for
            // this view to have an onClickListener already).
            setOnClickListener(null)
        }
    }

    /**
     * The set of animations to play when the Overlay is being shown from a hidden state (when the
     * ExpandableFab is opening).
     *
     * @param globalDurationMs the global Overlay opening animation duration that a client set on
     * the ExpandableFabLayout. If set, this value takes precedence over the local
     * [openingAnimationDurationMs].
     * */
    @JvmSynthetic
    internal fun openingAnimations(globalDurationMs: Long?): Animator {
        this.alpha = 0f
        this.visibility = View.VISIBLE

        return ObjectAnimator.ofFloat(this, "alpha", 0f,  overlayAlpha).apply {
            this.duration = globalDurationMs ?: openingAnimationDurationMs
        }
    }

    /**
     * The set of animations to play when the Overlay is being hidden from a visible state (when
     * the ExpandableFab is closing).
     *
     * @param globalDurationMs the global Overlay closing animation duration that a client set on
     * the ExpandableFabLayout. If set, this value takes precedence over the local
     * [closingAnimationDurationMs].
     * */
    @JvmSynthetic
    internal fun closingAnimations(globalDurationMs: Long?): Animator {
        return ObjectAnimator.ofFloat(this, "alpha", 0f).apply {
            this.duration = globalDurationMs ?: closingAnimationDurationMs
            addListener(hideOnAnimationEnd)
        }
    }
}