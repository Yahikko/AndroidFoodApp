package com.example.androidfoodapp.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.activities.MealActivity
import com.example.androidfoodapp.databinding.FragmentMealBottomSheetBinding
import com.example.androidfoodapp.fragments.HomeFragment
import com.example.androidfoodapp.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var homeViewModel: HomeViewModel

    private var mealId: String? = null
    private var mealName: String? = null
    private var mealThumb: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)

        homeViewModel = (activity as MainActivity).viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            homeViewModel.getMealById(it)
        }

        observeBottomSheetMeal()

        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThumb != null) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private fun observeBottomSheetMeal() {
        homeViewModel.observeBottomSheetLiveData().observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it.strMealThumb)
                .into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text = it.strArea
            binding.tvBottomSheetCategory.text = it.strCategory
            binding.tvBottomSheetMealName.text = it.strMeal

            mealName = it.strMeal
            mealThumb = it.strMealThumb
        }
    }
}