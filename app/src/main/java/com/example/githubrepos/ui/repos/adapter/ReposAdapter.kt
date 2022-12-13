package com.example.githubrepos.ui.repos.adapter

//import com.example.trendinggithubrepos.extension_functions.launchViewClicks
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.PagingDataAdapter
import com.bumptech.glide.Glide
import com.example.githubrepos.R
import com.example.githubrepos.base.BaseViewHolder
import com.example.githubrepos.databinding.ItemGitHubReposBinding
import com.example.githubrepos.model.Item

class ReposAdapter(private val lifecycleScope: LifecycleCoroutineScope) :
    PagingDataAdapter<Item, ReposAdapter.MyViewHolder>(Item.DIFF_CALLBACK) {

    var clickListener: ((repos: Item, position: Int, viewId: Int) -> Unit)? = null

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(
            ItemGitHubReposBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.CYAN)
        }
        holder.bind(position)
    }

    inner class MyViewHolder(dataBinding: ItemGitHubReposBinding) :
        BaseViewHolder<ItemGitHubReposBinding>(dataBinding) {

        override fun bind(pos: Int) {
            dataBinding.apply {
                getItem(pos).let { model ->
                    bModel = model

                    Glide.with(itemView)
                        .load(model?.owner?.avatar_url)
                        .centerCrop()
                        .error(android.R.drawable.stat_notify_error)
                        .into(avatar)
                }

                itemRootView.setOnClickListener {
                    it.setBackgroundColor(Color.BLACK)
                    selectedPosition = pos
                }
            }
            super.bind(pos)
        }
    }
}