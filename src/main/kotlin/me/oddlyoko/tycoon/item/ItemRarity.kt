package me.oddlyoko.tycoon.item

enum class ItemRarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    GODLY,
    SPECIAL,
    UNKNOWN;

    companion object {
        fun fromString(str: String): ItemRarity {
            return try {
                ItemRarity.valueOf(str)
            } catch (ex: Exception) {
                UNKNOWN
            }
        }
    }
}
