package com.muratipek.ak3_sharephotofirebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muratipek.ak3_sharephotofirebase.databinding.RecyclerRowBinding
import com.muratipek.ak3_sharephotofirebase.model.Post
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {
    class PostHolder(var binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerRowUserEmail.text = postList.get(position).userEmail
        holder.binding.commentText.text = postList.get(position).userComment
        Picasso.get().load(postList.get(position).imageUrl).into(holder.binding.recyclerRowImage)
    }
}