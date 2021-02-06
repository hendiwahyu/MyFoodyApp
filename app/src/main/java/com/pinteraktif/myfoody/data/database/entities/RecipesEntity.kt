package com.pinteraktif.myfoody.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pinteraktif.myfoody.models.FoodRecipe
import com.pinteraktif.myfoody.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}