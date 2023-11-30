package me.oddlyoko.tycoon

import cn.nukkit.entity.Entity
import cn.nukkit.entity.provider.CustomClassEntityProvider
import cn.nukkit.level.Location
import cn.nukkit.plugin.PluginBase
import me.oddlyoko.tycoon.commands.TycoonCommand
import me.oddlyoko.tycoon.config.Config
import me.oddlyoko.tycoon.entities.entities.EntityGoldChicken
import me.oddlyoko.tycoon.item.Item
import me.oddlyoko.tycoon.item.ItemManager
import me.oddlyoko.tycoon.item.ItemRarity
import me.oddlyoko.tycoon.item.types.WoodenSwordType
import me.oddlyoko.tycoon.mob.Mob
import me.oddlyoko.tycoon.mob.MobListener
import me.oddlyoko.tycoon.mob.MobManager
import me.oddlyoko.tycoon.mob.types.ChickenMobType
import me.oddlyoko.tycoon.mob.types.GoldChickenMobType
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
        logger.info("Loading mob entities")
        loadCustomMobEntities()
        logger.info("Loading mob types")
        loadMobTypes()
        logger.info("${MobManager.mobTypes.size} mob types loaded")
        logger.info("Loading mobs")
        loadMobs()
        logger.info("${MobManager.mobs.size} mobs loaded")
        // Items
        logger.info("Loading items")
        loadCustomItems()
        logger.info("Loading item types")
        loadItemTypes()
        logger.info("${ItemManager.itemTypes.size} item types loaded")
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

    private fun loadMobTypes() {
        MobManager.registerMobType("chicken", ChickenMobType)
        MobManager.registerMobType("gold_chicken", GoldChickenMobType)
    }

    private fun loadMobs() {
        Config.mobsConfig.mobs.forEach { mobConfig ->
            val zone = ZoneManager.getZone(mobConfig.zone)
            if (zone == null) {
                logger.error("Zone ${mobConfig.zone} not found")
                server.shutdown()
                return
            }
            val type = MobManager.getMobType(mobConfig.type)
            if (type == null) {
                logger.error("Mob type ${mobConfig.type} not found")
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
                type = type,
                probability = mobConfig.probability,
                maxAmount = mobConfig.maxAmount,
                health = health,
            )
            MobManager.registerMob(mob)
        }
    }

    // Items

    private fun loadCustomItems() {
        // TODO
    }

    private fun loadItemTypes() {
        ItemManager.registerItemType("wooden_sword", WoodenSwordType)
    }

    private fun loadItems() {
        Config.swordsConfig.swords.forEach { swordConfig ->
            val type = ItemManager.getItemType(swordConfig.type)
            if (type == null) {
                logger.error("Item type ${swordConfig.type} not found")
                server.shutdown()
                return
            }
            val damage = CustomBigNumber.fromString(swordConfig.damage)
            if (damage == null) {
                logger.error("Invalid damage for item ${swordConfig.id}: ${swordConfig.damage}")
                server.shutdown()
                return
            }
            val item = Item(
                id = swordConfig.id,
                name = swordConfig.name,
                type = type,
                damage = damage,
                rarity = ItemRarity.fromString(swordConfig.rarity.toString()),
            )
            ItemManager.registerItem(item)
        }
    }

    private fun loadListeners() {
        server.pluginManager.registerEvents(ZoneListener, this)
        server.pluginManager.registerEvents(MobListener, this)
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
