package ru.kotlix.frame.auth.server.coding

interface PasswordEncryptor {
    fun encrypt(password: String): String
}
