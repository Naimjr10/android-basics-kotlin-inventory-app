package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
 * The database class defines the list of entities and data access objects.
 * It is also the main access point for the underlying connection.
 *
 * Here's the general process for getting the RoomDatabase instance:
 * 1. Create a public abstract class that extends RoomDatabase.
 *    The new abstract class you defined acts as a database holder.
 *    The class you defined is abstract, because Room creates the implementation for you.
 * 2. Annotate the class with @Database.
 *    In the arguments, list the entities for the database and set the version number.
 * 3. Define an abstract method or property that returns an ItemDao Instance and
 *    the Room will generate the implementation for you.
 * 4. You only need one instance of the RoomDatabase for the whole app,
 *    so make the RoomDatabase a singleton.
 * 5. Use Room's Room.databaseBuilder to create your (item_database) database only if
 *    it doesn't exist. Otherwise, return the existing database.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        // Declare a private nullable variable INSTANCE for the database and initialize it to null.
        // The INSTANCE variable will keep a reference to the database, when one has been created.
        // This helps in maintaining a single instance of the database opened at a given time,
        // which is an expensive resource to create and maintain.
        //
        // Annotate INSTANCE with @Volatile. The value of a volatile variable will never be cached,
        // and all writes and reads will be done to and from the main memory.
        // This helps make sure the value of INSTANCE is always up-to-date
        // and the same for all execution threads.
        // It means that changes made by one thread to INSTANCE are visible
        // to all other threads immediately.
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        // Multiple threads can potentially run into a race condition and ask for
        // a database instance at the same time, resulting in two databases instead of one.
        // Wrapping the code to get the database inside a synchronized block means that
        // only one thread of execution at a time can enter this block of code,
        // which makes sure the database only gets initialized once.

        // return INSTANCE variable or if INSTANCE is null,
        // initialize it inside a synchronized{} block.
        // Use the elvis operator(?:) to do this
        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}