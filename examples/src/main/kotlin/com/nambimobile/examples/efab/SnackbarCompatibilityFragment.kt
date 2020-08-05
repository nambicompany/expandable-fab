package com.nambimobile.examples.efab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.nambimobile.widgets.efab.ExpandableFab

class SnackbarCompatibilityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.snackbar_compatibility_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFab = view.findViewById<ExpandableFab>(R.id.expandable_fab)
        val snackbarAbove = Snackbar.make(
            expandableFab,
            "I will show above the ExpandableFab.",
            Snackbar.LENGTH_LONG
        ).apply { anchorView = expandableFab }
        val snackbarBelow = Snackbar.make(
            expandableFab,
            "I will show below the ExpandableFab.",
            Snackbar.LENGTH_LONG
        )

        view.findViewById<MaterialButton>(R.id.snackbar_above_btn).setOnClickListener {
            snackbarAbove.show()
        }
        view.findViewById<MaterialButton>(R.id.snackbar_below_btn).setOnClickListener {
            snackbarBelow.show()
        }
    }
}