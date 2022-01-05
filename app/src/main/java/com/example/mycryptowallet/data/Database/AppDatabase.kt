package com.example.mycryptowallet.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mycryptowallet.data.DAO.CryptoDAO
import com.example.mycryptowallet.data.Entity.Crypto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Crypto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDAO(): CryptoDAO

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.cryptoDAO())
                }
            }
        }

        suspend fun populateDatabase(cryptoDAO: CryptoDAO) {
            cryptoDAO.deleteAll()

            var crypto =  Crypto("Bitcoin", "BTC",1.0,66.0, 66.0 )
            cryptoDAO.insert(crypto)
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
                ).addCallback(AppDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}