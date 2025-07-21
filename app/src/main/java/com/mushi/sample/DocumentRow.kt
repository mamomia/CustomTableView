package com.mushi.sample;

import android.text.InputType;

import com.mushi.customtableview.annotation.CellFieldType;
import com.mushi.customtableview.annotation.ColumnIgnore;
import com.mushi.customtableview.annotation.ColumnPosition;

public class DocumentRow {
    @ColumnIgnore
    public String ItemCode = "";
    @ColumnPosition(value = 1, heading = "Description", columnType = CellFieldType.Editable, inputType = InputType.TYPE_CLASS_TEXT)
    public String ItemDescription = "";
    @ColumnPosition(value = 2, heading = "WHS")
    public String WhsName = "General";
    @ColumnPosition(value = 3, heading = "WHS Qty", columnType = CellFieldType.Editable, inputType = InputType.TYPE_CLASS_NUMBER)
    public double WhsQty = 0.0;
    @ColumnPosition(value = 4, heading = "Pair", columnType = CellFieldType.Editable, inputType = InputType.TYPE_CLASS_NUMBER)
    public double Quantity = 0.0;
    @ColumnPosition(value = 5, heading = "Dozen", columnType = CellFieldType.Editable, inputType = InputType.TYPE_CLASS_NUMBER)
    public double Dozen = 0.0;
    @ColumnPosition(value = 6, heading = "Committed")
    public double Committed = 0.0;
    @ColumnPosition(value = 7, heading = "Unit Price", columnType = CellFieldType.Editable, inputType = InputType.TYPE_CLASS_NUMBER)
    public double UnitPrice = 0.0;
    @ColumnIgnore
    public double Tax = 0;
    @ColumnPosition(value = 8, heading = "Total")
    public double LineTotal = 0.0;
    @ColumnPosition(value = 9, heading = "Delete", columnType = CellFieldType.Action)
    public int ActionDelete = R.drawable.ic_delete;

    public DocumentRow() {

    }
}
