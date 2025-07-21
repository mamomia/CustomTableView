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
package com.mushi.customtableview.listener.scroll

import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.CellRecyclerView
import kotlin.math.abs

/**
 * Created by Mushi on 30/06/2017.
 */
class VerticalRecyclerViewListener(tableView: ITableView) : RecyclerView.OnScrollListener(),
    OnItemTouchListener {
    private val mRowHeaderRecyclerView = tableView.rowHeaderRecyclerView
    private val mCellRecyclerView = tableView.cellRecyclerView
    private var mLastTouchedRecyclerView: RecyclerView? = null

    // Y Position means row position
    private var mYPosition = 0
    private var mIsMoved = false

    private var mCurrentRVTouched: RecyclerView? = null

    private var dx = 0f
    private var dy = 0f

    /**
     * check which direction the user is scrolling
     *
     * @param ev
     * @return
     */
    private fun verticalDirection(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_MOVE) {
            if (dx == 0f) {
                dx = ev.x
            }
            if (dy == 0f) {
                dy = ev.y
            }
            val xdiff = abs(dx - ev.x)
            val ydiff = abs(dy - ev.y)
            dx = ev.x
            dy = ev.y

            // if user scrolled more horizontally than vertically
            return xdiff <= ydiff
        }

        return true
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        // Prevent multitouch, once we start to listen with a RV,
        // we ignore any other RV until the touch is released (UP)

        if ((mCurrentRVTouched != null && rv !== mCurrentRVTouched)) {
            return true
        }

        // If scroll direction is not Vertical, then ignore and reset last RV touched
        if (!verticalDirection(e)) {
            mCurrentRVTouched = null
            return false
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            mCurrentRVTouched = rv
            if (rv.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mLastTouchedRecyclerView != null && rv !== mLastTouchedRecyclerView) {
                    removeLastTouchedRecyclerViewScrollListener(false)
                }
                mYPosition = (rv as CellRecyclerView).scrolledY
                rv.addOnScrollListener(this)

                if (rv === mCellRecyclerView) {
                    Log.d(LOG_TAG, "mCellRecyclerView scroll listener added")
                } else if (rv === mRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener added")
                }

                // Refresh the value;
                mIsMoved = false
            }
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            mCurrentRVTouched = rv
            // Why does it matter ?
            // user scroll any recyclerView like brushing, at that time, ACTION_UP will be
            // triggered
            // before scrolling. So, we need to store whether it moved or not.
            mIsMoved = true
        } else if (e.action == MotionEvent.ACTION_UP) {
            mCurrentRVTouched = null
            val nScrollY = (rv as CellRecyclerView).scrolledY

            // TODO: Even if moved value is true and it may not scroll. This should be fixed.
            // TODO: The scenario is scroll lightly center RecyclerView vertically.
            // TODO: Below if condition may be changed later.

            // Is it just touched without scrolling then remove the listener
            if (mYPosition == nScrollY && !mIsMoved && rv.getScrollState() == RecyclerView
                    .SCROLL_STATE_IDLE
            ) {
                rv.removeOnScrollListener(this)

                if (rv === mCellRecyclerView) {
                    Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from up ")
                } else if (rv === mRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from up")
                }
            }

            mLastTouchedRecyclerView = rv
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
        // Because it is one of the main compared criterion to make each columns fit.

        if (recyclerView === mCellRecyclerView) {
            super.onScrolled(recyclerView, dx, dy)

            // The below code has been moved in CellLayoutManager
            //mRowHeaderRecyclerView.scrollBy(0, dy);
        } else if (recyclerView === mRowHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy)

            mCellRecyclerView.scrollBy(0, dy)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeOnScrollListener(this)
            mIsMoved = false
            mCurrentRVTouched = null
            if (recyclerView === mCellRecyclerView) {
                Log.d(
                    LOG_TAG, "mCellRecyclerView scroll listener removed from " +
                            "onScrollStateChanged"
                )
            } else if (recyclerView === mRowHeaderRecyclerView) {
                Log.d(
                    LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from " +
                            "onScrollStateChanged"
                )
            }
        }
    }

    /**
     * If current recyclerView that is touched to scroll is not same as the last one, this method
     * helps to remove the scroll listener of the last touched recyclerView.
     * This method is a little bit different from HorizontalRecyclerViewListener.
     *
     * @param isNeeded Is mCellRecyclerView scroll listener should be removed ? The scenario is a
     * user scrolls vertically using RowHeaderRecyclerView. After that, the user
     * scrolls horizontally using ColumnHeaderRecyclerView.
     */
    fun removeLastTouchedRecyclerViewScrollListener(isNeeded: Boolean) {
        if (mLastTouchedRecyclerView === mCellRecyclerView) {
            mCellRecyclerView.removeOnScrollListener(this)
            mCellRecyclerView.stopScroll()
            Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from last touched")
        } else {
            mRowHeaderRecyclerView.removeOnScrollListener(this)
            mRowHeaderRecyclerView.stopScroll()
            Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from last touched")
            if (isNeeded) {
                mCellRecyclerView.removeOnScrollListener(this)
                mCellRecyclerView.stopScroll()
                Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from last touched")
            }
        }
    }

    companion object {
        private val LOG_TAG: String = VerticalRecyclerViewListener::class.java.simpleName
    }
}
