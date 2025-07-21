/*
 * MIT License
 *
 * Copyright (c) 2025 Mushi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.mushi.customtableview.listener.itemclick

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.CellRecyclerView
import com.mushi.customtableview.handler.SelectionHandler
import com.mushi.customtableview.listener.ITableViewListener
import kotlin.math.abs

/**
 * Created by Mushi on 22.11.2017.
 */
abstract class AbstractItemClickListener(
    @JvmField protected var mRecyclerView: CellRecyclerView,
    @JvmField protected var mTableView: ITableView
) :
    OnItemTouchListener {
    private var mListener: ITableViewListener? = null
    protected var mGestureDetector: GestureDetector
    @JvmField
    protected var mSelectionHandler: SelectionHandler = mTableView.selectionHandler

    init {
        mGestureDetector =
            GestureDetector(mRecyclerView.context, object : SimpleOnGestureListener() {
                var start: MotionEvent? = null

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    return clickAction(mRecyclerView, e)
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    return doubleClickAction(e)
                }

                override fun onDown(e: MotionEvent): Boolean {
                    start = e
                    return false
                }

                override fun onLongPress(e: MotionEvent) {
                    // Check distance to prevent scroll to trigger the event
                    if (start != null && abs(start!!.rawX - e.rawX) < 20 && abs(
                            start!!.rawY - e.rawY
                        ) < 20
                    ) {
                        longPressAction(e)
                    }
                }
            })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(e)
        // Return false intentionally
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    protected val tableViewListener: ITableViewListener
        get() {
            if (mListener == null) {
                mListener = mTableView.tableViewListener
            }
            return mListener!!
        }

    protected abstract fun clickAction(view: RecyclerView, e: MotionEvent): Boolean

    protected abstract fun longPressAction(e: MotionEvent)

    protected abstract fun doubleClickAction(e: MotionEvent): Boolean
}
