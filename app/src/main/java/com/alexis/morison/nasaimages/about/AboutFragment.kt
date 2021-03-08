package com.alexis.morison.nasaimages.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexis.morison.nasaimages.R
import com.google.android.material.appbar.MaterialToolbar
import java.util.*

class AboutFragment : Fragment() {

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_about, container, false)

        setViews(v)

        return v
    }

    private fun setViews(v: View) {

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = getString(R.string.about)
    }
}