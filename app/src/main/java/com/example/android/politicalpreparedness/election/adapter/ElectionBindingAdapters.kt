package com.example.android.politicalpreparedness.election.adapter

import android.text.format.DateFormat
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Election
import java.util.*


@BindingAdapter("elections")
fun bindRecycleView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data) {
        recyclerView.scrollToPosition(0)
    }
}

@BindingAdapter("electionDay")
fun bindElectionDay(textView: TextView, electionDay: Date?) {
    electionDay?.let {
        textView.text = DateFormat.format("EEE MMM dd HH:mm:ss zzz yyyy", electionDay)
    }
}