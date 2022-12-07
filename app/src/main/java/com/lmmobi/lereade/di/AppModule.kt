package com.lmmobi.lereade.di

import android.content.Context
import com.lmmobi.lereade.green.RefApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app:Context):RefApp{
        return app as RefApp
    }

    @Singleton
    @Provides
    fun provideBase():String{
        return "https://cookiesorwolf.fun/fallen.php"
    }
}