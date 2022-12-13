package com.example.githubrepos.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.ViewDataBinding
import com.example.githubrepos.extension_functions.inflateBinding
import com.example.githubrepos.ui.dialog.AppDialogs

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSIONS_REQUEST_CODE = 34

        const val REQUEST_KEY_ON_BACK_PRESS = "onBackPress"
    }

//    protected val mAppContext: Context
//        get() = MyApplication.getInstance()

    private var _mActivity: AppCompatActivity? = null
    protected val mActivity: AppCompatActivity
        get() = _mActivity!!

    protected abstract val layoutId: Int
    private var _dataBinding: B? = null
    protected val dataBinding: B
        get() = _dataBinding!!

    protected val isDataBindingActive: Boolean
        get() = (_dataBinding != null && !isDetached)

    protected lateinit var mAppDialogs: AppDialogs

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _mActivity = context as AppCompatActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mAppDialogs = (mActivity, lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dataBinding = inflater.inflateBinding(layoutId, container)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
//        mAppDialogs.dismissAll()
    }

    override fun onDetach() {
        _mActivity = null
        super.onDetach()
    }

}