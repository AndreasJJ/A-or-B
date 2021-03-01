package com.andreasjj

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.OneToMany

import java.time.Instant
import java.util.UUID

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.AutoPopulated

@Entity
data class Game(
    @Id
    @AutoPopulated
    var id: UUID = UUID.randomUUID(),
    var owner: String,
    @DateCreated
    @Column(name = "created_timestamp")
    var createdTimestamp: Instant,
    @DateUpdated
    @Column(name="update_timestamp")
    var updateTimestamp: Instant,
    var title: String,
    @OneToMany
    var rounds: List<Round>,
    @Column(name="left_text")
    var leftText: String,
    @Column(name="right_text")
    var rightText: String,
)