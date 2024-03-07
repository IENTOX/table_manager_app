package com.extremex.tablemanager.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.ItemWeekdaysBinding
import com.extremex.tablemanager.lib.GetAttrColor
import com.extremex.tablemanager.lib.StandardCompanion

class ViewWeekdaysAdapter(private val context: Context, private val weekdays: Array<String>) : RecyclerView.Adapter<ViewWeekdaysAdapter.ViewHolder>() {

    val userPref = context.getSharedPreferences(StandardCompanion.USER_PREF_FILE_MANE, AppCompatActivity.MODE_PRIVATE)
    val userPrefEditor = userPref.edit()
    private val selectedPositions = mutableSetOf<Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
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
        val getColor = GetAttrColor(context)

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleSelection(position)
                }
            }
        }
        fun bind(weekday: String, position: Int) {
            getSelectedPositionsFromPrefetch()
            if (getSelectedPositionsFromPrefetch() != null){
                for (elements in getSelectedPositionsFromPrefetch()!!){
                    selectedPositions.add(elements)
                }
            }
            binding.apply {
                DaysDisplay.text = weekday
                if (selectedPositions.contains(position)) {
                    // Set the selected state
                    DaysDisplay.isSelected = true
                    DaysDisplay.setBackgroundResource(R.drawable.item_selected)
                    DaysDisplay.setTextColor(getColor.getTextColor().color)
                } else {
                    // Set the unselected state
                    DaysDisplay.isSelected = false
                    DaysDisplay.setTextColor(getColor.getTextInverseColor().color)
                    DaysDisplay.setBackgroundResource(R.drawable.item_deselected)

                }
            }
        }
    }

    private fun toggleSelection(position: Int) {
        var data = mutableListOf<Int>()
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
            userPrefEditor.remove(StandardCompanion.TIME_SLOTS_FIXED_DAYS_OFF)
            data  = selectedPositions.toMutableList()
        } else {
            selectedPositions.add(position)
            userPrefEditor.remove(StandardCompanion.TIME_SLOTS_FIXED_DAYS_OFF)
            data = selectedPositions.toMutableList()
        }
        var dataStream = ""
        for (i in data.indices){
            dataStream += "${data[i]}:"
        }
        Log.v(StandardCompanion.TIME_SLOTS_FIXED_DAYS_OFF, dataStream)
        userPrefEditor.putString(StandardCompanion.TIME_SLOTS_FIXED_DAYS_OFF,dataStream)
        userPrefEditor.commit()
        notifyItemChanged(position)
    }
    private fun getSelectedPositionsFromPrefetch(): MutableList<Int>? {
        val positionList = mutableListOf<Int>()
        val dataStream: String? =
            userPref.getString(StandardCompanion.TIME_SLOTS_FIXED_DAYS_OFF, "")
        return if (!dataStream.isNullOrBlank()) {
            val dataStreamArray = dataStream.split(":")
            for (element in dataStreamArray) {
                Log.v("ReceivedItemsFromStorage", element)
                try {
                    positionList.add(element.toInt())
                } catch (e: NumberFormatException){
                    //do nothing for now
                }
            }
            positionList
        }else {
            null
        }
    }
}