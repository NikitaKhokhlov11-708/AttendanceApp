package com.example.attendanceapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.TeacherAttendancePresenter
import com.example.attendanceapp.views.TeacherAttendanceView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_teacher_attendance.*
import kotlinx.android.synthetic.main.fragment_teacher_attendance.view.*
import java.util.*

class TeacherAttendanceFragment : MvpAppCompatFragment(), TeacherAttendanceView {

    @InjectPresenter
    lateinit var teacherAttendancePresenter: TeacherAttendancePresenter

    @ProvidePresenter
    fun provideTeacherAttendancePresenter() = TeacherAttendancePresenter()

    private var root: View? = null
    lateinit var btDialog: AlertDialog

    companion object {
        fun newInstance(): TeacherAttendanceFragment =
            TeacherAttendanceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_teacher_attendance, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setTitle("Загрузка...")

        builder.setView(R.layout.layout_loading_dialog)
        btDialog = builder.create()
        btDialog.show()


        root!!.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_all -> {
                    ll_student.visibility = View.GONE
                    getSelectedCourse()
                    showCalendar()
                }
                R.id.radio_group -> {
                    getSelectedCourse()
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
                    builder.setCancelable(false) // if you want user to wait for some process to finish,
                    builder.setTitle("Загрузка...")

                    builder.setView(R.layout.layout_loading_dialog)
                    btDialog = builder.create()
                    btDialog.show()
                    teacherAttendancePresenter.getGroups()
                    showCalendar()
                }
                R.id.radio_student -> {
                    getSelectedCourse()
                    ll_dateT.visibility = View.GONE
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
                    builder.setCancelable(false) // if you want user to wait for some process to finish,
                    builder.setTitle("Загрузка...")

                    builder.setView(R.layout.layout_loading_dialog)
                    btDialog = builder.create()
                    btDialog.show()
                    teacherAttendancePresenter.getStudents()
                }
            }
        }

        root!!.btnShow.setOnClickListener {
            if (root!!.radioGroup.checkedRadioButtonId == R.id.radio_all) {
                var day = datePicker.dayOfMonth.toString()
                if (datePicker.dayOfMonth.toString().length == 1)
                    day = "0" + datePicker.dayOfMonth
                var month = (datePicker.month + 1).toString()
                if ((datePicker.month + 1).toString().length == 1)
                    month = "0" + (datePicker.month + 1)
                val year = datePicker.year
                teacherAttendancePresenter.date = "$day $month $year"
                showStat()
            } else if (root!!.radioGroup.checkedRadioButtonId == R.id.radio_group) {
                var day = datePicker.dayOfMonth.toString()
                if (datePicker.dayOfMonth.toString().length == 1)
                    day = "0" + datePicker.dayOfMonth
                var month = (datePicker.month + 1).toString()
                if ((datePicker.month + 1).toString().length == 1)
                    month = "0" + (datePicker.month + 1)
                val year = datePicker.year
                teacherAttendancePresenter.date = "$day $month $year"
                getSelectedGroup()

                showStat()
            } else if (root!!.radioGroup.checkedRadioButtonId == R.id.radio_student) {
                getSelectedStudent()

                showStat()
            }
        }

        getCurrentUser()
        teacherAttendancePresenter.getAvaliableSubjects()

        return root
    }

    override fun getCurrentUser() {
        val mPrefs = this.activity!!.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString("CurrentUser", "")
        val user: User = gson.fromJson<User>(json, User::class.java)

        teacherAttendancePresenter.getUser(user)
    }

    override fun showSubj() {
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, teacherAttendancePresenter.listSubj
        )

        root!!.spinnerSubj.adapter = adapter
        btDialog.cancel()

    }

    override fun showCalendar() {
        val today = Calendar.getInstance()

        datePicker.maxDate = Date().time

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH),
            { view, year, monthOfYear, dayOfMonth ->

            })

        ll_dateT.visibility = View.VISIBLE
    }

    override fun showStudentsDropDown() {
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, teacherAttendancePresenter.listStudents
        )

        root!!.spinnerStudents.adapter = adapter

        ll_student.visibility = View.VISIBLE
        btDialog.cancel()
    }

    override fun showGroupsDropdown() {
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, teacherAttendancePresenter.listGroups
        )

        root!!.spinnerStudents.adapter = adapter

        ll_student.visibility = View.VISIBLE
        btDialog.cancel()
    }

    override fun showStat() {
        val statisticsFrag = StatisticsFragment()
        val arguments = Bundle()
        arguments.putString("courseName", teacherAttendancePresenter.courseName)

        when (root!!.radioGroup.checkedRadioButtonId) {
            R.id.radio_all -> {
                arguments.putString("dateT", teacherAttendancePresenter.date)
            }
            R.id.radio_group -> {
                arguments.putString("dateT", teacherAttendancePresenter.date)
                arguments.putString("group", teacherAttendancePresenter.selectedGroup)
            }
            R.id.radio_student -> {
                arguments.putString("student", teacherAttendancePresenter.selectedStudent)
            }
        }

        statisticsFrag.arguments = arguments
        val activity = view!!.context as AppCompatActivity
        statisticsFrag.show(activity.supportFragmentManager, "Statistics")
    }

    override fun getSelectedCourse() {
        teacherAttendancePresenter.courseName =
            teacherAttendancePresenter.listSubj[root!!.spinnerSubj.selectedItemId.toInt()]
    }

    override fun getSelectedGroup() {
        teacherAttendancePresenter.selectedGroup =
            teacherAttendancePresenter.listGroups[root!!.spinnerStudents.selectedItemId.toInt()]
    }

    override fun getSelectedStudent() {
        teacherAttendancePresenter.selectedStudent =
            teacherAttendancePresenter.listStudents[root!!.spinnerStudents.selectedItemId.toInt()]
    }
}