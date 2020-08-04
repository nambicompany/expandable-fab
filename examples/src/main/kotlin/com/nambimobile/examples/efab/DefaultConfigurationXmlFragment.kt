package com.nambimobile.examples.efab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nambimobile.widgets.efab.ExpandableFabLayout

class DefaultConfigurationXmlFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.default_configuration_xml_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Although you can set onClickListener functionality for ExpandableFab widget views via
        // XML, Android limits us to defining their methods in the parent Activity. This has a
        // number of downsides when using Fragments, especially from a re-usability standpoint. A
        // better solution would be to implement View.OnClickListener on the Fragment, and define
        // the onClickListeners cleanly like below (see the onClick method for the rest)
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.expandable_fab_layout)

        expandableFabLayout.portraitConfiguration.fabOptions.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.faboption_1 -> { }
            R.id.faboption_2 -> { }
            // so on and so forth...
        }
    }
}
