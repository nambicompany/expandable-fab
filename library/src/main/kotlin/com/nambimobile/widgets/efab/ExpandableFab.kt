package com.nambimobile.widgets.efab

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timer
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * The design and behavior of a Floating Action Button which can animate and expand to show
 * optional [FabOption]s when clicked. It's the focal point for the ExpandableFab widget's
 * functionality, from an end-user's perspective.
 *
 * Developer Notes:
 * 1) Layout_width and layout_height should be set to wrap_content, unless you're setting custom
 * dimensions.
 * 2) ExpandableFabs must be defined within an [ExpandableFabLayout] to function properly.
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
class ExpandableFab : FloatingActionButton {
    /** The [Orientation] this ExpandableFab is viewable in. Default value is [Orientation.PORTRAIT]. **/
    var orientation = Orientation.PORTRAIT
        private set

    /**
     * The color of the ExpandableFab. Default value is your app's colorAccent.
     *
     * Usage of this property is preferred over the inherited setBackgroundColor and
     * backgroundTintList methods.
     * */
    var efabColor = ContextCompat.getColor(context, R.color.colorAccent)
        set(value){
            this.backgroundTintList = ColorStateList.valueOf(value)
            field = value
        }

    /**
     * The drawable to show as the ExpandableFab's icon. Default value is a white plus sign vector
     * drawable.
     *
     * Usage of this property is preferred over the inherited set/getImageXXX methods.
     * */
    var efabIcon = ContextCompat.getDrawable(context, R.drawable.ic_plus_white_24dp)
        set(value) {
            this.setImageDrawable(value)
            field = value
        }

    /**
     * The size of this ExpandableFab (NOT its FabOptions. For that, see property [fabOptionSize]).
     * Must be [FabSize.MINI], [FabSize.NORMAL], [FabSize.AUTO] or [FabSize.CUSTOM].
     * FabSize.CUSTOM should be used when you're setting the size of the ExpandableFab's
     * layout_width and layout_height to a custom value manually (when using CUSTOM, make sure to
     * also set app:fabCustomSize equal to your custom layout_width/layout_height size and
     * app:maxImageSize equal to your icon size in order to ensure your icon is centered
     * properly). Default value is [FabSize.NORMAL].
     *
     * Usage of this property is preferred over the inherited set/getSize methods.
     * */
    var efabSize = FabSize.NORMAL
        set(value) {
            if(value != FabSize.CUSTOM){
                size = value.value
            }

            field = value
        }

    /**
     * The enabled state of this ExpandableFab and its label. Disabled ExpandableFabs and labels
     * will be visually distinct and unclickable. Default value is true (enabled).
     *
     * Usage of this property is preferred over the inherited is/setEnabled methods.
     *
     * NOTE: If you disable an ExpandableFab and its label, remember to re-enable it before
     * trying to update one of its properties. For example, disabling an ExpandableFab then
     * manually changing its background color will not automatically re-enable the ExpandableFab.
     * It, and its label, will remain disabled.
     * */
    var efabEnabled = true
        set(value) {
            if(value){
                // didn't overwrite this value, so just assign them again and we're good to go
                efabColor = efabColor
            } else {
                val disabledColor = ContextCompat.getColor(context, R.color.efab_disabled)
                backgroundTintList = ColorStateList.valueOf(disabledColor)
            }

            isEnabled = value
            label.labelEnabled = value
            field = value
        }

    /**
     * A float, in degrees, representing how much the ExpandableFab's icon will rotate when
     * animating. When opening, the ExpandableFab will rotate from 0 to this value. When closing,
     * the icon will rotate from this value to its initial position of 0. Default value is -135f.
     * */
    var iconAnimationRotationDeg = -135f

    /**
     * The size of each FabOption in this orientation (NOT the size of the ExpandableFab. For that,
     * see property [efabSize]). Must be [FabSize.MINI], [FabSize.NORMAL], [FabSize.AUTO] or
     * [FabSize.CUSTOM]. FabSize.CUSTOM should be used when you're setting the size of the
     * FabOptions' layout_width and layout_height to a custom value manually (when using CUSTOM,
     * make sure to also set app:fabCustomSize equal to your custom layout_width/layout_height
     * size and app:maxImageSize equal to your icon size in order to ensure your icon is centered
     * properly). Default value is [FabSize.MINI].
     *
     * Usage of this property is preferred over the inherited set/getSize methods on individual
     * FabOptions.
     * */
    var fabOptionSize = FabSize.MINI

