package com.example.androidfoodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidfoodapp.R
import com.example.androidfoodapp.activities.CategoryMealsActivity
import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.adapters.CategoriesAdapter
import com.example.androidfoodapp.databinding.CategoryItemBinding
import com.example.androidfoodapp.databinding.FragmentCategoriesBinding
import com.example.androidfoodapp.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater)

        homeViewModel = (activity as MainActivity).viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeCategories()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            val intent = Intent(context, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        homeViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoryList(categories)
        }
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }

    }
}