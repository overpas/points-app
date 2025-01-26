package com.example.pointsapp.points

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pointsapp.R
import com.example.pointsapp.domain.model.Point

class PointsAdapter : ListAdapter<Point, PointsAdapter.ViewHolder>(
    PointDiffCallback,
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_point_row, parent, false)
            .let(::ViewHolder)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    private object PointDiffCallback : DiffUtil.ItemCallback<Point>() {

        override fun areItemsTheSame(
            oldItem: Point,
            newItem: Point
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Point,
            newItem: Point
        ): Boolean = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val xTextView = view.findViewById<TextView>(R.id.xTextView)
        private val yTextView = view.findViewById<TextView>(R.id.yTextView)

        fun bind(point: Point) {
            xTextView.text = point.x.toString()
            yTextView.text = point.y.toString()
        }
    }
}
