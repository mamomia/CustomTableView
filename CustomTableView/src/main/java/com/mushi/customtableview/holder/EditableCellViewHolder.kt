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
package com.mushi.customtableview.holder

import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.LinearLayout
import com.mushi.customtableview.R
import com.mushi.customtableview.model.TableCell
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.util.CustomTextWatcher


/**
 * Created by evrencoskun on 23/10/2017.
 */
class EditableCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    public val cellText: EditText = itemView.findViewById(R.id.cell_data)
    private val cellContainer: LinearLayout = itemView.findViewById(R.id.cell_container)

    fun setCell(tableCell: TableCell?, watcher: CustomTextWatcher?) {
        cellText.setRawInputType(tableCell!!.inputType)
        cellText.setText(tableCell.data.toString())
        
        // If your TableView should have auto resize for cells & columns.
        // Then you should consider the below lines. Otherwise, you can ignore them.

        // It is necessary to remeasure itself.
        cellContainer.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        cellText.requestLayout()

        cellText.onFocusChangeListener =
            OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                if (hasFocus) {
                    cellText.addTextChangedListener(watcher)
                } else {
                    cellText.removeTextChangedListener(watcher)
                }
                tableCell.isInFocus = hasFocus
            }

        if (tableCell.isInFocus) {
            cellText.requestFocus()
            cellText.setSelection(tableCell.cursorPoint)
        }
    }
}
