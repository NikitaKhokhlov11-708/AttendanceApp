package com.example.attendanceapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.StatisticsPresenter
import com.example.attendanceapp.views.StatisticsView
import com.google.gson.Gson
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.android.synthetic.main.fragment_statistics.view.*


class StatisticsFragment : MvpAppCompatDialogFragment(), StatisticsView {

    @InjectPresenter
    lateinit var statisticsPresenter: StatisticsPresenter

    @ProvidePresenter
    fun provideStatisticsPresenter() = StatisticsPresenter()

    private var root: View? = null
    lateinit var btDialog: AlertDialog

    companion object {
        fun newInstance(): StatisticsFragment =
            StatisticsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_statistics, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setTitle("Загрузка...")

        builder.setView(R.layout.layout_loading_dialog)
        btDialog = builder.create()
        btDialog.show()
        getCurrentUser()

        if (arguments!!.containsKey("courseName")) {
            statisticsPresenter.courseName = arguments!!.getString("courseName")!!
            if (arguments!!.containsKey("student")) {
                statisticsPresenter.student = arguments!!.getString("student")!!
                root!!.tableStat.headerAdapter =
                    SimpleTableHeaderAdapter(this.context, "Дата", "Посещение")
                root!!.tableStat.columnCount = 2
                statisticsPresenter.getStudentStatistics()

            } else if (arguments!!.containsKey("group")) {
                statisticsPresenter.group = arguments!!.getString("group")!!
                statisticsPresenter.dateT = arguments!!.getString("dateT")!!
                root!!.tableStat.headerAdapter =
                    SimpleTableHeaderAdapter(this.context, "ФИО")
                root!!.tableStat.columnCount = 1
                statisticsPresenter.getGroupStatistics()
            } else {
                statisticsPresenter.dateT = arguments!!.getString("dateT")!!
                root!!.tableStat.headerAdapter =
                    SimpleTableHeaderAdapter(this.context, "ФИО")
                root!!.tableStat.columnCount = 1
                statisticsPresenter.getAllStatistics()
            }
        } else {
            if (arguments!!.containsKey("courseId")) {
                root!!.tableStat.headerAdapter =
                    SimpleTableHeaderAdapter(this.context, "Дата", "Посещение")
                root!!.tableStat.columnCount = 2

                statisticsPresenter.getStatistics(arguments!!.getString("courseId")!!)
            } else if (arguments!!.containsKey("date")) {
                root!!.tableStat.headerAdapter =
                    SimpleTableHeaderAdapter(this.context, "Предмет", "Посещение")
                root!!.tableStat.columnCount = 2

                statisticsPresenter.getStatisticsByDate(arguments!!.getString("date")!!)
            }
        }

        root!!.btnClose.setOnClickListener {
            dismiss()
        }


        btDialog.cancel()

        return root
    }

    override fun getCurrentUser() {
        val mPrefs = this.activity!!.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString("CurrentUser", "")
        val user: User = gson.fromJson<User>(json, User::class.java)

        statisticsPresenter.getUser(user)
    }

    override fun viewStat() {
        root!!.tableStat.dataAdapter =
            SimpleTableDataAdapter(this.context, statisticsPresenter.list)
        root!!.tableStat.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.setLayout(width, height)
    }

    override fun changeTableName(title: String) {
        dialog!!.setTitle(title)
    }
}