package com.example.adminblinkitclone.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.adapter.AdapterOrders
import com.example.adminblinkitclone.databinding.FragmentOrderBinding
import com.example.adminblinkitclone.models.OrderedItems
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch

class OrderFragment : Fragment() {


    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapter: AdapterOrders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        setStatusBarColor()
        getAllOrders()
        return binding.root
    }

    private fun getAllOrders() {
        binding.shimmerViewContainer.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllOrders().collect { orderList ->
                if (orderList.isNotEmpty()) {
                    val orderedList = ArrayList<OrderedItems>()
                    for (orders in orderList) {
                        val title = StringBuilder()
                        var totalPrice = 0

                        for (product in orders.orderList!!) {
                            val price = product.productPrice?.substring(1)?.toInt()
                            val itemCount = product.productCount!!
                            totalPrice += (price?.times(itemCount) ?: 0)

                            title.append("${product.productCategory}")
                        }

                        val orderedItems = OrderedItems(
                            orders.orderId,
                            orders.orderDate,
                            orders.orderStatus,
                            title.toString(),
                            totalPrice,
                            orders.userAddress
                        )
                        orderedList.add(orderedItems)
                    }

                    adapter = AdapterOrders(requireContext(), ::onOrderItemViewClicked)
                    binding.rvOrders.adapter = adapter
                    adapter.differ.submitList(orderedList)
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.rvOrders.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                } else {
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.rvOrders.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onOrderItemViewClicked(orderedItems: OrderedItems) {
        val bundle = Bundle().apply {
            putInt("status", orderedItems.itemStatus ?: 0)
            putString("orderId", orderedItems.orderId)
            putString("userAddress",orderedItems.userAddress)
        }
        findNavController().navigate(R.id.action_orderFragment_to_orderDetailFragment, bundle)
    }

    private fun setStatusBarColor(){
        activity?.window?.apply{
            val StatusBarColors = ContextCompat.getColor(requireContext(), R.color.orange)
            statusBarColor = StatusBarColors
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}