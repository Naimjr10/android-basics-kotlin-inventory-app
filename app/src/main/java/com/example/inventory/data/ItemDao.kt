package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    // The argument OnConflict tells the Room what to do in case of a conflict.
    // The OnConflictStrategy.IGNORE strategy ignores a new item
    // if it's primary key is already in the database.
    // When you call insert() from your Kotlin code,
    // Room executes a SQL query to insert the entity into the database.
    // (Note: The function can be named anything you want; it doesn't have to be called insert().)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update()
    suspend fun update(item: Item)

    @Delete()
    suspend fun delete(item: Item)

    // Notice the :id.
    // You use the colon notation in the query to reference arguments in the function.
    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Int): Flow<Item> // Using Flow or LiveData as return type will ensure you get
                                     // notified whenever the data in the database changes.
                                     // It is recommended to use Flow in the persistence layer.
                                     // The Room keeps this Flow updated for you, which means
                                     // you only need to explicitly get the data once.

    @Query("SELECT * from item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

}