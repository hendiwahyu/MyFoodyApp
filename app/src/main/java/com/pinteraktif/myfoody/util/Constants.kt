package com.pinteraktif.myfoody.util

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "df33e073e9e7426fbdb8337b2881f0c5"

        //API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM DATABASE
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
    }
}