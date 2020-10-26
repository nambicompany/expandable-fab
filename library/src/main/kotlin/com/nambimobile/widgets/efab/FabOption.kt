package com.nambimobile.widgets.efab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * The design and behavior of a single option within an [ExpandableFab].
 *
 * Developer Notes:
 * 1) Layout_width and layout_height should be set to wrap_content, unless you're setting
 * custom dimensions.
 * 2) FabOptions must be defined within an [ExpandableFabLayout] to function properly.
 * 3) [label.labelText] must be non-null for the optional label to show and other label properties
 * to take effect.
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
class FabOption : FloatingActionButton {

    /** The [Orientation] this FabOption is viewable in. Default value is [Orientation.PORTRAIT]. **/
    var orientation = Orientation.PORTRAIT
        private set

    /**
     * The color of the FabOption, as an integer in the form 0xAARRGGBB.
     *
     * If using an XML resource color value, retrieve it in the correct form by calling:
     * ContextCompat.getColor(context, R.color.name_of_color_resource).
     *
     * Default value is the colorAccent value defined in your app theme (styles.xml). If
     * colorAccent is not defined in your app theme, colorSecondary may be used.
     *
     * Usage of this property is preferred over the inherited setBackgroundColor and
     * backgroundTintList methods.
     * */
    var fabOptionColor = getThemeColorAccent(context)
        set(value){
            this.backgroundTintList = ColorStateList.valueOf(value)
            field = value
        }

    /**
     * The drawable to show as the FabOption's icon. Default value is null (no icon shown).
     *
     * Usage of this property is preferred over the inherited set/getImageXXX methods.
     * */
    var fabOptionIcon: Drawable? = null
        set(value) {
            this.setImageDrawable(value)
            field = value
        }

