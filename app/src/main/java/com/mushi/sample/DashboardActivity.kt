package com.mushi.sample

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.mushi.customtableview.TableView
import com.mushi.customtableview.listener.CellTextChangeListener
import com.mushi.customtableview.listener.TableCellListener
import com.mushi.customtableview.listener.TableViewListener
import com.mushi.customtableview.util.TableViewUtils
import com.mushi.customtableview.util.TableViewUtils.getCell
import com.mushi.customtableview.util.TableViewUtils.getCellList
import com.mushi.customtableview.util.TableViewUtils.getColumnHeaderList
import com.mushi.customtableview.util.TableViewUtils.getRowHeaderList
import com.mushi.sample.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private var binding: ActivityDashboardBinding? = null
    private val selectedListOfItems: ArrayList<DocumentRow> = ArrayList()
    private var tableViewAdapter: TableViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppUtility.setupUI(this, binding!!.main)

        for (i in 0 until 50) {
            val item = DocumentRow()
            item.ItemCode = "ItemCode-$i"
            item.ItemDescription = "Item Description - $i"
            selectedListOfItems.add(item)
        }
        initItemsListing(
            binding!!.tableView,
            selectedListOfItems,
            DocumentRow::class.java
        )
    }

    private fun <T> initItemsListing(tableView: TableView?, rowsList: List<T>, clazz: Class<T>?) {
        if (tableViewAdapter == null) {
            tableViewAdapter = TableViewAdapter()
            binding!!.tableView.isIgnoreSelectionColors = true
            binding!!.tableView.setAdapter(tableViewAdapter)
            binding!!.tableView.tableViewListener = tableView?.let {
                TableViewListener(it, object : TableCellListener {
                    override fun onCellClicked(
                        cellView: RecyclerView.ViewHolder,
                        column: Int,
                        row: Int
                    ) {
                        if (column == 8) {
                            selectedListOfItems.removeAt(row)
                            updateItemsListing()
                        }

                        if (!TableViewUtils.isColumnEditable(clazz, column)) {
                            AppUtility.hideKeyboard(this@DashboardActivity, binding!!.main)
                        }
                    }
                })
            }

            tableViewAdapter!!.setTableCellListener(object : CellTextChangeListener {
                override fun onColumnUpdated(
                    newData: String?,
                    column: Int,
                    row: Int,
                    cursor: Int
                ) {
                    if (newData.isNullOrEmpty()) return

                    if (column == 0) {
                        selectedListOfItems[row].ItemDescription = newData
                    } else if (column == 2) {
                        selectedListOfItems[row].WhsQty =
                            AppUtility.round(AppUtility.parseDouble(newData), 2)
                    } else if (column == 3) {
                        selectedListOfItems[row].Quantity =
                            AppUtility.round(AppUtility.parseDouble(newData), 2)
                        selectedListOfItems[row].Dozen =
                            AppUtility.round((selectedListOfItems[row].Quantity / 12), 2)
                    } else if (column == 4) {
                        selectedListOfItems[row].Dozen =
                            AppUtility.round(AppUtility.parseDouble(newData), 2)
                        selectedListOfItems[row].Quantity =
                            AppUtility.round((selectedListOfItems[row].Dozen * 12), 2)
                    } else if (column == 6) {
                        selectedListOfItems[row].UnitPrice =
                            AppUtility.round(AppUtility.parseDouble(newData), 2)
                    }

                    selectedListOfItems[row].LineTotal = AppUtility.round(
                        (selectedListOfItems[row].Quantity * selectedListOfItems[row].UnitPrice),
                        2
                    )
                    selectedListOfItems[row].Tax = 0.0

                    tableViewAdapter!!.updateSingleRow(
                        getCell(
                            selectedListOfItems[row],
                            DocumentRow::class.java,
                            (column + 1),
                            cursor
                        ),
                        row
                    )
                    resizeTableView()
                }
            })
        }
        tableViewAdapter!!.setAllItems(
            getColumnHeaderList(clazz),
            getRowHeaderList(rowsList.size),
            getCellList(rowsList, clazz)
        )
        binding!!.tableView.post { tableViewAdapter!!.notifyDataSetChanged() }
    }

    private fun updateItemsListing() {
        initItemsListing(
            binding!!.tableView,
            selectedListOfItems,
            DocumentRow::class.java
        )
        resizeTableView()
    }

    private fun resizeTableView() {
        binding!!.tableView.setColumnWidth(0, 600)
        binding!!.tableView.setColumnWidth(1, 400)
        binding!!.tableView.setColumnWidth(2, 200)
        binding!!.tableView.setColumnWidth(3, 200)
        binding!!.tableView.setColumnWidth(4, 200)
        binding!!.tableView.setColumnWidth(5, 300)
        binding!!.tableView.setColumnWidth(6, 200)
        binding!!.tableView.setColumnWidth(7, 200)
        binding!!.tableView.setColumnWidth(8, 200)
        binding!!.tableView.setColumnWidth(9, 150)
        binding!!.tableView.setColumnWidth(10, 150)
        binding!!.tableView.setColumnWidth(11, 150)
    }
}