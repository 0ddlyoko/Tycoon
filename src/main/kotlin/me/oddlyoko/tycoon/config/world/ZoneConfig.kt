package me.oddlyoko.tycoon.config.world


data class ZoneConfig(
    val id: String,
    val name: String,
    val maxMobCount: Int,
    val start: CoordConfig,
    val end: CoordConfig,
)
