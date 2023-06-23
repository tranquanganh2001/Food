package com.tranquanganh.food.fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tranquanganh.food.R
import com.tranquanganh.food.activity.CategoryActivity
import com.tranquanganh.food.activity.MainActivity
import com.tranquanganh.food.activity.MealActivity
import com.tranquanganh.food.adaters.CategoriesAdapter
import com.tranquanganh.food.adaters.MostPopularAdapter
import com.tranquanganh.food.databinding.FragmentHomeBinding
import com.tranquanganh.food.fragment.bottomsheet.MealBottomSheetFragment
import com.tranquanganh.food.pojo.MealsByCategory
import com.tranquanganh.food.vmvm.Homemvvm
import com.tranquanganh.food.pojo.Meal


class HomeFragment : Fragment() {
   private lateinit var binding : FragmentHomeBinding
   private lateinit var viewModel:Homemvvm
   private lateinit var saveMeal: Meal
   private lateinit var popularItemsAdapter : MostPopularAdapter
   private lateinit var categoriesAdapter:CategoriesAdapter
    companion object{
       const val  MEAL_ID = "com.tranquanganh.food.fragment.idMead"
        const val  MEAL_NAME = "com.tranquanganh.food.fragment.strnameMeal"
        const val  MEAL_THUMB = "com.tranquanganh.food.fragment.strthumbMeal"
        const val CATEGDRY_NAME = "com.tranquanganh.food.fragment.categoryName"
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
             viewModel = (activity as MainActivity).viewModel
      //  home = ViewModelProvider(this)[Homemvvm::class.java]
            popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
           binding =  FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observeRandomMeal()
        Log.d("TEST","ANH YEU EM")
        onRandomMealClick()

        viewModel.getPoppularItems()
        observePopularItemsliveData()
        onPopularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick ={meal->
            val mealBottSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            category -> val intent = Intent(activity,CategoryActivity::class.java)
            intent.putExtra(CATEGDRY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.obseverCategoriesLiveData().observe(viewLifecycleOwner, Observer {
            categories->
             categoriesAdapter.setCategoryList(categories)
        })

    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal->val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal
                .strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
            Log.d("TOI","DA O DAY")
        }
    }

    private fun observePopularItemsliveData() {
        viewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) {mealList->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.imgRandomMeal.setOnClickListener{
            val intentt=  Intent(activity,MealActivity::class.java)
            intentt.putExtra(MEAL_ID,saveMeal.idMeal)
            intentt.putExtra(MEAL_NAME,saveMeal.strMeal)
            intentt.putExtra(MEAL_THUMB,saveMeal.strMealThumb)
            startActivity(intentt)
        }

    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner, object : Observer<Meal> {
            override fun onChanged(t: Meal?) {
                Glide.with(this@HomeFragment)
                    .load(t!!.strMealThumb)
                    .into(binding.imgRandomMeal)
                    saveMeal = t
            }
        }
            
        )

    }


}