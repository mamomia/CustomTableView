package com.mushi.customtableview.model;

public class ActionColumn {
    private String column;
    private int icon;

    public ActionColumn(String column, int icon) {
        this.column = column;
        this.icon = icon;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
