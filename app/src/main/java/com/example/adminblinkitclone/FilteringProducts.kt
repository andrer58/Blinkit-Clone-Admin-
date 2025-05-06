package com.example.adminblinkitclone

import android.widget.Filter
import com.example.adminblinkitclone.adapter.AdapterProduct
import com.example.adminblinkitclone.models.Product
import java.util.Locale

class FilteringProducts(
    private val adapter: AdapterProduct,
    private val originalList: ArrayList<Product>
) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()

        if (!constraint.isNullOrBlank()) {
            val queryWords = constraint.toString()
                .trim()
                .lowercase(Locale.getDefault())
                .split(" ")

            val filteredList = originalList.filter { product ->
                val searchableText = listOfNotNull(
                    product.productTitle,
                    product.productCategory,
                    product.productPrice?.toString(),
                    product.productType
                ).joinToString(" ").lowercase(Locale.getDefault())

                queryWords.any { word -> searchableText.contains(word) }
            }

            result.values = ArrayList(filteredList)
            result.count = filteredList.size
        } else {
            result.values = originalList
            result.count = originalList.size
        }

        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        val filtered = results?.values as? ArrayList<Product> ?: originalList
        adapter.differ.submitList(filtered)
    }
}
