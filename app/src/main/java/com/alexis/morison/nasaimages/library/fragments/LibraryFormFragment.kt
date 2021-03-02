package com.alexis.morison.nasaimages.library.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.alexis.morison.nasaimages.R
import com.google.android.material.textfield.TextInputEditText
import java.time.Year
import java.util.*

private const val searchExtra = "search"

class LibraryFormFragment : Fragment() {

    private lateinit var searchInput: TextInputEditText
    private lateinit var startDateInput: TextInputEditText
    private lateinit var endDateInput: TextInputEditText
    private lateinit var searcBtn: Button

    private lateinit var toolbar: Toolbar

    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

            query = it.getString(searchExtra)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_library_form, container, false)

        setViews(v)

        setListeners()

        return v
    }

    private fun setViews(v: View) {

        searchInput = v.findViewById(R.id.search_input)

        if (query != "") {

            searchInput.setText(query)
        }

        startDateInput = v.findViewById(R.id.start_date_input)
        endDateInput = v.findViewById(R.id.end_date_input)
        searcBtn = v.findViewById(R.id.btn_search)

        startDateInput.setText("1950")
        endDateInput.setText(Year.now().value.toString())

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = "NASA Image Library"
    }

    private fun setListeners() {

        searcBtn.setOnClickListener {

            val fm = context as FragmentActivity

            val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragmentContainer, LibraryFragment.newInstance(
                    searchInput.text.toString().toLowerCase(Locale.getDefault()),
                    startDateInput.text.toString(),
                    endDateInput.text.toString()
            ))

            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =

                LibraryFormFragment().apply {
                    arguments = Bundle().apply {
                        putString(searchExtra, param1)
                    }
                }
    }
}