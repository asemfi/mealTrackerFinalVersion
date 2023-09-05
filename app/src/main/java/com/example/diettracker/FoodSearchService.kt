package com.example.diettracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodSearchService {

    @GET("/fdc/v1/foods/search")
    fun getFoodInfo(@Query("api_key") apiKey:String,
                    @Query("query") query:String):Call<SearchedFood>


}