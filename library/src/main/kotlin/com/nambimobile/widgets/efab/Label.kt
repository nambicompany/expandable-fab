package com.nambimobile.widgets.efab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

/**
 * A label of text attached to a view of the ExpandableFab widget.
 *
 * Developer Notes:
 * 1) Different views within the ExpandableFab widget can be labeled (like ExpandableFab and
 * FabOption). Each labeled view will set their own default values for the properties of their
 * specific label. Look at the documentation for the label property declared in each of theses
 * classes to see what the default values of their labels are.
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
class Label : AppCompatTextView {
    /**
     * The text of the Label. Can be set to null to hide the label. Usage of this property is
     * preferred over the inherited set/getText methods.
     * */
    var labelText: CharSequence? = null
        set(value) {
            if(value == null){
                visibility = View.GONE
            }

            value?.let { text = it }
            field = value
        }

    /**
     * The color of the Label's text, as an integer in the form 0xAARRGGBB.
     *
     * If using an XML resource color value, retrieve it in the correct form by calling:
     * ContextCompat.getColor(context, R.color.name_of_color_resource).
     *
     * Usage of this property is preferred over the inherited setTextColor method variations.
     * */
    var labelTextColor = ContextCompat.getColor(context, android.R.color.white)
        set(value) {
            setTextColor(value)
            field = value
        }

    /**
     * The size of the Label's text, as a float. Retrieve this value in the correct form from your
     * dimens.xml file using: resources.getDimension(R.dimen.name_of_text_size). Text sizes should
     * use sp as the unit. Usage of this property is preferred over the inherited set/getTextSize
     * methods.
     * */
    var labelTextSize = resources.getDimension(R.dimen.efab_label_text_size)
        set(value) {
            // resources.getDimension already converts SP to PX, so we set the unit to PX below
            setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
            field = value
        }

    /**
     * The color of the Label's background, as an integer in the form 0xAARRGGBB.
     *
     * If using an XML resource color value, retrieve it in the correct form by calling:
     * ContextCompat.getColor(context, R.color.name_of_color_resource).
     *
     * Usage of this property is preferred over the inherited setBackgroundColor method.
     * */
    var labelBackgroundColor = ContextCompat.getColor(context, R.color.efab_label_background)
        set(value) {
            background.let {
                when(it){
                    is GradientDrawable -> it.setColor(value)
                    else -> it.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
                }
            }

            field = value
        }

    /**
     * The elevation of the Label, in pixels as a positive float. Retrieve this value in the
     * correct form from your dimens.xml file using:
     * resources.getDimensionPixelSize(R.dimen.name_of_elevation_size). Usage of this property is
     * preferred over the inherited set/getElevation methods.
     * */
    var labelElevation = resources.getDimensionPixelSize(R.dimen.efab_label_elevation)
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties))
            }

            ViewCompat.setElevation(this, value.toFloat())
            field = value
        }

    /**
     * The enabled state of this Label. A disabled Label will be visually distinct and
     * unclickable. Default value is true (enabled).
     *
     * Set as 'internal' as clients should never need to call this directly - labeled views will
     * call this when appropriate.
     * */
    @get:JvmSynthetic
    @set:JvmSynthetic
    internal var labelEnabled = true
        set(value) {
            if(value){
                // didn't overwrite these values, so just assign them again and we're good to go
                labelBackgroundColor = labelBackgroundColor
                labelTextColor = labelTextColor
            } else {
                val disabledColor = ContextCompat.getColor(context, R.color.efab_disabled)
                val disabledTextColor = ContextCompat.getColor(context, R.color.efab_disabled_text)

                background.setColorFilter(disabledColor, PorterDuff.Mode.SRC_ATOP)
                setTextColor(disabledTextColor)
            }

            isEnabled = value
            field = value
        }

    /**
     * The Label's [LabelPosition], in relation to the view it's attached to.
     * */
    var position = LabelPosition.LEFT

    /**
     * The margin (in pixels as a positive float) between the Label and the view it is attached to.
     * Negative values are not allowed since clients should likely be changing the Label's
     * [position] if they wanted to move the Label to the opposite side of the view it's attached
     * to.
     * */
    var marginPx = 50f
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The distance (in pixels as a float) the Label will travel horizontally during animations.
     * A negative value will make the Label travel to the left, while a positive value will make
     * it travel to the right. See its use in [visibleToHiddenAnimations] and
     * [hiddenToVisibleAnimations].
     * */
    var translationXPx = 100f

    /**
     * The duration (in milliseconds as a positive long) of the animations that the Label will
     * play when going from a visible to hidden state. Set to 0L if you don't want animations played.
     * */
    var visibleToHiddenAnimationDurationMs = 250L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The duration (in milliseconds as a positive long) of the animations that the Label will
     * play when going from a hidden to visible state. Set to 0L if you don't want animations played.
     * */
    var hiddenToVisibleAnimationDurationMs = 75L
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties))
            }

            field = value
        }

    /**
     * The tension (as a positive float) in an OvershootInterpolator applied on the Label when it
     * is being shown from a hidden state. Larger values will exaggerate the effect more.
     *
     * The OvershootInterpolator allows us to have an animation where the Label will fling past
     * its [marginPx] when initially appearing, before smoothly coming back toward its [marginPx]
     * value.
     * */
    var overshootTension = 3.5f
        set(value) {
            if (value < 0){
                illegalArg(resources.getString(R.string.efab_label_illegal_optional_properties))
            }

            field = value
        }

    // Declared as a property so we don't create a new one each animation... slight waste reduction?
    private val hideOnAnimationEnd = object : AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            this@Label.visibility = View.GONE
        }
    }


    /**
     * Used to create a Label programmatically. Clients should not need to call this directly as
     * all labeled Views of the ExpandableFab widget already create their own labels which can
     * be retrieved and modified (but not reassigned) as needed.
     * */
    constructor(context: Context): super(context){
        // Kotlin does not use the declared custom setter of properties when setting their default
        // values. This is unfortunate, as our custom setters have logic that need to be executed
        // when the property is set. So these assignments, though seemingly redundant, ensure
        // each custom setter is called at instantiation and the logic is executed.
        labelText = labelText
        labelTextColor = labelTextColor
        labelTextSize = labelTextSize
        labelBackgroundColor = labelBackgroundColor
        labelElevation = labelElevation
        position = position
        marginPx = marginPx
        translationXPx = translationXPx
        visibleToHiddenAnimationDurationMs = visibleToHiddenAnimationDurationMs
        hiddenToVisibleAnimationDurationMs = hiddenToVisibleAnimationDurationMs
        overshootTension = overshootTension
    }

    /**
     * Called by the system when creating a Label via XML (private to disallow this). Clients
     * should not need to call this directly as all labeled Views of the ExpandableFab widget
     * already create their own labels which can be retrieved and modified (but not reassigned)
     * as needed.
     * */
    private constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)

    init {
        id = ViewCompat.generateViewId()
        visibility = View.GONE

        val backgroundDrawable = GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, R.color.efab_label_background))
            cornerRadius = resources.getDimension(R.dimen.efab_ui_margin_xxs)
            setPadding(
                resources.getDimension(R.dimen.efab_ui_margin_xs).toInt(),
                resources.getDimension(R.dimen.efab_ui_margin_xxs).toInt(),
                resources.getDimension(R.dimen.efab_ui_margin_xs).toInt(),
                resources.getDimension(R.dimen.efab_ui_margin_xxs).toInt()
            )
        }
        ViewCompat.setBackground(this, backgroundDrawable)
    }


    // ******************* Public methods available through the published API ******************* \\
    /**
     * Scales the Label down when pressed, and back up when released. Gives the user some visual
     * feedback to show that the Label was pressed.
     * */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                scaleX = 0.925f
                scaleY = 0.925f
            }
            MotionEvent.ACTION_UP -> {
                scaleX = 1f
                scaleY = 1f
            }
        }

        return super.onTouchEvent(event)
    }


    // ****************** Private / Internal methods not available to clients ******************* \\
    /**
     * The animations to play on the Label when it needs to become visible from a hidden state. The
     * Label will animate from [marginPx] + [translationXPx] to a margin of marginPx from the
     * view it's attached to (relative to its [position]), all while slowly appearing. Before
     * stopping, the label will slightly shoot past the marginPx value using the effect of an
     * OvershootInterpolator (with a tension of [label.overshootTension]).
     * */
    @JvmSynthetic
    internal fun hiddenToVisibleAnimations(): Animator {
        if(labelText == null){
            return AnimatorSet()
        }

        positionSelf()

        alpha = 0f
        visibility = View.VISIBLE

        val startTranslation = when(position){
            LabelPosition.LEFT -> -marginPx + translationXPx
            LabelPosition.RIGHT -> marginPx + translationXPx
        }
        val endTranslation =  when(position){
            LabelPosition.LEFT -> -marginPx
            LabelPosition.RIGHT -> marginPx
        }

        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@Label, "translationX", startTranslation, endTranslation).apply {
                    this.duration = hiddenToVisibleAnimationDurationMs
                    interpolator = OvershootInterpolator(overshootTension)
                },
                ObjectAnimator.ofFloat(this@Label, "alpha", 0f, 1f).apply {
                    this.duration = hiddenToVisibleAnimationDurationMs
                }
            )
        }
    }

    /**
     * The animations to play on the Label when it needs to hide from a visible state. The Label
     * will animate by [label.translationXPx] pixels from its current x position, all while slowly
     * fading out of sight.
     * */
    @JvmSynthetic
    internal fun visibleToHiddenAnimations(): Animator {
        if(labelText == null){
            return AnimatorSet()
        }

        // read these property names carefully... two diff properties. translationX is the
        // label's current x position (defined via Android framework) and translationXPx is a
        // custom property defined by this library.
        val endTranslation = translationX + translationXPx

        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@Label, "translationX", translationX, endTranslation).apply {
                    this.duration = visibleToHiddenAnimationDurationMs
                },
                ObjectAnimator.ofFloat(this@Label, "alpha", 0f).apply {
                    this.duration = visibleToHiddenAnimationDurationMs
                })
            addListener(hideOnAnimationEnd)
        }
    }

    private fun positionSelf(){
        (layoutParams as CoordinatorLayout.LayoutParams).let {
            if(it.anchorId != View.NO_ID){
                it.anchorGravity = position.value
                it.gravity = position.value
                layoutParams = it
            }
        }
    }

    @JvmSynthetic
    internal fun showLabel(){
        if(labelText != null){
            positionSelf()

            visibility = View.VISIBLE

            when(position){
                LabelPosition.LEFT -> translationX = -marginPx
                LabelPosition.RIGHT -> translationX = marginPx
            }
        }
    }

    @JvmSynthetic
    internal fun hideLabel(){
        visibility = View.GONE
    }
}