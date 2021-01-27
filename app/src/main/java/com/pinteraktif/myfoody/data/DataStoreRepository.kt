package com.pinteraktif.myfoody.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.PREFERENCE_BACK_ONLINE
import com.pinteraktif.myfoody.util.Constants.Companion.PREFERENCE_DIET_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.PREFERENCE_DIET_TYPE_ID
import com.pinteraktif.myfoody.util.Constants.Companion.PREFERENCE_MEAL_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.PREFERENCE_MEAL_TYPE_ID
import com.pinteraktif.myfoody.util.Constants.Companion.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    /** create data store  */
    private val dataStore = context.createDataStore(name = PREFERENCE_NAME)

    /** create some keys */

    private object PreferencesKey {
        val selectedMealType = stringPreferencesKey(PREFERENCE_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCE_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCE_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCE_BACK_ONLINE)
    }

    /** create recipes data flow  */


    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit {
            it[PreferencesKey.selectedMealType] = mealType
            it[PreferencesKey.selectedMealTypeId] = mealTypeId
            it[PreferencesKey.selectedDietType] = dietType
            it[PreferencesKey.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean){
        dataStore.edit {
            it[PreferencesKey.backOnline] = backOnline
        }
    }


    /** store meal dan diet data  */
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else throw exception
        }
        .map {
            val selectedMealType = it[PreferencesKey.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = it[PreferencesKey.selectedMealTypeId] ?: 0
            val selectedDietType = it[PreferencesKey.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = it[PreferencesKey.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    /** Store data from UI */
    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            } else throw exception
        }
        .map {
            val backOnline = it[PreferencesKey.backOnline] ?: false
            backOnline
        }
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int

)


