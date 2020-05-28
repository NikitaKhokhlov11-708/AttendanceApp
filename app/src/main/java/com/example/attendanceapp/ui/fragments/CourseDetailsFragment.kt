package com.example.attendanceapp.ui.fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.models.Course
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.CourseDetailsPresenter
import com.example.attendanceapp.views.CourseDetailsView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_course_details.*
import kotlinx.android.synthetic.main.fragment_course_details.view.*


class CourseDetailsFragment : MvpAppCompatDialogFragment(), CourseDetailsView {

    @InjectPresenter
    lateinit var courseDetailsPresenter: CourseDetailsPresenter

    @ProvidePresenter
    fun provideCourseDetailsPresenter() = CourseDetailsPresenter()

    private var root: View? = null
    private var mContext: Context? = null
    private lateinit var onDismissListener: DialogInterface.OnDismissListener
    lateinit var btDialog: AlertDialog
    val mBTA = BluetoothAdapter.getDefaultAdapter()

    companion object {
        fun newInstance(course: Course): CourseDetailsFragment {
            val courseDetailsFragment = CourseDetailsFragment()
            val args = Bundle()
            args.putSerializable("course", course)
            courseDetailsFragment.arguments = args

            return courseDetailsFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_course_details, null)

        dialog!!.setTitle("Просмотр курса")

        root!!.btnDelete.setOnClickListener {
            deleteCourse()
        }

        root!!.btnStartAttendance.setOnClickListener {
            onCheckInClick()
        }

        getCurrentUser()
        getCurrentCourse()

        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context!!)
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setTitle("Идет учет посещаемости...")
        builder.setNeutralButton("Завершить") { dialog, which ->
            activity!!.unregisterReceiver(mReceiver)
            mBTA.cancelDiscovery()
            mBTA.disable()
            dialog.cancel()
        }

        builder.setView(R.layout.layout_loading_dialog)
        btDialog = builder.create()

        return root
    }

    override fun getCurrentUser() {
        val mPrefs = this.activity!!.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString("CurrentUser", "")
        val user: User = gson.fromJson<User>(json, User::class.java)

        courseDetailsPresenter.getUser(user)
    }

    override fun getCurrentCourse() {
        courseDetailsPresenter.getCourse(arguments!!.getSerializable("course") as Course)
    }

    override fun deleteCourse() {
        courseDetailsPresenter.delete()
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun changeFragment() {
        dismiss()
    }

    override fun fillData(course: Course) {
        root!!.tv_name.text = course.name
        root!!.tv_teacher.text = course.teacherName
        when (course.type) {
            "courses" -> root!!.tv_type.text = "Курсы:"
            "groups" -> root!!.tv_type.text = "Группы:"
            "students" -> root!!.tv_type.text = "Студенты:"
        }

        listView.adapter = ArrayAdapter<String>(
            activity!!.baseContext,
            android.R.layout.simple_list_item_1,
            course.itemsList!!
        )
    }

    override fun onCheckInClick() {
        btDialog.show()

        val mBTA = BluetoothAdapter.getDefaultAdapter()

        if (mBTA == null) {
            Toast.makeText(
                activity!!.baseContext,
                "Нет доступного модуля Bluetooth",
                Toast.LENGTH_SHORT
            ).show()
            btDialog.cancel()
        }

        if (!mBTA.isEnabled) {
            Toast.makeText(activity!!.baseContext, "Включите Bluetooth", Toast.LENGTH_SHORT).show()
            btDialog.cancel()
        } else {
            courseDetailsPresenter.addAttendance()

            if (mBTA.isDiscovering) {
                mBTA.cancelDiscovery()
            }

            mBTA.startDiscovery()

            val ifilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            activity!!.registerReceiver(mReceiver, ifilter)
        }
    }

    val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                courseDetailsPresenter.checkInStudent(device.address)
            }
        }
    }
}