package com.example.spring_boot_kotlin.database.models

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("refresh_tokens")
data class RefreshToken(
    val userId:ObjectId,
    @Indexed(expireAfter="0s")
    val expireAt:Instant,
    val hashedToken:String,
    val created:Instant= Instant.now()
)