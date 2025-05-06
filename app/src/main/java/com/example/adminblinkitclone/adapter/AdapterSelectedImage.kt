package com.example.adminblinkitclone.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adminblinkitclone.databinding.ItemViewImageSelectionBinding

class AdapterSelectedImage(val imageUris : ArrayList<Uri>) : RecyclerView.Adapter<AdapterSelectedImage.SelectedImageViewHolder>(){

    inner class SelectedImageViewHolder(val binding : ItemViewImageSelectionBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedImageViewHolder {

        return SelectedImageViewHolder(ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(
        holder: SelectedImageViewHolder,
        position: Int
    ) {
          val image = imageUris[position]
          holder.binding.apply {
              ivImage.setImageURI(image)
          }

        holder.binding.closeButton.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                imageUris.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(currentPosition, imageUris.size)
            }
        }
    }

    override fun getItemCount(): Int {

        return imageUris.size
    }


}