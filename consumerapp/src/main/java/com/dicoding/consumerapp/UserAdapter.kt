package com.dicoding.consumerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.consumerapp.databinding.UsersBinding

class UserAdapter(private val listUser: ArrayList<UserData>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null


    interface OnItemClickCallback {
        fun onItemClicked(user: UserData)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    inner class ListViewHolder(private val binding: UsersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(imgPhoto)

                txtUsername.text = user.username
                txtName.text = user.name
                txtFollower.text = user.followers.toString()
                txtRepository.text = user.repository.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ListViewHolder {
        val binding = UsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    fun setData(items: ArrayList<UserData>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

}
