package com.example.attendanceapp

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import java.util.*


class MultiSelectionSpinner : Spinner, OnMultiChoiceClickListener {
    var _items: MutableList<String> = mutableListOf()
    var mSelection: BooleanArray? = null
    var simple_adapter: ArrayAdapter<String>

    constructor(context: Context?) : super(context) {
        simple_adapter = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_item
        )
        super.setAdapter(simple_adapter)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        simple_adapter = ArrayAdapter(
            context!!,
            R.layout.simple_spinner_item
        )
        super.setAdapter(simple_adapter)
    }

    override fun onClick(
        dialog: DialogInterface,
        which: Int,
        isChecked: Boolean
    ) {
        if (mSelection != null && which < mSelection!!.size) {
            mSelection!![which] = isChecked
            simple_adapter.clear()
            simple_adapter.add(buildSelectedItemString())
        } else {
            throw IllegalArgumentException(
                "Argument 'which' is out of bounds."
            )
        }
    }

    override fun performClick(): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setMultiChoiceItems(_items.toTypedArray(), mSelection, this)
        builder.show()
        return true
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        throw RuntimeException(
            "setAdapter is not supported by MultiSelectSpinner."
        )
    }

    fun setItems(items: Array<String>?) {
        //_items = items
        mSelection = BooleanArray(_items.size)
        simple_adapter.clear()
        simple_adapter.add(_items[0])
        Arrays.fill(mSelection, false)
    }

    fun setItems(items: List<String>) {
        //_items = items.toTypedArray()
        mSelection = BooleanArray(_items.size)
        simple_adapter.clear()
        simple_adapter.add(_items[0])
        Arrays.fill(mSelection, false)
    }

    fun setItem(item: String) {
        if (_items.isNullOrEmpty())
            _items.add(item)
        else if (!_items.contains(item))
            _items.add(item)
    }

    fun load() {
        mSelection = BooleanArray(_items.size)
        simple_adapter.clear()
        simple_adapter.add(_items[0])
        Arrays.fill(mSelection, false)
    }

    fun clear() {
        _items.clear()
        simple_adapter.clear()
    }

    fun setSelection(selection: Array<String>) {
        for (cell in selection) {
            for (j in _items.indices) {
                if (_items[j] == cell) {
                    mSelection!![j] = true
                }
            }
        }
    }

    fun setSelection(selection: List<String>) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
        }
        for (sel in selection) {
            for (j in _items.indices) {
                if (_items[j] == sel) {
                    mSelection!![j] = true
                }
            }
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }

    override fun setSelection(index: Int) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
        }
        if (index >= 0 && index < mSelection!!.size) {
            mSelection!![index] = true
        } else {
            throw IllegalArgumentException(
                "Index " + index
                        + " is out of bounds."
            )
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }

    fun setSelection(selectedIndicies: IntArray) {
        for (i in mSelection!!.indices) {
            mSelection!![i] = false
        }
        for (index in selectedIndicies) {
            if (index >= 0 && index < mSelection!!.size) {
                mSelection!![index] = true
            } else {
                throw IllegalArgumentException(
                    "Index " + index
                            + " is out of bounds."
                )
            }
        }
        simple_adapter.clear()
        simple_adapter.add(buildSelectedItemString())
    }

    val selectedStrings: List<String>
        get() {
            val selection: MutableList<String> =
                LinkedList()
            for (i in _items.indices) {
                if (mSelection!![i]) {
                    selection.add(_items[i])
                }
            }
            return selection
        }

    val selectedIndicies: List<Int>
        get() {
            val selection: MutableList<Int> = LinkedList()
            for (i in _items.indices) {
                if (mSelection!![i]) {
                    selection.add(i)
                }
            }
            return selection
        }

    private fun buildSelectedItemString(): String {
        val sb = StringBuilder()
        var foundOne = false
        for (i in _items.indices) {
            if (mSelection!![i]) {
                if (foundOne) {
                    sb.append(", ")
                }
                foundOne = true
                sb.append(_items[i])
            }
        }
        return sb.toString()
    }

    val selectedItemsAsString: String
        get() {
            val sb = StringBuilder()
            var foundOne = false
            for (i in _items.indices) {
                if (mSelection!![i]) {
                    if (foundOne) {
                        sb.append(", ")
                    }
                    foundOne = true
                    sb.append(_items[i])
                }
            }
            return sb.toString()
        }
}