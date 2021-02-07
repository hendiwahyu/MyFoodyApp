package com.pinteraktif.myfoody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pinteraktif.myfoody.adapters.FavoriteRecipesAdapter
import com.pinteraktif.myfoody.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {

    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            mAdapter: FavoriteRecipesAdapter?
        ) {
            if (favoritesEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> View.VISIBLE
                    is TextView -> View.VISIBLE
                    is RecyclerView -> View.INVISIBLE
                }
            } else {
                when (view) {
                    is ImageView -> View.INVISIBLE
                    is TextView -> View.INVISIBLE
                    is RecyclerView -> {
                        View.VISIBLE
                        mAdapter?.setData(favoritesEntity)
                    }
                }
            }
        }

    }
}