package com.example.spring_boot_kotlin.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {

    private val bCrypt = BCryptPasswordEncoder()

    fun encode(raw:String):String {
        return bCrypt.encode(raw)
    }

    fun matches(raw: String, hashed:String):Boolean{
       return bCrypt.matches(raw,hashed)
    }
}