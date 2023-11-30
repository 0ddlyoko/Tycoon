package me.oddlyoko.tycoon.config.sword

enum class SwordRarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
    MYTHIC,
    GODLY,
    SPECIAL,
    UNKNOWN;

    companion object {
        fun fromString(str: String): SwordRarity {
            return try {
                valueOf(str)
            } catch (ex: Exception) {
                UNKNOWN
            }
        }
    }
}
