package me.oddlyoko.tycoon.config.sword

data class SwordConfig(
    val id: String,
    val name: String,
    val type: String,
    val damage: String,
    val rarity: SwordRarity,
)
