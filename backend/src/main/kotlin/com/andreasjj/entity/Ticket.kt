package com.andreasjj

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column

import java.time.Instant
import java.util.UUID

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.AutoPopulated


@Entity
data class Ticket(
  @Id
  @AutoPopulated
  var id: UUID = UUID.randomUUID(),
  var token: String,
  @DateCreated
  @Column(name = "created_timestamp")
  var createdTimestamp: Instant,
  @DateUpdated
  @Column(name = "update_timestamp")
  var updateTimestamp: Instant,
  var ip: String,
  var used: Boolean = false,
)