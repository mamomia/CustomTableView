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
package com.mushi.customtableview.layoutmanager

import android.content.Context
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mushi.customtableview.ITableView
import com.mushi.customtableview.adapter.recyclerview.CellRecyclerView
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.listener.scroll.HorizontalRecyclerViewListener
import com.mushi.customtableview.util.TableViewUtils
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Mushi on 24/06/2017.
 */
class CellLayoutManager(context: Context, private val mTableView: ITableView) :
    LinearLayoutManager(context) {
    private val mColumnHeaderLayoutManager = mTableView.columnHeaderLayoutManager

    private val mRowHeaderRecyclerView = mTableView.rowHeaderRecyclerView

    private var mHorizontalListener: HorizontalRecyclerViewListener? = null

    private val mCachedWidthList = SparseArray<SparseIntArray>()

    //TODO: Store a single instance for both cell and column cache width values.
    private var mLastDy = 0
    private var mNeedSetLeft = false
    private var mNeedFit = false

    init {
        initialize()
    }

    private fun initialize() {
        this.orientation = VERTICAL
        // Add new one
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)

        // initialize the instances
        if (mHorizontalListener == null) {
            mHorizontalListener = mTableView.horizontalRecyclerViewListener
        }
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        if (mRowHeaderRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE &&
            !mRowHeaderRecyclerView.isScrollOthers
        ) {
            // CellRecyclerViews should be scrolled after the RowHeaderRecyclerView.
            // Because it is one of the main compared criterion to make each columns fit.
            mRowHeaderRecyclerView.scrollBy(0, dy)
        }

        val scroll = super.scrollVerticallyBy(dy, recycler, state)

        // It is important to determine right position to fit all columns which are the same y pos.
        mLastDy = dy
        return scroll
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            // It is important to set it 0 to be able to know which direction is being scrolled
            mLastDy = 0
        }
    }

    /**
     * This method helps to fit all columns which are displayed on screen.
     * Especially it will be called when TableView is scrolled on vertically.
     */
    fun fitWidthSize(scrollingUp: Boolean) {
        var left = mColumnHeaderLayoutManager.firstItemLeft
        for (i in mColumnHeaderLayoutManager.findFirstVisibleItemPosition()..<mColumnHeaderLayoutManager.findLastVisibleItemPosition() + 1) {
            left = fitSize(i, left, scrollingUp)
        }

        mNeedSetLeft = false
    }

    /**
     * This method helps to fit a column. it will be called when TableView is scrolled on
     * horizontally.
     */
    fun fitWidthSize(position: Int, scrollingLeft: Boolean) {
        fitSize(position, IGNORE_LEFT, false)

        if (mNeedSetLeft and scrollingLeft) {
            // Works just like invoke later of swing utils.
            val handler = Handler()
            handler.post { fitWidthSize2(true) }
        }
    }

    private fun fitSize(position: Int, left: Int, scrollingUp: Boolean): Int {
        var cellRight = -1

        val columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(position)
        val column = mColumnHeaderLayoutManager.findViewByPosition(position)

        if (column != null) {
            // Determine default right
            cellRight = column.left + columnCacheWidth + 1

            if (scrollingUp) {
                // Loop reverse order
                for (i in findLastVisibleItemPosition() downTo findFirstVisibleItemPosition()) {
                    cellRight = fit(position, i, left, cellRight, columnCacheWidth)
                }
            } else {
                // Loop for all rows which are visible.
                for (j in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() +
                        1) {
                    cellRight = fit(position, j, left, cellRight, columnCacheWidth)
                }
            }
        } else {
            Log.e(
                LOG_TAG,
                "Warning: column couldn't found for $position"
            )
        }
        return cellRight
    }

    private fun fit(
        xPosition: Int,
        yPosition: Int,
        left: Int,
        right: Int,
        columnCachedWidth: Int
    ): Int {
        var right = right
        val child = findViewByPosition(yPosition) as CellRecyclerView?

        if (child != null) {
            val childLayoutManager = child.layoutManager as ColumnLayoutManager?

            var cellCacheWidth = getCacheWidth(yPosition, xPosition)
            val cell = childLayoutManager!!.findViewByPosition(xPosition)

            // Control whether the cell needs to be fitted by column header or not.
            if (cell != null) {
                if (cellCacheWidth != columnCachedWidth || mNeedSetLeft) {
                    // This is just for setting width value

                    if (cellCacheWidth != columnCachedWidth) {
                        cellCacheWidth = columnCachedWidth
                        TableViewUtils.setWidth(cell, cellCacheWidth)

                        setCacheWidth(yPosition, xPosition, cellCacheWidth)
                    }

                    // Even if the cached values are same, the left & right value wouldn't change.
                    // mNeedSetLeft & the below lines for it.
                    if (left != IGNORE_LEFT && cell.left != left) {
                        // Calculate scroll distance

                        val scrollX = max(cell.left, left) - min(
                            cell.left,
                            left
                        )

                        // Update its left
                        cell.left = left

                        var offset = mHorizontalListener!!.scrollPositionOffset

                        // It shouldn't be scroll horizontally and the problem is gotten just for
                        // first visible item.
                        if (offset > 0 && xPosition == childLayoutManager
                                .findFirstVisibleItemPosition() && cellRecyclerViewScrollState != RecyclerView.SCROLL_STATE_IDLE
                        ) {
                            val scrollPosition = mHorizontalListener!!.scrollPosition
                            offset = mHorizontalListener!!.scrollPositionOffset + scrollX

                            // Update scroll position offset value
                            mHorizontalListener!!.scrollPositionOffset = offset
                            // Scroll considering to the desired value.
                            childLayoutManager.scrollToPositionWithOffset(scrollPosition, offset)
                        }
                    }

                    if (cell.width != cellCacheWidth) {
                        if (left != IGNORE_LEFT) {
                            // TODO: + 1 is for decoration item. It should be gotten from a
                            // generic method  of layoutManager
                            // Set right
                            right = cell.left + cellCacheWidth + 1
                            cell.right = right

                            childLayoutManager.layoutDecoratedWithMargins(
                                cell, cell.left,
                                cell.top, cell.right, cell.bottom
                            )
                        }

                        mNeedSetLeft = true
                    }
                }
            }
        }
        return right
    }

    /**
     * Alternative method of fitWidthSize().
     * The main difference is this method works after main thread draw the ui components.
     */
    fun fitWidthSize2(scrollingLeft: Boolean) {
        // The below line helps to change left & right value of the each column
        // header views
        // without using requestLayout().
        mColumnHeaderLayoutManager.customRequestLayout()

        // Get the right scroll position information from Column header RecyclerView
        val columnHeaderScrollPosition = mTableView.columnHeaderRecyclerView.scrolledX
        val columnHeaderOffset = mColumnHeaderLayoutManager.firstItemLeft
        val columnHeaderFirstItem = mColumnHeaderLayoutManager.findFirstVisibleItemPosition()

        // Fit all visible columns widths
        for (i in mColumnHeaderLayoutManager.findFirstVisibleItemPosition()..<mColumnHeaderLayoutManager.findLastVisibleItemPosition() + 1) {
            fitSize2(
                i, scrollingLeft, columnHeaderScrollPosition, columnHeaderOffset,
                columnHeaderFirstItem
            )
        }

        mNeedSetLeft = false
    }

    /**
     * Alternative method of fitWidthSize().
     * The main difference is this method works after main thread draw the ui components.
     */
    fun fitWidthSize2(position: Int, scrollingLeft: Boolean) {
        // The below line helps to change left & right value of the each column
        // header views
        // without using requestLayout().
        mColumnHeaderLayoutManager.customRequestLayout()

        // Get the right scroll position information from Column header RecyclerView
        val columnHeaderScrollPosition = mTableView.columnHeaderRecyclerView.scrolledX
        val columnHeaderOffset = mColumnHeaderLayoutManager.firstItemLeft
        val columnHeaderFirstItem = mColumnHeaderLayoutManager.findFirstVisibleItemPosition()

        // Fit all visible columns widths
        fitSize2(
            position, scrollingLeft, columnHeaderScrollPosition, columnHeaderOffset,
            columnHeaderFirstItem
        )


        mNeedSetLeft = false
    }

    private fun fitSize2(
        position: Int, scrollingLeft: Boolean, columnHeaderScrollPosition: Int,
        columnHeaderOffset: Int, columnHeaderFirstItem: Int
    ) {
        val columnCacheWidth = mColumnHeaderLayoutManager.getCacheWidth(position)
        val column = mColumnHeaderLayoutManager.findViewByPosition(position)

        if (column != null) {
            // Loop for all rows which are visible.
            for (j in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() + 1) {
                // Get CellRowRecyclerView

                val child = findViewByPosition(j) as CellRecyclerView?

                if (child != null) {
                    val childLayoutManager = child
                        .layoutManager as ColumnLayoutManager?

                    // Checking Scroll position is necessary. Because, even if they have same width
                    // values, their scroll positions can be different.
                    if (!scrollingLeft && columnHeaderScrollPosition != child.scrolledX) {
                        // Column Header RecyclerView has the right scroll position. So,
                        // considering it

                        childLayoutManager!!.scrollToPositionWithOffset(
                            columnHeaderFirstItem,
                            columnHeaderOffset
                        )
                    }

                    if (childLayoutManager != null) {
                        fit2(position, j, columnCacheWidth, column, childLayoutManager)
                    }
                }
            }
        }
    }

    private fun fit2(
        xPosition: Int, yPosition: Int, columnCachedWidth: Int, column: View,
        childLayoutManager: ColumnLayoutManager
    ) {
        var cellCacheWidth = getCacheWidth(yPosition, xPosition)
        val cell = childLayoutManager.findViewByPosition(xPosition)

        // Control whether the cell needs to be fitted by column header or not.
        if (cell != null) {
            if (cellCacheWidth != columnCachedWidth || mNeedSetLeft) {
                // This is just for setting width value

                if (cellCacheWidth != columnCachedWidth) {
                    cellCacheWidth = columnCachedWidth
                    TableViewUtils.setWidth(cell, cellCacheWidth)

                    setCacheWidth(yPosition, xPosition, cellCacheWidth)
                }

                // The left & right values of Column header can be considered. Because this
                // method will be worked
                // after drawing process of main thread.
                if (column.left != cell.left || column.right != cell.right) {
                    // TODO: + 1 is for decoration item. It should be gotten from a generic
                    // method  of layoutManager
                    // Set right & left values
                    cell.left = column.left
                    cell.right = column.right + 1
                    childLayoutManager.layoutDecoratedWithMargins(
                        cell, cell.left, cell
                            .top, cell.right, cell.bottom
                    )

                    mNeedSetLeft = true
                }
            }
        }
    }

    fun shouldFitColumns(yPosition: Int): Boolean {
        // Scrolling horizontally

        if (cellRecyclerViewScrollState == RecyclerView.SCROLL_STATE_IDLE) {
            val lastVisiblePosition = findLastVisibleItemPosition()
            val lastCellRecyclerView = findViewByPosition(lastVisiblePosition) as CellRecyclerView?

            if (lastCellRecyclerView != null) {
                if (yPosition == lastVisiblePosition) {
                    return true
                } else if (lastCellRecyclerView.isScrollOthers && yPosition ==
                    lastVisiblePosition - 1
                ) {
                    return true
                }
            }
        }
        return false
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChildWithMargins(child, widthUsed, heightUsed)

        // If has fixed width is true, than calculation of the column width is not necessary.
        if (mTableView.hasFixedWidth()) {
            return
        }

        val position = getPosition(child)

        val childLayoutManager = (child as CellRecyclerView)
            .layoutManager as ColumnLayoutManager?

        // the below codes should be worked when it is scrolling vertically
        if (cellRecyclerViewScrollState != RecyclerView.SCROLL_STATE_IDLE) {
            if (childLayoutManager!!.isNeedFit) {
                // Scrolling up

                if (mLastDy < 0) {
                    Log.e(
                        LOG_TAG,
                        "$position fitWidthSize all vertically up"
                    )
                    fitWidthSize(true)
                } else {
                    // Scrolling down
                    Log.e(
                        LOG_TAG,
                        "$position fitWidthSize all vertically down"
                    )
                    fitWidthSize(false)
                }
                // all columns have been fitted.
                childLayoutManager.clearNeedFit()
            }

            // Set the right initialPrefetch size to improve performance
            childLayoutManager.initialPrefetchItemCount = childLayoutManager.childCount

            // That means,populating for the first time like fetching all data to display.
            // It shouldn't be worked when it is scrolling horizontally ."getLastDx() == 0"
            // control for it.
        } else if (childLayoutManager!!.lastDx == 0 && cellRecyclerViewScrollState ==
            RecyclerView.SCROLL_STATE_IDLE
        ) {
            if (childLayoutManager.isNeedFit) {
                mNeedFit = true

                // all columns have been fitted.
                childLayoutManager.clearNeedFit()
            }

            if (mNeedFit) {
                // for the first time to populate adapter
                if (mTableView.rowHeaderLayoutManager.findLastVisibleItemPosition() == position) {
                    fitWidthSize2(false)
                    Log.e(
                        LOG_TAG,
                        "$position fitWidthSize populating data for the first time"
                    )

                    mNeedFit = false
                }
            }
        }
    }

    fun getVisibleCellViewsByColumnPosition(xPosition: Int): Array<AbstractViewHolder?> {
        val visibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1
        var index = 0
        val viewHolders = arrayOfNulls<AbstractViewHolder>(visibleChildCount)
        for (i in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() + 1) {
            val cellRowRecyclerView = findViewByPosition(i) as CellRecyclerView?

            val holder = cellRowRecyclerView?.findViewHolderForAdapterPosition(xPosition) as AbstractViewHolder?

            viewHolders[index] = holder

            index++
        }
        return viewHolders
    }

    fun getCellViewHolder(xPosition: Int, yPosition: Int): AbstractViewHolder? {
        val cellRowRecyclerView = findViewByPosition(yPosition) as CellRecyclerView?

        if (cellRowRecyclerView != null) {
            return cellRowRecyclerView.findViewHolderForAdapterPosition(xPosition) as AbstractViewHolder?
        }
        return null
    }

    fun remeasureAllChild() {
        // TODO: the below code causes requestLayout() improperly called by com.mushi.customtableview.adapter

        for (j in 0..<childCount) {
            val recyclerView = getChildAt(j) as CellRecyclerView?

            recyclerView!!.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            recyclerView.requestLayout()
        }
    }

    /**
     * Allows to set cache width value for single cell item.
     */
    fun setCacheWidth(row: Int, column: Int, width: Int) {
        var cellRowCache = mCachedWidthList[row]
        if (cellRowCache == null) {
            cellRowCache = SparseIntArray()
        }

        cellRowCache.put(column, width)
        mCachedWidthList.put(row, cellRowCache)
    }

    /**
     * Allows to set cache width value for all cell items that is located on column position.
     */
    fun setCacheWidth(column: Int, width: Int) {
        for (i in 0..<mRowHeaderRecyclerView.adapter!!.itemCount) {
            // set cache width for single cell item.
            setCacheWidth(i, column, width)
        }
    }

    fun getCacheWidth(row: Int, column: Int): Int {
        val cellRowCaches = mCachedWidthList[row]
        if (cellRowCaches != null) {
            return cellRowCaches[column, -1]
        }
        return -1
    }

    /**
     * Clears the widths which have been calculated and reused.
     */
    fun clearCachedWidths() {
        mCachedWidthList.clear()
    }

    val visibleCellRowRecyclerViews: Array<CellRecyclerView?>
        get() {
            val length =
                findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1
            val recyclerViews =
                arrayOfNulls<CellRecyclerView>(length)

            var index = 0
            for (i in findFirstVisibleItemPosition()..<findLastVisibleItemPosition() + 1) {
                recyclerViews[index] = findViewByPosition(i) as CellRecyclerView?
                index++
            }

            return recyclerViews
        }

    private val cellRecyclerViewScrollState: Int
        get() = mTableView.cellRecyclerView.scrollState

    companion object {
        private val LOG_TAG: String = CellLayoutManager::class.java.simpleName
        private const val IGNORE_LEFT = -99999
    }
}
