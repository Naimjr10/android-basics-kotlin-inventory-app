package com.example.inventory.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    /*
     * To interact with the database off the main thread,
     * start a coroutine and call the DAO method within it.
     * Inside the insertItem() method, use the viewModelScope.
     * launch to start a coroutine in the ViewModelScope.
     * Inside the launch function, call the suspend function insert() on itemDao passing in the item.
     * The ViewModelScope is an extension property to the ViewModel class that
     * automatically cancels its child coroutines when the ViewModel is destroyed.
     */
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    /*
     * Notice that you did not use viewModelScope.launch() for addNewItem() function,
     * but it is needed above in insertItem() when you call a DAO method.
     * The reason is that the suspend functions are only allowed to be called from
     * a coroutine or another suspend function.
     * The function itemDao.insert(item) is a suspend function.
     */
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

}