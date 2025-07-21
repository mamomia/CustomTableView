/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Mushi
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
package com.mushi.customtableview.popup

import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import com.mushi.customtableview.R
import com.mushi.customtableview.TableView
import com.mushi.customtableview.holder.ColumnHeaderViewHolder
import com.mushi.customtableview.sort.SortState
import androidx.core.view.get

/**
 * Created by Mushi on 24.12.2017.
 */
class ColumnHeaderLongPressPopup(
    viewHolder: ColumnHeaderViewHolder,
    private val mTableView: TableView
) :
    PopupMenu(viewHolder.itemView.context, viewHolder.itemView), PopupMenu.OnMenuItemClickListener {
    private val mXPosition = viewHolder.adapterPosition

    init {
        initialize()
    }

    private fun initialize() {
        createMenuItem()
        changeMenuItemVisibility()

        this.setOnMenuItemClickListener(this)
    }

    private fun createMenuItem() {
        val context = mTableView.context
        this.menu.add(Menu.NONE, ASCENDING, 0, context.getString(R.string.sort_ascending))
        this.menu.add(Menu.NONE, DESCENDING, 1, context.getString(R.string.sort_descending))

        // add new one ...
    }

    private fun changeMenuItemVisibility() {
        // Determine which one shouldn't be visible
        val sortState = mTableView.getSortingStatus(mXPosition)
        if (sortState == SortState.UNSORTED) {
            // Show others
        } else if (sortState == SortState.DESCENDING) {
            // Hide DESCENDING menu item
            menu[1].setVisible(false)
        } else if (sortState == SortState.ASCENDING) {
            // Hide ASCENDING menu item
            menu[0].setVisible(false)
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        // Note: item id is index of menu item..
        when (menuItem.itemId) {
            ASCENDING -> mTableView.sortColumn(mXPosition, SortState.ASCENDING)
            DESCENDING -> mTableView.sortColumn(mXPosition, SortState.DESCENDING)
        }
        return true
    }

    companion object {
        // Menu Item constants
        private const val ASCENDING = 1
        private const val DESCENDING = 2
    }
}
