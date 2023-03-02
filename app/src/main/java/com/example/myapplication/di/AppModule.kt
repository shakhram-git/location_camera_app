package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.Database
import com.example.myapplication.data.LandmarksService
import com.example.myapplication.data.PhotoModelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "database")
            .build()
    }

    @Provides
    fun providePhotoModelDao(appDatabase: Database): PhotoModelDao {
        return appDatabase.photoModelDao()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.opentripmap.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideLandmarksSource(retrofit: Retrofit): LandmarksService {
        return retrofit.create(LandmarksService::class.java)
    }


}