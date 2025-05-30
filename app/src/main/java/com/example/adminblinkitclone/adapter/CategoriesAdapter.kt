package com.example.adminblinkitclone.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminblinkitclone.databinding.ItemViewProductCategoryBinding
import com.example.adminblinkitclone.models.Categories
import android.view.LayoutInflater

class CategoriesAdapter (
    private val categoryArrayList: ArrayList<Categories>,
    val onCategoryClicked: (Categories) -> Unit
): RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {


    class CategoriesViewHolder(val binding : ItemViewProductCategoryBinding) :  RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : CategoriesViewHolder {
            return CategoriesViewHolder(ItemViewProductCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

        }

        override fun getItemCount() : Int {
            return categoryArrayList.size
        }

        override fun onBindViewHolder(holder : CategoriesViewHolder, position : Int) {
            val category = categoryArrayList[position]
            holder.binding.apply{
                ivCategoryImage.setImageResource(category.icon)
                tvCategoryTitle.text = category.category

            }
            holder.itemView.setOnClickListener {
                onCategoryClicked(category)
            }
        }

}


