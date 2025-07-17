package com.mushi.customtableview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public interface CellTextChangeListener {
    void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row);
}
