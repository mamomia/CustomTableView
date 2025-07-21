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
import android.widget.ImageView
import android.widget.LinearLayout
import com.mushi.customtableview.R
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mushi.customtableview.model.Cell

class ActionCellViewHolder(itemView: View) : AbstractViewHolder(itemView) {
    val cell_image: ImageView =
        itemView.findViewById(R.id.cell_image)
    private val cell_container: LinearLayout =
        itemView.findViewById(R.id.cell_container)

    fun setCell(tableCell: Cell?) {
        try {
            val actionIcon = tableCell!!.data as Int
            cell_image.setImageResource(actionIcon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG: String = ActionCellViewHolder::class.java.simpleName
    }
}