    /**
     * How each FabOption in this orientation will be positioned relative to the previous
     * FabOption. Default value is [FabOptionPosition.ABOVE].
     * */
    var fabOptionPosition = FabOptionPosition.ABOVE

    /**
     * The margin (in pixels as a positive float) between the first FabOption in this orientation
     * and the ExpandableFab itself. Depending on the size you set for the ExpandableFab, you may
     * want this margin to be different than [successiveFabOptionMarginPx]. Default value is 80f.
     * */
    var firstFabOptionMarginPx = 80f
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_efab_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The margin (in pixels as a positive float) between successive FabOptions in this
     * ExpandableFab orientation. See [firstFabOptionMarginPx] if trying to set the margin
     * between the first FabOption in this orientation and the ExpandableFab itself. Default value
     * is 75f.
     * */
    var successiveFabOptionMarginPx = 75f
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_efab_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the ExpandableFab's opening animations.
     * Default value is 250L.
     * */
    var openingAnimationDurationMs = 250L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_efab_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the ExpandableFab's closing animations.
     * Default value is 500L.
     * */
    var closingAnimationDurationMs = 500L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_efab_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The tension (as a positive float) applied on the ExpandableFab's icon to mimic an
     * AnticipateInterpolator when it's playing its closing animations. An AnticipateInterpolator
     * allows us to have a closing animation where the ExpandableFab's icon will begin rotating
     * slightly backwards, before smoothly rotating forward to its default 0 degree rotation.
     * Every multiple of 10f will be a full 360 degree rotation backwards. Default value is 2f.
     * */
    var closingAnticipateTension = 2f
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_efab_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The optional label attached to this ExpandableFab. The label will only be shown when its
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
     * visibleToHiddenAnimationDurationMs = 125L
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
        visibleToHiddenAnimationDurationMs = 125L
        hiddenToVisibleAnimationDurationMs = 250L
        overshootTension = 3.5f
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

    /** Called before starting animations. Set by the parent layout. Not to be used by clients. **/
    @get:JvmSynthetic
    @set:JvmSynthetic
    internal var onAnimationStart: (() -> Unit)? = null
        get() {
            if(field == null){
                illegalState(resources.getString(R.string.efab_layout_must_be_child_of_expandablefab_layout))
            }

            return field
        }
        set // Redundant declaration, but must be defined for JvmSynthetic to hide from Java clients

    private var animationTimer: Timer? = null


    /**
     * Used to create an ExpandableFab programmatically (do not use the other constructor
     * ExpandableFab(context, attributeSet) - it is for use by the Android framework when
     * inflating an ExpandableFab via XML). This constructor keeps all optional properties of an
     * ExpandableFab and its optional label at their default values, though you can always change
     * these values after instantiation by using the appropriate setter methods (with the
     * exception of [orientation], which cannot be changed after instantiation).
     *
     * See documentation for an exhaustive list of all optional properties and their default values.
     *
     * Please review the Notes documented at the top of the class for guidelines and limitations
     * when using ExpandableFab.
     * */
    constructor(context: Context, orientation: Orientation? = Orientation.PORTRAIT): super(context){
        setOptionalProperties(orientation = orientation ?: this.orientation)
    }

