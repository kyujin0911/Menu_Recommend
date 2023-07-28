package kr.ac.tukorea.recommend.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import kr.ac.tukorea.recommend.menu.databinding.ItemRestaurantBinding

class RestaurantAdapter( val list: MutableList<RestaurantItem>) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){
    class RestaurantViewHolder(val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
                val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val binding = ItemRestaurantBinding.inflate(inflater, parent, false)
                return RestaurantViewHolder(binding)
        }
    override fun onBindViewHolder(holder: RestaurantAdapter.RestaurantViewHolder, position: Int) {
            holder.binding.apply {
                val restaurant = list[position]
                restaurantName.text = restaurant.name.toString().trim()
                category.text = restaurant.category
                address.text = restaurant.address
                var tempReview = restaurant.review_count.toString()
                if (tempReview == "999")
                    tempReview = "$tempReview+"
                reviewCount.text = "리뷰 $tempReview"
                var tempRate = restaurant.rate.toString()
                if(tempRate == "0.0")
                    tempRate ="0.00"
                rate.text = tempRate
            }
        }
    override fun getItemCount(): Int {
        return list.size
    }
}
