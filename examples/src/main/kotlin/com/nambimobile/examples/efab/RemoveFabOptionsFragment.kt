package com.nambimobile.examples.efab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.button.MaterialButton
import com.nambimobile.widgets.efab.ExpandableFab
import com.nambimobile.widgets.efab.ExpandableFabLayout

class RemoveFabOptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.remove_faboptions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val expandableFabLayout = view.findViewById<ExpandableFabLayout>(R.id.expandable_fab_layout)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val removeButton = view.findViewById<MaterialButton>(R.id.remove_faboption)
        val indices = expandableFabLayout.portraitConfiguration.fabOptions.indices.toMutableList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, indices).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.setNotifyOnChange(true)
            spinner.adapter = it
        }

        // Have the ExpandableFab be automatically opened when the Fragment is created
        view.findViewById<ExpandableFab>(R.id.expandable_fab).callOnClick()

        removeButton.setOnClickListener {
            spinner.selectedItem?.let {
                val indexToRemove = it.toString().toInt()

                // Removing FabOption at the selected index (zero based index) from the ExpandableFab
                expandableFabLayout.getCurrentConfiguration().fabOptions.removeAt(indexToRemove)

                // Updating the Spinner to show the remaining indices
                adapter.clear()
                val remainingIndices =
                    expandableFabLayout.portraitConfiguration.fabOptions.indices.toMutableList()
                adapter.addAll(remainingIndices)
            }
        }

        // To remove a FabOption by...

        // ... index in Kotlin
        // expandableFabLayout.getCurrentConfiguration().fabOptions.removeAt(3)

        // ... object reference in Kotlin
        // val fabOptionToRemove = expandableFabLayout.portraitConfiguration.fabOptions[0]
        // expandableFabLayout.portraitConfiguration.fabOptions.remove(fabOptionToRemove)

        // ... index in Java
        // expandableFabLayout.getPortraitConfiguration().getFabOptions().remove(1);

        // ... object reference in Java
        // FabOption fabOptionToRemove = expandableFabLayout.getPortraitConfiguration().getFabOptions().get(4);
        // expandableFabLayout.getPortraitConfiguration().getFabOptions().remove(fabOptionToRemove);
    }
}
