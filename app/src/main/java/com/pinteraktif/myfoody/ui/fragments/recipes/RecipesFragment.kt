package com.pinteraktif.myfoody.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.adapters.RecipesAdapter
import com.pinteraktif.myfoody.databinding.FragmentRecipesBinding
import com.pinteraktif.myfoody.util.NetworkResult
import com.pinteraktif.myfoody.util.observeOnce
import com.pinteraktif.myfoody.viewmodels.MainViewModel
import com.pinteraktif.myfoody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch
import java.util.zip.Inflater

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        binding?.mainViewModel = mainViewModel

        setupRecycleView()
        readDatabase()

        return binding?.root
    }

    private fun setupRecycleView() {
       binding?.shimmerRecyclerView?.adapter = mAdapter
        binding?.shimmerRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    Log.d("Recipes Fragment", "readDatabase: Called")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }

    }

    private fun requestApiData() {
        Log.d("Recipes Fragment", "requestApiData: Called")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { responseNetworkFoodRecipe ->
            when (responseNetworkFoodRecipe) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    responseNetworkFoodRecipe.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()

                    /** No Connection access method */
                    loadDataFromCache()

                    Toast.makeText(
                        requireContext(),
                        responseNetworkFoodRecipe.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> showShimmerEffect()
            }
        })
    }

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, {database ->
                if (database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }


    private fun showShimmerEffect() {
        binding?.shimmerRecyclerView?.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding?.shimmerRecyclerView?.hideShimmer()
    }


}