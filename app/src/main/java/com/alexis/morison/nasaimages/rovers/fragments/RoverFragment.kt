package com.alexis.morison.nasaimages.rovers.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.rovers.models.Rover
import com.alexis.morison.nasaimages.rovers.models.RoverQuery
import com.google.android.material.appbar.MaterialToolbar

private const val searchExtra = "search"


class RoverFragment : Fragment() {

    private lateinit var toolbar: MaterialToolbar

    private var query: RoverQuery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            query = it.getSerializable(searchExtra) as RoverQuery
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_rover, container, false)

        toolbar = activity!!.findViewById(R.id.toolbar_id_container)
        toolbar.title = query!!.rover

        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: RoverQuery) =
                RoverFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(searchExtra, param1)
                    }
                }
    }
}