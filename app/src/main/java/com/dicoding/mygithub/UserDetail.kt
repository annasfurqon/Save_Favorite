package com.dicoding.mygithub

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.mygithub.databinding.ActivityUserDetailBinding
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion._ID
import com.dicoding.mygithub.db.FavoriteHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetail : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var dbHelper: FavoriteHelper
    private var isFavorite = false

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

        dbHelper = FavoriteHelper.getInstance(applicationContext)
        dbHelper.open()
        setStatusFavorite(isFavorite)
        val cursor: Cursor = dbHelper.queryById(user.username.toString())

        if (cursor.moveToNext()) {
            isFavorite = true
            setStatusFavorite(true)
        }


        binding.favAdd.setOnClickListener(this)

    }

    private fun setStatusFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.favAdd.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
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

    override fun onClick(v: View) {
        val data = intent.getParcelableExtra<UserData>(EXTRA_USER) as UserData
        when (v.id) {
            R.id.fav_add -> {
                if (isFavorite) {
                    val idUser = data.username.toString()
                    dbHelper.deleteById(idUser)
                    Toast.makeText(this, "Dihapus dari favorite", Toast.LENGTH_SHORT).show()
                    setStatusFavorite(false)
                    isFavorite = false
                } else {
                    Toast.makeText(this, "Ditambah ke favorite", Toast.LENGTH_SHORT).show()
                    val values = ContentValues()
                    values.put(_ID, data.username)
                    values.put(NAME, data.name)
                    values.put(AVATAR, data.avatar)
                    values.put(COMPANY, data.company)
                    values.put(LOCATION, data.location)
                    values.put(REPOSITORY, data.repository)
                    values.put(FOLLOWERS, data.followers)
                    values.put(FOLLOWING, data.following)
                    values.put(FAVORITE, "isFav")

                    isFavorite = true
                    contentResolver.insert(CONTENT_URI, values)
                    //dbHelper.insert(values)
                    setStatusFavorite(true)
                }
            }
        }
    }
}


