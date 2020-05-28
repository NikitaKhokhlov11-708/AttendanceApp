package com.example.attendanceapp.ui.activities

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.adapters.TeacherTabsAdapter
import com.example.attendanceapp.presenters.TeacherPresenter
import com.example.attendanceapp.views.TeacherView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_teacher.*

class TeacherActivity : MvpAppCompatActivity(), TeacherView {

    @InjectPresenter
    lateinit var teacherPresenter: TeacherPresenter

    @ProvidePresenter
    fun provideTeacherPresenter() = TeacherPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        title = "Преподаватель"

        tab_layout.addTab(tab_layout.newTab().setText("Курсы"))
        tab_layout.addTab(tab_layout.newTab().setText("Просмотр посещаемости"))
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL
        val tabsAdapter = TeacherTabsAdapter(
            supportFragmentManager,
            tab_layout.tabCount
        )
        view_pager.adapter = tabsAdapter
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                view_pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}
