package com.example.attendanceapp.ui.activities

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.attendanceapp.R
import com.example.attendanceapp.adapters.StudentTabsAdapter
import com.example.attendanceapp.presenters.StudentPresenter
import com.example.attendanceapp.views.StudentView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_student.*

class StudentActivity : MvpAppCompatActivity(), StudentView {

    @InjectPresenter
    lateinit var studentPresenter: StudentPresenter

    @ProvidePresenter
    fun provideStudentPresenter() = StudentPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        title = "Студент"

        tab_layout.addTab(tab_layout.newTab().setText("Отметиться"))
        tab_layout.addTab(tab_layout.newTab().setText("Просмотр посещаемости"))
        tab_layout.tabGravity = TabLayout.GRAVITY_FILL
        val tabsAdapter = StudentTabsAdapter(
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
