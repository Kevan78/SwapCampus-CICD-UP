package com.example.swapcampus_geoffroykevan

import com.example.swapcampus_geoffroykevan.data.AuthRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthRepositoryTest {
    private val auth = AuthRepository()

    @Test
    fun omarCanLogIn() {
        val user = auth.authenticate("omar@gmail.com", "omar123")
        assertEquals("Omar Tir", user?.name)
    }

    @Test
    fun wrongPasswordFails() {
        assertNull(auth.authenticate("omar@gmail.com", "bad"))
    }
}
