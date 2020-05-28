package com.example.attendanceapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.models.Course
import com.example.attendanceapp.ui.fragments.CourseDetailsFragment
import kotlinx.android.synthetic.main.recyclerview_item_row.view.*


class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.CourseHolder>() {
    var courses = ArrayList<Course>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item_row, parent, false)
        return CourseHolder(inflatedView)
    }

    override fun getItemCount() = courses.size

    override fun onBindViewHolder(holder: CourseHolder, position: Int) {
        val itemCourse = courses[position]
        holder.bindCourse(itemCourse)
    }

    class CourseHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var course: Course? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val detailsFragment = CourseDetailsFragment()

            val arguments = Bundle()
            arguments.putSerializable("course", course)
            detailsFragment.arguments = arguments
            val activity = view.context as AppCompatActivity
            val fragmentManager = activity.supportFragmentManager

            detailsFragment.show(fragmentManager, "courseDetailsFragment")
        }

        companion object {
            private val COURSE_KEY = "COURSE"
        }

        fun bindCourse(course: Course) {
            this.course = course
            view.name.text = course.name
            view.teacher.text = course.teacherName
        }
    }
}