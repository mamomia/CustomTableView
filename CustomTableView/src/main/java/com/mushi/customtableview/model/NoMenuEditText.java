package com.mushi.customtableview.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public class NoMenuEditText extends androidx.appcompat.widget.AppCompatEditText {

    public NoMenuEditText(Context context) {
        super(context);
        init();
    }

    public NoMenuEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoMenuEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Disable long press
        setLongClickable(false);
        setTextIsSelectable(false);

        // Disable selection-based ActionMode (Copy/SelectAll/Cut)
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });

        // Disable menu when cursor is placed (Paste popup)
        setCustomInsertionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        // Completely block context menu actions
        return false;
    }

    @Override
    public boolean isTextSelectable() {
        return false;
    }

    private boolean mProgrammaticSelection = false;

    @Override
    public void setSelection(int index) {
        // Clamp index to valid range
        int len = getText() == null ? 0 : getText().length();
        int safeIndex = Math.max(0, Math.min(index, len));

        mProgrammaticSelection = true;
        try {
            super.setSelection(safeIndex);
        } finally {
            mProgrammaticSelection = false;
        }
    }

    @Override
    public void setSelection(int start, int stop) {
        int len = getText() == null ? 0 : getText().length();

        int safeStart = Math.max(0, Math.min(start, len));
        int safeStop = Math.max(0, Math.min(stop, len));

        // If they request a range selection, collapse it to a caret at safeStart
        // (we don't want range selection to appear)
        mProgrammaticSelection = true;
        try {
            if (safeStart == safeStop) {
                super.setSelection(safeStart);
            } else {
                // collapse selection to start (this places caret at safeStart)
                super.setSelection(safeStart);
            }
        } finally {
            mProgrammaticSelection = false;
        }
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        // If we're in the middle of a programmatic selection, let it happen
        if (mProgrammaticSelection) {
            super.onSelectionChanged(selStart, selEnd);
            return;
        }

        // If user tries to create a range selection, collapse it to a caret at selStart
        if (selStart != selEnd) {
            // Use super.setSelection to avoid re-entering our override logic
            super.setSelection(selStart);
            return;
        }

        super.onSelectionChanged(selStart, selEnd);
    }
}
