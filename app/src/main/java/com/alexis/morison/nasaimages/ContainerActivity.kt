package com.alexis.morison.nasaimages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import com.alexis.morison.nasaimages.fragments.ApodFragment
import kotlinx.android.synthetic.main.activity_container.*

class ContainerActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        toolbar = toolbar_id

        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        when (intent.getIntExtra("id", 1)) {

            1 -> {
                loadFragment(ApodFragment.newInstance(intent.getStringExtra("date").toString()))
                toolbar.title = "APOD"
            }
            2 -> {
                loadFragment(ApodFragment())
                toolbar.title = "Earth"
            }
            3 -> {
                loadFragment(ApodFragment())
                toolbar.title = "EPIC"
            }
            4 -> {
                loadFragment(ApodFragment())
                toolbar.title = "Mars Rover Photos"
            }
            else -> {
                loadFragment(ApodFragment())
                toolbar.title = "NASA Image and Video Library"
            }
        }
    }

    private fun loadFragment(f: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainer, f)
        fragmentTransaction.commit()
    }
}