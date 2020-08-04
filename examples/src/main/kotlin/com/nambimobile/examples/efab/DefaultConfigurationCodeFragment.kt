package com.nambimobile.examples.efab

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.nambimobile.widgets.efab.*

class DefaultConfigurationCodeFragment : Fragment() {
    private lateinit var expandableFabLayout: ExpandableFabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.default_configuration_code_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureExpandableFab()

        // The library provides easy access to the views of the ExpandableFab widget via the
        // portraitConfiguration and landscapeConfiguration properties of ExpandableFabLayout.
        // The views will only be available through these means after calls to
        // ExpandableFabLayout.addView/addViews. Note, if you didn't add a particular view (say
        // an Overlay in Landscape orientation, for example) that property will be null!
        expandableFabLayout.portraitConfiguration.overlay
        expandableFabLayout.landscapeConfiguration.efab
        expandableFabLayout.portraitConfiguration.fabOptions

        // If you just need access to the currently showing widget, regardless of the screen's
        // current orientation, the library provides a helper for that too.
        expandableFabLayout.getCurrentConfiguration().overlay

        // And if you need to wipe all configurations of an ExpandableFabLayout and reset its
        // state, use the below method. Note: no other variation of removeView will guarantee
        // proper internal state control. Use removeAllViews only.
        // expandableFabLayout.removeAllViews() - Removes everything for portrait AND landscape!
    }

    private fun configureExpandableFab(){
        val parentActivity = requireActivity()

        // ExpandableFabLayout extends CoordinatorLayout. So when setting layoutParams of the
        // children views like Overlay, ExpandableFab and FabOptions, you should use
        // CoordinatorLayout.LayoutParams. The layoutParams of the ExpandableFabLayout itself
        // however should use the LayoutParams type of whatever ViewGroup it is a child of. In
        // this example, our fragment layout's root view is a RelativeLayout.
        expandableFabLayout = ExpandableFabLayout(parentActivity).apply {
            layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        val overlayPortrait = Overlay(parentActivity, Orientation.PORTRAIT).apply {
            id = ViewCompat.generateViewId()
            layoutParams = CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val overlayLandscape = Overlay(parentActivity, Orientation.LANDSCAPE).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

        val efabPortrait = ExpandableFab(parentActivity).apply {
            layoutParams = CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
                .also {
                    it.gravity = Gravity.BOTTOM.or(Gravity.END)
                    it.bottomMargin = resources.getDimensionPixelSize(R.dimen.ui_margin_medium)
                    it.rightMargin = resources.getDimensionPixelSize(R.dimen.ui_margin_medium)

                    if(Build.VERSION.SDK_INT >= 17){
                        it.marginEnd = resources.getDimensionPixelSize(R.dimen.ui_margin_medium)
                    }
                }
        }
        val efabLandscape = ExpandableFab(parentActivity, Orientation.LANDSCAPE).apply {
            efabColor = ContextCompat.getColor(parentActivity, R.color.randomColor3)

            layoutParams = CoordinatorLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
                .also {
                    it.gravity = Gravity.BOTTOM.or(Gravity.END)
                    it.bottomMargin = resources.getDimensionPixelSize(R.dimen.ui_margin_medium)
                    it.rightMargin = resources.getDimensionPixelSize(R.dimen.ui_margin_medium)

                    if(Build.VERSION.SDK_INT >= 17){
                        it.marginEnd = resources.getDimensionPixelSize(R.dimen.ui_margin_medium)
                    }
                }
        }

        val portraitFabOptions = arrayOf(
            FabOption(parentActivity).apply {
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_walk_white_24dp)
                label.labelText = "Option 1"
            },
            FabOption(parentActivity).apply {
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_bike_white_24dp)
                label.labelText = "Option 2"
            },
            FabOption(parentActivity).apply {
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_car_white_24dp)
                label.labelText = "Option 3"
            },
            FabOption(parentActivity).apply {
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_train_white_24dp)
                label.labelText = "Option 4"
            },
            FabOption(parentActivity).apply {
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_flight_white_24dp)
                label.labelText = "Option 5"
            }
        )
        val landscapeFabOptions = arrayOf(
            FabOption(parentActivity, Orientation.LANDSCAPE).apply {
                fabOptionColor = ContextCompat.getColor(parentActivity, R.color.randomColor3)
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_event_white_24dp)
                label.labelText = "Unique Option 1 for Landscape!"
            },
            FabOption(parentActivity, Orientation.LANDSCAPE).apply {
                fabOptionColor = ContextCompat.getColor(parentActivity, R.color.randomColor3)
                fabOptionIcon = ContextCompat.getDrawable(parentActivity, R.drawable.ic_reminder_white_24dp)
                label.labelText = "Unique Option 2 for Landscape!"
            }
        )

        // Adds the ExpandableFabLayout to the rootView of our fragment (a RelativeLayout)
        (view as ViewGroup).addView(expandableFabLayout)

        // Adds the children views of the ExpandableFab widget to the ExpandableFabLayout
        expandableFabLayout.addViews(
            overlayPortrait,
            overlayLandscape,
            efabPortrait,
            efabLandscape,
            *portraitFabOptions,
            *landscapeFabOptions)
    }
}
