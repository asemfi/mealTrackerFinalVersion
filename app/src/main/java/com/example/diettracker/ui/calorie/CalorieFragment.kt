package com.example.diettracker.ui.calorie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diettracker.MainActivity
import com.example.diettracker.R
import com.example.diettracker.SecondActivity
import com.example.diettracker.databinding.FragmentCalorieBinding
import com.example.diettracker.totalCalorieValue

class CalorieFragment : Fragment() {

private var _binding: FragmentCalorieBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val slideshowViewModel =
            ViewModelProvider(this).get(CalorieViewModel::class.java)

    _binding = FragmentCalorieBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textSlideshow
    slideshowViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it

    }
    // Set button click listener
      val btnAddList = binding.btnAddList
      btnAddList.setOnClickListener(View.OnClickListener { // Handle the button click here
          val myIntent = Intent(activity, SecondActivity::class.java)
          val sharedPreferences = activity?.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
          val name = sharedPreferences?.getString("name","Buddy")

          myIntent.putExtra("nameText", "Hello $name!")
          startActivity(myIntent)
      })

      return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        binding.progressBar2.max = MainActivity.maxCals
        binding.progressBar2.progress = MainActivity.calories
        binding.txtPercent.text = MainActivity.calories.toString()
    }
}