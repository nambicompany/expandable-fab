package com.nambimobile.examples.efab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomappbar.BottomAppBar
import com.nambimobile.widgets.efab.ExpandableFab

class BottomAppBarCompatibility : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_app_bar_compatibility_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // If you only have a single ExpandableFab that's shown in both portrait and landscape
        // orientations, the below logic is not needed. You can ignore the following code.

        // For those with different ExpandableFabs per orientation: multiple Views in a layout
        // cannot anchor to the same View (the BottomAppBar) at the same time. So at runtime, we
        // need to determine which ExpandableFab should anchor to the BottomAppBar and which
        // should clear its anchor. This is simple to do, and is shown below.

        val expandableFabPortrait = view.findViewById<ExpandableFab>(R.id.expandable_fab_portrait)
        val expandableFabLandscape = view.findViewById<ExpandableFab>(R.id.expandable_fab_landscape)
        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottom_app_bar)

        if(expandableFabPortrait.visibility == View.VISIBLE){
            expandableFabPortrait.updateLayoutParams<CoordinatorLayout.LayoutParams> {anchorId = bottomAppBar.id}
            expandableFabLandscape.updateLayoutParams<CoordinatorLayout.LayoutParams> {anchorId = View.NO_ID}
        } else {
            expandableFabPortrait.updateLayoutParams<CoordinatorLayout.LayoutParams> {anchorId = View.NO_ID}
            expandableFabLandscape.updateLayoutParams<CoordinatorLayout.LayoutParams> {anchorId = bottomAppBar.id}
        }
    }
}
