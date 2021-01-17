package com.pinteraktif.myfoody.data

import com.pinteraktif.myfoody.data.network.FoodRecipeApi
import com.pinteraktif.myfoody.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipeApi: FoodRecipeApi) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipeApi.getRecipes(queries)
    }
}