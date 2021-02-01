package com.pinteraktif.myfoody.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.models.ExtendedIngredient
import com.pinteraktif.myfoody.util.Constants.Companion.BASE_IMAGE_URL
import com.pinteraktif.myfoody.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.item_ingredients_row_layout.view.*
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = ingredientsList[position]
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + currentIngredient.image) {
            crossfade(600)
            error(R.drawable.ic_error_placholder)
        }
        holder.itemView.ingredient_name.text = currentIngredient.name.capitalize(Locale.ROOT)
        holder.itemView.ingredient_amount.text = currentIngredient.amount.toString()
        holder.itemView.ingredient_unit.text = currentIngredient.unit
        holder.itemView.ingredient_consistency.text = currentIngredient.consistency
        holder.itemView.ingredient_original.text = currentIngredient.original

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)

    }
}