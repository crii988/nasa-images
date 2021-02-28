package com.alexis.morison.nasaimages.library.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.adapters.ApodItemsAdapter
import com.alexis.morison.nasaimages.apod.fragments.ApodDetailsFragment
import com.alexis.morison.nasaimages.apod.models.APOD
import com.alexis.morison.nasaimages.library.fragments.LibraryDetailsFragment
import com.alexis.morison.nasaimages.library.fragments.LibraryFragment
import com.alexis.morison.nasaimages.library.models.Library
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.apod_menu_card.view.*
import kotlinx.android.synthetic.main.library_menu_card.view.*

class LibraryItemsAdapter(private val items: List<Library>) : RecyclerView.Adapter<LibraryItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.library_menu_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item: Library) = with(v) {

            library_card_title.text = item.title

            Picasso.get()
                .load(item.href)
                .error(R.drawable.apod)
                .into(library_card_image)

            library_card.setOnClickListener {

                val fm = v.context as FragmentActivity

                val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.fragmentContainer, LibraryDetailsFragment.newInstance(item))
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
    }
}