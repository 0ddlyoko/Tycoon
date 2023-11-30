package me.oddlyoko.tycoon.commands

import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandEnum
import cn.nukkit.command.data.CommandParamType
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.command.tree.ParamList
import cn.nukkit.command.utils.CommandLogger
import me.oddlyoko.tycoon.item.ItemManager
import me.oddlyoko.tycoon.mob.MobManager

class TycoonCommand : Command("tycoon") {
    init {
        permission = "tycoon.command.tycoon"
        commandParameters.clear()
        val mobs = MobManager.mobs.keys
        val items = ItemManager.items.keys
        addCommandParameters("spawn_mob", arrayOf(
            CommandParameter.newEnum("spawn", CommandEnum("SpawnMob", "mob")),
            CommandParameter.newEnum("mob", mobs.toTypedArray()),
        ))
        addCommandParameters("spawn_item", arrayOf(
            CommandParameter.newEnum("spawn", CommandEnum("SpawnItem", "item")),
            CommandParameter.newEnum("item", items.toTypedArray()),
            CommandParameter.newType("pickupDelay", true, CommandParamType.INT)
        ))
        enableParamTree()
    }

    override fun execute(sender: CommandSender, commandLabel: String, result: MutableMap.MutableEntry<String, ParamList>, log: CommandLogger): Int {
        val list = result.value
        when (result.key) {
            "spawn_mob" -> {
                val mob: String = list.getResult(1)
                val theMob = MobManager.getMob(mob)?.spawn(sender.location)
                if (theMob == null) {
                    sender.sendMessage("§cError while spawning mob $mob")
                    return 0
                }
                sender.sendMessage("Spawn mob $mob")
                return 1
            }
            "spawn_item" -> {
                val item: String = list.getResult(1)
                var pickupDelay = 10
                if (list.hasResult(2))
                    pickupDelay = list.getResult(2)
                val theItem = ItemManager.getItem(item)?.spawn(sender.location, pickupDelay = pickupDelay)
                if (theItem == null) {
                    sender.sendMessage("§cError while spawning item $item")
                    return 0
                }
                sender.sendMessage("Spawn item $item")
                return 1
            }
        }
        return 0
    }
}
