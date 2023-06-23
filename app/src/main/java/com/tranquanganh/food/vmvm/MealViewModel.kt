package com.tranquanganh.food.vmvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tranquanganh.food.db.MealDatabase
import com.tranquanganh.food.retrofit.RetrofitInstance
import com.tranquanganh.food.pojo.MeaList
import com.tranquanganh.food.pojo.Meal
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MealViewModel(
  val  mealDatabase : MealDatabase
) : ViewModel() {
    var mealDetailsLiveData = MutableLiveData<Meal>()

     fun getIDMeal( id:String)
     {
         RetrofitInstance.api.getIDmeal(id)
             .enqueue(object : Callback, retrofit2.Callback<MeaList> {
                 override fun onResponse(call: Call<MeaList>, response: Response<MeaList>) {
                 if(response.body() != null)
                 {
                     var idMeal_ = response.body()!!.meals[0]
                     mealDetailsLiveData.value = idMeal_
                 }
                 else
                 {
                   return
                 }

                 }

                 override fun onFailure(call: Call<MeaList>, t: Throwable) {

                 }

             })
     }
     fun observerLiveData():LiveData<Meal>
     {
         return mealDetailsLiveData
     }

    fun inserMeal(meal : Meal)
    {
        viewModelScope.launch{
            mealDatabase.mealDao().upsert(meal)
        }
    }





}