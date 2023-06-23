package com.tranquanganh.food.activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tranquanganh.food.R
import com.tranquanganh.food.databinding.ActivityMealBinding
import com.tranquanganh.food.db.MealDatabase
import com.tranquanganh.food.fragment.HomeFragment
import com.tranquanganh.food.vmvm.MealViewModel
import com.tranquanganh.food.pojo.Meal
import com.tranquanganh.food.vmvm.MealViewModelFactory


class MealActivity : AppCompatActivity() {
    lateinit var binding: ActivityMealBinding
    lateinit var mealId:String
    lateinit var mealName:String
    lateinit var mealThumb:String
      lateinit var mealMvvm : MealViewModel
     var youtube :String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("T","ANH YEU EM RAT NHIEU NHIEU")

        val mealDatabase = MealDatabase.getIstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        ViewModelProvider(this , viewModelFactory)[MealViewModel::class.java]
            .also { mealMvvm = it }



        //mealMvvm = ViewModelProvider(this)[MealViewModel::class.java]


        getMealInformationFromIntent()
        setInformationInViews()
        mealMvvm.getIDMeal(mealId)

        observerMealDeatialsLiveData()

        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnaddtofav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.inserMeal(it)
                Toast.makeText(this, "Meal save",Toast.LENGTH_LONG).show()
            }
        }
    }


    private var mealToSave:Meal? = null
    private fun observerMealDeatialsLiveData()
    {
        mealMvvm.observerLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal?) {
                val meal = t
                mealToSave = meal
                binding.tvCategory.text = meal!!.strCategory
                binding.tvArea.text = meal!!.strArea
                binding.tvInstructionsStep.text = meal!!.strInstructions
                youtube = meal!!.strYoutube

            }

        })

    }
    private fun onYoutubeImageClick() {
      binding.imgYoutube.setOnClickListener {
          Log.d("T","NOT WORK")
          val intent = Intent(Intent.ACTION_VIEW,Uri.parse(youtube))
          startActivity(intent)
      }
    }


    private fun setInformationInViews() {
       Glide.with(application).load(mealThumb).into(binding.imgMealDetail)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun getMealInformationFromIntent() {
       var intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        Log.d("TEST",mealId)
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        Log.d("TEST",mealName)

        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
        Log.d("TEST",mealThumb)
    }
}