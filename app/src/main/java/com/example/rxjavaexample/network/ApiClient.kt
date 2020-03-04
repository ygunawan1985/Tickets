package com.example.rxjavaexample.network

import com.example.rxjavaexample.di.DaggerTicketComponent
import com.example.rxjavaexample.network.model.Price
import com.example.rxjavaexample.network.model.Ticket
import io.reactivex.Single
import javax.inject.Inject

class ApiClient {
    @Inject
    lateinit var apiService: ApiService

    init {
        DaggerTicketComponent.create().inject(this)
    }

    fun searchTickets(from: String, to: String): Single<ArrayList<Ticket>> {
        return apiService.searchTickets(from, to)
    }

    fun getPrice(flightNumber: String, from: String, to: String): Single<Price> {
        return apiService.getPrice(flightNumber, from, to)
    }
}