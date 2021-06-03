package com.dicoding.mygithub

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragements: Fragment? = null
        when(position){
            0 -> fragements = FollowerFragment.newInstance(username)
            1 -> fragements = FollowingFragment.newInstance(username)
        }
        return fragements as Fragment
    }
}