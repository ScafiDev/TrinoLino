package com.example.trinolino.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trinolino.Classi.MenuItem
import com.example.trinolino.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> get() = _menuItems

    fun fetchMenuItems(context: Context) {
        Client.retrofit.getMenuItems().enqueue(object : Callback<List<MenuItem>> {
            override fun onResponse(call: Call<List<MenuItem>>, response: Response<List<MenuItem>>) {
                if (response.isSuccessful) {
                    _menuItems.postValue(response.body())
                } else {
                    Toast.makeText(context, "Failed to fetch menu items: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
