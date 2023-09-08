package com.example.androidfoodapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidfoodapp.adapters.CategoryMealsAdapter
import com.example.androidfoodapp.databinding.ActivityCategoryMealsBinding
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding

    lateinit var categoryMealsAdapter: CategoryMealsAdapter
    private lateinit var mealName: String

    private val categoryMealsViewModel by viewModels<CategoryMealsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        mealName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        categoryMealsViewModel.getMealsByCategory(mealName)
        observeMeals()

        onMealClick()
    }

    private fun onMealClick() {
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
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