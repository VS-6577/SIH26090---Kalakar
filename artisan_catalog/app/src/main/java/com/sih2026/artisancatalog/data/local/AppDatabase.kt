package com.sih2026.artisancatalog.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "artisan_catalog.db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDemoData(database.productDao())
                    }
                }
            }

            private suspend fun populateInitialDemoData(dao: ProductDao) {
                if (dao.getProductCount() == 0) {
                    val sample1 = ProductEntity(
                        id = "prod_sample_01",
                        title = "Handcrafted Terracotta Chai Kulhad Set",
                        description = "Set of 4 traditional clay kulhad cups handcrafted by rural artisans using riverbed clay, sun-dried and fired in wood kilns. 100% natural, biodegradable and eco-friendly.",
                        craftHistory = "Clay pottery is a 5000-year-old Indian craft originating from the Indus Valley Civilization. Each kulhad is hand-thrown on a traditional potter's wheel.",
                        imageUris = listOf("sample_pottery_1", "sample_pottery_2"),
                        voiceTranscription = "I made these four clay chai cups on my wheel using local red clay and fired them in wood furnace.",
                        materialCost = 120.0,
                        laborCost = 180.0,
                        margin = 80.0,
                        suggestedPrice = 380.0,
                        category = "POTTERY",
                        language = "en",
                        createdAt = System.currentTimeMillis() - 86400000L,
                        syncStatus = "LOCAL_ONLY"
                    )
                    dao.insertProduct(sample1)
                }
            }
        }
    }
}
