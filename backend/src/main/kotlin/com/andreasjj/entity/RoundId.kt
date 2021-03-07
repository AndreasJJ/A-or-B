package com.andreasjj.entity

import com.andreasjj.Game
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.GeneratedValue
import javax.persistence.ManyToOne

@Embeddable
data class RoundId(
    @ManyToOne
    @Column(name="game_id")
    val gameId: Game,
    @GeneratedValue
    @Column(name="round_number")
    val roundNumber: Long
)