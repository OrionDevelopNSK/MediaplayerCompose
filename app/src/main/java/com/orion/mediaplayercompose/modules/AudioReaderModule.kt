package com.orion.mediaplayercompose.modules

import android.content.Context
import com.orion.mediaplayercompose.data.database.AudioReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AudioReaderModule {

    @Singleton
    @Provides
    fun audioReader(@ApplicationContext context: Context) : AudioReader{
        return AudioReader(context)
    }
}