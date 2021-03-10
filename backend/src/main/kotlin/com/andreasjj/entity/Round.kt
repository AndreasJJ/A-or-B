package com.andreasjj

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.OneToOne
import javax.persistence.Column
import javax.persistence.Embeddable

import java.time.Instant
import java.util.*

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.EmbeddedId

import com.andreasjj.utilities.UUIDSerializer
import kotlinx.serialization.Serializable

@Entity
data class Round(
    @Id
    @GeneratedValue
    var roundId: Long,
    @DateCreated
    @Column(name = "created_timestamp")
    var createdTimestamp: Instant,
    @DateUpdated
    @Column(name="update_timestamp")
    var updateTimestamp: Instant,
    var title: String,
    var link: String,
)

@Embeddable
data class RoundId(
    @OneToOne
    @Column(name="game_id")
    val gameId: Game,
    @GeneratedValue
    @Column(name="round_id")
    val roundId: Long
)