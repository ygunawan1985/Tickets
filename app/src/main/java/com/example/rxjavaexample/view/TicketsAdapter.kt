package com.example.rxjavaexample.view

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rxjavaexample.R
import com.example.rxjavaexample.network.model.Ticket
import kotlinx.android.synthetic.main.ticket_row.view.*

class TicketsAdapter(private val tickets: ArrayList<Ticket>): RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    fun updateTickets(tickets: ArrayList<Ticket>) {
        this.tickets.clear()
        this.tickets.addAll(tickets)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun setValues(ticket: Ticket) {
            Glide.with(itemView.context).load(ticket.airline.logo).apply(RequestOptions.circleCropTransform()).into(itemView.logo)
            itemView.airline_name.text = ticket.airline.name
            itemView.departure.text = ticket.departure + " Dep"
            itemView.arrival.text = ticket.arrival + " Dest"
            itemView.duration.text = ticket.flightNumber
            itemView.duration.append(", " + ticket.duration)
            itemView.number_of_stops.text = String.format("%d Stops", ticket.numberOfStops)

            if (!TextUtils.isEmpty(ticket.instructions)) {
                itemView.duration.append(", " + ticket.instructions)
            }

            if (ticket.price != null) {
                itemView.price.text = "₹" + String.format("%.0f", ticket.price!!.price)
                itemView.number_of_seats.text = String.format("%s Seats", ticket.price!!.seats)
                itemView.loader.visibility = View.INVISIBLE
            } else {
                itemView.loader.visibility = View.VISIBLE
            }
        }

        override fun onClick(view: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ticket_row, parent, false))
    }

    override fun getItemCount(): Int = tickets.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.setValues(tickets[position])
}