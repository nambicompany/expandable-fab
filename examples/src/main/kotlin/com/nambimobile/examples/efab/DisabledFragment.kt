package com.nambimobile.examples.efab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nambimobile.widgets.efab.ExpandableFab

class DisabledFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.disabled_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFab = view.findViewById<ExpandableFab>(R.id.expandable_fab)

        view.findViewById<TextView>(R.id.info_block).setOnClickListener {
            expandableFab.efabEnabled = !expandableFab.efabEnabled
        }
    }
}