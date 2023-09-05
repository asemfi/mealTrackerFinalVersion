package com.example.diettracker.ui.food

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.diettracker.MainActivity
import com.example.diettracker.databinding.FragmentFoodBinding
import java.lang.Math.round
import kotlin.math.roundToInt



class FoodFragment : Fragment() {
private var _binding: FragmentFoodBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val galleryViewModel =
            ViewModelProvider(this).get(FoodViewModel::class.java)

    _binding = FragmentFoodBinding.inflate(inflater, container, false)
    val root: View = binding.root
    val textView: TextView = binding.textGallery

    galleryViewModel.text.observe(viewLifecycleOwner) {
      textView.text = it
    }

    return root
  }
override fun onResume(){
  super.onResume()

  // Note that carbs and fat have been switched
  binding.barProtein.progress = MainActivity.protein
  binding.barCarbs.progress = MainActivity.fat
  binding.barFats.progress = MainActivity.carbs
}
override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
