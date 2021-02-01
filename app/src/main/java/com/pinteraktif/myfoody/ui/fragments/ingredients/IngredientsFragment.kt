package com.pinteraktif.myfoody.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.adapters.IngredientsAdapter
import com.pinteraktif.myfoody.models.Result
import com.pinteraktif.myfoody.util.Constants.Companion.BASE_IMAGE_URL
import com.pinteraktif.myfoody.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args  = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        setupRecyclerView(view)
        myBundle?.extendedIngredients.let {
            if (it != null) {
                mAdapter.setData(it)
            }
        }

        return view
    }

    private fun setupRecyclerView(view: View){
        view.ingredients_recyclerView.adapter = mAdapter
        view.ingredients_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}