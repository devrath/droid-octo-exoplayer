package com.example.code.exoplayer.types.styled.di

import android.content.Context
import com.example.code.exoplayer.types.styled.core.StyledExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExoplayerModule {

    @Provides
    @Singleton
    fun provideStyledExoPlayer(@ApplicationContext application: Context): StyledExoPlayer {
        return StyledExoPlayer(application)
    }

}