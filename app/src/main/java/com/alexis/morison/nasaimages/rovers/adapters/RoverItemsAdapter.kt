package com.alexis.morison.nasaimages.rovers.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.alexis.morison.nasaimages.R
import com.alexis.morison.nasaimages.rovers.fragments.RoverDetailsFragment
import com.alexis.morison.nasaimages.rovers.models.Rover
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.library_menu_card.view.*
import kotlinx.android.synthetic.main.rover_menu_card.view.*

class RoverItemsAdapter(private val items: List<Rover>) : RecyclerView.Adapter<RoverItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.rover_menu_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {

        fun bind(item: Rover) = with(v) {

            rover_card_date.text = item.earth_date
            rover_card_sol.text = item.sol_date.toString()

            Picasso.get()
                .load(item.img_src)
                .error(R.drawable.rover)
                .resize(500, 500)
                .centerCrop()
                .into(rover_card_image, object: Callback {

                    override fun onSuccess() {
                        rover_card_progress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        rover_card_progress.visibility = View.GONE
                    }
                })

            rover_card.setOnClickListener {

                val fm = v.context as FragmentActivity

                val fragmentTransaction = fm.supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.fragmentContainer, RoverDetailsFragment.newInstance(item))
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            rover_card_share.setOnClickListener {

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, item.img_src)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Share URL")
                ContextCompat.startActivity(context, shareIntent, null)
            }
        }
    }
}