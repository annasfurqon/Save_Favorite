package com.dicoding.mygithub

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithub.databinding.ActivityFavoriteUserBinding
import com.dicoding.mygithub.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.mygithub.db.FavoriteHelper
import com.dicoding.mygithub.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UserAdapter
    private val list : ArrayList<UserData> = ArrayList()

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"
        binding.rvUser.setHasFixedSize(true)
        showRecyclerList()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserData>(EXTRA_STATE)
            if (list != null) {
                adapter.setData(list)
            }
        }
        //
    }

    override fun onResume() {
        super.onResume()
        loadUserAsync()
    }


    private fun showRecyclerList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(list)
        adapter.notifyDataSetChanged()
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserData) {
                showSelectedUser(user)
            }
        })
    }
    private fun showSelectedUser(user: UserData) {
        val intent = Intent(this@FavoriteUserActivity, UserDetail::class.java)
        intent.putExtra(UserDetail.EXTRA_USER, user)
        startActivity(intent)
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val userHelper = FavoriteHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArray(cursor)
            }

            val user = deferredUser.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (user.size > 0) {
                adapter.setData(user)
                adapter.notifyDataSetChanged()
            } else {
                adapter.setData(user)
                showSnackbarMessage()
            }
            userHelper.close()
        }
    }
    private fun showSnackbarMessage() {
        Toast.makeText(this, "data kosong", Toast.LENGTH_SHORT).show()
    }
}