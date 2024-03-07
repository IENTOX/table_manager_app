package com.extremex.tablemanager.admin.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentDashboardBinding
import com.extremex.tablemanager.lib.AdminMessage
import com.extremex.tablemanager.dialog.AdminMessagesDialog
import com.extremex.tablemanager.applet.CodeGenerator
import com.extremex.tablemanager.adapter.PendingTeachersAdapter
import com.extremex.tablemanager.models.PendingTeacherObject

/*
*   <a href="https://www.flaticon.com/free-icons/application" title="application icons">Application icons created by Freepik - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/login" title="login icons">Login icons created by Pixel perfect - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/reset-password" title="reset password icons">Reset password icons created by Uniconlabs - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/logout" title="logout icons">Logout icons created by HideMaru - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/certified" title="certified icons">Certified icons created by Afian Rochmah Afif - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/collaboration" title="collaboration icons">Collaboration icons created by Freepik - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/contribution" title="Contribution icons">Contribution icons created by Circlon Tech - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/terms-and-conditions" title="terms and conditions icons">Terms and conditions icons created by Anggara - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/privacy-policy" title="privacy-policy icons">Privacy-policy icons created by Pixel perfect - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/cab" title="cab icons">Cab icons created by Freepik - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/day-and-night" title="day and night icons">Day and night icons created by Freepik - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/female" title="female icons">Female icons created by Prosymbols Premium - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/books" title="books icons">Books icons created by popo2021 - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/classroom" title="classroom icons">Classroom icons created by Freepik - Flaticon</a>
*   login icon attr/ <a href="https://www.flaticon.com/free-icons/login" title="login icons">Login icons created by afif fudin - Flaticon</a>
*   hold icon attr/ <a href="https://www.flaticon.com/free-icons/stop" title="stop icons">Stop icons created by Freepik - Flaticon</a>
*   notification icon attr/ <a href="https://www.flaticon.com/free-icons/notification" title="notification icons">Notification icons created by Freepik - Flaticon</a>
*   settings icon attr/  <a href="https://www.flaticon.com/free-icons/settings" title="settings icons">Settings icons created by Pixel perfect - Flaticon</a>
*   dashboard icon attr/  <a href="https://www.flaticon.com/free-icons/dashboard" title="dashboard icons">Dashboard icons created by Pixel perfect - Flaticon</a>
* */


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    interface ItemListeners{
        fun onAddTimeSlot()
        fun onManageTeacher()
        fun onManageClassroom()
        fun onManageSubject()
    }

    private var listener: ItemListeners? = null
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var prefs: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemListeners){
            listener = context
        } else {
            throw IllegalArgumentException("ItemListeners has to be implemented to the root Activity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences(
            "FRAGMENT_TRANSACTIONS",
            AppCompatActivity.MODE_PRIVATE
        )
        init()
        binding.RoomCode.text = smartGenerateRoomCode() + "   "
        binding.ShareCodeButton.setOnClickListener {
            shareClassroomCode(binding.RoomCode.text.trim().toString())
        }


        val pendingTeacherList = listOf(
            PendingTeacherObject("John Doe", "10/02/2024", "JFY7484731"),
            PendingTeacherObject("Jane Smith", "10/02/2024", "FD0TR634ER"),
            PendingTeacherObject("Robert Brown", "10/02/2024", "IDN46WU45W"),
            PendingTeacherObject("Emily Clark", "10/02/2024", "IW4R8FG32Q"),
            PendingTeacherObject("Michael White", "10/02/2024", "QPR857E543")
        )
        /*
        val pendingTeacherList = listOf(
            PendingTeacherObject("","","")
        )
        */
        val pendingAdapter = PendingTeachersAdapter(pendingTeacherList)
        binding.TeachersListView.layoutManager = LinearLayoutManager(requireContext())
        binding.TeachersListView.adapter = pendingAdapter

        binding.MessagesButton.setOnClickListener {
            viewMessages()
        }


        binding.AddSubjectButton.setOnClickListener {
            listener?.onManageSubject()
        }

        binding.AddClassroomButton.setOnClickListener {
            listener?.onManageClassroom()
        }

        binding.AddTimeSlotButton.setOnClickListener {
            listener?.onAddTimeSlot()
        }

        binding.ManageTeachersButton.setOnClickListener {
            listener?.onManageTeacher()
        }
    }

    private fun init(){
        val prefEditor = prefs.edit()
        prefEditor.putInt("LastFragmentMain", 0)
        prefEditor.commit()
    }

    private fun smartGenerateRoomCode() :String{
        return CodeGenerator().generateCode()
    }

    private fun shareClassroomCode(classroomCode: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        val sub = "Classroom Code:\n\n"
        val shareBody = "Here the the joining code for your classroom: $classroomCode, Please make sure that the classroom code is not shared publicly"
        share.putExtra(Intent.EXTRA_SUBJECT, sub)
        share.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(share, "Classroom Joining code"))
    }

    private fun viewMessages() {
        val messagesDialog = AdminMessagesDialog(requireContext())

        messagesDialog.setAcceptListener(object : AdminMessagesDialog.AcceptListener {
            override fun onAccept(message: AdminMessage) {
                TODO("Not yet implemented")
            }
        })

        messagesDialog.setHoldListener(object : AdminMessagesDialog.HoldListener {
            override fun onHold(message: AdminMessage) {
                TODO("Not yet implemented")
            }
        })

        messagesDialog.setRejectListener(object : AdminMessagesDialog.RejectListener {
            override fun onReject(message: AdminMessage) {
                TODO("Not yet implemented")
            }
        })
        messagesDialog.setCloseListener(object : AdminMessagesDialog.CloseListener {
            override fun onClose() {
                // do on close
            }
        })
        messagesDialog.show()
    }
}