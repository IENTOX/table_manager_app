package com.extremex.tablemanager.admin.fragment

import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.extremex.tablemanager.R
import com.extremex.tablemanager.databinding.FragmentDashboardBinding
import com.extremex.tablemanager.lib.AddClassroomDialog
import com.extremex.tablemanager.lib.AddSubjectsDialog
import com.extremex.tablemanager.lib.AddTimeSlotDialog
import com.extremex.tablemanager.lib.AdminMessage
import com.extremex.tablemanager.lib.AdminMessagesDialog
import com.extremex.tablemanager.lib.AllTeachers
import com.extremex.tablemanager.lib.AssignTeachersDialog
import com.extremex.tablemanager.lib.AvailableTeachers
import com.extremex.tablemanager.lib.CodeGenerator
import com.extremex.tablemanager.lib.ManageTeachersDialog
import com.extremex.tablemanager.lib.TeachersAllAvailabilityAdapter
import com.extremex.tablemanager.lib.TeachersAvailabilityAdapter
import com.extremex.tablemanager.lib.TeachersUnavailabilityAdapter
import com.extremex.tablemanager.lib.UnavailableTeachers

/*
*
*
*   <a href="https://www.flaticon.com/free-icons/books" title="books icons">Books icons created by popo2021 - Flaticon</a>
*   <a href="https://www.flaticon.com/free-icons/classroom" title="classroom icons">Classroom icons created by Freepik - Flaticon</a>
*   login icon attr/ <a href="https://www.flaticon.com/free-icons/login" title="login icons">Login icons created by afif fudin - Flaticon</a>
*   hold icon attr/ <a href="https://www.flaticon.com/free-icons/stop" title="stop icons">Stop icons created by Freepik - Flaticon</a>
*   notification icon attr/ <a href="https://www.flaticon.com/free-icons/notification" title="notification icons">Notification icons created by Freepik - Flaticon</a>
*   settings icon attr/  <a href="https://www.flaticon.com/free-icons/settings" title="settings icons">Settings icons created by Pixel perfect - Flaticon</a>
*   dashboard icon attr/  <a href="https://www.flaticon.com/free-icons/dashboard" title="dashboard icons">Dashboard icons created by Pixel perfect - Flaticon</a>
* */


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        //return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.RoomCode.text = smartGenerateRoomCode() + "   "
        binding.ShareCodeButton.setOnClickListener {
            shareClassroomCode(binding.RoomCode.text.trim().toString())
        }
        val selectedList = requireContext().resources.getStringArray(R.array.TeachersAvailability)

        val adapter = ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, selectedList)
        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
        binding.TeachersAvailabilitySpinner.adapter = adapter

        binding.ToggleTeachersList.setOnClickListener {
            if (binding.TeachersListView.isVisible && binding.TeachersAvailabilitySpinner.isVisible){
                binding.ToggleTeachersList.text = "View all teachers"
                binding.TeachersListView.visibility = View.GONE
                binding.TeachersAvailabilitySpinner.visibility = View.GONE
            } else {
                binding.ToggleTeachersList.text = "Hide..."
                binding.TeachersListView.visibility = View.VISIBLE
                binding.TeachersAvailabilitySpinner.visibility = View.VISIBLE
            }
        }
        binding.AddSubjectButton.setOnClickListener {
            showAddSubjectsDialog()
        }

        binding.AssignTeachersButton.setOnClickListener {
            showAssignTeachersDialog()
        }

        binding.AddClassroomButton.setOnClickListener {
            showAddClassroomDialog()
        }

        binding.AddTimeSlotButton.setOnClickListener {
            showAddTimeSlotsDialog()
        }

        binding.ManageTeachersButton.setOnClickListener {
            showManageTeachersDialog()
        }



        var defaultSelection = selectedList[0]

        binding.TeachersAvailabilitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.TeachersAvailabilitySpinner.notifySubtreeAccessibilityStateChanged(
                    view!!,
                    view,
                    position
                )
                defaultSelection = selectedList[position]
                when (position) {
                    0 -> {

                        val allTeacherList = listOf(
                            AllTeachers("John Doe", "ID001", "Available"),
                            AllTeachers("Jane Smith", "ID002", "Not Available"),
                            AllTeachers("Robert Brown", "ID003", "Available"),
                            AllTeachers("Emily Clark", "ID004", "Available"),
                            AllTeachers("Michael White", "ID005", "Not Available")
                        )
                        val allAdapter = TeachersAllAvailabilityAdapter(allTeacherList)
                        binding.TeachersListView.adapter = allAdapter
                        binding.TeachersListView.layoutManager =
                            LinearLayoutManager(requireContext())

                    }

                    1 -> {

                        val availableTeacherList = listOf(
                            AvailableTeachers("John Doe", "ID001"),
                            AvailableTeachers("Robert Brown", "ID003"),
                            AvailableTeachers("Emily Clark", "ID004")
                        )
                        val availableAdapter = TeachersAvailabilityAdapter(availableTeacherList)
                        binding.TeachersListView.adapter = availableAdapter
                        binding.TeachersListView.layoutManager =
                            LinearLayoutManager(requireContext())
                    }

                    2 -> {

                        val unavailableTeacherList = listOf(
                            UnavailableTeachers("Jane Smith", "ID002", "26 Jun 2024"),
                            UnavailableTeachers("Michael White", "ID005", "10 Oct 2023")
                        )
                        val unavailableAdapter = TeachersUnavailabilityAdapter(unavailableTeacherList)
                        binding.TeachersListView.adapter = unavailableAdapter
                        binding.TeachersListView.layoutManager =
                            LinearLayoutManager(requireContext())
                    }

                    else -> {

                        val allTeacherList = listOf(
                            AllTeachers("John Doe", "ID001", "Available"),
                            AllTeachers("Jane Smith", "ID002", "Not Available"),
                            AllTeachers("Robert Brown", "ID003", "Available"),
                            AllTeachers("Emily Clark", "ID004", "Available"),
                            AllTeachers("Michael White", "ID005", "Not Available")
                        )
                        val allAdapter = TeachersAllAvailabilityAdapter(allTeacherList)
                        binding.TeachersListView.adapter = allAdapter
                        binding.TeachersListView.layoutManager =
                            LinearLayoutManager(requireContext())
                    }
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                defaultSelection = selectedList[0]
            }
        }
        binding.MessagesButton.setOnClickListener {
            viewMessages()
        }

    }

    private fun smartGenerateRoomCode() :String{
        return CodeGenerator().generateCode()
    }

    private fun shareClassroomCode(classroomCode: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        val sub = "Classroom Code:\n\n"
        val shareBody = " Here the the joining code for your classroom: $classroomCode, Please make sure that the classroom code is not shared publicly"
        share.putExtra(Intent.EXTRA_SUBJECT, sub)
        share.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(share, "Classroom Joining code"))
    }
    private fun showAddClassroomDialog() {
        val addClassroomDialog = AddClassroomDialog(requireContext())
        addClassroomDialog.show()
    }
    private fun showAddSubjectsDialog() {
        val addSubjectsDialog = AddSubjectsDialog(requireContext())
        addSubjectsDialog.show()
    }
    private fun showAssignTeachersDialog() {
        val assignTeachersDialog = AssignTeachersDialog(requireContext())
        assignTeachersDialog.show()
    }
    private fun showAddTimeSlotsDialog() {
        val addTimeSlotsDialog = AddTimeSlotDialog(requireContext())
        addTimeSlotsDialog.show()
    }
    private fun showManageTeachersDialog() {
        val manageTeachersDialog = ManageTeachersDialog(requireContext())
        manageTeachersDialog.show()
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
                TODO("Not yet implemented")
            }
        })
        messagesDialog.show()
    }
}