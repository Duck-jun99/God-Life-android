package com.godlife.god_life.di

import com.godlife.network.BuildConfig
import com.godlife.network.retrofit.RetrofitNetworkApi
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(500, TimeUnit.SECONDS)
            .connectTimeout(500, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_DOMAIN)
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit): RetrofitNetworkApi =
        retrofit.create(RetrofitNetworkApi::class.java)


    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }


}

