package com.pinteraktif.myfoody.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pinteraktif.myfoody.R
import com.pinteraktif.myfoody.databinding.ItemIngredientsRowLayoutBinding
import com.pinteraktif.myfoody.models.ExtendedIngredient
import com.pinteraktif.myfoody.util.Constants.Companion.BASE_IMAGE_URL
import com.pinteraktif.myfoody.util.RecipesDiffUtil
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: ItemIngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        companion object {
//            fun from(parent: ViewGroup): MyViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ItemIngredientsRowLayoutBinding.inflate(layoutInflater)
//                return MyViewHolder(binding)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemIngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredientsList[position]
        holder.binding.ingredientImageView.load(BASE_IMAGE_URL + currentIngredient.image) {
            crossfade(600)
            error(R.drawable.ic_error_placholder)
        }
        holder.binding.ingredientName.text = currentIngredient.name.capitalize(Locale.ROOT)
        holder.binding.ingredientAmount.text = currentIngredient.amount.toString()
        holder.binding.ingredientUnit.text = currentIngredient.unit
        holder.binding.ingredientConsistency.text = currentIngredient.consistency
        holder.binding.ingredientOriginal.text = currentIngredient.original

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