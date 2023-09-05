package com.example.diettracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerAdapter(val foodList: ArrayList<FoodListItem>) :
    RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>() {
    //Ref: Blackboard example

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file
        val foodName = itemView.findViewById<TextView>(R.id.food_name)
        val foodServingSize = itemView.findViewById<TextView>(R.id.food_servingSize)
        val foodServingUnit = itemView.findViewById<TextView>(R.id.food_servingUnit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder
        // To understand how RecyclerAdapter works , look at the logcat
        Log.d("RecView", "onCreateViewHolder called")

        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentItem = foodList[position]
        holder.foodName.text = currentItem.foodName
        holder.foodServingSize.text = "Serving size:${currentItem.servingSize}"
        // food calories
        holder.foodServingUnit.text = currentItem.servingUnit
    }

}