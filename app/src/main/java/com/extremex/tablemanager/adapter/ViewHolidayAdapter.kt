package com.extremex.tablemanager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemHolidayBinding
import com.extremex.tablemanager.lib.HolidayInfoModel
import com.extremex.tablemanager.lib.StandardCompanion
import com.extremex.tablemanager.lib.SwipeToDeleteCallbackH
import java.lang.IndexOutOfBoundsException

class ViewHolidayAdapter(private val context: Context, private val holidayData: MutableList<HolidayInfoModel>) : RecyclerView.Adapter<ViewHolidayAdapter.ViewHolder>() {

    // Define a listener interface
    interface ViewHolidayAdapterListener {
        fun onHolidayCleared()
    }

    // Listener instance
    private var listener: ViewHolidayAdapterListener? = null
    private val swipeToDeleteCallback = SwipeToDeleteCallbackH(this)
    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
    private val pref: SharedPreferences = context.getSharedPreferences(StandardCompanion.TIME_SLOTS_CUSTOM_HOLIDAY_FILE_MANE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

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
        @SuppressLint("SetTextI18n")
        fun bind(holidayData: HolidayInfoModel) {
            binding.apply {
                StartDate.text = "${holidayData.holidayStartDay.day}/${holidayData.holidayStartDay.month}/${holidayData.holidayStartDay.year}"
                EndDate.text = "${holidayData.holidayEndDay.day}/${holidayData.holidayEndDay.month}/${holidayData.holidayEndDay.year}"
                HolidayCount.text = "${holidayData.holidayNumber} ${holidayData.holidayUnit}"
                HolidayName.text = holidayData.holidayName

            }
        }
    }

    fun removeItem(position: Int) {
        try {
            val fromPrefKeys = pref.all.keys.toMutableList()
            editor.remove(fromPrefKeys[position])
            editor.commit()
            listener?.onHolidayCleared()
            notifyItemRemoved(position)
        } catch (e: IndexOutOfBoundsException) {
            Log.v("deletion", "current position: $position")
            for (i in pref.all.keys) {
                Log.v("deletion", "OutOfBoundsFile -> ${pref.getString(i, "")}")
            }
        }
    }

    // Setter method for the listener
    fun setListener(listener: ViewHolidayAdapterListener) {
        this.listener = listener
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
