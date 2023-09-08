package com.example.androidfoodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfoodapp.activities.MainActivity
import com.example.androidfoodapp.activities.MealActivity
import com.example.androidfoodapp.adapters.MealsAdapter
import com.example.androidfoodapp.databinding.FragmentFavoritesBinding
import com.example.androidfoodapp.pojo.Meal
import com.example.androidfoodapp.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var favoritesAdapter: MealsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater)

        homeViewModel = (activity as MainActivity).viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observerFavorites()

        onMealClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favoritesAdapter.differ.currentList[position]

                homeViewModel.deleteMeal(meal)

                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        homeViewModel.insertMeal(meal)
                    }.show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }

    private fun onMealClick() {
        favoritesAdapter.onItemClick = {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        favoritesAdapter = MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }

    private fun observerFavorites() {
        homeViewModel.observeFavoritesMealsLiveData().observe(viewLifecycleOwner) { meals ->
            favoritesAdapter.differ.submitList(meals)
        }
    }
}