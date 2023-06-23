package com.tranquanganh.food.retrofit

import com.tranquanganh.food.pojo.CategoryList
import com.tranquanganh.food.pojo.MealsByCategoryList
import com.tranquanganh.food.pojo.MeaList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun randomMeal() : Call<MeaList>

    @GET("lookup.php?")
    fun getIDmeal(@Query("i") id:String) :Call<MeaList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName :String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories():Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) :Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s")searchQuery: String):Call<MeaList>
}