package com.example.mycryptowallet.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mycryptowallet.data.DAO.CryptoDAO
import com.example.mycryptowallet.data.DAO.OrderDAO
import com.example.mycryptowallet.data.Entity.Crypto
import com.example.mycryptowallet.data.Entity.Order
import com.example.mycryptowallet.data.Entity.OrderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Crypto::class, Order::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDAO(): CryptoDAO
    abstract fun orderDAO(): OrderDAO

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase( database.orderDAO())
                }
            }
        }

        suspend fun populateDatabase(orderDAO: OrderDAO) {
            //cryptoDAO.deleteAll()
            orderDAO.deleteAll()

//            val crypto =  Crypto("Bitcoin", "BTC",1.0,66.0, 66.0 )
//            cryptoDAO.insert(crypto)

            val order = Order("Bitcoin", OrderType.BUY, 66.0, 66.0)
            orderDAO.insert(order)
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crypto_database"
                ).addCallback(AppDatabaseCallback(scope)).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}