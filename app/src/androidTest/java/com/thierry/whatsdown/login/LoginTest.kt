package com.thierry.whatsdown.login

import com.thierry.whatsdown.User
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class LoginTest {

    @Test
    fun testLoginValid() = runBlocking{
        val user = User.login("Kevin", "Zli12345")
        assertNotNull(user)
        assertEquals("Kevin", user!!.username)
    }

    @Test
    fun testLoginInvalid() = runBlocking{
        val user = User.login("invaliduser", "invalidpass")
        assertNull(user)
    }
}