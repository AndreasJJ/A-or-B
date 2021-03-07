package com.andreasjj

import com.andreasjj.entity.RoundId

import javax.persistence.Entity
import javax.persistence.Column

import java.time.Instant

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.EmbeddedId
import javax.persistence.Id
import javax.persistence.GeneratedValue

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