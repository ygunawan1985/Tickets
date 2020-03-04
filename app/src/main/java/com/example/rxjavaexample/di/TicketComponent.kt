package com.example.rxjavaexample.di

import com.example.rxjavaexample.network.ApiClient
import com.example.rxjavaexample.viewmodel.MainActivityViewModel
import dagger.Component

@Component(modules = [TicketModule::class])
interface TicketComponent {
    fun inject(apiClient: ApiClient)

    fun inject(viewModel: MainActivityViewModel)
}