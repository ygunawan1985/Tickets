package com.example.rxjavaexample.view

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rxjavaexample.R
import com.example.rxjavaexample.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val from = "DEL"
    private val to = "HYD"
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var ticketsAdapter: TicketsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "$from > $to"

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        ticketsAdapter = TicketsAdapter(arrayListOf())

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, 1)
            addItemDecoration(GridSpacingItemDecoration(1, dpToPx(5F), true))
            itemAnimator = DefaultItemAnimator()
            adapter = ticketsAdapter
        }

        viewModel.loadData()

        viewModel.getTickets().observe(this, Observer { tickets ->
            tickets?.let {
                ticketsAdapter.updateTickets(it)
            }
        })
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .roundToInt()
    }

}