package com.example.attendanceapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.attendanceapp.ui.fragments.CheckInFragment
import com.example.attendanceapp.ui.fragments.StudentAttendanceFragment

class StudentTabsAdapter(fm: FragmentManager, private var mNumOfTabs: Int) :
    FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return mNumOfTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CheckInFragment()
            }
            1 -> {
                StudentAttendanceFragment()
            }
            else -> CheckInFragment()
        }
    }
}