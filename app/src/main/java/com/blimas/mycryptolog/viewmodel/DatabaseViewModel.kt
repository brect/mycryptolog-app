package com.blimas.mycryptolog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class DatabaseViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val userId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    private val _wallets = MutableLiveData<List<Wallet>>()
    val wallets: LiveData<List<Wallet>> = _wallets

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    init {
        getWallets()
    }

    // --- Wallet CRUD ---
    fun createWallet(walletName: String) {
        userId?.let { currentUserId ->
            val walletId = database.child("wallets").child(currentUserId).push().key ?: ""
            val newWallet = Wallet(id = walletId, name = walletName)
            database.child("wallets").child(currentUserId).child(walletId).setValue(newWallet)
        }
    }

    fun updateWallet(wallet: Wallet) {
        userId?.let { currentUserId ->
            if (wallet.id.isBlank()) return
            database.child("wallets").child(currentUserId).child(wallet.id).child("name")
                .setValue(wallet.name)
        }
    }

    fun deleteWallet(wallet: Wallet) {
        userId?.let { currentUserId ->
            if (wallet.id.isBlank()) return
            // First, delete all transactions associated with the wallet
            database.child("transactions").child(wallet.id).removeValue()
                .addOnSuccessListener {
                    // After transactions are deleted, delete the wallet itself
                    database.child("wallets").child(currentUserId).child(wallet.id).removeValue()
                }
        }
    }

    // --- Transaction CRUD ---
    fun saveTransaction(transaction: Transaction) {
        if (transaction.walletId.isBlank()) return
        val transactionId =
            database.child("transactions").child(transaction.walletId).push().key ?: ""
        database.child("transactions").child(transaction.walletId).child(transactionId)
            .setValue(transaction.copy(id = transactionId))
            .addOnSuccessListener { recalculateHoldings(transaction.walletId) }
    }

    fun updateTransaction(transaction: Transaction) {
        if (transaction.id.isBlank() || transaction.walletId.isBlank()) return
        database.child("transactions").child(transaction.walletId).child(transaction.id)
            .setValue(transaction)
            .addOnSuccessListener { recalculateHoldings(transaction.walletId) }
    }

    fun deleteTransaction(transaction: Transaction) {
        if (transaction.id.isBlank() || transaction.walletId.isBlank()) return
        database.child("transactions").child(transaction.walletId).child(transaction.id)
            .removeValue()
            .addOnSuccessListener { recalculateHoldings(transaction.walletId) }
    }

    private fun recalculateHoldings(walletId: String) {
        userId?.let { currentUserId ->
            database.child("transactions").child(walletId).get().addOnSuccessListener {
                val allTransactions =
                    it.children.mapNotNull { snap -> snap.getValue<Transaction>() }
                val newHoldings = allTransactions
                    .groupBy { tx -> tx.crypto }
                    .mapValues { (_, txs) ->
                        txs.sumOf { tx ->
                            if (tx.type.equals(
                                    "BUY",
                                    true
                                )
                            ) tx.quantity else -tx.quantity
                        }
                    }
                database.child("wallets").child(currentUserId).child(walletId)
                    .child("cryptoHoldings").setValue(newHoldings)
            }
        }
    }

    // --- Data Fetching ---
    fun getWallets() {
        userId?.let { currentUserId ->
            database.child("wallets").child(currentUserId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val walletList = snapshot.children.mapNotNull {
                            it.getValue<Wallet>()?.copy(id = it.key ?: "")
                        }
                        _wallets.value = walletList
                        getTransactionsForWallets(walletList.map { it.id })
                    }

                    override fun onCancelled(error: DatabaseError) { /* Handle error */
                    }
                })
        }
    }

    private fun getTransactionsForWallets(walletIds: List<String>) {
        val allTransactions = mutableListOf<Transaction>()
        val transactionsRef = database.child("transactions")
        _transactions.value = emptyList()

        walletIds.forEach { walletId ->
            transactionsRef.child(walletId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    allTransactions.removeAll { it.walletId == walletId }
                    snapshot.children.mapNotNull {
                        it.getValue<Transaction>()?.copy(id = it.key ?: "")
                    }.forEach { allTransactions.add(it) }
                    _transactions.postValue(allTransactions.sortedByDescending { it.date })
                }

                override fun onCancelled(error: DatabaseError) { /* Handle error */
                }
            })
        }
    }
}