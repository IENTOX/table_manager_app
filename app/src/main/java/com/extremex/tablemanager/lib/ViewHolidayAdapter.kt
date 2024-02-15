package com.extremex.tablemanager.lib

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.extremex.tablemanager.databinding.ItemHolidayBinding

class ViewHolidayAdapter(private val context: Context, private val holidayData: MutableList<HolidayInfoModel>) : RecyclerView.Adapter<ViewHolidayAdapter.ViewHolder>() {

    // Initialize the ItemTouchHelper
    private val swipeToDeleteCallback = SwipeToDeleteCallbackH(this)
    private val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
    private val pref: SharedPreferences = context.getSharedPreferences(StandardCompanion.TIME_SLOTS_CUSTOM_HOLIDAY_FILE_MANE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()
    private var keys_d = pref.all.keys.toMutableList()
    private var value_d = pref.all.values.toMutableList()

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
                EndDate.text = "${holidayData.holidayEndDay.day}/${holidayData.holidayEndDay.month}/${holidayData.holidayEndDay.year}"
                HolidayCount.text = "${holidayData.holidayNumber} ${holidayData.holidayUnit}"
                HolidayName.text = holidayData.holidayName

            }
        }
    }

    fun removeItem(position: Int, name: String="Item") {
        if (keys_d.size == value_d.size && keys_d.isNotEmpty()){
            editor.remove(keys_d[position])
            editor.commit()
            keys_d = pref.all.keys.toMutableList()
            value_d = pref.all.values.toMutableList()
            notifyItemRemoved(position)
            notifyItemRangeChanged(0,keys_d.size-1)

        } else {
            val dialog = CustomDialog(context, null, null)
            dialog.createBasicCustomDialog(
                "Dismiss", "An error was caused while deleting ${name}, try again later.",
                true,
                true,
                "failed To Delete"
            )
        }
    }

    // Attach the ItemTouchHelper to the RecyclerView in your Fragment or Activity
    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
