package com.sih2026.artisancatalog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sih2026.artisancatalog.domain.model.CraftCategory
import com.sih2026.artisancatalog.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val craftHistory: String,
    val imageUris: List<String>,
    val voiceTranscription: String,
    val materialCost: Double,
    val laborCost: Double,
    val margin: Double,
    val suggestedPrice: Double,
    val category: String,
    val language: String,
    val createdAt: Long,
    val syncStatus: String
) {
    fun toDomain(): Product {
        val cat = try {
            CraftCategory.valueOf(category)
        } catch (_: Exception) {
            CraftCategory.OTHER
        }
        return Product(
            id = id,
            title = title,
            description = description,
            craftHistory = craftHistory,
            imageUris = imageUris,
            voiceTranscription = voiceTranscription,
            materialCost = materialCost,
            laborCost = laborCost,
            margin = margin,
            suggestedPrice = suggestedPrice,
            category = cat,
            language = language,
            createdAt = createdAt,
            syncStatus = syncStatus
        )
    }

    companion object {
        fun fromDomain(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                title = product.title,
                description = product.description,
                craftHistory = product.craftHistory,
                imageUris = product.imageUris,
                voiceTranscription = product.voiceTranscription,
                materialCost = product.materialCost,
                laborCost = product.laborCost,
                margin = product.margin,
                suggestedPrice = product.suggestedPrice,
                category = product.category.name,
                language = product.language,
                createdAt = product.createdAt,
                syncStatus = product.syncStatus
            )
        }
    }
}
