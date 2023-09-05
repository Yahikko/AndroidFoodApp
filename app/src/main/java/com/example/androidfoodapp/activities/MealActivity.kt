package com.example.androidfoodapp.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.androidfoodapp.R
import com.example.androidfoodapp.databinding.ActivityMealBinding
import com.example.androidfoodapp.db.MealDatabase
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.viewModel.MealViewModel
import com.example.androidfoodapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealViewModel: MealViewModel

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLing: String

    private var mealToSave: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInViews()

        loadingCase()
        mealViewModel.getMealDetails(mealId)
        observerMealDetails()

        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFavorites.setOnClickListener {
            mealToSave?.let {
                mealViewModel.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLing))
            startActivity(intent)
        }
    }

    private fun observerMealDetails() {
        mealViewModel.observerMealDetailsLiveData().observe(
            this
        ) { meal ->
            responseCase()

            mealToSave = meal

            binding.tvCategory.text = "Category: ${meal?.strCategory}"
            binding.tvArea.text = "Category: ${meal?.strArea}"
            binding.tvInstructionsSteps.text = "Category: ${meal?.strInstructions}"

            youtubeLing = meal.strYoutube ?: ""
        }
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID) ?: ""
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME) ?: ""
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB) ?: ""
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolBar.title = mealName
        setToolBarColor()
    }

    private fun setToolBarColor() {
        binding.collapsingToolBar.setCollapsedTitleTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.collapsingToolBar.setExpandedTitleTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )
        )
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFavorites.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun responseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFavorites.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}