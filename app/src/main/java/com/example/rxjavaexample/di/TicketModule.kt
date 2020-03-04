package com.example.rxjavaexample.di

import com.example.rxjavaexample.network.ApiClient
import com.example.rxjavaexample.network.ApiService
import com.example.rxjavaexample.network.Const
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class TicketModule {
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideApiClient(): ApiClient {
        return ApiClient()
    }
}