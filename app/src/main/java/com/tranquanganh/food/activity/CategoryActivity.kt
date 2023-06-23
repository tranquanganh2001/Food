package com.tranquanganh.food.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tranquanganh.food.R
import com.tranquanganh.food.adaters.CategoryMealsAdapter
import com.tranquanganh.food.databinding.ActivityCategoryBinding
import com.tranquanganh.food.fragment.HomeFragment
import com.tranquanganh.food.vmvm.CategoryMealsViewModel

class CategoryActivity : AppCompatActivity() {

    lateinit var binding :ActivityCategoryBinding
    lateinit var categoyMealsViewModel :CategoryMealsViewModel
    lateinit var categoryMealsAdapter : CategoryMealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preparRecyclerView()

        categoyMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoyMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGDRY_NAME)!!)

        categoyMealsViewModel.observeMealsLiveData().observe(this,{mealsList->
            categoryMealsAdapter.setMealsList(mealsList)


        })
    }

    private fun preparRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }
}