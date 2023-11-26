package me.oddlyoko.tycoon.config.world

data class WorldConfig(
    val id: String,
    val spawn: SpawnConfig,
    val zones: List<ZoneConfig>,
)
