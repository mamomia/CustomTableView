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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.CellRecyclerView

/**
 * Created by Mushi on 19/06/2017.
 */
class HorizontalRecyclerViewListener(tableView: ITableView) : RecyclerView.OnScrollListener(),
    OnItemTouchListener {
    private val mColumnHeaderRecyclerView = tableView.columnHeaderRecyclerView
    private val mCellLayoutManager = tableView.cellRecyclerView.layoutManager
    private var mLastTouchedRecyclerView: RecyclerView? = null

    // X position means column position
    private var mXPosition = 0
    private var mIsMoved = false

    /**
     * When parent RecyclerView scrolls vertically, the child horizontal recycler views should be
     * displayed on right scroll position. So the first complete visible position of the
     * recyclerView is stored as a member to use it for a new attached recyclerview whose
     * orientation is horizontal as well.
     *
     * @see .getScrollPositionOffset
     */
    /**
     * To change default scroll position that is before TableView is not populated.
     */
    var scrollPosition: Int = 0

    /**
     * Users can scroll the recyclerViews to the any x position which may not the exact position. So
     * we need to know store the offset value to locate a specific location for a new attached
     * recyclerView
     *
     * @see .getScrollPosition
     */
    var scrollPositionOffset: Int = 0

    private var mCurrentRVTouched: RecyclerView? = null

    private val mVerticalRecyclerViewListener = tableView.verticalRecyclerViewListener

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        // Prevent multitouch, once we start to listen with a RV,
        // we ignore any other RV until the touch is released (UP)

        if (mCurrentRVTouched != null && rv !== mCurrentRVTouched) {
            return true
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            mCurrentRVTouched = rv
            if (rv.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                if (mLastTouchedRecyclerView != null && rv !== mLastTouchedRecyclerView) {
                    if (mLastTouchedRecyclerView === mColumnHeaderRecyclerView) {
                        mColumnHeaderRecyclerView.removeOnScrollListener(this)
                        mColumnHeaderRecyclerView.stopScroll()
                        Log.d(
                            LOG_TAG, "Scroll listener  has been removed to " +
                                    "mColumnHeaderRecyclerView at last touch control"
                        )
                    } else {
                        val lastTouchedIndex = getIndex(mLastTouchedRecyclerView!!)

                        // Control whether the last touched recyclerView is still attached or not
                        if (lastTouchedIndex >= 0 && lastTouchedIndex < (mCellLayoutManager
                                ?.childCount ?: 0)
                        ) {
                            // Control the scroll listener is already removed. For instance; if user
                            // scroll the parent recyclerView vertically, at that time,
                            // ACTION_CANCEL
                            // will be triggered that removes the scroll listener of the last
                            // touched
                            // recyclerView.
                            if (!(mLastTouchedRecyclerView as CellRecyclerView)
                                    .isHorizontalScrollListenerRemoved
                            ) {
                                // Remove scroll listener of the last touched recyclerView
                                // Because user touched another recyclerView before the last one get
                                // SCROLL_STATE_IDLE state that removes the scroll listener
                                (mCellLayoutManager!!.getChildAt(lastTouchedIndex) as RecyclerView)
                                    .removeOnScrollListener(this)

                                Log.d(
                                    LOG_TAG, "Scroll listener  has been removed to " +
                                            (mLastTouchedRecyclerView as CellRecyclerView).id + " CellRecyclerView " +
                                            "at last touch control"
                                )

                                // the last one scroll must be stopped to be sync with others
                                (mCellLayoutManager.getChildAt(lastTouchedIndex) as RecyclerView)
                                    .stopScroll()
                            }
                        }
                    }
                }

                mXPosition = (rv as CellRecyclerView).scrolledX
                rv.addOnScrollListener(this)
                Log.d(
                    LOG_TAG, ("Scroll listener  has been added to " + rv.getId() + " at action "
                            + "down")
                )
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
            val nScrollX = (rv as CellRecyclerView).scrolledX
            // Is it just touched without scrolling then remove the listener
            if (mXPosition == nScrollX && !mIsMoved) {
                rv.removeOnScrollListener(this)
                Log.d(
                    LOG_TAG, "Scroll listener  has been removed to " + rv.getId() + " at " +
                            "action" + " up"
                )
            }

            mLastTouchedRecyclerView = rv
        } else if (e.action == MotionEvent.ACTION_CANCEL) {
            // ACTION_CANCEL action will be triggered if users try to scroll vertically
            // For this situation, it doesn't matter whether the x position is changed or not
            // Beside this, even if moved action will be triggered, scroll listener won't
            // triggered on cancel action. So, we need to change state of the mIsMoved value as
            // well.

            // Renew the scroll position and its offset

            renewScrollPosition(rv)

            rv.removeOnScrollListener(this)
            Log.d(
                LOG_TAG, "Scroll listener  has been removed to " + rv.id + " at action " +
                        "cancel"
            )

            mIsMoved = false

            mLastTouchedRecyclerView = rv

            mCurrentRVTouched = null
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // Column Header should be scrolled firstly. Because it is the compared recyclerView to
        // make column width fit.

        if (recyclerView === mColumnHeaderRecyclerView) {
            super.onScrolled(recyclerView, dx, dy)

            // Scroll each cell recyclerViews
            for (i in 0..<mCellLayoutManager!!.childCount) {
                val child = mCellLayoutManager.getChildAt(i) as CellRecyclerView?
                // Scroll horizontally
                child!!.scrollBy(dx, 0)
            }
        } else {
            // Scroll column header recycler view as well
            //mColumnHeaderRecyclerView.scrollBy(dx, 0);

            super.onScrolled(recyclerView, dx, dy)

            // Scroll each cell recyclerViews except the current touched one
            for (i in 0..<mCellLayoutManager!!.childCount) {
                val child = mCellLayoutManager.getChildAt(i) as CellRecyclerView?
                if (child != recyclerView) {
                    // Scroll horizontally
                    child!!.scrollBy(dx, 0)
                }
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            // Renew the scroll position and its offset
            renewScrollPosition(recyclerView)

            recyclerView.removeOnScrollListener(this)
            Log.d(
                LOG_TAG, ("Scroll listener has been removed to " + recyclerView.id + " at "
                        + "onScrollStateChanged")
            )
            mIsMoved = false

            // When a user scrolls horizontally, VerticalRecyclerView add vertical scroll
            // listener because of touching process.However, mVerticalRecyclerViewListener
            // doesn't know anything about it. So, it is necessary to remove the last touched
            // recyclerView which uses the mVerticalRecyclerViewListener.
            val isNeeded = mLastTouchedRecyclerView !== mColumnHeaderRecyclerView
            mVerticalRecyclerViewListener.removeLastTouchedRecyclerViewScrollListener(isNeeded)
        }
    }

    private fun getIndex(rv: RecyclerView): Int {
        for (i in 0..<mCellLayoutManager!!.childCount) {
            if (mCellLayoutManager.getChildAt(i) === rv) {
                return i
            }
        }
        return -1
    }

    /**
     * This method calculates the current scroll position and its offset to help new attached
     * recyclerView on window at that position and offset
     *
     * @see .getScrollPosition
     * @see .getScrollPositionOffset
     */
    private fun renewScrollPosition(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        scrollPosition = layoutManager!!.findFirstCompletelyVisibleItemPosition()

        // That means there is no completely visible Position.
        if (scrollPosition == -1) {
            scrollPosition = layoutManager.findFirstVisibleItemPosition()

            // That means there is just a visible item on the screen
            if (scrollPosition == layoutManager.findLastVisibleItemPosition()) {
                // in this case we use the position which is the last & first visible item.
            } else {
                // That means there are 2 visible item on the screen. However, second one is not
                // completely visible.
                scrollPosition = scrollPosition + 1
            }
        }

        scrollPositionOffset = layoutManager.findViewByPosition(scrollPosition)!!.left
    }

    companion object {
        private val LOG_TAG: String = HorizontalRecyclerViewListener::class.java.simpleName
    }
}
