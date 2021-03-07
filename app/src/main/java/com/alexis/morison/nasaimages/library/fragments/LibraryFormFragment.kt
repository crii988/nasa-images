package com.alexis.morison.nasaimages.library.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import com.alexis.morison.nasaimages.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import java.time.Year
import java.util.*

private const val searchExtra = "search"

class LibraryFormFragment : Fragment() {

    private lateinit var searchInput: TextInputEditText
    private lateinit var searcBtn: Button
    private lateinit var slider: RangeSlider
    private lateinit var startYearInput: TextInputEditText
    private lateinit var endYearInput: TextInputEditText

    private lateinit var toolbar: MaterialToolbar

    private var query = ""
    private var startYear = 1920
    private var endYear = Year.now().value

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

        searcBtn = v.findViewById(R.id.btn_search)
        startYearInput = v.findViewById(R.id.input_start_year)
        endYearInput = v.findViewById(R.id.input_end_year)

        startYearInput.setText("1920")
        endYearInput.setText(Year.now().value.toString())

        slider = v.findViewById(R.id.slider_years)
        slider.valueFrom = 1920.0F
        slider.valueTo = Year.now().value.toFloat()
        slider.values = listOf(1920.0F, Year.now().value.toFloat())

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = resources.getString(R.string.library_title)
    }

    private fun setListeners() {

        searcBtn.setOnClickListener {

            search()
        }

        searchInput.setOnEditorActionListener { _, i, _ ->

            if (i == EditorInfo.IME_ACTION_SEARCH) {

                search()
            }
            true
        }

        slider.addOnChangeListener { slider, _, _ ->

            startYear = slider.values[0].toInt()
            endYear = slider.values[1].toInt()

            startYearInput.setText(startYear.toString())
            endYearInput.setText(endYear.toString())
        }

        startYearInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                try {
                    startYear = p0.toString().toInt()
                    if (startYear > 1920) slider.values = listOf(startYear.toFloat(), slider.values[1])

                    startYearInput.setSelection(startYearInput.length())
                }
                catch (e: Exception) {

                    Log.d("asdasd", e.message.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        endYearInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                try {
                    endYear = p0.toString().toInt()
                    if (endYear > 1920) slider.values = listOf(slider.values[0], endYear.toFloat())

                    endYearInput.setSelection(endYearInput.length())
                }
                catch (e: Exception) {

                    Log.d("asdasd", e.message.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun search() {

        val fm = context as FragmentActivity

        val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainer, LibraryFragment.newInstance(
                searchInput.text.toString().toLowerCase(Locale.getDefault()),
                startYear.toString(),
                endYear.toString()
        ))

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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