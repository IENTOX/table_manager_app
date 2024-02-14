package com.extremex.tablemanager.lib

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemHolidayBinding

class ViewHolidayAdapter(private val context: Context, private val holidayData: MutableList<HolidayInfoModel>) : RecyclerView.Adapter<ViewHolidayAdapter.ViewHolder>() {

    // Initialize the ItemTouchHelper
    private val swipeToDeleteCallback = SwipeToDeleteCallbackH(this)
    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHolidayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun getItemCount() = holidayData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holidayData[position])
    }

    inner class ViewHolder(
        private val context: Context,
        private val binding: ItemHolidayBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holidayData: HolidayInfoModel) {
            binding.apply {
                StartDate.text = "${holidayData.holidayStartDay.day}/${holidayData.holidayStartDay.month}/${holidayData.holidayStartDay.year}"
                HolidayCount.text = "${holidayData.holidayNumber} ${holidayData.holidayUnit}"
                HolidayName.text = holidayData.holidayName
            }
        }
    }

    fun removeItem( position: Int) {
        val pref = context.getSharedPreferences(StandardCompanion.TIME_SLOTS_CUSTOM_HOLIDAY_FILE, Context.MODE_PRIVATE)
        val edit = pref.edit()
        val key = holidayData[position]
        if (position > 0) {
            holidayData.removeAt(position)
            Log.w("remove key:", "selected key is $key")
            edit.remove(key.toString())
            edit.commit()
            holidayData.removeAt(position)
            notifyItemRemoved(0)
            notifyItemRangeRemoved(0, holidayData.size -1)
            notifyDataSetChanged()
        } else if (position == 0) {
            holidayData.removeAt(0)
            Log.w("remove key:", "selected key is $key")
            edit.remove(key.toString())
            edit.commit()
            holidayData.removeAt(position)
            notifyItemRemoved(0)
            notifyItemRangeRemoved(0, holidayData.size -1)
            notifyDataSetChanged()
        }
    }private fun getCustomHolidays(): MutableList<HolidayInfoModel>{
        val pref = context.getSharedPreferences(StandardCompanion.TIME_SLOTS_CUSTOM_HOLIDAY_FILE, Context.MODE_PRIVATE)
        var holidays = mutableListOf<HolidayInfoModel>()
        val position = (pref.all.keys.size -1) ?: 0
        if (position > 0){
            for (i in 1.. position){
                try {
                    val raw: MutableList<String> = pref.getString(i.toString(),"None")?.split("ӿ")?.toMutableList() ?: mutableListOf("Unknown")
                    if (raw[0] != "Unknown") {
                        val name = raw[0]
                        val rawDate = raw[1]
                        val number = raw[2].toInt()
                        val  unit = raw[3]
                        val rawDateDate: MutableList<String> = rawDate.split("@").toMutableList()
                        val day = rawDateDate[0].toInt()
                        val month = rawDateDate[1].toInt()
                        val year = rawDateDate[2].toInt()

                        holidays.add(HolidayInfoModel(name, DateModel(day, month, year), number, unit))
                    }
                } catch ( e: NumberFormatException){
                    // nothing to do for now
                }
            }
        } else {
            pref.getString(1.toString(),"None")
        }

        return holidays
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
