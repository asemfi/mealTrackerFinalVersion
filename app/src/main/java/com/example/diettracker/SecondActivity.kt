package com.example.diettracker

import CustomDialog
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

private val FILE_NAME = "SavedFoodList"
var selectedFood = ""
var servSize = ""
var servUnit = ""
val foodList = ArrayList<FoodListItem>()
var calorieValue = 0.0
var totalCalorieValue = 0.0


class SecondActivity : AppCompatActivity() {
    val apiKey = "gQkKB3Cyz3egJJLyVoU1YzrNQ9FpmqYNzao2oShh"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val welcomeTxt = intent.getStringExtra("nameText")
        findViewById<TextView>(R.id.user_name2).text = welcomeTxt
        Log.d("Fuad", "$welcomeTxt")


        val searchView = findViewById<SearchView>(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                Log.d("Search", "$query")
                val userInput = query.toString()
                findViewById<SearchView>(R.id.search_view).hideKeyboard()
                selectedFood = userInput

                apiCall(apiKey, userInput) { returnValue ->
                    val servingSize = returnValue[0]
                    val servingUnit = returnValue[1]
                    servSize = servingSize
                    servUnit = servingUnit

                    val servingSizeUnit = "$servingSize $servingUnit"
                    val foodCategory = returnValue[2]
                    findViewById<TextView>(R.id.food_category).text =
                        "Food Category:\n$foodCategory"
                    findViewById<TextView>(R.id.serving_size).text =
                        "Serving Size $servingSizeUnit"

                    //setting calorie values
                    calorieValue += returnValue[16].toDouble()


                    //calling the dialog box
                    val dialog = CustomDialog()
                    dialog.setReturnValue(returnValue)
                    dialog.show(supportFragmentManager, "CustomDialog")
                }

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text changes
                return true
            }
        })
        searchView.setOnSearchClickListener {
            // Handle search icon click
        }
    }

    fun addToRecyclerView(view: View) {
        //sends notification alert with toast msg when calorie reaches 500
        if (calorieValue > 500.0) {
            alert(calorieValue)
        }
        // 2000 is the general daily calorie intake max value
        if (calorieValue == 2000.0 || calorieValue > 2000.0) {
            showNotification()
        }
        val addedFood = FoodListItem(selectedFood, servSize, servUnit)
        //val foodList= ArrayList<FoodListItem>()
        foodList.add(addedFood)
        Log.d("FoodList", "$foodList")
        //setting calorie value
        findViewById<TextView>(R.id.tv_calorie).text = calorieValue.toString()
        totalCalorieValue += calorieValue

        //creating recycler view for food intake list
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = MyRecyclerAdapter(addToFoodList(foodList))
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Add a divider between rows -- Optional
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        selectedFood = ""
        servSize = ""
        servUnit = ""
    }

    fun deleteFromRecyclerView(view: View) {
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val gson = Gson()
        val editor = sharedPreferences.edit()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)


        // Create a SharedPreferences instance to retrieve data
        // Retrieve data using the key, default value is empty string in case no saved data in there
        val sharedPrefFood = sharedPreferences.getString("foodList", "") ?: ""

        if (sharedPrefFood.isNotEmpty()) {
            // Retrieve data using the key, default value is empty string in case no saved data in there
            val sType = object : TypeToken<ArrayList<FoodListItem>>() {}.type
            val savedFoodList = gson.fromJson<ArrayList<FoodListItem>>(sharedPrefFood, sType)

            savedFoodList.clear()
            Log.d("savedFoodList", savedFoodList.toString())

            val savedFoodJson = gson.toJson(savedFoodList)
            editor.putString("foodList", savedFoodJson)
            editor.apply()
            // clears from savedFoodList

            foodList.clear()
            recyclerView.adapter = MyRecyclerAdapter(savedFoodList)
            // Update the adapter
            recyclerView.adapter?.notifyDataSetChanged()

            // clears the calories
            calorieValue = 0.0
            findViewById<TextView>(R.id.tv_calorie).text = calorieValue.toString()
            MainActivity.calories = 0
            MainActivity.protein = 0
            MainActivity.carbs = 0
            MainActivity.fat = 0
        }

    }

    private fun addToFoodList(list: ArrayList<FoodListItem>): ArrayList<FoodListItem> {

        val foods = ArrayList<FoodListItem>()

        //selectedFood

        // The for loop will generate [size] amount of contact data and store in a list
        for (i in 0 until list.size) {
            Log.d("FoodList", "RecView Called")
            val food = FoodListItem(list[i].foodName, list[i].servingSize, list[i].servingUnit)
            foods.add(food)
        }
        // return the list of contacts
        return foods
    }

    //showing notification
    private fun showNotification() {
        val intent = Intent(this, NotificationService::class.java)

        startService(intent)
    }


    private fun saveData() {
        // Create a SharedPreferences instance for edit
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val foodListGson = gson.toJson(foodList)
        editor.putString("foodList", foodListGson)

        // save info
        //editor.putString("selectedFood", savedFood)

        // apply changes -- DO NOT FORGET!!!
        editor.apply()

    }

    private fun loadData() {
        val gson = Gson()

        // Create a SharedPreferences instance to retrieve data
        val sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        // Retrieve data using the key, default value is empty string in case no saved data in there
        val sharedPrefFood = sharedPreferences.getString("foodList", "") ?: ""
        if (sharedPrefFood.isNotEmpty()) {
            // Retrieve data using the key, default value is empty string in case no saved data in there
            val sType = object : TypeToken<ArrayList<FoodListItem>>() {}.type
            val savedFoodList = gson.fromJson<ArrayList<FoodListItem>>(sharedPrefFood, sType)
            // Clear the list and in case there are some items
            foodList.clear()
            for (food in savedFoodList) {

                foodList.add(food)
                Log.d("FoodList shared pref", "$food")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
        addToFoodList(foodList)
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    //function sends notifcation sound and toast msg
    private fun alert(calorie: Double) {
        val caloriePercentage = (calorie / 2000) * 100
        val caloriePercentageFormatted = String.format("%.2f", caloriePercentage)
        val mp = MediaPlayer.create(this, R.raw.alert)
        mp.start()
        Toast.makeText(
            this,
            "$caloriePercentageFormatted% of Max Calorie Consumed for Today",
            Toast.LENGTH_SHORT
        ).show()

    }


    fun apiCall(apiKey: String, query: String, callback: (Array<String>) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val foodSearchAPI = retrofit.create(FoodSearchService::class.java)
        foodSearchAPI.getFoodInfo(apiKey, query).enqueue(object : Callback<SearchedFood> {
            override fun onResponse(
                call: Call<SearchedFood>,
                response: Response<SearchedFood>
            ) {
                val body = response.body()
                if (body == null) {
                    Log.d("Fuad", "Invalid response")
                    callback(emptyArray())
                } else {
                    if (body.foods.size == 0) {
                        Toast.makeText(
                            this@SecondActivity,
                            "Enter a valid food name",
                            Toast.LENGTH_SHORT
                        ).show()

                        return
                        return
                    }
                    val returnValue = Array<String>(20) { "" }
                    setNutrientValues(body, returnValue)
                    setDailyValuePercentageUnits(body, returnValue)
                    val searchedFood = body.foods?.get(0)
                    //searching calorie and protein
                    //searching calorie: Energy is the keyword inside the api response
                    val calorieValue = searchedFood?.let { macroNutrientsValues("Energy", it) }

                    //searching protein
                    //handling it seperately since protein does not have dv value
                    val proteinValue = searchedFood?.let { macroNutrientsValues("Protein", it) }
                    Log.d("macroProtein", "$proteinValue")
//

                    //searching carb and fat
                    val carbValue =
                        searchedFood?.let { macroNutrientsDV("Carbohydrate, by difference", it) }
                    Log.d("macroCarb", "$carbValue")
//              searching fat
//
//
                    val fatValue = searchedFood?.let { macroNutrientsDV("Total lipid (fat)", it) }
                    Log.d("macroFat", "$fatValue")


                    // Update global values for others to access
                    MainActivity.protein += proteinValue?.toInt() ?: 0
                    MainActivity.carbs += carbValue?.toInt() ?: 0
                    MainActivity.fat += fatValue?.toInt() ?: 0
                    MainActivity.calories += calorieValue?.toInt() ?: 0

                    //contains calorie value
                    returnValue[16] = calorieValue.toString()

                    callback(returnValue)
                }
            }


            override fun onFailure(call: Call<SearchedFood>, t: Throwable) {
                Log.d("Fuad", "On failure : $t")
                callback(emptyArray())
            }
        })
    }

    //searching for carb and fat dv
    private fun macroNutrientsDV(nutrients: String, searchedFood: foodDetails): Double {
        val nutrient = searchedFood.foodNutrients?.find { it.nutrientName == nutrients }
            ?: // handle nutrient not found
            return 0.0
        //since we just need two values

        return nutrient.percentDailyValue
    }

    //searching for protein and calorie values
    private fun macroNutrientsValues(nutrients: String, searchedFood: foodDetails): Double {
        val nutrient = searchedFood.foodNutrients?.find { it.nutrientName == nutrients }
            ?: // handle nutrient not found
            return 0.0


        return nutrient.value
    }


    //extracting values for dialog box nutrients
    private fun setNutrientValues(body: SearchedFood, returnValue: Array<String>) {

        //since the api returns value even with wrong query
        //but the foods[] object is empty in that case
        if (body.foods.isNullOrEmpty()) {
            Log.d("ApiCall", "Invalid Input")
            exitProcess(-1)
        }
        returnValue[0] = body.foods[0].servingSize.toString()
        returnValue[1] = body.foods[0].servingSizeUnit
        returnValue[2] = body.foods[0].foodCategory
        returnValue[3] = body.foods[0].foodNutrients[0].value.toString()
        returnValue[4] = body.foods[0].foodNutrients[7].nutrientName    // Protein
        returnValue[5] = body.foods[0].foodNutrients[8].nutrientName    // Carbs
        returnValue[6] = body.foods[0].foodNutrients[13].nutrientName   // Fats
        returnValue[7] = body.foods[0].foodNutrients[11].nutrientName   // Fiber
        returnValue[8] = body.foods[0].foodNutrients[10].nutrientName   // Sugar
        returnValue[9] = body.foods[0].foodNutrients[12].nutrientName   // Potassium
    }

    //dialog box nutrient DV values
    private fun setDailyValuePercentageUnits(body: SearchedFood, returnValue: Array<String>) {
        returnValue[10] =
            body.foods[0].foodNutrients[7].percentDailyValue.toString()    // Protein Unit
        returnValue[11] =
            body.foods[0].foodNutrients[8].percentDailyValue.toString()    // Protein Unit
        returnValue[12] =
            body.foods[0].foodNutrients[13].percentDailyValue.toString()    // Protein Unit
        returnValue[13] = body.foods[0].foodNutrients[11].percentDailyValue.toString()
        returnValue[14] = body.foods[0].foodNutrients[10].percentDailyValue.toString()
        returnValue[15] = body.foods[0].foodNutrients[12].percentDailyValue.toString()
    }


}




