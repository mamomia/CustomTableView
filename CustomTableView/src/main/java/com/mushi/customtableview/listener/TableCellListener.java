package com.mushi.customtableview.listener;

public interface TableCellListener {
    void onColumnUpdated(String newData, int column, int row, int cursor);
}
