package com.rahafcs.co.rightway.di.modules

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rahafcs.co.rightway.R
import com.rahafcs.co.rightway.data.source.AuthRemoteDataSource
import com.rahafcs.co.rightway.data.source.WorkoutDataSource
import com.rahafcs.co.rightway.data.source.WorkoutRemoteDataSource
import com.rahafcs.co.rightway.network.WorkoutApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideWorkoutRemoteDataSource(
        workoutApiService: WorkoutApiService,
        coroutineDispatcher: CoroutineDispatcher,
    ): WorkoutDataSource =
        WorkoutRemoteDataSource(workoutApiService, coroutineDispatcher)

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideGoogleSignInOptions(context: Context): GoogleSignInOptions =
        GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client))
            .requestEmail()
            .build()

    @Singleton
    @Provides
    fun provideAuthRemoteDataSource(context: Context, googleSignInOptions: GoogleSignInOptions) =
        AuthRemoteDataSource(context, googleSignInOptions)
}
