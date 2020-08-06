package com.example.abcfivrr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.abcfivrr.MyAdapter.MyViewHolder

class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        val layoutIdForListItem = R.layout.demo_image
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false
        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class MyViewHolder(itemView: View) : ViewHolder(itemView)
}