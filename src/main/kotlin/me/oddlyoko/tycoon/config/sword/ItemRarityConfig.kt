package me.oddlyoko.tycoon.config.sword

import me.oddlyoko.tycoon.item.ItemRarity

enum class ItemRarityConfig {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    GODLY,
    SPECIAL,
    UNKNOWN;

    fun toRarity(): ItemRarity = ItemRarity.fromString(name)

    companion object {
        fun fromString(str: String): ItemRarityConfig {
            return try {
                valueOf(str)
            } catch (ex: Exception) {
                UNKNOWN
            }
        }
    }
}
