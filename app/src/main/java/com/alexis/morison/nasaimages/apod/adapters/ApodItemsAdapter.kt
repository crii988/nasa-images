package com.alexis.morison.nasaimages.apod.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.apod.fragments.ApodDetailsFragment
import com.alexis.morison.nasaimages.apod.models.APOD
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.apod_menu_card.view.*
import kotlinx.android.synthetic.main.main_menu_card.view.*

class ApodItemsAdapter(private val items: List<APOD>) : RecyclerView.Adapter<ApodItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.apod_menu_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item: APOD) = with(v) {

            apod_card_date.text = item.date
            apod_card_title.text = item.title

            Picasso.get()
                .load(item.url)
                .error(R.drawable.apod)
                .resize(500, 500)
                .centerCrop()
                .into(apod_card_image, object:Callback {

                    override fun onSuccess() {
                        apod_card_progress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        apod_card_progress.visibility = View.GONE
                    }
                })

            apod_card.setOnClickListener {

                val fm = v.context as FragmentActivity

                val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.fragmentContainer, ApodDetailsFragment.newInstance(item))
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            apod_card_share.setOnClickListener {

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, item.hdurl)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share URL")
                startActivity(context, shareIntent, null)
            }
        }
    }
}