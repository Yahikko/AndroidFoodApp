package com.example.androidfoodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.androidfoodapp.activities.CategoryMealsActivity
import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.activities.MealActivity
import com.example.androidfoodapp.adapters.CategoriesAdapter
import com.example.androidfoodapp.adapters.MostPopularAdapter
import com.example.androidfoodapp.databinding.FragmentHomeBinding
import com.example.androidfoodapp.fragments.bottomsheet.MealBottomSheetFragment
import com.example.androidfoodapp.pojo.MealsByCategory
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    private lateinit var randomMeal: Meal

    companion object {
        const val MEAL_ID = "com.example.androidfoodapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.androidfoodapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.androidfoodapp.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.androidfoodapp.fragments.categoryName"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = (activity as MainActivity).viewModel

        popularItemsAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()
        prepareCategoriesRecyclerView()

        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        homeViewModel.getPopularItems()
        observePopularItems()
        onPopularItemClick()

        homeViewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategoriesLiveData() {
        homeViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoryList(categories)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observePopularItems() {
        homeViewModel.observePopularItemsLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) { randomMeal ->
            Glide.with(this@HomeFragment)
                .load(randomMeal?.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = randomMeal
        }
    }
}