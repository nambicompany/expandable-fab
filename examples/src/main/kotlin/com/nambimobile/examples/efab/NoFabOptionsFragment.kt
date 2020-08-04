package com.nambimobile.examples.efab

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.nambimobile.widgets.efab.ExpandableFab
import com.nambimobile.widgets.efab.ExpandableFabLayout

class NoFabOptionsFragment : Fragment(), View.OnClickListener {
    private lateinit var expandableFabLayout: ExpandableFabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.no_faboptions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        expandableFabLayout = view.findViewById(R.id.expandable_fab_layout)
        view.findViewById<ExpandableFab>(R.id.expandable_fab).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.expandable_fab -> {
                val spannableString = SpannableString(
                    resources.getString(R.string.no_faboptions_fragment_dialog_message)).also {
                        Linkify.addLinks(it, Linkify.WEB_URLS)
                    }

                val dialog = AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.no_faboptions_fragment_dialog_title)
                    .setMessage(spannableString)
                    .setPositiveButton(android.R.string.ok){ dialog, _ ->
                        dialog.cancel()
                    }
                    .setOnDismissListener { expandableFabLayout.close() }
                    .create()

                dialog.show()

                // must be set after calling show
                dialog.findViewById<TextView>(android.R.id.message)?.movementMethod =
                    LinkMovementMethod.getInstance()
            }
        }
    }
}