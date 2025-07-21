/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
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

package com.mushi.customtableview.model;

import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mushi.customtableview.annotation.CellFieldType;
import com.mushi.customtableview.filter.IFilterableModel;
import com.mushi.customtableview.sort.ISortableModel;

/**
 * Created by mushi on 11/06/2017.
 */

public class Cell implements ISortableModel, IFilterableModel {
    @NonNull
    private String mId;
    @Nullable
    private Object mData;
    @NonNull
    private String mFilterKeyword;

    private CellFieldType level;
    private int inputType;
    private int cursorPoint;
    private Boolean isInFocus;

    public Cell(@NonNull String id, @Nullable Object data) {
        init(id, data, CellFieldType.Header, InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE, 1, false);
    }

    public Cell(@NonNull String id, @Nullable Object data, CellFieldType level) {
        init(id, data, level, InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE, 1, false);
    }

    public Cell(@NonNull String id, @Nullable Object data, CellFieldType level, int inputType) {
        init(id, data, level, inputType, 1, false);
    }

    public Cell(@NonNull String id, @Nullable Object data, CellFieldType level, int inputType, int cursorPoint, boolean hasFocus) {
        init(id, data, level, inputType, cursorPoint, hasFocus);
    }

    private void init(@NonNull String id, @Nullable Object data, CellFieldType level, int inputType, int cursorPoint, boolean hasFocus) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
        this.level = level;
        this.inputType = inputType;
        this.cursorPoint = cursorPoint;
        this.isInFocus = hasFocus;
    }

    @Override
    public void setFilterableKeyword(@NonNull String s) {
        this.mFilterKeyword = s;
    }

    @Override
    public void setId(@NonNull String s) {
        this.mId = s;
    }

    @Override
    public void setContent(@Nullable Object o) {
        this.mData = 0;
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @NonNull
    @Override
    public String getId() {
        return mId;
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @Nullable
    @Override
    public Object getContent() {
        return mData;
    }

    @Nullable
    public Object getData() {
        return mData;
    }

    @NonNull
    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }

    public CellFieldType getLevel() {
        return level;
    }

    public void setLevel(CellFieldType level) {
        this.level = level;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public int getCursorPoint() {
        return cursorPoint;
    }

    public void setCursorPoint(int cursorPoint) {
        this.cursorPoint = cursorPoint;
    }

    public Boolean getInFocus() {
        return isInFocus;
    }

    public void setInFocus(Boolean inFocus) {
        isInFocus = inFocus;
    }
}
