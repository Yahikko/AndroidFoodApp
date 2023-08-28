package com.example.androidfoodapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidfoodapp.adapters.CategoryMealsAdapter
import com.example.androidfoodapp.databinding.ActivityCategoryMealsBinding
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding

    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    lateinit var mealName: String

    private val categoryMealsViewModel by viewModels<CategoryMealsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mealName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        prepareRecyclerView()
        categoryMealsViewModel.getMealsByCategory(mealName)
        observeMeals()

    }

    private fun observeMeals() {
        categoryMealsViewModel.observeMealsLiveData().observe(this) {
            binding.tvCategoryCount.text = "$mealName: ${it.size}"
            categoryMealsAdapter.setMealsList(it)
        }
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}