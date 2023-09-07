package kr.ac.tukorea.recommend.menu.util

data class RestaurantInfo(
    val name: String? = null,
    val category: String? = null,
    val address: String? = null,
    val rate: Double? = null,
    val review_count: Int? = null,
    val Latitude: Double? = null,
    val Longitude: Double? = null,
    var index: Int? = null,
    var ddabong: Int? = null
)
