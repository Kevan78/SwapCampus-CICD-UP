package com.example.swapcampus_geoffroykevan.data

import com.example.swapcampus_geoffroykevan.domain.UserAccount
import com.example.swapcampus_geoffroykevan.domain.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

class AuthRepository {

    private val accounts = listOf(
        UserAccount(1, "Omar Tir", "omar@gmail.com", "omar123", LocalDate(2026, 9, 1), UserRole.STUDENT),
        UserAccount(2, "Amine", "amine@gmail.com", "amine123", LocalDate(2026, 9, 2), UserRole.STUDENT),
        UserAccount(3, "Geoffroy Kevan", "kevan@gmail.com", "kevan123", LocalDate(2026, 9, 1), UserRole.ADMINISTRATOR)
    )

    private val _currentUser = MutableStateFlow<UserAccount?>(null)

    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    fun authenticate(email: String, password: String): UserAccount? {
        val account = accounts.firstOrNull {
            it.email.equals(email.trim(), ignoreCase = true) &&
                it.passwordHash == password &&
                it.active
        }
        if (account != null) {
            _currentUser.value = account
        }
        return account
    }

    fun logout() {
        _currentUser.value = null
    }

    fun findUser(id: Int): UserAccount? = accounts.firstOrNull { it.id == id }
}
