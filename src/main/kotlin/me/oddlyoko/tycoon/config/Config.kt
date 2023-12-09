package me.oddlyoko.tycoon.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import me.oddlyoko.tycoon.config.mob.MobsConfig
import me.oddlyoko.tycoon.config.sword.ItemsConfig
import me.oddlyoko.tycoon.config.world.WorldsConfig
import java.io.File

object Config {
    lateinit var worldsConfig: WorldsConfig
        private set
    lateinit var mobsConfig: MobsConfig
        private set
    lateinit var itemsConfig: ItemsConfig
        private set

    fun loadConfig(dataFolder: File) {
        loadWorldsConfig(dataFolder)
        loadMobsConfig(dataFolder)
        loadSwordsConfig(dataFolder)
    }

    private fun loadWorldsConfig(dataFolder: File) {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule.Builder().build())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        worldsConfig = mapper.readValue(dataFolder.resolve("worlds.yml"), WorldsConfig::class.java)
    }

    private fun loadMobsConfig(dataFolder: File) {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule.Builder().build())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mobsConfig = mapper.readValue(dataFolder.resolve("mobs.yml"), MobsConfig::class.java)
    }

    private fun loadSwordsConfig(dataFolder: File) {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule.Builder().build())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        itemsConfig = mapper.readValue(dataFolder.resolve("items.yml"), ItemsConfig::class.java)
    }
}
