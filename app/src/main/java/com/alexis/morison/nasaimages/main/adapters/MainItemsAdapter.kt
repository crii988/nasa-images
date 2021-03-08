package com.alexis.morison.nasaimages.main.adapters

import android.content.Intent
import android.transition.AutoTransition
import android.transition.Slide
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.container.ContainerActivity
import com.alexis.morison.nasaimages.main.MainActivity
import com.alexis.morison.nasaimages.main.models.MainItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.apod_menu_card.view.*
import kotlinx.android.synthetic.main.fragment_apod.view.*
import kotlinx.android.synthetic.main.main_menu_card.view.*


class MainItemsAdapter(private val items: List<MainItem>) : RecyclerView.Adapter<MainItemsAdapter.ViewHolder>() {

    var recyclerRoot: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerRoot = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.main_menu_card,
            parent,
            false
        )

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], recyclerRoot!!
    )

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        private var requestQueue: RequestQueue? = null

        private val apiKey = "XdRrmURyk5bW91jnAyoHbaAngJrF8vKIiQiZI6AV"

        fun bind(item: MainItem, recyclerRoot: RecyclerView) = with(v) {

            main_card_title.text = item.title
            main_card_description.text = item.description
            text_view_more_info.text = item.more_info

            requestQueue = Volley.newRequestQueue(context)

            when (item.id) {

                1 -> getAPOD(v)
                2 -> getDrawable(main_card_image, R.drawable.earth)
                3 -> getDrawable(main_card_image, R.drawable.epic)
                4 -> getDrawable(main_card_image, R.drawable.rover)
                5 -> getDrawable(main_card_image, R.drawable.library)
                else -> getDrawable(main_card_image, R.drawable.apod)
            }

            main_card.setOnClickListener {

                val id = item.id

                val intent = Intent(context, ContainerActivity::class.java).apply {

                    putExtra("id", id)
                }

                startActivity(context, intent, null)
            }

            btn_more_info.setOnClickListener {

                if (text_view_more_info.visibility == View.GONE) {

                    text_view_more_info.visibility = View.VISIBLE
                    btn_more_info.text = "Less info"

                    TransitionManager.beginDelayedTransition(recyclerRoot)
                }
                else {
                    btn_more_info.text = "More info"
                    text_view_more_info.visibility = View.GONE

                    TransitionManager.beginDelayedTransition(recyclerRoot)
                }
            }
        }

        private fun getAPOD(view: View) {

            view.main_card_progress.visibility = View.VISIBLE

            val url = "https://api.nasa.gov/planetary/apod?api_key=$apiKey"

            val json = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->

                    Picasso.get()
                        .load(response.getString("url"))
                        .error(R.drawable.apod)
                        .resize(500, 500)
                        .centerCrop()
                        .into(view.main_card_image, object: Callback {

                            override fun onSuccess() {
                                view.main_card_progress.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                view.main_card_progress.visibility = View.GONE
                            }
                        })
                },
                { _ ->
                    Picasso.get()
                        .load(R.drawable.apod)
                        .into(view.main_card_image)

                    view.main_card_progress.visibility = View.GONE
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