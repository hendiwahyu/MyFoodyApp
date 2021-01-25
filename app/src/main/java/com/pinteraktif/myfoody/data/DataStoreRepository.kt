package com.pinteraktif.myfoody.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.pinteraktif.myfoody.util.Constants.Companion.DEFAULT_MEAL_TYPE
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
}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int

)


