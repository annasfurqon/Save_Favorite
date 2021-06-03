package com.dicoding.consumerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.consumerapp.databinding.Users2Binding

class FragmentAdapter(private val listUser: ArrayList<FragmentData>) :
        RecyclerView.Adapter<FragmentAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: Users2Binding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(user: FragmentData) {
            with(binding) {
                Glide.with(itemView.context)
                        .load(user.avatar)
                        .into(imgPhoto)

                txtUsername.text = user.username

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentAdapter.ListViewHolder {
        val binding = Users2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size
}