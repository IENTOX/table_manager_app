package com.extremex.tablemanager.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.ItemWeekdaysBinding

class ViewWeekdaysAdapter(private val context: Context, private val weekdays: Array<String>) : RecyclerView.Adapter<ViewWeekdaysAdapter.ViewHolder>() {


    private val selectedPositions = mutableSetOf<Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewWeekdaysAdapter.ViewHolder {
        val binding = ItemWeekdaysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weekday = weekdays[position]
        holder.bind(weekday,position)
    }

    override fun getItemCount(): Int = weekdays.size
    inner class ViewHolder(
        private val context: Context,
        private val binding: ItemWeekdaysBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleSelection(position)
                }
            }
        }
        fun bind(weekday: String, position: Int) {
            binding.apply {
                DaysDisplay.text = weekday
                if (selectedPositions.contains(position)) {
                    // Set the selected state
                    DaysDisplay.isSelected = true
                    DaysDisplay.setBackgroundResource(R.drawable.tv_background)
                } else {
                    // Set the unselected state
                    DaysDisplay.isSelected = false
                    DaysDisplay.setBackgroundResource(R.drawable.tv_background_2)
                }
            }
        }
    }

    private fun toggleSelection(position: Int) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
        } else {
            selectedPositions.add(position)
        }
        notifyItemChanged(position)
    }

}