    /**
     * Called by the system when creating an ExpandableFab via XML (don't call this directly).
     * To create an ExpandableFab programmatically, use the ExpandableFab(context, orientation)
     * constructor.
     * */
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        // parsing properties of the ExpandableFab's Label set via XML
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ExpandableFab, 0, 0).apply {
            try {
                val labelPositionIndex = getInt(R.styleable.ExpandableFab_label_position, LabelPosition.LEFT.ordinal)
                val visibleToHiddenDuration =
                    getString(R.styleable.ExpandableFab_label_visibleToHiddenAnimationDurationMs)?.toLong()
                        ?: label.visibleToHiddenAnimationDurationMs
                val hiddenToVisibleDuration =
                    getString(R.styleable.ExpandableFab_label_hiddenToVisibleAnimationDurationMs)?.toLong()
                        ?: label.hiddenToVisibleAnimationDurationMs

                label.apply {
                    labelText = getString(R.styleable.ExpandableFab_label_text)
                    labelTextColor = getColor(
                        R.styleable.ExpandableFab_label_textColor,
                        label.labelTextColor
                    )
                    labelTextSize = getDimension(
                        R.styleable.ExpandableFab_label_textSize,
                        label.labelTextSize
                    )
                    labelBackgroundColor = getColor(
                        R.styleable.ExpandableFab_label_backgroundColor,
                        label.labelBackgroundColor
                    )
                    labelElevation = getDimensionPixelSize(
                        R.styleable.ExpandableFab_label_elevation,
                        label.labelElevation
                    )
                    position = LabelPosition.values()[labelPositionIndex]
                    marginPx = getFloat(R.styleable.ExpandableFab_label_marginPx, label.marginPx)
                    visibleToHiddenAnimationDurationMs = visibleToHiddenDuration
                    hiddenToVisibleAnimationDurationMs = hiddenToVisibleDuration
                    overshootTension = getFloat(
                        R.styleable.ExpandableFab_label_overshootTension,
                        label.overshootTension
                    )
                    translationXPx = getFloat(
                        R.styleable.ExpandableFab_label_translationXPx,
                        label.translationXPx
                    )
                }
            }  catch (e: Exception) {
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties), e)
            } finally {
                recycle()
            }
        }

        // parsing properties of the ExpandableFab set via XML. We call this after parsing
        // properties of the label, as some of the FabOption's properties may effect the label
        // and we don't want those values to be overridden (like fabOptionEnabled).
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ExpandableFab, 0, 0).apply {
            try {
                val orientationIndex = getInt(R.styleable.ExpandableFab_efab_orientation, Orientation.PORTRAIT.ordinal)
                val fabOptionPositionIndex = getInt(R.styleable.ExpandableFab_efab_fabOptionPosition, FabOptionPosition.ABOVE.ordinal)
                val efabSizeIndex = getInt(R.styleable.ExpandableFab_efab_size, FabSize.NORMAL.ordinal)
                val fabOptionSizeIndex = getInt(R.styleable.ExpandableFab_efab_fabOptionSize, FabSize.MINI.ordinal)
                val openingDuration = getString(R.styleable.ExpandableFab_efab_openingAnimationDurationMs)?.toLong()
                    ?: openingAnimationDurationMs
                val closingDuration = getString(R.styleable.ExpandableFab_efab_closingAnimationDurationMs)?.toLong()
                    ?: closingAnimationDurationMs

                setOptionalProperties(
                    orientation = Orientation.values()[orientationIndex],
                    efabColor = getColor(R.styleable.ExpandableFab_efab_color, efabColor),
                    efabIcon = getDrawable(R.styleable.ExpandableFab_efab_icon) ?: efabIcon,
                    efabSize = FabSize.values()[efabSizeIndex],
                    efabEnabled = getBoolean(R.styleable.ExpandableFab_efab_enabled, true),
                    iconAnimationRotationDeg = getFloat(R.styleable.ExpandableFab_efab_iconAnimationRotationDeg, iconAnimationRotationDeg),
                    fabOptionSize = FabSize.values()[fabOptionSizeIndex],
                    fabOptionPosition = FabOptionPosition.values()[fabOptionPositionIndex],
                    firstFabOptionMarginPx = getFloat(R.styleable.ExpandableFab_efab_firstFabOptionMarginPx, firstFabOptionMarginPx),
                    successiveFabOptionMarginPx = getFloat(R.styleable.ExpandableFab_efab_successiveFabOptionMarginPx, successiveFabOptionMarginPx),
                    openingAnimationDurationMs = openingDuration,
                    closingAnimationDurationMs = closingDuration,
                    closingAnticipateTension = getFloat(R.styleable.ExpandableFab_efab_closingAnticipateTension, closingAnticipateTension)
                )
            } catch (e: Exception) {
                illegalArg(resources.getString(R.string.efab_efab_illegal_optional_properties), e)
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
    }


    // ******************* Public methods available through the published API ******************* \\
    /**
     * Registers a callback to be invoked when this ExpandableFab or its label is clicked. The
     * default behavior the ExpandableFab will be executed before this custom callback.
     * */
    override fun setOnClickListener(onClickListener: OnClickListener?) {
        super.setOnClickListener {
            defaultOnClickBehavior?.invoke()
            onClickListener?.onClick(it)
        }

        setLabelOnClickListener()
    }

    /**
     * Called when the view is detached from a window. At this point it no longer has a surface
     * for drawing. Stops any animations on the ExpandableFab, as it's no longer on screen.
     * */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        animationTimer?.cancel()
    }

    /** Shows the ExpandableFab and optional label. **/
    override fun show() {
        super.show()
        label.showLabel()
    }

    /** Hides the ExpandableFab and optional label. **/
    override fun hide() {
        super.hide()
        label.hideLabel()
    }

    /**
     * Sets the size of the ExpandableFab. Overridden to ensure we never set the size to be
     * [FabSize.CUSTOM].
     *
     * Clients should never need to call this directly. Instead, set [ExpandableFab.efabSize].
     * */
    override fun setSize(size: Int) {
        if(size != FabSize.CUSTOM.value){
            super.setSize(size)
        }
    }


    // ****************** Private / Internal methods not available to clients ******************* \\
    /**
     * Sets the optional properties of this ExpandableFab.
     *
     * Kotlin does not use the declared custom setter of properties when setting their default
     * values. This is unfortunate, as our custom setters have logic that need to be executed
     * when the property is set. So we must assign each property manually to ensure the custom
     * setters are executed, even if we're just setting it with the already set default value.
     * */
    private fun setOptionalProperties(
        orientation: Orientation = this.orientation,
        efabColor: Int = this.efabColor,
        efabIcon: Drawable? = this.efabIcon,
        efabSize: FabSize = this.efabSize,
        efabEnabled: Boolean = this.efabEnabled,
        iconAnimationRotationDeg: Float = this.iconAnimationRotationDeg,
        fabOptionSize: FabSize = this.fabOptionSize,
        fabOptionPosition: FabOptionPosition = this.fabOptionPosition,
        firstFabOptionMarginPx: Float = this.firstFabOptionMarginPx,
        successiveFabOptionMarginPx: Float = this.successiveFabOptionMarginPx,
        openingAnimationDurationMs: Long = this.openingAnimationDurationMs,
        closingAnimationDurationMs: Long = this.closingAnimationDurationMs,
        closingAnticipateTension: Float = this.closingAnticipateTension
    ){
        this.orientation = orientation
        this.efabColor = efabColor
        this.efabIcon = efabIcon
        this.iconAnimationRotationDeg = iconAnimationRotationDeg
        this.efabSize = efabSize
        this.efabEnabled = efabEnabled
        this.fabOptionSize = fabOptionSize
        this.fabOptionPosition = fabOptionPosition
        this.firstFabOptionMarginPx = firstFabOptionMarginPx
        this.successiveFabOptionMarginPx = successiveFabOptionMarginPx
        this.openingAnimationDurationMs = openingAnimationDurationMs
        this.closingAnimationDurationMs = closingAnimationDurationMs
        this.closingAnticipateTension = closingAnticipateTension

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
     * The animations to play when the ExpandableFab is opening. This kicks off the
     * ExpandableFab's manual icon animation along with its optional label's animation.
     *
     * @param onAnimationFinished the callback our parent wants us to invoke when our animations
     * are done.
     * */
    @JvmSynthetic
    internal fun openingAnimations(onAnimationFinished: () -> Unit): Animator {
        manualIconAnimation(
            durationMs = openingAnimationDurationMs,
            startRotationDegrees = 0f,
            endRotationDegrees = iconAnimationRotationDeg,
            onAnimationFinished = {
                onAnimationFinished()
            }
        )

        return AnimatorSet().apply { play(label.visibleToHiddenAnimations()) }
    }

    /**
     * The animations to play when the ExpandableFab is closing. This kicks off the
     * ExpandableFab's manual icon animation along with its optional label's animation.
     *
     * @param onAnimationFinished the callback our parent wants us to invoke when our animations
     * are done.
     * */
    @JvmSynthetic
    internal fun closingAnimations(onAnimationFinished: () -> Unit): Animator {
        val baseAnticipateDegrees = abs(iconAnimationRotationDeg / 10f)
        val totalDegreesToAnticipate = baseAnticipateDegrees * closingAnticipateTension
        val endRotationDegrees = if(iconAnimationRotationDeg < 0){
            iconAnimationRotationDeg - totalDegreesToAnticipate
        } else {
            iconAnimationRotationDeg + totalDegreesToAnticipate
        }
        val anticipateDuration = closingAnimationDurationMs / 5L
        val epsilon = 0.01
        // If anticipateDegrees == 0f or -0f, then we DON'T want to run the anticipate animation
        // at all (abs(0-0) will be less than epsilon). However, anything over 0 is acceptable.
        val showAntipateAnimations = abs(totalDegreesToAnticipate - 0f) > epsilon

        // The regular closing animation to play, taking into account any anticipate rotation
        fun regularClosingAnimation(
            durationMs: Long,
            startingRotationDegrees: Float
        ){
            Timer().schedule(delay = if(showAntipateAnimations) 100L else 0L){
                manualIconAnimation(
                    durationMs = durationMs,
                    startRotationDegrees = startingRotationDegrees,
                    endRotationDegrees = 0f,
                    onAnimationFinished = {
                        onAnimationFinished()
                    }
                )
            }
        }

        if(showAntipateAnimations){
            manualIconAnimation(
                durationMs = anticipateDuration,
                startRotationDegrees = iconAnimationRotationDeg,
                endRotationDegrees = endRotationDegrees,
                onAnimationFinished = {
                    regularClosingAnimation(
                        durationMs = closingAnimationDurationMs - anticipateDuration,
                        startingRotationDegrees = endRotationDegrees
                    )
                }
            )
        } else {
            regularClosingAnimation(
                durationMs = closingAnimationDurationMs,
                startingRotationDegrees = iconAnimationRotationDeg
            )
        }

        return AnimatorSet().apply { play(label.hiddenToVisibleAnimations()) }
    }

    /**
     * Manually animates (rotates) the ExpandableFab's drawable icon (instead of using an Animator).
     *
     * If we were to use an Animator here on the ExpandableFab view to do the rotations, it would
     * cause the chain of FabOptions attached to it to kind of jitter up and down. I believe this
     * is due to the first FabOption in the same orientation being anchored to the top of the
     * ExpandableFab (see [ExpandableFabLayout.addFabOption]). So when the top of the
     * ExpandableFab is rotating away, some weird layout issue is occurring causing the
     * FabOptions to try to move as well, but within some set of constrains due to the anchor.
     *
     * To get around this, we rotate the drawable ([efabIcon]) on the ExpandableFab directly,
     * instead of rotating the entire ExpandableFab view. I'd prefer to use an Animator like the
     * other views of the widget are using (Overlay, FabOption, Label), so will eventually
     * revisit this logic.
     *
     * @param durationMs The length of time in milliseconds that the animation should last.
     * @param startRotationDegrees The rotation the icon should start at, in degrees.
     * @param endRotationDegrees The rotation the icon should end at, in degrees.
     * @param onAnimationFinished Callback to be invoked when this animation is finished. WILL BE
     * CALLED ON THE UI THREAD, SO NOTHING BLOCKING!
     * */
    private fun manualIconAnimation(
        durationMs: Long,
        startRotationDegrees: Float,
        endRotationDegrees: Float,
        onAnimationFinished: () -> Unit
    ){
        /**
         * totalRotationDegrees - the total rotation that will be animated
         * degreesPerMs - how much rotation in degrees should occur on the [efabIcon] per
         * millisecond (totalRotationDegrees divided by the [durationMs] of the animation).
         * updateIntervalMs - how quickly we will redraw the icon with the latest rotation
         * (10ms = animation will be drawn at a speed of 100 frames per second).
         * degreesPerUpdate - how much rotation will actually be done per visible updateIntervalMs
         * (degreesPerMs * updateIntervalMs), as we're not actually drawing each millisecond.
         * epsilon - floating point comparisons are flaky, so we use this value to represent the
         * maximum acceptable difference between floats that we'll tolerate and still consider them
         * equal.
         * currentRotation - what the rotation of the ExpandableFab's icon will be at the end of
         * a given update interval. Will always be capped at [endRotationDegrees] by the
         * animation's end (so icon will never finish slightly off).
         * matrix - the transformation matrix that is applied to the ExpandableFab's icon when it
         * is drawn. What allows us to do the actual rotations.
         * */
        val totalRotationDegrees = abs(endRotationDegrees - startRotationDegrees)
        val degreesPerMs = abs(totalRotationDegrees / durationMs)
        val updateIntervalMs = 10L
        val degreesPerUpdate = degreesPerMs * updateIntervalMs
        val epsilon = 0.01
        var currentRotationDegrees = startRotationDegrees
        val matrix = Matrix()
        onAnimationStart?.invoke()

        animationTimer = timer(period = updateIntervalMs){
            currentRotationDegrees = if(endRotationDegrees > currentRotationDegrees){
                currentRotationDegrees += degreesPerUpdate
                min(currentRotationDegrees, endRotationDegrees)
            } else {
                currentRotationDegrees -= degreesPerUpdate
                max(currentRotationDegrees, endRotationDegrees)
            }

            // Executes the drawable rotation logic on the main (UI) thread
            post {
                this@ExpandableFab.drawable?.let {
                    matrix.reset()
                    scaleType = ScaleType.MATRIX
                    matrix.postRotate(
                        currentRotationDegrees,
                        (it.bounds.width() / 2).toFloat(),
                        (it.bounds.height() / 2).toFloat())
                    imageMatrix = matrix
                }
            }

            if(abs(endRotationDegrees - currentRotationDegrees) < epsilon){
                cancel()

                // invokes the callback on the main (UI) thread
                post { onAnimationFinished() }
            }
        }
    }
}