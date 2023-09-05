package com.example.diettracker

import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.diettracker.ui.profile.ProfileFragment

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.system.exitProcess

val BASE_URL = "https://api.nal.usda.gov/"

class MainActivity : AppCompatActivity() {

    companion object{
        var protein:Int=0
        var carbs:Int=0
        var fat:Int=0
        var calories:Int=0
        var maxCals:Int=2500
    }

    private lateinit var nameText: EditText
    private lateinit var ageText: EditText
    private lateinit var heightText: EditText
    private lateinit var weightText: EditText
    private lateinit var genderText: String



    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //saving data in view model
        nameText = findViewById(R.id.user_name)
        ageText = findViewById(R.id.user_age)
        heightText = findViewById(R.id.user_height)
        weightText = findViewById(R.id.user_weight)
        genderText = ""

        //hiding keyboard after final input
        findViewById<EditText>(R.id.user_weight).hideKeyboard()


        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)



        radioGroup.setOnCheckedChangeListener{group, checkedId ->
            val checkedRadioButton = findViewById<RadioButton>(checkedId)
            var gender=checkedRadioButton.text.toString()

            if(genderText == "Female"){
                maxCals = 2000
            }

            Log.d("Radio Check","$gender")
            genderText=gender

            val height = Integer.parseInt(heightText.text.toString())
            val weight = Integer.parseInt(weightText.text.toString())
            val age = Integer.parseInt(ageText.text.toString())
            //testing bmi function
            val myResult=calculateBmiAndFatPercentage(height,weight,age,gender)
        }
    }

    private fun saveData() {
        // Create a SharedPreferences instance for edit
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // save info
        editor.putString("name", nameText.text.toString())
        editor.putString("age", ageText.text.toString())
        editor.putString("height", heightText.text.toString())
        editor.putString("weight", weightText.text.toString())

        editor.putString("gender", genderText)

        editor.apply()

    }

    private fun loadData() {

        // Create a SharedPreferences instance to retrieve data
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        // Get name
        val name = sharedPreferences.getString("name", "")
        val age = sharedPreferences.getString("age", "")
        val height = sharedPreferences.getString("height", "")
        val weight = sharedPreferences.getString("weight", "")

        // Set the obtained values to editTexts
        nameText.setText(name)
        ageText.setText(age)
        heightText.setText(height)
        weightText.setText(weight)
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun onStop() {
        saveData()
        super.onStop()
    }

    //fun for getting into navdrawer activity
    fun proceedButton(view: View) {
        val myIntent = Intent(this, NavDrawer::class.java)
        val name = findViewById<EditText>(R.id.user_name).text.toString()
        myIntent.putExtra("nameText", "Hello $name!")
        startActivity(myIntent)
    }
}


// Helper function to hide the keyboard for any view/widget
fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}
//function to calculate bmi and fat percentage
fun calculateBmiAndFatPercentage(heightCm: Int, weightLbs: Int, age: Int, gender: String): ArrayList<String> {
    // Convert height from cm to m
    val heightM = heightCm / 100.0

    // Convert weight from lbs to kg
    val weightKg = weightLbs / 2.20462

    // Calculate BMI and its suggestion
    var bmiSuggestion = ""
    val bmi = weightKg / (heightM * heightM)
    val formattedBMIString = String.format("%.2f", bmi)

    when {
        bmi < 18.5 -> bmiSuggestion = "Underweight"
        bmi <= 24.9 -> bmiSuggestion = "Normal weight"
        bmi <= 29.9 -> bmiSuggestion = "Overweight"
        else -> bmiSuggestion = "Obese"
    }

    // Calculate body fat percentage using the BMI and gender
    val fatPercentage = if (gender == "Male") {
        (1.2 * bmi) + (0.23 * age) - 16.2
    } else {
        (1.2 * bmi) + (0.23 * age) - 5.4
    }
    val formattedFP=String.format("%.2f",fatPercentage)

    // Return the results as an ArrayList of strings
    return arrayListOf(formattedBMIString, formattedFP, bmiSuggestion)
}

