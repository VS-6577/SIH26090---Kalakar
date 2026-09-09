package com.sih2026.artisancatalog

import com.sih2026.artisancatalog.data.local.ProductEntity
import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductModelTest {

    @Test
    fun productEntity_conversionToDomainAndBack_preservesAllFields() {
        val domainProduct = Product(
            id = "test_id_101",
            title = "Embroidered Kantha Bag",
            description = "Artisan cotton bag",
            craftHistory = "Heritage craft story",
            imageUris = listOf("file:///path1.jpg", "file:///path2.jpg"),
            voiceTranscription = "Handmade bag with stitching",
            materialCost = 180.0,
            laborCost = 170.0,
            margin = 100.0,
            suggestedPrice = 450.0,
            category = CraftCategory.EMBROIDERY,
            language = "en",
            createdAt = 1700000000000L,
            syncStatus = "LOCAL_ONLY"
        )

        val entity = ProductEntity.fromDomain(domainProduct)
        assertEquals("test_id_101", entity.id)
        assertEquals("EMBROIDERY", entity.category)
        assertEquals(450.0, entity.suggestedPrice, 0.001)

        val restoredDomain = entity.toDomain()
        assertEquals(domainProduct.id, restoredDomain.id)
        assertEquals(domainProduct.title, restoredDomain.title)
        assertEquals(domainProduct.category, restoredDomain.category)
        assertEquals(domainProduct.suggestedPrice, restoredDomain.suggestedPrice, 0.001)
        assertEquals(2, restoredDomain.imageUris.size)
    }
}
