package com.example.rxjavaexample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavaexample.di.DaggerTicketComponent
import com.example.rxjavaexample.network.ApiClient
import com.example.rxjavaexample.network.model.Ticket
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {
    @Inject
    lateinit var apiClient: ApiClient

    private val tickets = MutableLiveData<ArrayList<Ticket>>()
    private val disposable = CompositeDisposable()
    private val from = "DEL"
    private val to = "HYD"

    init {
        DaggerTicketComponent.create().inject(this)
    }

    fun loadData() {
        val ticketsObservable: ConnectableObservable<ArrayList<Ticket>> =
            fetchTickets(from, to).replay()

        /**
         * Fetching all tickets first
         * Observable emits List<Ticket> at once
         * All the items will be added to RecyclerView
         * */
        disposable.add(
            ticketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<Ticket>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: ArrayList<Ticket>) {
                        tickets.value = t
                        Log.d("TAG_TICKETS", t.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG_TICKETS", "onError: ", e)
                    }
                })
        )

        /**
         * Fetching individual ticket price
         * First FlatMap converts single ArrayList<Ticket> to multiple emissions
         * Second FlatMap makes HTTP call on each Ticket emission
         * */
        disposable.add(
            ticketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                /**
                * Converting ArrayList<Ticket> emission to single Ticket emissions
                * */
                .flatMap { t -> Observable.fromIterable(t) }
                /**
                * Fetching price on each Ticket emission
                * */
                .flatMap { t -> getPriceObservable(t) }
                .subscribeWith(object: DisposableObserver<Ticket>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: Ticket) {
                        val position = tickets.value?.indexOf(t)

                        if (position == -1) {
                            return
                        }

                        val ticketsList = tickets.value
                        position?.let { ticketsList?.set(it, t) }
                        tickets.value = ticketsList
                    }

                    override fun onError(e: Throwable) {
                        Log.e("TAG_TICKETS", "onError: ", e)
                    }

                })
        )

        //Calling connect to start emission
        ticketsObservable.connect()
    }

    private fun fetchTickets(from: String, to: String): Observable<ArrayList<Ticket>> {
        return apiClient.searchTickets(from, to)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getPriceObservable(ticket: Ticket): Observable<Ticket> {
        return apiClient.getPrice(ticket.flightNumber, ticket.from, ticket.to)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { price ->
                ticket.price = price
                ticket
            }
    }

    fun getTickets(): LiveData<ArrayList<Ticket>> = tickets

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}