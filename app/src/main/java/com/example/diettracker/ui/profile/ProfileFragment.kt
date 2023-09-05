package com.example.diettracker.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.request.transition.Transition
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diettracker.databinding.FragmentProfileBinding
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.diettracker.R
import org.w3c.dom.Text
import com.example.diettracker.calculateBmiAndFatPercentage

class ProfileFragment : Fragment() {

    var uri: Uri? = null

    companion object{
        val request:Int = 1
    }
private var _binding: FragmentProfileBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(

    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
      val homeViewModel =
          ViewModelProvider(this).get(ProfileViewModel::class.java)

      _binding = FragmentProfileBinding.inflate(inflater, container, false)
      val root: View = binding.root

      val textView: TextView = binding.textHome
      homeViewModel.text.observe(viewLifecycleOwner) {
          textView.text = it
      }

      binding.btnUpload.setOnClickListener {
          val intent = Intent(Intent.ACTION_GET_CONTENT)
          intent.type = "image/*"
          startActivityForResult(intent, request)
      }
      loadData()

      val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
      val imageUri = sharedPref?.getString("profileImageUri", null)
      Glide.with(this).load(imageUri).into(binding.imgProfile)

      return root
  }


    // Lets the user upload a profile image and stores the location in shared preferences
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navImage = activity?.findViewById<ImageView>(R.id.imgProfileHeader)

        if(requestCode == request
            && resultCode == Activity.RESULT_OK
            && data!=null && data.data !=null){
            uri = data.data

            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            sharedPref?.edit()?.putString("profileImageUri",data.data.toString())?.apply()

            Glide.with(this).load(uri).into(binding.imgProfile)

        }
    }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadData() {
        // Create a SharedPreferences instance to retrieve data
        //val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

        // Get name
        val sharedPreferences = activity?.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
        val name = sharedPreferences?.getString("name", "default_value")
        val weight = sharedPreferences?.getString("weight", "default_value")
        val height = sharedPreferences?.getString("height", "default_value")
        val age = sharedPreferences?.getString("age", "default_value")

        binding.txtProfile.text = name

        val gender = sharedPreferences?.getString("gender", "default_value")
        Log.d("Profile fragment","$gender")

        //calculating value using the user input
        if (gender != null) {
            val myResult=calculateBmiAndFatPercentage(height?.toInt()!!,weight?.toInt()!!,age?.toInt()!!,gender)
            Log.d("BMI","BMI:$myResult")
            val bmiText:TextView=binding.txtDisplayBMI
            val fatText:TextView=binding.txtDisplayFat
            val statusText:TextView=binding.txtDisplayStatus

            bmiText.text=myResult[0]
            fatText.text=myResult[1]
            statusText.text=myResult[2]
        }

        val nameView: TextView = binding.txtName
        val weightView: TextView = binding.txtWeight
        val heightView: TextView = binding.txtHeight
        val ageView: TextView = binding.txtAge

        nameView.text = name
        weightView.text = "$weight lbs"
        heightView.text = "$height cm"
        ageView.text = age
    }
}