    /**
     * The enabled state of this FabOption and its label. Disabled FabOptions and labels will be
     * visually distinct and unclickable. Default value is true (enabled).
     *
     * Usage of this property is preferred over the inherited is/setEnabled methods.
     *
     * NOTE: If you disable a FabOption and its label, remember to re-enable it before trying to
     * update one of its properties. For example, disabling a FabOption then manually changing
     * its background color will not automatically re-enable the FabOption. It, and its label,
     * will remain disabled.
     * */
    var fabOptionEnabled = true
        set(value) {
            if(value){
                // didn't overwrite this value, so just assign them again and we're good to go
                fabOptionColor = fabOptionColor
            } else {
                val disabledColor = ContextCompat.getColor(context, R.color.efab_disabled)
                backgroundTintList = ColorStateList.valueOf(disabledColor)
            }

            isEnabled = value
            label.labelEnabled = value
            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the animations that will be played
     * when this FabOption is being shown from a hidden state (when the ExpandableFab is opening).
     * Set to 0L if you don't want opening animations played. Default value is 125L.
     * */
    var openingAnimationDurationMs = 125L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_faboption_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the animations that will be played
     * when this FabOption is being hidden from a visible state (when the ExpandableFab is closing).
     * Set to 0L if you don't want closing animations played. Default value is 75L.
     * */
    var closingAnimationDurationMs = 75L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_faboption_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The tension (as a positive float) in an OvershootInterpolator applied on the FabOption when
     * it's playing its animations for being shown from a hidden state (when the ExpandableFab is
     * opening). The OvershootInterpolator allows us to have an animation where the FabOption
     * will grow past its regular size when initially appearing, before smoothly shrinking down
     * to regular size. Larger values will exaggerate the effort more. Default value is 3.5f.
     * */
    var openingOvershootTension = 3.5f
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_faboption_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The optional label attached to this FabOption. The label will only be shown when its
     * labelText is not null. Default values for the label are as follows:
     *
     * labelText = null
     * labelTextColor = white
     * labelTextSize = 14sp
     * labelBackgroundColor = gray (#333333)
     * labelElevation = 6dp
     * position = LabelPosition.LEFT
     * marginPx = 50f
     * translationPx = 100f
     * visibleToHiddenAnimationDurationMs = 75L
     * hiddenToVisibleAnimationDurationMs = 250L
     * overshootTension = 3.5f
     * */
    val label = Label(context).apply {
        labelText = null
        labelTextColor = ContextCompat.getColor(context, android.R.color.white)
        labelTextSize = resources.getDimension(R.dimen.efab_label_text_size)
        labelBackgroundColor = ContextCompat.getColor(context, R.color.efab_label_background)
        labelElevation = resources.getDimensionPixelSize(R.dimen.efab_label_elevation)
        position = LabelPosition.LEFT
        marginPx = 50f
        translationXPx = 100f
        visibleToHiddenAnimationDurationMs = 75L
        hiddenToVisibleAnimationDurationMs = 250L
        overshootTension = 3.5f
    }

    /** Default onClick functionality. Set by the parent layout. Not to be used by clients. **/
    @get:JvmSynthetic
    @set:JvmSynthetic
    internal var defaultOnClickBehavior: (() -> Boolean)? = null
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
            this@FabOption.visibility = View.GONE
        }
    }


    /**
     * Used to create a FabOption programmatically (do not use the other constructor
     * FabOption(context, attributeSet) - it is for use by the Android framework when inflating a
     * FabOption via XML). This constructor keeps all optional properties of a FabOption at their
     * default values, though you can always change these values after instantiation by using the
     * appropriate setter methods (with the exception of [orientation], which cannot be changed
     * after instantiation).
     *
     * See documentation for an exhaustive list of all optional properties and their default values.
     *
     * Please review the Notes documented at the top of the class for guidelines and limitations
     * when using FabOption.
     * */
    constructor(context: Context, orientation: Orientation? = Orientation.PORTRAIT): super(context){
        setOptionalProperties(orientation = orientation ?: this.orientation)
    }

    /**
     * Called by the system when creating a FabOption via XML (don't call this directly).
     * To create a FabOption programmatically, use the FabOption(context, orientation) constructor.
     * */
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        // parsing properties of the FabOption's Label set via XML
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.FabOption, 0, 0).apply {
            try {
                val labelPositionIndex = getInt(R.styleable.FabOption_label_position, LabelPosition.LEFT.ordinal)
                val visibleToHiddenDuration =
                    getString(R.styleable.FabOption_label_visibleToHiddenAnimationDurationMs)?.toLong()
                        ?: label.visibleToHiddenAnimationDurationMs
                val hiddenToVisibleDuration =
                    getString(R.styleable.FabOption_label_hiddenToVisibleAnimationDurationMs)?.toLong()
                        ?: label.hiddenToVisibleAnimationDurationMs

                label.apply {
                    labelText = getString(R.styleable.FabOption_label_text)
                    labelTextColor = getColor(
                        R.styleable.FabOption_label_textColor,
                        label.labelTextColor
                    )
                    labelTextSize = getDimension(
                        R.styleable.FabOption_label_textSize,
                        label.labelTextSize
                    )
                    labelBackgroundColor = getColor(
                        R.styleable.FabOption_label_backgroundColor,
                        label.labelBackgroundColor
                    )
                    labelElevation = getDimensionPixelSize(
                        R.styleable.FabOption_label_elevation,
                        label.labelElevation
                    )
                    position = LabelPosition.values()[labelPositionIndex]
                    marginPx = getFloat(R.styleable.FabOption_label_marginPx, label.marginPx)
                    visibleToHiddenAnimationDurationMs = visibleToHiddenDuration
                    hiddenToVisibleAnimationDurationMs = hiddenToVisibleDuration
                    overshootTension = getFloat(
                        R.styleable.FabOption_label_overshootTension,
                        label.overshootTension
                    )
                    translationXPx = getFloat(
                        R.styleable.FabOption_label_translationXPx,
                        label.translationXPx
                    )
                }
            }  catch (e: Exception) {
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties), e)
            } finally {
                recycle()
            }
        }

        // parsing properties of the FabOption set via XML. We call this after parsing properties
        // of the label, as some of the FabOption's properties may effect the label and we don't
        // want those values to be overridden (like fabOptionEnabled).
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.FabOption, 0, 0).apply {
            try {
                val orientationIndex = getInt(R.styleable.FabOption_fab_orientation, Orientation.PORTRAIT.ordinal)
                val openingDuration = getString(R.styleable.FabOption_fab_openingAnimationDurationMs)?.toLong()
                    ?: openingAnimationDurationMs
                val closingDuration = getString(R.styleable.FabOption_fab_closingAnimationDurationMs)?.toLong()
                    ?: closingAnimationDurationMs

                setOptionalProperties(
                    orientation = Orientation.values()[orientationIndex],
                    fabOptionColor = getColor(R.styleable.FabOption_fab_color, fabOptionColor),
                    fabOptionIcon = getDrawable(R.styleable.FabOption_fab_icon),
                    fabOptionEnabled = getBoolean(R.styleable.FabOption_fab_enabled, true),
                    openingAnimationDurationMs = openingDuration,
                    closingAnimationDurationMs = closingDuration,
                    openingOvershootTension = getFloat(R.styleable.FabOption_fab_openingOvershootTension,
                        openingOvershootTension)
                )
            } catch (e: Exception) {
                illegalArg(resources.getString(R.string.efab_faboption_illegal_optional_properties), e)
            } finally {
                recycle()
            }
        }
    }

    init {
        if (id == View.NO_ID){
            id = ViewCompat.generateViewId()
        }

        // removes default black color tint from icons (Material Theme), showing their true colors
        ImageViewCompat.setImageTintList(this, null)
        visibility = View.GONE
    }


    // ******************* Public methods available through the published API ******************* \\
    /**
     * Registers a callback to be invoked when this FabOption or its label is clicked. The
     * default behavior the FabOption will be executed before this custom callback.
     *
     * Note: Only one FabOption can be clicked per ExpandableFab opening or closing animation.
     * For example, if you set long durations on the opening animations of the ExpandableFab, then
     * the first clicked FabOption will fire its onClickListener, and all subsequent clicks
     * to this or other FabOptions will be ignored until the ExpandableFab has completely finished
     * its animation (this is to prevent a user from spam clicking multiple FabOptions during long
     * animations).
     * */
    override fun setOnClickListener(onClickListener: OnClickListener?) {
        super.setOnClickListener {
            val canCallCustomListener = defaultOnClickBehavior?.invoke()

            if(canCallCustomListener ?: false){
                onClickListener?.onClick(it)
            }
        }

        setLabelOnClickListener()
    }

    /**
     * Sets the size of the FabOption. Overridden to ensure we never set the size to be
     * [FabSize.CUSTOM].
     *
     * Clients should never need to call this directly. Instead, set [ExpandableFab.fabOptionSize].
     * */
    override fun setSize(size: Int) {
        if(size != FabSize.CUSTOM.value){
            super.setSize(size)
        }
    }


    // ****************** Private / Internal methods not available to clients ******************* \\
    /**
     * Sets the optional properties of this FabOption.
     *
     * Kotlin does not use the declared custom setter of properties when setting their default
     * values. This is unfortunate, as our custom setters have logic that need to be executed
     * when the property is set. So we must assign each property manually to ensure the custom
     * setters are executed, even if we're just setting it with the already set default value.
     * */
    private fun setOptionalProperties(
        orientation: Orientation = this.orientation,
        fabOptionColor: Int = this.fabOptionColor,
        fabOptionIcon: Drawable? = null,
        fabOptionEnabled: Boolean = this.fabOptionEnabled,
        openingAnimationDurationMs: Long = this.openingAnimationDurationMs,
        closingAnimationDurationMs: Long = this.closingAnimationDurationMs,
        openingOvershootTension: Float = this.openingOvershootTension
    ){
        this.orientation = orientation
        this.fabOptionColor = fabOptionColor
        fabOptionIcon?.let { this.fabOptionIcon = it }
        this.fabOptionEnabled = fabOptionEnabled
        this.openingAnimationDurationMs = openingAnimationDurationMs
        this.closingAnimationDurationMs = closingAnimationDurationMs
        this.openingOvershootTension = openingOvershootTension

        if(hasOnClickListeners()){
            // If the view has a listener set, it was set via the onClick attribute in XML. However
            // the label was not created at that time, so we mirror the logic on the label now.
            setLabelOnClickListener()
        } else {
            // In all other cases we want to call setOnClickListener with null to set the default
            // onClick behavior without any custom user onClick behavior.
            setOnClickListener(null)
        }
    }

    // The safe call may seem unnecessary on the non-null Label type below, but it seems default
    // values for properties are actually not set by Kotlin by the first time this is called. So
    // somehow even though the label is instantiated at the top of the class, by the time the
    // Android Framework is parsing XML layout files (specifically the 'onClick' attribute, which
    // calls setOnClickListener, where the below function is first called) the label is not yet
    // created. So we need the safe call... on a type that should never be null. Will look more
    // into this later.
    private fun setLabelOnClickListener() = label?.setOnClickListener { this.callOnClick() }

    /**
     * The set of animations to play when the FabOption is being shown from a hidden state (when
     * the ExpandableFab is opening). This kicks off the FabOption's animation along with its
     * optional label's animation.
     * */
    @JvmSynthetic
    internal fun openingAnimations(
        index: Int,
        size: FabSize,
        position: FabOptionPosition,
        firstFabOptionMarginPx: Float,
        successiveFabOptionMarginPx: Float
    ): Animator {
        this.alpha = 0f
        this.visibility = View.VISIBLE
        this.size = size.value

        val (firstMarginPx, successiveMarginPx) = when(position){
            FabOptionPosition.ABOVE -> Pair(-firstFabOptionMarginPx, -successiveFabOptionMarginPx)
            FabOptionPosition.BELOW -> Pair(firstFabOptionMarginPx, successiveFabOptionMarginPx)
        }

        val openingAnimations = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@FabOption, "scaleX", 0f, 1f).apply {
                    this.duration = openingAnimationDurationMs
                    interpolator = OvershootInterpolator(openingOvershootTension)
                },
                ObjectAnimator.ofFloat(this@FabOption, "scaleY", 0f, 1f).apply {
                    this.duration = openingAnimationDurationMs
                    interpolator = OvershootInterpolator(openingOvershootTension)
                },
                ObjectAnimator.ofFloat(this@FabOption, "alpha", 0f, 1f).apply {
                    this.duration = openingAnimationDurationMs
                },
                if (index == 0) {
                    ObjectAnimator.ofFloat(this@FabOption, "translationY", firstMarginPx).apply {
                        this.duration = 1L
                    }
                } else {
                    ObjectAnimator.ofFloat(this@FabOption, "translationY", successiveMarginPx).apply {
                        this.duration = 1L
                    }
                }
            )
        }

        return AnimatorSet().apply { playTogether(openingAnimations, label.hiddenToVisibleAnimations()) }
    }

    /**
     * The set of animations to play when the FabOption is being hidden from a visible state (when
     * the ExpandableFab is closing). This kicks off the FabOption's animation along with its
     * optional label's animation.
     * */
    @JvmSynthetic
    internal fun closingAnimations(): Animator {
        val closingAnimations = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@FabOption, "scaleX", 0f).apply {
                    this.duration = closingAnimationDurationMs
                },
                ObjectAnimator.ofFloat(this@FabOption, "scaleY", 0f).apply {
                    this.duration = closingAnimationDurationMs
                },
                ObjectAnimator.ofFloat(this@FabOption, "alpha", 0f).apply {
                    this.duration = closingAnimationDurationMs
                }
            )
            addListener(hideOnAnimationEnd)
        }

        return AnimatorSet().apply { playTogether(closingAnimations, label.visibleToHiddenAnimations()) }
    }
}