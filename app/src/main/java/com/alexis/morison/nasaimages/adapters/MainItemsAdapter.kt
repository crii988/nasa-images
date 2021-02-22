package com.alexis.morison.nasaimages.adapters

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.ApodActivity
import com.alexis.morison.nasaimages.ContainerActivity
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.models.APOD
import com.alexis.morison.nasaimages.models.MainItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_menu_card.view.*


class MainItemsAdapter(private val items: List<MainItem>) : RecyclerView.Adapter<MainItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.main_menu_card,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        private var requestQueue: RequestQueue? = null

        private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"

        fun bind(item: MainItem) = with(v) {

            main_card_title.text = item.title
            main_card_description.text = item.description

            requestQueue = Volley.newRequestQueue(context)

            when (item.id) {

                1 -> getAPOD(v)
                2 -> getDrawable(main_card_image, R.drawable.earth)
                3 -> getDrawable(main_card_image, R.drawable.epic)
                4 -> getDrawable(main_card_image, R.drawable.rover)
                else -> getDrawable(main_card_image, R.drawable.library)
            }

            main_card.setOnClickListener {

                val id = item.id

                val intent = Intent(context, ApodActivity::class.java).apply {

                    putExtra("ID", id)
                }

                startActivity(context, intent, null)
            }
        }

        private fun getAPOD(view: View) {

            val url = "https://api.nasa.gov/planetary/apod?api_key=$apiKey"

            val json = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->

                    Picasso.get()
                        .load(response.getString("url"))
                        .error(R.drawable.apod)
                        .into(view.main_card_image)
                },
                { _ ->
                    Picasso.get()
                        .load(R.drawable.apod)
                        .into(view.main_card_image)
                }
            )

            requestQueue?.add(json)
        }

        private fun getDrawable(view: ImageView, drawable: Int) {

            Picasso.get()
                .load(drawable)
                .into(view)
        }
    }
}