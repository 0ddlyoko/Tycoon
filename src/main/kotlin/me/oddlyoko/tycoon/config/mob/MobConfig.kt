package me.oddlyoko.tycoon.config.mob

data class MobConfig(
    val id: String,
    val zone: String,
    val name: String,
    val type: String,
    val probability: Int,
    val maxAmount: Int,
    val health: String,
)
