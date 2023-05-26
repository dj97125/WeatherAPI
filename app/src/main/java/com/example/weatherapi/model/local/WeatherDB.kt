package com.example.weatherapi.model.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.weatherapi.model.local.entities.WeatherEntity


@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodaysWeather(weatherEntity: WeatherEntity)

    @Query("DELETE FROM WeatherEntity WHERE date=:date")
    suspend fun deletePastWeather(date: String)

    @Query("SELECT * FROM WeatherEntity order by id")
    suspend fun getTodaysWeather(): List<WeatherEntity>


}


@Database(
    version = 1,
    entities = [WeatherEntity::class],
    exportSchema = false
)
abstract class WeatherDB : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
