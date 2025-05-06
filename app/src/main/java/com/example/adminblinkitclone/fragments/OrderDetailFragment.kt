package com.example.adminblinkitclone.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.adapter.AdapterCartProducts
import com.example.adminblinkitclone.databinding.FragmentOrderDetailBinding
import com.example.adminblinkitclone.utils
import com.example.adminblinkitclone.viewModels.AdminViewModel
import kotlinx.coroutines.launch
import kotlin.getValue


class OrderDetailFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var adapterCartProducts: AdapterCartProducts
    private var status = 0
    private var currentStatus = 0
    private var orderId = " "

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        getValues()
        setupStatus(status)
        onBackButtonClicked()
        viewLifecycleOwner.lifecycleScope.launch { getOrderedProducts() }

        setStatusBarColor()

        onChangeStatusButtonClicked()
        return binding.root
    }

    private fun onChangeStatusButtonClicked() {
        binding.btnChangeStatus.setOnClickListener {
            val popUpMenu = PopupMenu(requireContext(),it)
            popUpMenu.menuInflater.inflate(R.menu.pop_up,popUpMenu.menu)
            popUpMenu.show()
            popUpMenu.setOnMenuItemClickListener {menu ->
            when(menu.itemId){

                R.id.menuReceived ->{
                    currentStatus = 1
                    if(currentStatus > status){
                        status = 1
                        setupStatus(1)
                        viewModel.updateOrderStatus(orderId,1)
                    }
                    else{
                        utils.showToast(requireContext(),"Order is Already Received")
                    }

                    true
                }
                R.id.menuDispatched ->{
                    currentStatus = 2
                    if(currentStatus > status){
                        status = 2
                        setupStatus(2)
                        viewModel.updateOrderStatus(orderId,2)
                    }
                    else{
                        utils.showToast(requireContext(),"Order is Already Dispatched")
                    }
                    true
                }
                R.id.menuDelivered ->{
                    currentStatus = 3
                    if(currentStatus > status){
                        status = 3
                        setupStatus(3)
                        viewModel.updateOrderStatus(orderId,3)
                    }
                    else{
                        utils.showToast(requireContext(),"Order is Already Delivered")
                    }
                    true
                }

                else -> false

            }

            }
        }
    }


    private suspend fun getOrderedProducts() {
        viewModel.getOrderedProducts(orderId).collect { cartList ->
            adapterCartProducts = AdapterCartProducts()
            binding.rvProductsItem.adapter = adapterCartProducts
            adapterCartProducts.differ.submitList(cartList)
        }
    }

    private fun setupStatus(status : Int) {
        val green = ContextCompat.getColorStateList(requireContext(), R.color.green)
        val views = listOf(binding.iv1, binding.iv2, binding.view1, binding.view2, binding.iv3, binding.iv4, binding.view3)

        when (status) {
            0 -> views[0].backgroundTintList = green
            1 -> views.subList(0, 3).forEach { it.backgroundTintList = green }
            2 -> views.subList(0, 5).forEach { it.backgroundTintList = green }
            3 -> views.forEach { it.backgroundTintList = green }
        }
    }

    private fun getValues() {
        val bundle = arguments
        status = bundle?.getInt("status") ?: 0
        orderId = bundle?.getString("orderId").orEmpty()
        binding.tvUserAddress.text = bundle?.getString("userAddress").orEmpty()
    }

    private fun onBackButtonClicked() {
        binding.tbOrderDetailFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_orderDetailFragment_to_orderFragment)
        }
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