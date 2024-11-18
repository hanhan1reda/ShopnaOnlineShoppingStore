package com.example.shopna.presentation.view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.shopna.data.model.AddOrRemoveCartResponse
import com.example.shopna.data.model.AddOrderRequest
import com.example.shopna.data.model.AddOrderResponse
import com.example.shopna.data.model.GetCartResponse
import com.example.shopna.data.model.GetOrderResponse
import com.example.shopna.data.model.OrderDetailsResponse
import com.example.shopna.data.model.UpdateCartResponse
import com.example.shopna.data.network.RetrofitInstance
import com.example.shopna.presentation.view.home.updateLocale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class CartViewModel(navigator: Navigator, val context: Context) : ViewModel() {

    private val _cart = MutableStateFlow<AddOrRemoveCartResponse?>(null)
    val cart: StateFlow<AddOrRemoveCartResponse?> get() = _cart

    private val _addOrderResponse = MutableStateFlow<AddOrderResponse?>(null)
    val addOrderResponse: StateFlow<AddOrderResponse?> get() = _addOrderResponse


    private val _orderDetails = MutableStateFlow<OrderDetailsResponse?>(null)
    val orderDetails: StateFlow<OrderDetailsResponse?> get() = _orderDetails

    private val _orders = MutableStateFlow<GetOrderResponse?>(null)
    val orders: StateFlow<GetOrderResponse?> get() = _orders

    private val _cartData = MutableStateFlow<GetCartResponse?>(null)
    val cartData: StateFlow<GetCartResponse?> get() = _cartData
    private val _updateCart = MutableStateFlow<UpdateCartResponse?>(null)
    val updateCart: StateFlow<UpdateCartResponse?> get() = _updateCart

    private val authViewModel: AuthViewModel = AuthViewModel(navigator, context)
    private val api = RetrofitInstance.apiClient
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _isUpdateCartLoading = MutableStateFlow(false)
    val isUpdateCartLoading: StateFlow<Boolean> get() = _isUpdateCartLoading



    private var dataFetched = false

    init {
        val sharedPreferences =context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val languageCode = java.util.Locale.getDefault().language
        updateLocale(context,sharedPreferences.getString("langCode",languageCode)?:languageCode)
        RetrofitInstance.setLanguage(sharedPreferences.getString("langCode",languageCode)?:languageCode)

    }

    fun fetchCartData(forceRefresh: Boolean = false) {
        if (dataFetched && !forceRefresh) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                authViewModel.getAuthToken()?.let { RetrofitInstance.setAuthToken(it) }
                val response = api.getCart()
                if (response.isSuccessful) {
                    _cartData.value = response.body()
                    dataFetched = true
                    _isLoading.value = false

                } else {
                    Log.e("Cart", "Failed to fetch cart: ${response.errorBody()?.string()}")
                    _isLoading.value = false

                }
            } catch (e: Exception) {
                Log.e("Cart", "Error: ${e.message}")
                _isLoading.value = false

            } finally {

            }
        }
    }
    fun getOrders() {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                authViewModel.getAuthToken()?.let { RetrofitInstance.setAuthToken(it) }
                val response = api.getOrders()
                if (response.isSuccessful) {
                    _orders.value = response.body()
                    _isLoading.value = false

                } else {
                    Log.e("orders", "Failed to fetch orders: ${response.errorBody()?.string()}")
                    _isLoading.value = false

                }
            } catch (e: Exception) {
                Log.e("orders", "Error: ${e.message}")
                _isLoading.value = false

            } finally {

            }
        }
    }
    fun addOrder(addOrderRequest: AddOrderRequest) {

        viewModelScope.launch {
            _isLoading.value = true
            try {
                authViewModel.getAuthToken()?.let { RetrofitInstance.setAuthToken(it) }
                val response = api.addOrder(addOrderRequest)
                if (response.isSuccessful) {
                    _addOrderResponse.value = response.body()
                    fetchCartData(forceRefresh = true)
                    _isLoading.value = false
                    Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT).show()


                } else {
                    Log.e("orders", "Failed to add orders: ${response.errorBody()?.string()}")
                    _isLoading.value = false

                }
            } catch (e: Exception) {
                Log.e("orders", "Error: ${e.message}")
                _isLoading.value = false

            } finally {

            }
        }
    }

    fun orderDetails(orderId: Int){

        viewModelScope.launch {
            _isLoading.value = true
            try {
                authViewModel.getAuthToken()?.let { RetrofitInstance.setAuthToken(it) }
                val response = api.getOrderDetails(orderId)
                if (response.isSuccessful) {
                    _orderDetails.value = response.body()
                    _isLoading.value = false


                } else {
                    Log.e("orders", "Failed: ${response.errorBody()?.string()}")
                    _isLoading.value = false

                }
            } catch (e: Exception) {
                Log.e("orders", "Error: ${e.message}")
                _isLoading.value = false

            } finally {

            }
        }
    }

    fun addOrDeleteCart(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                authViewModel.getAuthToken()?.let { RetrofitInstance.setAuthToken(it) }
                val response = api.addOrDeleteCart(productId)
                if (response.isSuccessful) {
                    Log.d("Cart", "Product added/removed from cart")
                    _cart.value = response.body()
                    fetchCartData(forceRefresh = true).let {
                        _isLoading.value = false

                    } // Refresh cart after update
                } else {
                    Log.e("Cart", "Failed to add/remove product to/from cart")
                }
            } catch (e: Exception) {
                Log.e("Cart", "Error: ${e.message}")
            } finally {
            }
        }
    }
    fun updateCart(productId: Int , quantity:Int) {
        viewModelScope.launch {
            _isUpdateCartLoading.value = true
            try {
                authViewModel.getAuthToken()?.let { RetrofitInstance.setAuthToken(it) }
                val response = api.updateCart(productId,quantity)
                if (response.isSuccessful) {
                    Log.d("Cart", "Product updated successfully")
                    _updateCart.value = response.body()
                    fetchCartData(forceRefresh = true).let {
                        _isUpdateCartLoading.value = false

                    } // Refresh cart after update


                } else {
                    Log.e("Cart", "Failed to update product")
                    _isUpdateCartLoading.value = false

                }
            } catch (e: Exception) {
                Log.e("Cart", "Error: ${e.message}")
                _isUpdateCartLoading.value = false

            } finally {
            }
        }
    }
}


