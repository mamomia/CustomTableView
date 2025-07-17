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

package com.mushi.sample;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mushi.customtableview.adapter.AbstractTableAdapter;
import com.mushi.customtableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.mushi.customtableview.annotation.CellFieldType;
import com.mushi.customtableview.holder.EditableCellViewHolder;
import com.mushi.customtableview.listener.TableCellListener;
import com.mushi.customtableview.sort.SortState;
import com.mushi.customtableview.holder.ActionCellViewHolder;
import com.mushi.customtableview.holder.ColumnHeaderViewHolder;
import com.mushi.customtableview.holder.DataCellViewHolder;
import com.mushi.customtableview.holder.RowHeaderViewHolder;
import com.mushi.customtableview.model.ColumnHeader;
import com.mushi.customtableview.model.RowHeader;
import com.mushi.customtableview.model.TableCell;
import com.mushi.customtableview.util.CustomTextWatcher;

import java.util.List;

/**
 * Created by Mushi on 11/06/2017.
 * <p>
 */

public class TableViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, TableCell> {

    private List<List<TableCell>> cellItems;
    @Nullable
    private TableCellListener mTableCellListener;
    private static final int ACTION_CELL_TYPE = 1;
    private static final int EDITABLE_CELL_TYPE = 2;

    public TableViewAdapter() {
        super();
    }

    @Override
    public void setAllItems(@Nullable List<ColumnHeader> columnHeaders, @Nullable List<RowHeader> rowHeaders, @Nullable List<List<TableCell>> cells) {
        super.setAllItems(columnHeaders, rowHeaders, cells);
        this.cellItems = cells;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout;
        if (viewType == ACTION_CELL_TYPE) {
            layout = inflater.inflate(com.mushi.customtableview.R.layout.table_view_action_cell_layout, parent, false);
            return new ActionCellViewHolder(layout);
        } else if (viewType == EDITABLE_CELL_TYPE) {
            layout = inflater.inflate(com.mushi.customtableview.R.layout.table_view_editable_cell_layout, parent, false);
            return new EditableCellViewHolder(layout);
        }
        layout = inflater.inflate(com.mushi.customtableview.R.layout.table_view_data_cell_layout, parent, false);
        return new DataCellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable TableCell cellItemModel, int columnPosition, int rowPosition) {
        if (holder.getItemViewType() == ACTION_CELL_TYPE) {
            ActionCellViewHolder viewHolder = (ActionCellViewHolder) holder;
            viewHolder.setCell(cellItemModel);
        } else if (holder.getItemViewType() == EDITABLE_CELL_TYPE) {
            EditableCellViewHolder viewHolder = (EditableCellViewHolder) holder;
            viewHolder.setCell(cellItemModel, new CustomTextWatcher(columnPosition, rowPosition) {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mTableCellListener != null) {
                        int cursor = viewHolder.getCellText().getSelectionStart();
                        mTableCellListener.onColumnUpdated(s.toString(), getColumn(), getRow(), cursor);
                    }
                }
            });
        } else {
            DataCellViewHolder viewHolder = (DataCellViewHolder) holder;
            viewHolder.setCell(cellItemModel);
        }
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get Column Header xml Layout
        View layout = LayoutInflater.from(parent.getContext()).inflate(com.mushi.customtableview.R.layout.table_view_column_header_layout, parent, false);
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable ColumnHeader columnHeaderItemModel, int columnPosition) {
        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeaderItemModel);
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(com.mushi.customtableview.R.layout.table_view_row_header_layout, parent, false);
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable RowHeader rowHeaderItemModel, int rowPosition) {
        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(String.valueOf(rowHeaderItemModel.getData()));
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        // Get Corner xml layout
        View corner = LayoutInflater.from(parent.getContext())
                .inflate(com.mushi.customtableview.R.layout.table_view_corner_layout, parent, false);
        corner.setOnClickListener(view -> {
            SortState sortState = TableViewAdapter.this.getTableView().getRowHeaderSortingStatus();
            if (sortState != SortState.ASCENDING) {
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.ASCENDING);
            } else {
                TableViewAdapter.this.getTableView().sortRowHeader(SortState.DESCENDING);
            }
        });
        return corner;
    }

    @Override
    public int getCellItemViewType(int column) {
        TableCell item = getCellItem(column, 0);
        if (item != null) {
            if (item.getLevel() == CellFieldType.Action)
                return ACTION_CELL_TYPE;
            if (item.getLevel() == CellFieldType.Editable)
                return EDITABLE_CELL_TYPE;
        }
        return 0;
    }

    public void updateSingleRow(List<TableCell> updatedRow, int rowPosition) {
        if (cellItems != null && rowPosition < cellItems.size()) {
            cellItems.set(rowPosition, updatedRow);
            setCellItems(cellItems);
        }
    }

    @Nullable
    public TableCellListener getTableCellListener() {
        return mTableCellListener;
    }

    public void setTableCellListener(@Nullable TableCellListener listener) {
        mTableCellListener = listener;
    }
}
