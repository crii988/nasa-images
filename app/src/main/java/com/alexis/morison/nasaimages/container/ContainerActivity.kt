package com.alexis.morison.nasaimages.container

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Transition
import android.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.fragments.ApodFragment
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.library.fragments.LibraryFormFragment
import com.alexis.morison.nasaimages.library.fragments.LibraryFragment
import com.alexis.morison.nasaimages.rovers.fragments.RoverFormFragment
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.apod_menu_card.*
import kotlinx.android.synthetic.main.apod_menu_card.view.*

class ContainerActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        toolbar = toolbar_id_container

        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        if (savedInstanceState == null) {
            when (intent.getIntExtra("id", 1)) {

                1 -> {
                    loadFragment(ApodFragment())
                    toolbar.title = resources.getString(R.string.apod_title)
                }
                2 -> {
                    //loadFragment(ApodFragment())
                    toolbar.title = "Earth"
                }
                3 -> {
                    //loadFragment(ApodFragment())
                    toolbar.title = "EPIC"
                }
                4 -> {
                    loadFragment(RoverFormFragment())
                    toolbar.title = resources.getString(R.string.rovers_title)
                }
                5 -> {
                    loadFragment(LibraryFormFragment())
                    toolbar.title = resources.getString(R.string.library_title)
                }
                else -> {
                    //loadFragment(LibraryFormFragment())
                    toolbar.title = "Favorites"
                }
            }
        }


    }

    private fun loadFragment(f: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainer, f)
        fragmentTransaction.commit()
    }
}