package com.example.expker.data

import com.example.expker.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CategoryRepository(private val categoryDao: CategoryDao) {
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun getCategoryById(id: String): Category? {
        return categoryDao.getCategoryById(id)
    }
}

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) {
    val allTransactionsWithCategory: Flow<List<TransactionWithCategory>> = 
        transactionDao.getAllTransactions().combine(categoryDao.getAllCategories()) { transactions, categories ->
            transactions.map { transaction ->
                TransactionWithCategory(
                    transaction = transaction,
                    category = categories.find { it.id == transaction.categoryId }
                )
            }
        }

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun getTransactionById(id: String): Transaction? {
        return transactionDao.getTransactionById(id)
    }
}
