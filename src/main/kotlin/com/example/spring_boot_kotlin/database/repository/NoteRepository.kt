package com.example.spring_boot_kotlin.database.repository

import com.example.spring_boot_kotlin.database.models.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository:MongoRepository<Note,ObjectId> {
      fun findByOwnerId(ownerId: ObjectId):List<Note>
}