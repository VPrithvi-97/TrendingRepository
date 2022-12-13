package com.example.githubrepos.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<B : ViewDataBinding>(protected val dataBinding: B) :
    RecyclerView.ViewHolder(dataBinding.root) {

    open fun bind(pos: Int) {
        dataBinding.executePendingBindings()
    }

    fun unBind(){
        dataBinding.unbind()
    }
}