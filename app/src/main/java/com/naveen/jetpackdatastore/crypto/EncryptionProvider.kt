package com.naveen.jetpackdatastore.crypto

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.KeysetHandle

object EncryptionProvider {
    private const val KEYSET_NAME = "naveen_keyset"
    private const val PREF_FILE_NAME = "NaveenPrivateDataBase"
    private const val MASTER_KEY_URI = "android-keystore://datastore_master_key"

    @Volatile
    private var aeadInstance: Aead? = null

    fun getAead(context: Context): Aead {
        val existing = aeadInstance
        if (existing != null) return existing
        synchronized(this) {
            val again = aeadInstance
            if (again != null) return again
            AeadConfig.register()
            val keysetHandle: KeysetHandle = AndroidKeysetManager.Builder()
                .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
                .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
                .withMasterKeyUri(MASTER_KEY_URI)
                .build()
                .keysetHandle
            val aead = keysetHandle.getPrimitive(Aead::class.java)
            aeadInstance = aead
            return aead
        }
    }
}


