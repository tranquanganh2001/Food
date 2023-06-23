package com.tranquanganh.food.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tranquanganh.food.R
import com.tranquanganh.food.activity.MainActivity
import com.tranquanganh.food.adaters.MealsAdapter
import com.tranquanganh.food.databinding.FragmentSearchBinding
import com.tranquanganh.food.vmvm.Homemvvm


class SearchFragment : Fragment() {

    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel:Homemvvm
    private lateinit var searchRecyclerviewAdapter : MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparreRecylerView()
        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }
        observeSearchdMealsLiveData()
    }

    private fun observeSearchdMealsLiveData() {
        viewModel.observeSeachdMealsLiveData().observe(viewLifecycleOwner,
        Observer {
            mealsList -> searchRecyclerviewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if( searchQuery.isNotEmpty())
        {
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun preparreRecylerView() {
        searchRecyclerviewAdapter = MealsAdapter()
        binding.rvSearchdMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = searchRecyclerviewAdapter
        }
    }


}