package kr.ac.tukorea.recommend.menu.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.recyclerview.widget.RecyclerView
import kr.ac.tukorea.recommend.menu.MapViewActivity
import kr.ac.tukorea.recommend.menu.databinding.ItemRestaurantBinding

class RestaurantAdapter(
    private val list: MutableList<RestaurantInfo>,
    private val itemClickListener: ItemClickListener? = null
) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: RestaurantInfo) {
            binding.apply {
                restaurantName.text = restaurant.name.toString().trim()
                category.text = restaurant.category
                address.text = restaurant.address
                var tempReview = restaurant.review_count.toString()
                if (tempReview == "999") {
                    tempReview = "$tempReview+"
                }
                reviewCount.text = String.format("%-7s", "리뷰 $tempReview")
                rate.text = String.format("%.02f", restaurant.rate)
                thumbCount.text = restaurant.ddabong.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemRestaurantBinding.inflate(inflater, parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = list[position]
        holder.apply {
            bind(restaurant)
            itemView.setOnClickListener { itemClickListener?.onClick(restaurant) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ItemClickListener {
        fun onClick(restaurantInfo: RestaurantInfo)
    }
}
