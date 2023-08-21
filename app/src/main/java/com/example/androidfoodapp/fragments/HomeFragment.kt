package com.example.androidfoodapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.androidfoodapp.databinding.FragmentHomeBinding
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getRandomMeal()
        observerRandomMeal()
    }

    private fun observerRandomMeal() {
        homeViewModel.observeRandomMealLiveData().observe(
            viewLifecycleOwner
        ) { randomMeal ->
            Glide.with(this@HomeFragment)
                .load(randomMeal?.strMealThumb)
                .into(binding.imgRandomMeal)
        }
    }
}