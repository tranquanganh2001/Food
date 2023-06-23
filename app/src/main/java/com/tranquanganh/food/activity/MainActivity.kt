package com.tranquanganh.food.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tranquanganh.food.R
import com.tranquanganh.food.db.MealDatabase
import com.tranquanganh.food.vmvm.HomeViewModelFactory
import com.tranquanganh.food.vmvm.Homemvvm

class MainActivity : AppCompatActivity() {

    val viewModel : Homemvvm by lazy(){
        val mealDatabase = MealDatabase.getIstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelFactory)[Homemvvm::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
}