package com.example.attendanceapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.adapters.RecyclerAdapter
import com.example.attendanceapp.models.User
import com.example.attendanceapp.presenters.CoursesPresenter
import com.example.attendanceapp.views.CoursesView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_courses.view.*

class CoursesFragment : MvpAppCompatFragment(), CoursesView {

    @InjectPresenter
    lateinit var coursesPresenter: CoursesPresenter

    @ProvidePresenter
    fun provideCoursesPresenter() = CoursesPresenter()

    private var root: View? = null
    lateinit var btDialog: AlertDialog
    private var adapter = RecyclerAdapter()

    companion object {
        fun newInstance(): CoursesFragment =
            CoursesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_courses, null)

        root!!.btnAdd.setOnClickListener { onAddClick() }

        getCurrentUser()
        coursesPresenter.getCourses()

        return root
    }

    override fun getCurrentUser() {
        val mPrefs = this.activity!!.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = mPrefs.getString("CurrentUser", "")
        val user: User = gson.fromJson<User>(json, User::class.java)

        coursesPresenter.getUser(user)
    }

    private fun onAddClick() {
        val fr = AddCourseFragment()
        fr.show(fragmentManager!!, "addCourseFragment")
    }

    override fun initRecycler() {
        adapter.courses = coursesPresenter.myList
        root!!.recyclerView.layoutManager = LinearLayoutManager(context)
        root!!.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager(context).orientation
        )
        root!!.recyclerView.addItemDecoration(dividerItemDecoration)
    }
}