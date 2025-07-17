package com.mushi.sample

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mushi.customtableview.TableView
import com.mushi.customtableview.listener.TableViewListener
import com.mushi.customtableview.model.ActionColumn
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
                TableViewListener(it) { _, columnIndex, rowIndex ->
                    if (columnIndex === 8) {
                        selectedListOfItems.removeAt(rowIndex)
                        updateItemsListing()
                    }

                    // hide keyboard if cell is not edit table
                    if (!TableViewUtils.isColumnEditable(clazz, columnIndex))
                        AppUtility.hideKeyboard(this, binding!!.main)
                }
            }

            tableViewAdapter!!.setTableCellListener { newData, columnIndex, rowIndex, cursor ->
                if (columnIndex === 0) {
                    // updated Pair
                    selectedListOfItems[rowIndex].ItemDescription = newData
                } else if (columnIndex === 2) {
                    // updated Pair
                    selectedListOfItems[rowIndex].WhsQty =
                        AppUtility.round(AppUtility.parseDouble(newData), 2)
                } else if (columnIndex === 3) {
                    // updated Pair
                    selectedListOfItems[rowIndex].Quantity =
                        AppUtility.round(AppUtility.parseDouble(newData), 2)
                    selectedListOfItems[rowIndex].Dozen =
                        AppUtility.round((selectedListOfItems[rowIndex].Quantity / 12), 2)
                } else if (columnIndex === 4) {
                    // updated Dozen
                    selectedListOfItems[rowIndex].Dozen =
                        AppUtility.round(AppUtility.parseDouble(newData), 2)
                    selectedListOfItems[rowIndex].Quantity =
                        AppUtility.round((selectedListOfItems[rowIndex].Dozen * 12), 2)
                } else if (columnIndex === 6) {
                    // updated Price
                    selectedListOfItems[rowIndex].UnitPrice =
                        AppUtility.round(AppUtility.parseDouble(newData), 2)
                }

                selectedListOfItems[rowIndex].LineTotal = AppUtility.round(
                    (selectedListOfItems[rowIndex].Quantity * selectedListOfItems[rowIndex].UnitPrice),
                    2
                )
                selectedListOfItems[rowIndex].Tax = 0.0

                tableViewAdapter!!.updateSingleRow(
                    getCell(
                        selectedListOfItems[rowIndex],
                        DocumentRow::class.java,
                        (columnIndex + 1),
                        cursor
                    ),
                    rowIndex
                )
                resizeTableView()
            }
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