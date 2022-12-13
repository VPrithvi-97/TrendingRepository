package com.example.githubrepos.ui.repos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.githubrepos.R
import com.example.githubrepos.base.BaseViewHolder
import com.example.githubrepos.databinding.ItemFooterLoadStateBinding
import com.example.githubrepos.util.Utilities

class ReposLoadStateAdapter(
    private val mLifecycleScope: LifecycleCoroutineScope,
    private val retry: () -> Unit
) : LoadStateAdapter<ReposLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        LoadStateViewHolder(
            ItemFooterLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class LoadStateViewHolder(mDataBinding: ItemFooterLoadStateBinding) :
        BaseViewHolder<ItemFooterLoadStateBinding>(mDataBinding) {

        init {
            dataBinding.apply {
//                btnRetry.launchViewClick(mLifecycleScope) { retry.invoke() }
            }
        }

        fun bind(loadState: LoadState) {
            dataBinding.apply {
                bLoadState = loadState
//                bMsg =
//                    Kite.string[if (Utilities.isInternetAvailable(root.context)) R.string.err_something_went_wrong else R.string.err_no_internet]
//                executePendingBindings()
            }
        }

    }

}