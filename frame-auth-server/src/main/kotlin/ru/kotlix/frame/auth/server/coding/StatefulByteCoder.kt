package ru.kotlix.frame.auth.server.coding

interface StatefulByteCoder {
    fun encode(byteArray: ByteArray): ByteArray

    fun decode(byteArray: ByteArray): ByteArray
}
