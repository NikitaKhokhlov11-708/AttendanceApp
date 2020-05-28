package com.example.attendanceapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.AddCoursePresenter
import com.example.attendanceapp.views.AddCourseView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_add_course.*
import kotlinx.android.synthetic.main.fragment_add_course.view.*


class AddCourseFragment : MvpAppCompatDialogFragment(), AddCourseView,
    MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener {

    @InjectPresenter
    lateinit var addCoursePresenter: AddCoursePresenter

    @ProvidePresenter
    fun provideAddCoursePresenter() = AddCoursePresenter()

    private var root: View? = null
    private var mContext: Context? = null

    companion object {
        fun newInstance(): AddCourseFragment =
            AddCourseFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_add_course, null)

        dialog!!.setTitle("Создать курс")

        root!!.btnAddCourse.setOnClickListener {
            addCourse()
        }

        root!!.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            addCoursePresenter.selectedItems = mutableListOf()

            when (checkedId) {
                R.id.radio_course -> addCoursePresenter.getAllCourses()
                R.id.radio_groups -> addCoursePresenter.getAllGroups()
                R.id.radio_students -> addCoursePresenter.getAllStudents()
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

        addCoursePresenter.getUser(user)
    }

    override fun addCourse() {
        addCoursePresenter.selectedItems = spinnerMulti.selectedStrings
        if (!TextUtils.isEmpty(etName.text) &&
            addCoursePresenter.selectedItems.isNotEmpty() &&
            addCoursePresenter.courseFlag != ""
        ) {
            addCoursePresenter.addCourse(etName.text.toString())
        } else
            Toast.makeText(activity, "Заполните все поля!", Toast.LENGTH_LONG).show()
    }

    override fun OnMultiSpinnerItemSelected(chosenItems: MutableList<String>?) {

        //This is where you get all your items selected from the Multi Selection Spinner :)
        for (i in chosenItems!!.indices) {
            // TODO
        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun addItemToSpinner(item: String) {
        root!!.spinnerMulti.setItem(item)
        root!!.spinnerMulti.load()
    }

    override fun clearSpinner() {
        root!!.spinnerMulti.clear()
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
}