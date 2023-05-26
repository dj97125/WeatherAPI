package com.example.weatherapi.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.weatherapi.common.BASE_URL
import com.example.weatherapi.common.DATABASE_NAME
import com.example.weatherapi.common.TAG
import com.example.weatherapi.domain.Repository
import com.example.weatherapi.domain.RepositoryImpl
import com.example.weatherapi.model.local.LocalDataSource
import com.example.weatherapi.model.local.LocalDataSourceImpl
import com.example.weatherapi.model.local.WeatherDB
import com.example.weatherapi.model.network.NetworkDataSource
import com.example.weatherapi.model.network.NetworkDataSourceImpl
import com.example.weatherapi.model.network.WeatherApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(ViewModelComponent::class)
interface Module {

    @Binds
    fun bindRepository(
        repositoryImpl: RepositoryImpl
    ): Repository

    @Binds
    fun bindNetworkDataSource(
        networkDataSourceImpl: NetworkDataSourceImpl
    ): NetworkDataSource

    @Binds
    fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource


    companion object {
        @Provides
        fun provideService(): WeatherApi =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkHttpClient())
                .build()
                .create(WeatherApi::class.java)

        @Provides
        fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        @Provides
        fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO


        @Provides
        fun provideCoroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope =
            CoroutineScope(dispatcher)


        @Provides
        fun provideRoom(@ApplicationContext context: Context): WeatherDB =
            Room.databaseBuilder(
                context,
                WeatherDB::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration().build()


        @Provides
        fun provideCountryDao(dataBase: WeatherDB) = dataBase.weatherDao()

        @Provides
        fun provideEXceptionHandler(): CoroutineExceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                Log.e(TAG, "$throwable")
            }


    }
}