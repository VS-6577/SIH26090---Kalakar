package com.sih2026.artisancatalog.data.repository

import com.sih2026.artisancatalog.data.local.ProductDao
import com.sih2026.artisancatalog.data.local.ProductEntity
import com.sih2026.artisancatalog.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(id: String): Product?
    suspend fun saveProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    suspend fun deleteProductById(id: String)
}

// Also alias / implement CatalogRepository as requested in SIH prompt
interface CatalogRepository : ProductRepository

class ProductRepositoryImpl(
    private val productDao: ProductDao
) : CatalogRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: String): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun saveProduct(product: Product) {
        productDao.insertProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun deleteProductById(id: String) {
        productDao.deleteProductById(id)
    }
}
