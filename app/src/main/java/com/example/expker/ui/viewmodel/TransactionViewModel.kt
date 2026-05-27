package com.example.expker.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expker.data.CategoryRepository
import com.example.expker.data.TransactionRepository
import com.example.expker.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

enum class SortOrder {
    RECENT, ASCENDING, DESCENDING
}

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.RECENT)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val categories: StateFlow<List<Category>> = categoryRepository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<TransactionWithCategory>> = transactionRepository.allTransactionsWithCategory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionWithCategory>> = combine(
        allTransactions,
        _searchQuery,
        _sortOrder
    ) { list, query, sort ->
        var filtered = if (query.isEmpty()) {
            list
        } else {
            list.filter { item ->
                fuzzyMatch(item.transaction.title, query) || 
                fuzzyMatch(String.format("%.2f", item.transaction.amount), query)
            }
        }

        when (sort) {
            SortOrder.RECENT -> filtered // Already sorted by DAO
            SortOrder.ASCENDING -> filtered.sortedBy { it.transaction.amount }
            SortOrder.DESCENDING -> filtered.sortedByDescending { it.transaction.amount }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun fuzzyMatch(text: String, query: String): Boolean {
        if (query.isEmpty()) return true
        val cleanText = text.lowercase()
        val cleanQuery = query.lowercase()
        
        var textIdx = 0
        var queryIdx = 0
        
        while (textIdx < cleanText.length && queryIdx < cleanQuery.length) {
            if (cleanText[textIdx] == cleanQuery[queryIdx]) {
                queryIdx++
            }
            textIdx++
        }
        return queryIdx == cleanQuery.length
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    val totalBalance: StateFlow<Double> = allTransactions.map { list ->
        list.sumOf { if (it.transaction.type == TransactionType.INCOME) it.transaction.amount else -it.transaction.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalIncome: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.transaction.type == TransactionType.INCOME }.sumOf { it.transaction.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double> = allTransactions.map { list ->
        list.filter { it.transaction.type == TransactionType.EXPENSE }.sumOf { it.transaction.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addTransaction(title: String, amount: Double, category: Category, type: TransactionType) {
        viewModelScope.launch {
            val newTransaction = Transaction(
                id = UUID.randomUUID().toString(),
                title = title,
                amount = amount,
                date = "Today",
                categoryId = category.id,
                type = type,
                timestamp = System.currentTimeMillis()
            )
            transactionRepository.insertTransaction(newTransaction)
        }
    }

    fun editTransaction(id: String, title: String, amount: Double, category: Category, type: TransactionType) {
        viewModelScope.launch {
            val existing = transactionRepository.getTransactionById(id)
            if (existing != null) {
                val updatedTransaction = existing.copy(
                    title = title,
                    amount = amount,
                    categoryId = category.id,
                    type = type,
                    timestamp = System.currentTimeMillis() // Keep it fresh or keep original? 
                    // Usually edit keeps original timestamp unless it's a "move to top" behavior.
                    // User said "showup in recent order", so maybe update it? 
                    // Let's keep original for edit unless user specifies.
                )
                transactionRepository.updateTransaction(updatedTransaction)
            }
        }
    }

    fun deleteTransaction(id: String) {
        viewModelScope.launch {
            val existing = transactionRepository.getTransactionById(id)
            if (existing != null) {
                transactionRepository.deleteTransaction(existing)
            }
        }
    }

    suspend fun getTransactionById(id: String): Transaction? {
        return transactionRepository.getTransactionById(id)
    }

    fun addCategory(name: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: androidx.compose.ui.graphics.Color) {
        viewModelScope.launch {
            val newCategory = Category(
                id = UUID.randomUUID().toString(),
                name = name,
                iconName = IconHelper.getNameByIcon(icon),
                colorHex = color.value.toLong()
            )
            categoryRepository.insertCategory(newCategory)
        }
    }

}
