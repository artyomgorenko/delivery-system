package ru.delivery.system.android.adapters

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.databinding.BindingAdapter
import android.databinding.adapters.ListenerUtil
import android.databinding.ObservableList
import com.android.databinding.library.baseAdapters.BR
import ru.delivery.system.android.handlers.ButtonHandler
import ru.delivery.system.android.R

object CustomAdapter {
    @JvmStatic
    @BindingAdapter("entries", "layout")
    fun <T> setEntries(viewGroup: ViewGroup, oldEntries: ObservableList<T>?, oldLayoutId: Int, newEntries: ObservableList<T>?, newLayoutId: Int) {
        if (oldEntries === newEntries && oldLayoutId == newLayoutId) {
            return  // nothing has changed
        }

        val listener2 = object : ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(sender: ObservableList<T>?) {
                resetViews(viewGroup, newLayoutId, sender!!)
            }

            override fun onItemRangeRemoved(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                resetViews(viewGroup, newLayoutId, sender!!)
            }

            override fun onItemRangeMoved(sender: ObservableList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                resetViews(viewGroup, newLayoutId, sender!!)
            }

            override fun onItemRangeInserted(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                resetViews(viewGroup, newLayoutId, sender!!)
            }

            override fun onItemRangeChanged(sender: ObservableList<T>?, positionStart: Int, itemCount: Int) {
                resetViews(viewGroup, newLayoutId, sender!!)
            }
        }

        if (oldEntries !== newEntries/* && listener2 != null*/) {
            oldEntries?.removeOnListChangedCallback(listener2)
        }

        ListenerUtil.trackListener(viewGroup, listener2, R.id.entryListener)

        if (newEntries == null) {
            viewGroup.removeAllViews()
        } else {
            if (newEntries !== oldEntries) {
                newEntries.addOnListChangedCallback(listener2)
            }
            resetViews(viewGroup, newLayoutId, newEntries)
        }
    }
}

private fun bindLayout(inflater: LayoutInflater, parent: ViewGroup, layoutId: Int, entry: Any): ViewDataBinding {
    val binding = DataBindingUtil.inflate<ViewDataBinding>(
        inflater,
        layoutId, parent, false
    )
    binding.setVariable(BR.data, entry)
    binding.setVariable(BR.handler, ButtonHandler())
    return binding
}

private fun resetViews(parent: ViewGroup, layoutId: Int, entries: List<*>) {
    parent.removeAllViews()
    if (layoutId == 0) {
        return
    }
    val inflater = parent.context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    for (i in entries.indices) {
        val entry = entries[i]
        val binding = bindLayout(inflater, parent, layoutId, entry!!)
        parent.addView(binding.root)
    }
}

//
//private class EntryChangeListener(private val mTarget: ViewGroup, private var mLayoutId: Int)
//    : ObservableList.OnListChangedCallback<ObservableList<*>>() {
//
//    fun setLayoutId(layoutId: Int) {
//        mLayoutId = layoutId
//    }
//
//    override fun onChanged(observableList: ObservableList<*>) {
//        resetViews(mTarget, mLayoutId, observableList)
//    }
//
//    override fun onItemRangeChanged(observableList: ObservableList<*>, start: Int, count: Int) {
//        resetViews(mTarget, mLayoutId, observableList)
//    }
//
//    override fun onItemRangeInserted(observableList: ObservableList<*>, start: Int, count: Int) {
//        resetViews(mTarget, mLayoutId, observableList)
//    }
//
//    override fun onItemRangeMoved(observableList: ObservableList<*>, from: Int, to: Int, count: Int) {
//        resetViews(mTarget, mLayoutId, observableList)
//    }
//
//    override fun onItemRangeRemoved(observableList: ObservableList<*>, start: Int, count: Int) {
//        resetViews(mTarget, mLayoutId, observableList)
//    }
//}

