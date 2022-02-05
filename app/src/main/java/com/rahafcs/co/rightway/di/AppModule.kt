package com.rahafcs.co.rightway.di

import android.content.Context
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.network.WorkoutApi
import com.rahafcs.co.rightway.network.WorkoutApiService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Provides
    @Singleton
    fun provideWorkoutApi(): WorkoutApiService = WorkoutApi.retrofitService

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context.applicationContext

    @Provides
    @Singleton
    fun provideGoogleSignInOptions(context: Context): GoogleSignInOptions =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client))
            .requestEmail()
            .build()

//    @Provides
//    @Singleton
//    fun provideApiHelper(apiHelper: WorkoutApiServiceImpl): WorkoutApiService = apiHelper

//    @Binds
//    abstract fun bindAnalyticsService(
//        analyticsServiceImpl: WorkoutApiServiceImpl,
//    ): WorkoutApiService

//    @Binds
//    abstract fun bindWorkoutDataSource(impl: WorkoutApi): WorkoutApiService

//    @Singleton
//    @Binds
//    abstract fun bindWorkoutDataSource(impl: WorkoutRemoteDataSource): WorkoutDataSource
}

// @Module
// @InstallIn(SingletonComponent::class)
// object NetworkModule {
//    @Singleton
//    @Provides
//    fun provideHttpClient(): OkHttpClient {
//        return OkHttpClient
//            .Builder()
//            .readTimeout(15, TimeUnit.SECONDS)
//            .connectTimeout(15, TimeUnit.SECONDS)
//            .build()
//    }
// }
