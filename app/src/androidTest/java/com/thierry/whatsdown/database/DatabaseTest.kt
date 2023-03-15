package com.thierry.whatsdown.database
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Assert.*
import org.junit.Test

class DatabaseTest {
    @Test
    fun testConnection() {
        val firestore = DataBase.connect()
        assertNotNull(firestore)
        assertTrue(firestore is FirebaseFirestore)
    }
}
