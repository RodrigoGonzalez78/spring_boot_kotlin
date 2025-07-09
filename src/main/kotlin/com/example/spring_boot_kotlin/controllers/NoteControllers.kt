package com.example.spring_boot_kotlin.controllers

import com.example.spring_boot_kotlin.database.models.Note
import com.example.spring_boot_kotlin.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Security
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteControllers(
    private val noteRepository: NoteRepository
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    data class NoteRequest(
        val id:String?,
        val title:String,
        val content:String,
        val color:Long
    )

    data class NoteResponse(
        val id:String,
        val title:String,
        val content:String,
        val color:Long,
        val createdAt:Instant
    )

    @PostMapping
    fun save(@RequestBody body: NoteRequest):NoteResponse{

        val ownerId = SecurityContextHolder.getContext().authentication.principal as String

        log.info("############################################### :$ownerId")
        val note = noteRepository.save(


            Note(
                id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(ownerId)
            )
        )
        return NoteResponse(
            id=note.id.toHexString(),
            title = note.title,
            content = note.content,
            color = note.color,
            createdAt = note.createdAt
        )
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById( @PathVariable id: String){
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String

        val note = noteRepository.findById(ObjectId(id)).orElseThrow{
            IllegalArgumentException("Note not found")
        }

        if (ownerId == note.ownerId.toHexString()){
            noteRepository.deleteById(ObjectId(id))
        }


    }

    @GetMapping
    fun findByOwnerId():List<NoteResponse>{
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.findByOwnerId(ownerId = ObjectId(ownerId)).map {
            it.toResponse()
        }
    }


    private fun Note.toResponse():NoteControllers.NoteResponse{
        return NoteResponse(
            id=id.toHexString(),
            title =title,
            content = content,
            color =color,
            createdAt =createdAt
        )
    }
}