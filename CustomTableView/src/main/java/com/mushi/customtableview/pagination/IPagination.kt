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
package com.mushi.customtableview.pagination

import com.mushi.customtableview.pagination.Pagination.OnTableViewPageTurnedListener

interface IPagination {
    /**
     * Loads the next page of the data set to the table view.
     */
    fun nextPage()

    /**
     * Loads the previous page of the data set to the table view.
     */
    fun previousPage()

    /**
     * Loads the data set of the specified page to the table view.
     *
     * @param page The page to be loaded.
     */
    fun goToPage(page: Int)

    /**
     * Sets the OnTableViewPageTurnedListener for this Pagination.
     *
     * @param onTableViewPageTurnedListener The OnTableViewPageTurnedListener.
     */
    fun setOnTableViewPageTurnedListener(onTableViewPageTurnedListener: OnTableViewPageTurnedListener?)

    /**
     * Removes the OnTableViewPageTurnedListener for this Pagination.
     */
    fun removeOnTableViewPageTurnedListener()

    /**
     * @return The current page loaded to the table view.
     */
    val currentPage: Int

    /**
     * @return The number of items per page loaded to the table view.
     */
    /**
     * Sets the number of items (rows) per page to be displayed in the table view.
     *
     * @param numItems The number of items per page.
     */
    var itemsPerPage: Int

    /**
     * @return The number of pages in the pagination.
     */
    val pageCount: Int

    /**
     * @return Current pagination state of the table view.
     */
    val isPaginated: Boolean
}
