package com.example.diettracker

data class SearchedFood(
    val totalHits: Int,
    val totalPages: Int,
    val foods: List<foodDetails>
)

data class foodDetails(
    val foodCategory: String,
    val servingSize: Double,
    val servingSizeUnit: String,
    val foodNutrients: List<nutrients>

)

data class nutrients(
    //val nutrientId:Int,
    val nutrientName: String,
    val unitName: String,
    val value: Double,
    val percentDailyValue:Double
)

