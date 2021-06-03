package com.dicoding.consumerapp

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.consumerapp.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.followings
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User Detail"

        val user = intent.getParcelableExtra<UserData>(EXTRA_USER) as UserData
        setDetail(user)
        viewPagerConfig(user)
    }

    private fun viewPagerConfig(user: UserData) {
        val sectionsPagerAdapter = SectionPageAdapter(this)
        sectionsPagerAdapter.username = user.username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setDetail(user: UserData) {
        val text1 = "${user.username}"
        val text2 = "${user.name}"
        val text3 = "${user.location}"
        val text4 = "${user.company}"
        val text5 = user.followers.toString()
        val text6 = user.following.toString()
        val text7 = user.repository.toString()

        binding.txtUsername.text = text1
        binding.txtName.text = text2
        binding.lokasi.text = text3
        binding.perusahaan.text = text4
        binding.txtFollower.text = text5
        binding.txtFollowing.text = text6
        binding.txtRepository.text = text7
        Glide.with(this)
            .load(user.avatar.toString())
            .into(binding.avatar)

    }

}


