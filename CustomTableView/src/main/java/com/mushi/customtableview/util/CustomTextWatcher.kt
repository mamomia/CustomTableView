package com.mushi.customtableview.util;

import android.text.TextWatcher;

public abstract class CustomTextWatcher implements TextWatcher {
    protected int column;
    protected int row;

    public CustomTextWatcher(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
