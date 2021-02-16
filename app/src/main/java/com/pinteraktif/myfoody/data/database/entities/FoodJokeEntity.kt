package com.pinteraktif.myfoody.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pinteraktif.myfoody.models.FoodJoke
import com.pinteraktif.myfoody.util.Constants.Companion.FOOD_JOKE_ENTITY


@Entity(tableName = FOOD_JOKE_ENTITY)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}