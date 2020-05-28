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
import com.example.attendanceapp.presenters.StudentAttendancePresenter
import com.example.attendanceapp.views.StudentAttendanceView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_student_attendance.*
import kotlinx.android.synthetic.main.fragment_student_attendance.view.*
import java.util.*


class StudentAttendanceFragment : MvpAppCompatFragment(), StudentAttendanceView {

    @InjectPresenter
    lateinit var studentAttendancePresenter: StudentAttendancePresenter

    @ProvidePresenter
    fun provideStudentAttendancePresenter() = StudentAttendancePresenter()

    private var root: View? = null
    lateinit var btDialog: AlertDialog

    companion object {
        fun newInstance(): StudentAttendanceFragment =
            StudentAttendanceFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_student_attendance, null)

        root!!.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_subj -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
                    builder.setCancelable(false) // if you want user to wait for some process to finish,
                    builder.setTitle("Загрузка...")

                    builder.setView(R.layout.layout_loading_dialog)
                    btDialog = builder.create()
                    btDialog.show()
                    ll_date.visibility = View.GONE
                    studentAttendancePresenter.getAvaliableSubjects()
                }
                R.id.radio_day -> {
                    ll_subj.visibility = View.GONE
                    showCalendar()
                }
            }
        }

        root!!.btnShow.setOnClickListener {
            if (root!!.radioGroup.checkedRadioButtonId == R.id.radio_subj) {
                val s = studentAttendancePresenter.list[root!!.spinnerSubj.selectedItemId.toInt()]
                studentAttendancePresenter.getCourseByName(s)
            } else if (root!!.radioGroup.checkedRadioButtonId == R.id.radio_day) {
                var day = datePicker.dayOfMonth.toString()
                if (datePicker.dayOfMonth.toString().length == 1)
                    day = "0" + datePicker.dayOfMonth
                var month = (datePicker.month + 1).toString()
                if ((datePicker.month + 1).toString().length == 1)
                    month = "0" + (datePicker.month + 1)
                val year = datePicker.year
                studentAttendancePresenter.date = "$day $month $year"
                showStat()
            }
        }

        getCurrentUser()

        return root
    }

    override fun getCurrentUser() {
        val mPrefs = this.activity!!.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString("CurrentUser", "")
        val user: User = gson.fromJson<User>(json, User::class.java)

        studentAttendancePresenter.getUser(user)
    }

    override fun showSubj() {
        val adapter = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, studentAttendancePresenter.list
        )

        root!!.spinnerSubj.adapter = adapter
        ll_subj.visibility = View.VISIBLE
        btDialog.cancel()

    }

    override fun showCalendar() {
        val today = Calendar.getInstance()

        datePicker.maxDate = Date().time

        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH),
            { view, year, monthOfYear, dayOfMonth ->

            })

        ll_date.visibility = View.VISIBLE
    }

    override fun showStat() {
        val statisticsFrag = StatisticsFragment()
        val arguments = Bundle()

        when (root!!.radioGroup.checkedRadioButtonId) {
            R.id.radio_subj -> arguments.putString("courseId", studentAttendancePresenter.courseId)
            R.id.radio_day -> arguments.putString("date", studentAttendancePresenter.date)
        }

        statisticsFrag.arguments = arguments
        val activity = view!!.context as AppCompatActivity
        statisticsFrag.show(activity.supportFragmentManager, "Statistics")
    }
}