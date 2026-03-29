package com.blimas.mycryptolog.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blimas.mycryptolog.model.Transaction
import com.blimas.mycryptolog.model.Wallet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val db: DatabaseReference,
    private val auth: FirebaseAuth
) : WalletRepository {

    private val userId: String?
        get() = auth.currentUser?.uid

    override fun getWallets(): LiveData<List<Wallet>> {
        val liveData = MutableLiveData<List<Wallet>>()
        val id = userId ?: return liveData

        db.child("users").child(id).child("wallets")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val wallets = snapshot.children.mapNotNull { it.getValue(Wallet::class.java) }
                    liveData.value = wallets
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return liveData
    }

    override fun getTransactions(walletIds: List<String>): LiveData<List<Transaction>> {
        val liveData = MutableLiveData<List<Transaction>>()
        val id = userId ?: return liveData

        db.child("users").child(id).child("transactions")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val transactions = snapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                    liveData.value = transactions
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return liveData
    }

    override suspend fun createWallet(name: String) {
        val id = userId ?: return
        val walletRef = db.child("users").child(id).child("wallets").push()
        val wallet = Wallet(id = walletRef.key ?: "", name = name)
        walletRef.setValue(wallet)
    }

    override suspend fun updateWallet(wallet: Wallet) {
        val id = userId ?: return
        db.child("users").child(id).child("wallets").child(wallet.id).setValue(wallet)
    }

    override suspend fun deleteWallet(wallet: Wallet) {
        val id = userId ?: return
        db.child("users").child(id).child("wallets").child(wallet.id).removeValue()
    }

    override suspend fun saveTransaction(transaction: Transaction) {
        val id = userId ?: return
        val transRef = db.child("users").child(id).child("transactions").push()
        val newTransaction = transaction.copy(id = transRef.key ?: "")
        transRef.setValue(newTransaction)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val id = userId ?: return
        db.child("users").child(id).child("transactions").child(transaction.id).setValue(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        val id = userId ?: return
        db.child("users").child(id).child("transactions").child(transaction.id).removeValue()
    }
}