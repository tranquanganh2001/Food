package com.tranquanganh.food.vmvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tranquanganh.food.db.MealDatabase

class HomeViewModelFactory (
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return Homemvvm(mealDatabase) as T
    }
}