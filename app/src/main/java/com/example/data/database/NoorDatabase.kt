package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.DhikrDao
import com.example.data.dao.SessionDao
import com.example.data.dao.WiridProgressDao
import com.example.data.model.DhikrEntity
import com.example.data.model.SessionEntity
import com.example.data.model.WiridProgressEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [DhikrEntity::class, SessionEntity::class, WiridProgressEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NoorDatabase : RoomDatabase() {
    abstract fun dhikrDao(): DhikrDao
    abstract fun sessionDao(): SessionDao
    abstract fun wiridProgressDao(): WiridProgressDao

    companion object {
        @Volatile
        private var INSTANCE: NoorDatabase? = null

        val DEFAULT_DHIKRS = listOf(
            DhikrEntity(
                id = "subhanallah",
                name = "Subhanallah",
                arabic = "سُبْحَانَ ٱللَّٰهِ",
                transliteration = "Subḥānallāh",
                translation = "Maha Suci Allah",
                category = "Tasbih",
                target = 33,
                isCustom = false,
                isFavorite = true,
                createdAt = 1000L,
                source = "Hadis Shahih (Dzikir Pokok)",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 1,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "alhamdulillah",
                name = "Alhamdulillah",
                arabic = "ٱلْحَمْدُ لِلَّٰهِ",
                transliteration = "Al-ḥamdu lillāh",
                translation = "Segala puji bagi Allah",
                category = "Tahmid",
                target = 33,
                isCustom = false,
                isFavorite = true,
                createdAt = 2000L,
                source = "Hadis Shahih (Dzikir Pokok)",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 2,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "allahu_akbar",
                name = "Allahu Akbar",
                arabic = "ٱللَّٰهُ أَكْبَرُ",
                transliteration = "Allāhu Akbar",
                translation = "Allah Maha Besar",
                category = "Takbir",
                target = 33,
                isCustom = false,
                isFavorite = true,
                createdAt = 3000L,
                source = "Hadis Shahih (Dzikir Pokok)",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 3,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "astaghfirullah",
                name = "Astaghfirullah",
                arabic = "أَسْتَغْفِرُ ٱللَّٰهَ",
                transliteration = "Astaghfirullāh",
                translation = "Aku memohon ampunan kepada Allah",
                category = "Istighfar",
                target = 100,
                isCustom = false,
                isFavorite = false,
                createdAt = 4000L,
                source = "Hadis Shahih",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 4,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "la_ilaha_illallah",
                name = "Laa ilaaha illallah",
                arabic = "لَا إِلَٰهَ إِلَّا ٱللَّٰهُ",
                transliteration = "Lā ilāha illallāh",
                translation = "Tiada sesembahan yang berhak disembah selain Allah",
                category = "Tahlil",
                target = 100,
                isCustom = false,
                isFavorite = true,
                createdAt = 5000L,
                source = "Hadis Shahih",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 5,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "subhanallahi_wa_bihamdihi",
                name = "Subhanallahi wa Bihamdihi",
                arabic = "سُبْحَانَ ٱللَّٰهِ وَبِحَمْدِهِ",
                transliteration = "Subḥānallāhi wa bi-ḥamdihi",
                translation = "Maha Suci Allah dengan segala puji bagi-Nya",
                category = "Tasbih",
                target = 100,
                isCustom = false,
                isFavorite = false,
                createdAt = 6000L,
                source = "HR. Al-Bukhari & Muslim",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 6,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "hasbunallah",
                name = "Hasbunallah wa Ni'mal Wakeel",
                arabic = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
                transliteration = "Ḥasbunallāhu wa ni‘mal-wakīl",
                translation = "Cukuplah Allah menjadi Penolong kami dan Dia sebaik-baik Pelindung",
                category = "Doa & Dzikir",
                target = 100,
                isCustom = false,
                isFavorite = false,
                createdAt = 7000L,
                source = "QS. Ali Imran: 173",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 7,
                sourceType = WiridSourceTypes.AMALAN_TAMBAHAN
            ),
            DhikrEntity(
                id = "shalawat",
                name = "Shalawat Nabi",
                arabic = "ٱللَّٰهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ",
                transliteration = "Allāhumma ṣalli ‘alā Muḥammad",
                translation = "Ya Allah, limpahkanlah rahmat kepada Nabi Muhammad",
                category = "Shalawat",
                target = 100,
                isCustom = false,
                isFavorite = false,
                createdAt = 8000L,
                source = "Amalan Sunnah",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 8,
                sourceType = WiridSourceTypes.HADIS
            ),
            DhikrEntity(
                id = "hauqalah",
                name = "Laa Hawla wa Laa Quwwata",
                arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِٱللَّٰهِ",
                transliteration = "Lā ḥawla wa lā quwwata illā billāh",
                translation = "Tiada daya dan kekuatan kecuali dengan pertolongan Allah",
                category = "Hauqalah",
                target = 33,
                isCustom = false,
                isFavorite = false,
                createdAt = 9000L,
                source = "HR. Al-Bukhari & Muslim",
                moduleType = WiridModuleTypes.GENERAL,
                sortOrder = 9,
                sourceType = WiridSourceTypes.HADIS
            )
        )

        val ALL_V2_SEED_DHIKRS: List<DhikrEntity>
            get() = DEFAULT_DHIKRS + WiridSeedData.AL_GHAZALI_WIRIDS + WiridSeedData.ADDITIONAL_WIRIDS

        /**
         * Safe non-destructive migration from V1 (NOOR COUNTER) to V2 (TASBIH NOOR V2.0).
         * Preserves all custom dhikrs, favorites, modified targets, and session history.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE dhikrs ADD COLUMN source TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE dhikrs ADD COLUMN moduleType TEXT NOT NULL DEFAULT 'GENERAL'")
                db.execSQL("ALTER TABLE dhikrs ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE dhikrs ADD COLUMN sourceType TEXT NOT NULL DEFAULT 'UMUM'")

                db.execSQL("ALTER TABLE sessions ADD COLUMN moduleType TEXT NOT NULL DEFAULT 'TASBIH'")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `wirid_progress` (
                        `id` TEXT NOT NULL,
                        `wiridId` TEXT NOT NULL,
                        `moduleType` TEXT NOT NULL,
                        `dateKey` TEXT NOT NULL,
                        `currentCount` INTEGER NOT NULL,
                        `target` INTEGER NOT NULL,
                        `isCompleted` INTEGER NOT NULL,
                        `sessionRecorded` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): NoorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoorDatabase::class.java,
                    "noor_counter_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.dhikrDao()?.insertDhikrsIgnore(ALL_V2_SEED_DHIKRS)
                            }
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // Safely seed new V2 wirids if upgrading from V1 without touching existing rows
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.dhikrDao()?.insertDhikrsIgnore(ALL_V2_SEED_DHIKRS)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
