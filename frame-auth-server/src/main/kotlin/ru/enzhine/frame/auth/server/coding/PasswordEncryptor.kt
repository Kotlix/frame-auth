package ru.enzhine.frame.auth.server.coding

import org.springframework.stereotype.Component

interface PasswordEncryptor {
    fun encrypt(password: String): String
}