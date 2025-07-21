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
package com.mushi.customtableview.sort

/**
 * In order to keep RowHeader DataSet and Main DataSet aligned
 * it is required to sort RowHeader the same.
 * So if MainDataSet row 1 moved to position 10, RowHeader 1 move to position 10 too.
 * To accomplish that we need to set a comparator that use MainDataSet
 * in order to sort RowHeader.
 * Created by Mushi on 7/2/18.
 */
class ColumnForRowHeaderSortComparator(
    private val rowHeaderList: List<ISortableModel>,
    private val referenceList: List<List<ISortableModel>>,
    private val column: Int,
    private val sortState: SortState
) : Comparator<ISortableModel> {

    private val columnSortComparator = ColumnSortComparator(column, sortState)

    override fun compare(o1: ISortableModel, o2: ISortableModel): Int {
        val index1 = rowHeaderList.indexOf(o1)
        val index2 = rowHeaderList.indexOf(o2)

        val content1 = referenceList[index1][column].content
        val content2 = referenceList[index2][column].content

        return if (sortState == SortState.DESCENDING) {
            columnSortComparator.compareContent(content2, content1)
        } else {
            columnSortComparator.compareContent(content1, content2)
        }
    }
}

