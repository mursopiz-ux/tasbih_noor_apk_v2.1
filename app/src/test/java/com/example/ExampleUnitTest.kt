package com.example

import com.example.data.database.NoorDatabase
import com.example.data.database.WiridModuleTypes
import com.example.data.database.WiridSeedData
import com.example.data.database.WiridSourceTypes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `al ghazali collection contains 10 authentic wirids with complete fields`() {
        val ghazali = WiridSeedData.AL_GHAZALI_WIRIDS
        assertEquals(10, ghazali.size)
        ghazali.forEachIndexed { index, entity ->
            assertEquals(WiridModuleTypes.AL_GHAZALI, entity.moduleType)
            assertEquals(WiridSourceTypes.BIDAYATUL_HIDAYAH, entity.sourceType)
            assertEquals(index + 1, entity.sortOrder)
            assertTrue("Arabic missing on ${entity.id}", entity.arabic.isNotBlank())
            assertTrue("Transliteration missing on ${entity.id}", entity.transliteration.isNotBlank())
            assertTrue("Translation missing on ${entity.id}", entity.translation.isNotBlank())
            assertTrue("Source missing on ${entity.id}", entity.source.contains("Bidāyatul Hidāyah"))
        }
    }

    @Test
    fun `additional wirid modules cover all 8 required categories`() {
        val expectedModules = listOf(
            WiridModuleTypes.WIRID_PAGI,
            WiridModuleTypes.WIRID_PETANG,
            WiridModuleTypes.SETELAH_SALAT,
            WiridModuleTypes.ISTIGHFAR,
            WiridModuleTypes.SELAWAT,
            WiridModuleTypes.TASBIH_TAHMID_TAKBIR,
            WiridModuleTypes.WIRID_MALAM,
            WiridModuleTypes.DOA_TIDUR
        )
        val byModule = WiridSeedData.ADDITIONAL_WIRIDS.groupBy { it.moduleType }
        expectedModules.forEach { module ->
            val items = byModule[module].orEmpty()
            assertTrue("Module $module should not be empty", items.isNotEmpty())
            items.forEach { item ->
                assertTrue(item.arabic.isNotBlank())
                assertTrue(item.transliteration.isNotBlank())
                assertTrue(item.translation.isNotBlank())
                assertTrue(item.source.isNotBlank())
                assertTrue(
                    item.sourceType == WiridSourceTypes.HADIS ||
                        item.sourceType == WiridSourceTypes.AMALAN_TAMBAHAN
                )
            }
        }
    }

    @Test
    fun `all v2 seed dhikr ids are unique`() {
        val all = NoorDatabase.ALL_V2_SEED_DHIKRS
        val uniqueIds = all.map { it.id }.toSet()
        assertEquals(all.size, uniqueIds.size)
    }
}
