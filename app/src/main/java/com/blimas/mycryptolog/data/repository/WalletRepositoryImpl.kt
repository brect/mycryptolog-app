package com.blimas.mycryptolog.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blimas.mycryptolog.data.mapper.toDomain
import com.blimas.mycryptolog.data.mapper.toDto
import com.blimas.mycryptolog.data.model.WalletDto
import com.blimas.mycryptolog.domain.model.Wallet
import com.blimas.mycryptolog.domain.repository.WalletRepository
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
                    val wallets = snapshot.children.mapNotNull { child ->
                        child.getValue(WalletDto::class.java)?.toDomain(child.key ?: "")
                    }
                    liveData.value = wallets
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return liveData
    }

    override suspend fun createWallet(name: String) {
        val id = userId ?: return
        val walletRef = db.child("users").child(id).child("wallets").push()
        val wallet = Wallet(name = name)
        walletRef.setValue(wallet.toDto())
    }

    override suspend fun updateWallet(wallet: Wallet) {
        val id = userId ?: return
        if (wallet.id.isBlank()) return
        db.child("users").child(id).child("wallets").child(wallet.id).setValue(wallet.toDto())
    }

    override suspend fun deleteWallet(wallet: Wallet) {
        val id = userId ?: return
        if (wallet.id.isBlank()) return

        db.child("users").child(id).child("transactions").child(wallet.id).removeValue()
        db.child("users").child(id).child("wallets").child(wallet.id).removeValue()
    }
}
