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

package com.mushi.customtableview.model;

import android.text.InputType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mushi.customtableview.annotation.CellFieldType;
import com.mushi.customtableview.filter.IFilterableModel;
import com.mushi.customtableview.sort.ISortableModel;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class TableCell implements ISortableModel, IFilterableModel {
    @NonNull
    private final String mId;
    @Nullable
    private final Object mData;
    @NonNull
    private final String mFilterKeyword;
    @NonNull
    private final CellFieldType mLevel;
    private final int mInputType;
    private final int mCursorPoint;
    private boolean mIsInFocus;

    public TableCell(@NonNull String id, @Nullable Object data) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
        this.mLevel = CellFieldType.Header;
        this.mIsInFocus = false;
        this.mInputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
        this.mCursorPoint = 1;
    }

    public TableCell(@NonNull String id, @Nullable Object data, @NonNull CellFieldType level) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
        this.mLevel = level;
        this.mIsInFocus = false;
        this.mInputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE;
        this.mCursorPoint = 1;
    }

    public TableCell(@NonNull String id, @Nullable Object data, @NonNull CellFieldType level, int inputType) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
        this.mLevel = level;
        this.mIsInFocus = false;
        this.mInputType = inputType;
        this.mCursorPoint = 1;
    }

    public TableCell(@NonNull String id, @Nullable Object data, @NonNull CellFieldType level, int inputType, int cursorPoint) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
        this.mLevel = level;
        this.mIsInFocus = false;
        this.mInputType = inputType;
        this.mCursorPoint = cursorPoint;
    }

    public TableCell(@NonNull String id, @Nullable Object data, @NonNull CellFieldType level, int inputType, int cursorPoint, boolean hasFocus) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
        this.mLevel = level;
        this.mInputType = inputType;
        this.mCursorPoint = cursorPoint;
        this.mIsInFocus = hasFocus;
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
    public CellFieldType getLevel() {
        return mLevel;
    }

    public int getInputType() {
        return mInputType;
    }

    public int getCursorPoint() {
        return mCursorPoint;
    }

    @NonNull
    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }

    public boolean getIsInFocus() {
        return mIsInFocus;
    }

    public void setIsInFocus(boolean mIsInFocus) {
        this.mIsInFocus = mIsInFocus;
    }
}