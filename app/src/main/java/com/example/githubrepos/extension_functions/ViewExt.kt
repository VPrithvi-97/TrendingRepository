package com.example.githubrepos.extension_functions

import android.content.Context
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 *   View Ext
 */

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.disable(alphaValue: Float = 0.4f) {
    alpha = alphaValue
    isLongClickable = false
    isClickable = false
    isFocusable = false
}

fun View.enable() {
    alpha = 1f
    isLongClickable = true
    isClickable = true
    isFocusable = true
}

fun View.disableTouch() {
    isEnabled = false
    if (this is ViewGroup)
        (0 until childCount).map(::getChildAt).forEach { it.disableTouch() }
}

fun View.enableTouch() {
    isEnabled = true
    if (this is ViewGroup)
        (0 until childCount).map(::getChildAt).forEach { it.enableTouch() }
}

//Keyboard handle
fun View.showKeyboard(forced: Boolean = false) {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(
        this,
        if (forced) InputMethodManager.SHOW_FORCED else InputMethodManager.SHOW_IMPLICIT
    )
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : View> View.getFirstParent(): T? {
    kotlin.runCatching {
        if (T::class.isInstance(parent)) {
            return parent as T
        } else if (T::class.isInstance(parent.parent)) {
            return parent.parent as T
        }
    }
    return null
}


/**
 *  TextView Functions
 */


fun TextView.showError(@StringRes resId: Int) {
    showError(context.getString(resId))
}

fun TextView.hideError() {
    showError(null)
}

fun TextView.showError(string: String?) {
    if (TextInputEditText::class.isInstance(this)) {
        getFirstParent<TextInputLayout>()?.error = string
    }
}

fun TextView.allowOnlyAlphaNumericCharacters(maxLength: Int = Int.MAX_VALUE) {
    filters = filters.plus(
        listOf(
            InputFilter { src, _, _, _, _, _ ->
                if (src.toString().matches(Regex("[a-zA-Z 0-9]+"))) {
                    src
                } else ""
            },
            InputFilter.LengthFilter(maxLength)
        )
    )
}

/**
 *  EditText Functions
 */

fun EditText.setSelectionAtEnd() {
    setSelection(text?.toString()?.length ?: 0)
}

fun EditText.setOnDoneListener(onDonePress: () -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            onDonePress()
            true
        } else false
    }
}


/**
 *  GridLayoutManager Ext
 */

fun RecyclerView.setSpanCountDynamic(block: (pos: Int) -> Int) {
    if (layoutManager !is GridLayoutManager) throw RuntimeException("Layout Manager is not GridLayoutManager")
    (layoutManager as GridLayoutManager).spanSizeLookup =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return block(position)
            }
        }
}

/**
 *    MotionLayout
 */

fun MotionLayout.onTransitionCompleted(onCompleted: (currentId: Int) -> Unit) {
    setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {}
        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            onCompleted(currentId)
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    })
}


fun ChipGroup.setChildrenEnabled(enable: Boolean) {
    children.forEach { it.isEnabled = enable }
}