package com.pinteraktif.myfoody.data.database

import androidx.room.*
import com.pinteraktif.myfoody.data.database.entities.FavoritesEntity
import com.pinteraktif.myfoody.data.database.entities.FoodJokeEntity
import com.pinteraktif.myfoody.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    /**
     * ListRecipes Entity access.
     * Create Insert, Read to Local Database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    /**
     * Favorite Entity access.
     * Create Insert, Read, Delete, and Delete all favorite recipes to Local Database
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table ")
    suspend fun deleteAllFavoriteRecipes()

    /**
     * FoodJoke Entity access.
     * Create Insert, Read, Delete, and Delete all favorite recipes to Local Database
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_entity ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

}