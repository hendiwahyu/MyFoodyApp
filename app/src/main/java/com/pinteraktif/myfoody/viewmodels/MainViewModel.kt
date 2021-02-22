package com.pinteraktif.myfoody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.pinteraktif.myfoody.data.Repository
import com.pinteraktif.myfoody.data.database.entities.FavoritesEntity
import com.pinteraktif.myfoody.data.database.entities.FoodJokeEntity
import com.pinteraktif.myfoody.data.database.entities.RecipesEntity
import com.pinteraktif.myfoody.models.FoodJoke
import com.pinteraktif.myfoody.models.FoodRecipe
import com.pinteraktif.myfoody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository, application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoritesEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodJoke(foodJokeEntity)
        }


    /** RETROFIT */
    private var _searchRecipesResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    val searchRecipesResponse: LiveData<NetworkResult<FoodRecipe>> get() = _searchRecipesResponse

    private var _recipesResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    val recipesResponse: LiveData<NetworkResult<FoodRecipe>> get() = _recipesResponse

    private var _foodJokeResponse = MutableLiveData<NetworkResult<FoodJoke>>()
    val foodJokeResponse: LiveData<NetworkResult<FoodJoke>> get() = _foodJokeResponse

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSaveCall(searchQuery)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)

    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        _recipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                _recipesResponse.value = handleFoodRecipesResponse(response)

                /** Offline cache */
                val foodRecipe = _recipesResponse.value?.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }

            } catch (e: Exception) {
                _recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else
            _recipesResponse.value = NetworkResult.Error("No Internet Connection")

    }

    private suspend fun searchRecipesSaveCall(searchQuery: Map<String, String>) {
        _searchRecipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                _searchRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: java.lang.Exception) {
                _searchRecipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else
            _searchRecipesResponse.value = NetworkResult.Error("No internet connection")
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        _foodJokeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                _foodJokeResponse.value = handleFoodJokeResponse(response)

                /** Offline cache */
                val foodJoke = _foodJokeResponse.value?.data
                if (foodJoke != null){
                    offlineCacheFoodJoke(foodJoke)
                }

            } catch (e: java.lang.Exception) {
                _foodJokeResponse.value = NetworkResult.Error("Food Joke not found")
            }
        } else {
            _foodJokeResponse.value = NetworkResult.Error("No internet connection")
        }
    }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke){
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API key limited")
            response.body()?.results.isNullOrEmpty() -> NetworkResult.Error("Recipes not found.")
            response.isSuccessful -> {
                val foodRecipes = response.body()
                NetworkResult.Success(foodRecipes!!)
            }
            else -> NetworkResult.Error(response.message())
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        return when {
            response.message().toString().contains("timeout") -> NetworkResult.Error("Timeout")
            response.code() == 402 -> NetworkResult.Error("API key limited")
            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke!!)
            }
            else -> NetworkResult.Error(response.message())
        }

    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}


