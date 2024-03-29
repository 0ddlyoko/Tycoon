package me.oddlyoko.tycoon

import cn.nukkit.entity.Entity
import cn.nukkit.entity.provider.CustomClassEntityProvider
import cn.nukkit.level.Location
import cn.nukkit.plugin.PluginBase
import me.oddlyoko.tycoon.commands.TycoonCommand
import me.oddlyoko.tycoon.config.Config
import me.oddlyoko.tycoon.entities.entities.EntityGoldChicken
import me.oddlyoko.tycoon.item.ItemManager
import me.oddlyoko.tycoon.mob.Mob
import me.oddlyoko.tycoon.mob.MobDrop
import me.oddlyoko.tycoon.mob.MobListener
import me.oddlyoko.tycoon.mob.MobManager
import me.oddlyoko.tycoon.player.PlayerListener
import me.oddlyoko.tycoon.util.CustomBigNumber
import me.oddlyoko.tycoon.zone.Zone
import me.oddlyoko.tycoon.zone.ZoneListener
import me.oddlyoko.tycoon.zone.ZoneManager
import java.util.*

class Tycoon: PluginBase() {

    override fun onEnable() {
        logger.info("Tycoon loading...")
        INSTANCE = this
        logger.info("Loading configuration")
        Config.loadConfig(dataFolder)
        logger.info("Configuration loaded")
        loadRegions()
        logger.info("${ZoneManager.zones.size} regions loaded")
        // Mobs
        logger.info("Loading custom mob entities")
        loadCustomMobEntities()
        logger.info("Loading mobs")
        loadMobs()
        logger.info("${MobManager.mobs.size} mobs loaded")
        // Items
        logger.info("Loading custom items")
        loadCustomItems()
        logger.info("Loading items")
        loadItems()
        logger.info("${ItemManager.items.size} items loaded")
        loadListeners()
        loadCommands()
        logger.info("Spawning mobs")
        ZoneManager.spawnMobs()
        logger.info("Tycoon enabled")
    }

    private fun loadRegions() {
        Config.worldsConfig.worlds.forEach { world ->
            val level = server.getLevelByName(world.id)
            if (level == null) {
                logger.error("World ${world.id} not found")
                server.shutdown()
                return
            }
            world.zones.forEach { zone ->
                ZoneManager.registerZone(Zone(
                    id = zone.id,
                    name = zone.name,
                    maxMobCount = zone.maxMobCount,
                    startPos = Location(zone.start.x.toDouble(), zone.start.y.toDouble(), zone.start.z.toDouble(), level),
                    endPos = Location(zone.end.x.toDouble(), zone.end.y.toDouble(), zone.end.z.toDouble(), level),
                ))
            }
        }
    }

    // Mobs

    private fun loadCustomMobEntities() {
        Entity.registerCustomEntity(CustomClassEntityProvider(EntityGoldChicken::class.java))
    }

    private fun loadMobs() {
        Config.mobsConfig.mobs.forEach { mobConfig ->
            val zone = ZoneManager.getZone(mobConfig.zone)
            if (zone == null) {
                logger.error("Zone ${mobConfig.zone} not found")
                server.shutdown()
                return
            }
            val health = CustomBigNumber.fromString(mobConfig.health)
            if (health == null) {
                logger.error("Invalid health for mob ${mobConfig.id}: ${mobConfig.health}")
                server.shutdown()
                return
            }
            val mob = Mob(
                id = mobConfig.id,
                zone = zone,
                name = mobConfig.name,
                type = mobConfig.type,
                probability = mobConfig.probability,
                maxAmount = mobConfig.maxAmount,
                health = health,
                exp = mobConfig.exp,
                drops = mobConfig.drops.map {
                    MobDrop(ItemManager.getItem(it.item), it.probability)
                }
            )
            MobManager.registerMob(mob)
        }
    }

    // Items

    private fun loadCustomItems() {
        // TODO
    }

    private fun loadItems() {
        Config.itemsConfig.swords.forEach { swordConfig ->
            ItemManager.registerItem(swordConfig.toSword())
        }
    }

    private fun loadListeners() {
        server.pluginManager.registerEvents(ZoneListener, this)
        server.pluginManager.registerEvents(MobListener, this)
        server.pluginManager.registerEvents(PlayerListener, this)
    }

    private fun loadCommands() {
        server.commandMap.register("tycoon", TycoonCommand())
    }

    override fun onDisable() {
        logger.info("Tycoon disabled")
    }

    companion object {
        lateinit var INSTANCE: Tycoon
            private set

        val RANDOM_UUID = UUID.randomUUID().toString()
    }
}
