package com.example

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.NoorDatabase
import com.example.data.database.WiridModuleTypes
import com.example.data.database.WiridSourceTypes
import com.example.data.model.DhikrEntity
import com.example.data.preferences.SettingsManager
import com.example.data.repository.NoorRepository
import com.example.util.DateUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w411dp-h915dp-xxhdpi")
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private var testDb: NoorDatabase? = null

    @After
    fun tearDown() {
        testDb?.close()
    }

    @Test
    fun `app branding metadata and strings match TASBIH NOOR`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("TASBIH NOOR", context.getString(R.string.app_name))
        assertEquals("A Peaceful Digital Dhikr Experience.", context.getString(R.string.app_tagline))
        assertEquals("Premium Islamic Digital Tasbih and Daily Wird Companion.", context.getString(R.string.app_description))
        assertEquals("2.01", BuildConfig.VERSION_NAME)
        assertEquals(3, BuildConfig.VERSION_CODE)
    }

    @Test
    fun `date utils returns formatted hijri and gregorian dates`() {
        val hijri = DateUtils.getFormattedHijriDate()
        val gregorian = DateUtils.getFormattedGregorianDate()
        val days7 = DateUtils.getLastNDaysKeys(7)
        val days28 = DateUtils.getLastNDaysKeys(28)

        assertTrue(hijri.isNotBlank())
        assertTrue(gregorian.isNotBlank())
        assertEquals(7, days7.size)
        assertEquals(28, days28.size)
        assertEquals(DateUtils.todayDateKey(), days7.last().first)
    }

    @Test
    fun `migration 1 to 2 preserves v1 custom dhikrs and sessions while supporting v2 wirids`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbName = "migration_v1_v2_test.db"
        context.deleteDatabase(dbName)

        // Create a real V1 SQLite schema and insert V1 user data
        val callback = object : SupportSQLiteOpenHelper.Callback(1) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `dhikrs` (
                        `id` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `arabic` TEXT NOT NULL,
                        `transliteration` TEXT NOT NULL,
                        `translation` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `target` INTEGER NOT NULL,
                        `isCustom` INTEGER NOT NULL,
                        `isFavorite` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `sessions` (
                        `id` TEXT NOT NULL,
                        `dhikrId` TEXT NOT NULL,
                        `dhikrName` TEXT NOT NULL,
                        `arabic` TEXT NOT NULL,
                        `count` INTEGER NOT NULL,
                        `target` INTEGER NOT NULL,
                        `isCompleted` INTEGER NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `durationSeconds` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO dhikrs VALUES (
                        'custom_v1', 'Dzikir Pribadi V1', 'سُبْحَانَ اللّٰهِ', 'Subhanallah',
                        'Maha Suci Allah', 'Kustom', 33, 1, 1, 123456
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO sessions VALUES (
                        'session_v1', 'subhanallah', 'Subhanallah', 'سُبْحَانَ اللّٰهِ',
                        33, 33, 1, 1700000000000, 45
                    )
                    """.trimIndent()
                )
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {}
        }

        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(dbName)
                .callback(callback)
                .build()
        )
        helper.writableDatabase.close()

        val migratedDb = Room.databaseBuilder(context, NoorDatabase::class.java, dbName)
            .addMigrations(NoorDatabase.MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()
        testDb = migratedDb

        // Seed V2 non-destructively (IGNORE existing IDs)
        migratedDb.dhikrDao().insertDhikrsIgnore(NoorDatabase.ALL_V2_SEED_DHIKRS)

        val restoredCustom = migratedDb.dhikrDao().getDhikrById("custom_v1")
        assertNotNull(restoredCustom)
        assertEquals("Dzikir Pribadi V1", restoredCustom?.name)
        assertTrue(restoredCustom?.isCustom == true)

        val sessions = migratedDb.sessionDao().getAllSessionsList()
        assertEquals(1, sessions.size)
        assertEquals("session_v1", sessions.first().id)

        val ghazaliList = migratedDb.dhikrDao().getDhikrsByModule(WiridModuleTypes.AL_GHAZALI).first()
        assertEquals(10, ghazaliList.size)

        context.deleteDatabase(dbName)
    }

    @Test
    fun `repository wirid increment records single session on completion and exports v1 v2 compatible backup`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, NoorDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        testDb = db
        val settingsManager = SettingsManager(context)
        val repo = NoorRepository(db.dhikrDao(), db.sessionDao(), db.wiridProgressDao(), settingsManager)
        repo.ensureSeeded()

        val ghazaliFirst = repo.getDhikrById("ghazali_1_tahlil_tauhid")!!
        // Tap 10 times with target = 10; continue 1 extra time -> should only create 1 session record
        repeat(11) {
            repo.incrementWirid(ghazaliFirst, effectiveTarget = 10)
        }

        val sessions = db.sessionDao().getAllSessionsList()
        assertEquals(1, sessions.size)
        assertEquals(10, sessions.first().count)
        assertTrue(sessions.first().isCompleted)

        val progress = db.wiridProgressDao().getProgressById("${DateUtils.todayDateKey()}_${ghazaliFirst.id}")
        assertNotNull(progress)
        assertEquals(11, progress?.currentCount)
        assertTrue(progress?.isCompleted == true)

        // Reset wirid back to 0
        repo.resetSingleWirid(ghazaliFirst, effectiveTarget = 10)
        val afterReset = db.wiridProgressDao().getProgressById("${DateUtils.todayDateKey()}_${ghazaliFirst.id}")
        assertEquals(0, afterReset?.currentCount)
        assertFalse(afterReset?.isCompleted == true)

        // Add custom dhikr and export/restore JSON backup
        repo.saveCustomDhikr(
            id = null,
            name = "Doa Kustom",
            arabic = "رَبَّنَا آتِنَا",
            transliteration = "Rabbana atina",
            translation = "Ya Tuhan kami berilah kami kebaikan",
            category = "Doa",
            target = 33,
            isFavorite = true
        )
        val backupJson = repo.exportBackupJson()
        assertTrue(backupJson.contains("TASBIH NOOR"))

        repo.clearAllData()
        repo.ensureSeeded()
        val restoreResult = repo.restoreBackupJson(backupJson)
        assertTrue(restoreResult.isSuccess)
        assertTrue(db.sessionDao().getAllSessionsList().isNotEmpty())
        assertTrue(db.dhikrDao().getAllDhikrsList().any { it.name == "Doa Kustom" })
    }

    @Test
    fun `cuj splash transitions to dashboard and navigates across tasbih wirid al ghazali and history`() {
        // Step through sequential splash animations (450ms + 550ms + 650ms = 1650ms)
        repeat(15) {
            composeTestRule.mainClock.advanceTimeBy(200L)
            composeTestRule.waitForIdle()
        }

        // Dashboard assertions
        composeTestRule.onNodeWithTag("dashboard_screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Assalamu'alaikum").assertIsDisplayed()
        composeTestRule.onNodeWithTag("dashboard_tasbih_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("dashboard_al_ghazali_card").assertIsDisplayed()

        // Navigate to Digital Tasbih & tap counter
        composeTestRule.onNodeWithTag("dashboard_tasbih_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("main_counter_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("keep_screen_on_toggle").assertIsDisplayed()
        composeTestRule.onNodeWithTag("main_tap_button").assertExists()
        composeTestRule.onNodeWithTag("main_tap_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("next_dhikr_button").assertExists()

        // Navigate to Wirid Hub -> Imam Al-Ghazali
        composeTestRule.onNodeWithTag("nav_item_wirid").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("wirid_hub_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("wirid_module_card_${WiridModuleTypes.AL_GHAZALI}").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("wirid_module_screen_${WiridModuleTypes.AL_GHAZALI}").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ghazali_target_10").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ghazali_target_70").assertIsDisplayed()
        composeTestRule.onNodeWithTag("ghazali_target_100").assertIsDisplayed()

        // Navigate to History & Settings
        composeTestRule.onNodeWithTag("nav_item_history").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("history_screen").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_item_settings").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("settings_screen").assertIsDisplayed()

        // Scroll to Support Developer section at the end of Settings
        composeTestRule.onNodeWithTag("about_app_card").performScrollTo()
        composeTestRule.onNodeWithText("Versi 2.01").assertIsDisplayed()

        composeTestRule.onNodeWithTag("support_developer_card").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("qris_support_image").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("6880857990").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("a.n. Mursopi zaini").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("copy_bank_account_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        val clipboard = ApplicationProvider.getApplicationContext<Context>()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        assertEquals("6880857990", clipboard.primaryClip?.getItemAt(0)?.text?.toString())
    }
}
