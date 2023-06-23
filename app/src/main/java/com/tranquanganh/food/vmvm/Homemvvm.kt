package com.tranquanganh.food.vmvm
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranquanganh.food.db.MealDatabase
import com.tranquanganh.food.pojo.*
import com.tranquanganh.food.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query


class Homemvvm(
private val mealDatabase: MealDatabase
) :ViewModel(){
    private var randomMealLiveData = MutableLiveData<Meal> ()
    private var poppularItemsLiData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchMealsLiveData = MutableLiveData<List<Meal>>()

    fun getRandomMeal()
    {
        RetrofitInstance.api.randomMeal().enqueue(object : Callback<MeaList> {
            override fun onResponse(call: Call<MeaList>, response: Response<MeaList>) {
                if (response.body() != null) {
                    val randomMeal= response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MeaList>, t: Throwable) {
                Log.d("TEST" , "Respon fall")
            }
        })
    }

    fun getPoppularItems()
    {
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null)
                {
                    poppularItemsLiData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("TAG",t.message.toString())
            }

        })
    }

    fun getCategories()
    {
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>
        {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                  response.body()?.let {
                      categoryList -> categoriesLiveData.postValue(categoryList.categories)
                  }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
              Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal)
    {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun getMealById(id :String)
    {
        RetrofitInstance.api.getIDmeal(id).enqueue(object :Callback<MeaList> {
            override fun onResponse(call: Call<MeaList>, response: Response<MeaList>) {
                val meal = response.body()?.meals?.first()
                meal?.let{meal->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MeaList>, t: Throwable) {
               Log.e("HomeViewModel",t.message.toString())
            }

        })
    }

    fun inserMeal(meal : Meal)
    {
        viewModelScope.launch{
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object :Callback<MeaList>{
            override fun onResponse(call: Call<MeaList>, response: Response<MeaList>) {
                val mealsList = response.body()?.meals
                mealsList?.let{
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MeaList>, t: Throwable) {
                Log.e("HomeViewModel",t.message.toString())
            }

        }

    )
    fun observeSeachdMealsLiveData() : LiveData<List<Meal>> = searchMealsLiveData

    fun observeRandomMealLivedata():LiveData<Meal>
    {
        return randomMealLiveData
    }

    fun observePopularItemsLiveData():LiveData<List<MealsByCategory>>
    {
        return poppularItemsLiData
    }

    fun obseverCategoriesLiveData():LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData():LiveData<List<Meal>>
    {
        return favoritesMealsLiveData
    }

    fun observeBottomSheetMeal() : LiveData<Meal> = bottomSheetMealLiveData
}