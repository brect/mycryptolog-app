package com.blimas.mycryptolog.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blimas.mycryptolog.domain.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val db: DatabaseReference,
    private val auth: FirebaseAuth
) : TransactionRepository {

    private val userId: String?
        get() = auth.currentUser?.uid

    override fun getTransactions(walletIds: List<String>): LiveData<List<Transaction>> {
        val liveData = MutableLiveData<List<Transaction>>()

        if (walletIds.isEmpty()) {
            liveData.value = emptyList()
            return liveData
        }

        val allTransactionsMap = mutableMapOf<String, List<Transaction>>()
        val id = userId ?: return liveData

        walletIds.forEach { walletId ->
            db.child("users").child(id).child("transactions").child(walletId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val transactions = snapshot.children.mapNotNull { child ->
                            child.getValue(Transaction::class.java)?.apply {
                                this.id = child.key ?: ""
                            }
                        }
                        allTransactionsMap[walletId] = transactions
                        liveData.value = allTransactionsMap.values.flatten().sortedByDescending { it.date }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        return liveData
    }

    override suspend fun saveTransaction(transaction: Transaction) {
        val id = userId ?: return
        if (transaction.walletId.isBlank()) return
        val transRef = db.child("users").child(id).child("transactions").child(transaction.walletId).push()
        transRef.setValue(transaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val id = userId ?: return
        if (transaction.id.isBlank() || transaction.walletId.isBlank()) return
        db.child("users").child(id).child("transactions").child(transaction.walletId).child(transaction.id).setValue(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        val id = userId ?: return
        if (transaction.id.isBlank() || transaction.walletId.isBlank()) return
        db.child("users").child(id).child("transactions").child(transaction.walletId).child(transaction.id).removeValue()
    }
}