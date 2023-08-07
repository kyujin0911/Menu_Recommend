package kr.ac.tukorea.recommend.menu

data class RestaurantItem(
    val name: String? = null,
    val category: String? = null,
    val address: String? = null,
    val rate: Double? = null,
    val review_count: Int? = null,
    val Latitude: Double? = null,
    val Longitude: Double? = null,
    var ddabong: Int = 0,
    var index: Int = 0

)
