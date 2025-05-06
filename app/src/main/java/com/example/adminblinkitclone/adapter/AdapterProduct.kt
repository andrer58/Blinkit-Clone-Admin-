package com.example.adminblinkitclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel
import com.example.adminblinkitclone.FilteringProducts
import com.example.adminblinkitclone.databinding.ItemViewProductBinding
import com.example.adminblinkitclone.models.Product

class AdapterProduct(
    val onEditButtonClicked: (Product) -> Unit
) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(), Filterable {

    class ProductViewHolder(val binding: ItemViewProductBinding) : RecyclerView.ViewHolder(binding.root)

    // DiffUtil setup for AsyncListDiffer
    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    // Filter-related variables
    private lateinit var filteringProducts: FilteringProducts
    var originalList = ArrayList<Product>()

    fun setProductList(list: ArrayList<Product>) {
        originalList = list
        differ.submitList(list)
        filteringProducts = FilteringProducts(this, originalList)
    }

    override fun getFilter(): Filter {
        return filteringProducts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            val imageList = ArrayList<SlideModel>()
            product.productImagesUris?.forEach {
                imageList.add(SlideModel(it.toString()))
            }

            ivImageSlider.setImageList(imageList)
            tvProductTitle.text = product.productTitle

            val quantity = product.productQuantity.toString() + product.productUnit
            tvProductQuantity.text = quantity

            tvProductPrice.text = "₹${product.productPrice}"
        }

        holder.itemView.setOnClickListener {
            onEditButtonClicked(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
