package com.pinteraktif.myfoody.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinteraktif.myfoody.adapters.IngredientsAdapter
import com.pinteraktif.myfoody.databinding.FragmentIngredientsBinding
import com.pinteraktif.myfoody.models.Result
import com.pinteraktif.myfoody.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        binding?.let { setupRecyclerView(it.root) }
        myBundle?.extendedIngredients.let { ingredientList ->
            ingredientList?.let { mAdapter.setData(it) }
        }

        return binding?.root
    }

    private fun setupRecyclerView(view: View) {
        binding?.ingredientsRecyclerView?.adapter = mAdapter
        binding?.ingredientsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}